import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatTable } from '@angular/material/table';
import { PageEvent } from '@angular/material/paginator';
import { Inventory } from '../../models/inventory.model';
import { InventoryService } from '../../services/inventory.service';
import { InventoryDialogComponent } from './inventory-dialog.component';
import { BaseListComponent } from '../../components/base-list/base-list.component';

@Component({
  selector: 'app-inventory-list',
  templateUrl: './inventory-list.component.html',
  styleUrls: ['./inventory-list.component.scss']
})
export class InventoryListComponent implements OnInit {
  @ViewChild(MatTable) table!: MatTable<Inventory>;
  @ViewChild(BaseListComponent) baseList!: BaseListComponent;

  displayedColumns: string[] = [
    'productName',
    'quantity', 
    'purchaseContractNo', 
    'status',
    'actions'
  ];
  
  dataSource: Inventory[] = [];
  loading = false;
  totalElements = 0;
  pageSize = 10;
  pageIndex = 0;

  constructor(
    private inventoryService: InventoryService,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.loadData();
  }

  onPageChange(event: PageEvent): void {
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;
    this.loadData();
  }
  
  onSearch(searchText: string): void {
    this.pageIndex = 0;
    // TODO: Implement search functionality if needed
    this.loadData();
  }

  loadData(event?: PageEvent): void {
    this.loading = true;
    if (event) {
      this.pageIndex = event.pageIndex;
      this.pageSize = event.pageSize;
    }

    this.inventoryService.getAll(this.pageIndex, this.pageSize)
      .subscribe({
        next: (response) => {
          this.dataSource = response.data.content;
          this.totalElements = response.data.totalElements;
          this.loading = false;
        },
        error: (error) => {
          console.error('Error loading inventories:', error);
          this.loading = false;
        }
      });
  }

  openDialog(inventory?: Inventory): void {
    const dialogRef = this.dialog.open(InventoryDialogComponent, {
      width: '800px',
      data: inventory || {}
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadData();
      }
    });
  }

  delete(id: number): void {
    if (confirm('Are you sure you want to delete this inventory?')) {
      this.inventoryService.delete(id).subscribe({
        next: () => {
          this.loadData();
        },
        error: (error) => {
          console.error('Error deleting inventory:', error);
        }
      });
    }
  }
}
