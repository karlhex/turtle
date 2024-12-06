import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatTable } from '@angular/material/table';
import { PageEvent } from '@angular/material/paginator';
import { Contract, ContractQuery } from '../../models/contract.model';
import { ContractService } from '../../services/contract.service';
import { ContractDialogComponent } from './contract-dialog.component';
import { BaseListComponent } from '../../components/base-list/base-list.component';
import { ContractType } from '../../types/contract-type.enum';
import { ContractStatus } from '../../types/contract-status.enum';
import { ApiResponse } from '../../models/api.model';
import { Page } from '../../models/page.model';

@Component({
  selector: 'app-contract-list',
  templateUrl: './contract-list.component.html',
  styleUrls: ['./contract-list.component.scss']
})
export class ContractListComponent implements OnInit {
  @ViewChild(MatTable) table!: MatTable<Contract>;
  @ViewChild(BaseListComponent) baseList!: BaseListComponent;

  displayedColumns: string[] = [
    'contractNo',
    'title',
    'type',
    'status',
    'totalAmount',
    'currency',
    'buyerCompany',
    'sellerCompany',
    'signingDate',
    'startDate',
    'endDate',
    'actions'
  ];
  
  dataSource: Contract[] = [];
  contractTypes = Object.values(ContractType);
  contractStatuses = Object.values(ContractStatus);
  loading = false;
  totalElements = 0;
  pageSize = 10;
  pageIndex = 0;

  constructor(
    private contractService: ContractService,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    this.loading = true;
    const query: ContractQuery = {
      page: this.pageIndex,
      size: this.pageSize,
      search: this.baseList?.searchText
    };

    this.contractService.getContracts(query).subscribe({
      next: (response: ApiResponse<Page<Contract>>) => {
        this.dataSource = response.data.content;
        this.totalElements = response.data.totalElements;
        this.loading = false;
        if (this.table) {
          this.table.renderRows();
        }
      },
      error: (error) => {
        console.error('Failed to load contracts:', error);
        this.loading = false;
      }
    });
  }

  onSearch(searchText: string): void {
    this.pageIndex = 0;
    this.loadData();
  }

  onAdd(): void {
    this.openDialog();
  }

  onPageChange(event: PageEvent): void {
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;
    this.loadData();
  }

  openDialog(contract?: Contract): void {
    const dialogRef = this.dialog.open(ContractDialogComponent, {
      width: '800px',
      data: contract || {}
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadData();
      }
    });
  }

  deleteContract(id: number): void {
    if (confirm('确定要删除这个合同吗？')) {
      this.loading = true;
      this.contractService.deleteContract(id).subscribe({
        next: () => {
          this.loadData();
        },
        error: (error) => {
          console.error('Failed to delete contract:', error);
          this.loading = false;
        }
      });
    }
  }
}
