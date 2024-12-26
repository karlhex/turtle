import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { PageEvent } from '@angular/material/paginator';
import { MatTable } from '@angular/material/table';
import { BaseListComponent } from '../../../components/base-list/base-list.component';
import { ReimbursementService } from '../../../services/reimbursement.service';
import { Reimbursement, ReimbursementSearchParams } from '../../../models/reimbursement.model';
import { ReimbursementDialogComponent } from '../reimbursement-dialog/reimbursement-dialog.component';
import { ConfirmDialogComponent } from '@app/components/confirmdialog/confirm-dialog.component';
import { TranslateService } from '@ngx-translate/core';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-reimbursement-list',
  templateUrl: './reimbursement-list.component.html',
  styleUrls: ['./reimbursement-list.component.scss']
})
export class ReimbursementListComponent implements OnInit {
  @ViewChild(BaseListComponent) baseList!: BaseListComponent;
  @ViewChild(MatTable) table!: MatTable<Reimbursement>;

  displayedColumns: string[] = [
    'reimbursementNo',
    'applicationDate',
    'applicant',
    'project',
    'totalAmount',
    'approvalDate',
    'approver',
    'actions'
  ];

  reimbursements: Reimbursement[] = [];
  loading = false;
  totalElements = 0;
  pageSize = 10;
  pageIndex = 0;
  searchText = '';

  constructor(
    private reimbursementService: ReimbursementService,
    private dialog: MatDialog,
    private translate: TranslateService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.loadReimbursements();
  }

  loadReimbursements(): void {
    this.loading = true;
    const params: ReimbursementSearchParams = {
      page: this.pageIndex,
      size: this.pageSize,
      sort: 'applicationDate,desc'
    };

    if (this.searchText) {
      // Add search criteria based on your requirements
      // For example, search by reimbursement number
      params.reimbursementNo = this.searchText;
    }

    this.reimbursementService.getAll(params).subscribe({
      next: (response) => {
        this.reimbursements = response.content;
        this.totalElements = response.totalElements;
        this.table?.renderRows();
      },
      error: (error) => {
        console.error('Error loading reimbursements:', error);
        this.snackBar.open(
          this.translate.instant('reimbursement.error.loadFailed'),
          this.translate.instant('common.action.close'),
          { duration: 3000 }
        );
      },
      complete: () => {
        this.loading = false;
      }
    });
  }

  onSearch(searchText: string): void {
    this.searchText = searchText;
    this.pageIndex = 0;
    this.loadReimbursements();
  }

  onPageChange(event: PageEvent): void {
    this.pageSize = event.pageSize;
    this.pageIndex = event.pageIndex;
    this.loadReimbursements();
  }

  onAdd(): void {
    const dialogRef = this.dialog.open(ReimbursementDialogComponent, {
      width: '800px',
      data: { mode: 'create' }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadReimbursements();
      }
    });
  }

  onEdit(reimbursement: Reimbursement): void {
    const dialogRef = this.dialog.open(ReimbursementDialogComponent, {
      width: '800px',
      data: { mode: 'edit', reimbursement }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadReimbursements();
      }
    });
  }

  onDelete(reimbursement: Reimbursement): void {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      width: '400px',
      data: {
        title: this.translate.instant('reimbursement.dialog.deleteTitle'),
        message: this.translate.instant('reimbursement.dialog.deleteMessage', {
          no: reimbursement.reimbursementNo
        })
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loading = true;
        this.reimbursementService.delete(reimbursement.id).subscribe({
          next: () => {
            this.snackBar.open(
              this.translate.instant('reimbursement.success.deleted'),
              this.translate.instant('common.action.close'),
              { duration: 3000 }
            );
            this.loadReimbursements();
          },
          error: (error) => {
            console.error('Error deleting reimbursement:', error);
            this.snackBar.open(
              this.translate.instant('reimbursement.error.deleteFailed'),
              this.translate.instant('common.action.close'),
              { duration: 3000 }
            );
            this.loading = false;
          }
        });
      }
    });
  }
}
