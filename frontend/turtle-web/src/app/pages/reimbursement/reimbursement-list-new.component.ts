import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { TranslateService } from '@ngx-translate/core';
import { ListPageConfig } from '../../components/list-page/list-page.component';
import { Reimbursement } from '../../models/reimbursement.model';
import { ReimbursementService } from '../../services/reimbursement.service';
import { ReimbursementInputNewComponent } from './reimbursement-input-new.component';
import { ReimbursementStatus } from '../../types/reimbursement-status.enum';

@Component({
  selector: 'app-reimbursement-list-new',
  templateUrl: './reimbursement-list-new.component.html',
  styleUrls: ['./reimbursement-list-new.component.scss']
})
export class ReimbursementListNewComponent implements OnInit {
  config: ListPageConfig = {
    title: 'REIMBURSEMENT.TITLE',
    columns: [
      {
        key: 'reimbursementNo',
        label: 'REIMBURSEMENT.REIMBURSEMENT_NO',
        type: 'text',
        sortable: true,
        width: '15%'
      },
      {
        key: 'applicationDate',
        label: 'REIMBURSEMENT.APPLICATION_DATE',
        type: 'date',
        sortable: true,
        width: '12%'
      },
      {
        key: 'applicantId',
        label: 'REIMBURSEMENT.APPLICANT',
        type: 'text',
        sortable: true,
        width: '15%'
      },
      {
        key: 'projectId',
        label: 'REIMBURSEMENT.PROJECT',
        type: 'text',
        sortable: false,
        width: '15%'
      },
      {
        key: 'totalAmount',
        label: 'REIMBURSEMENT.TOTAL_AMOUNT',
        type: 'number',
        sortable: true,
        width: '12%',
        formatter: (value: number) => `¥${value.toFixed(2)}`
      },
      {
        key: 'status',
        label: 'REIMBURSEMENT.STATUS',
        type: 'text',
        sortable: true,
        width: '10%',
        formatter: (value: ReimbursementStatus) => {
          switch (value) {
            case ReimbursementStatus.DRAFT: return 'REIMBURSEMENT.STATUS_DRAFT';
            case ReimbursementStatus.PENDING: return 'REIMBURSEMENT.STATUS_PENDING';
            case ReimbursementStatus.APPROVED: return 'REIMBURSEMENT.STATUS_APPROVED';
            case ReimbursementStatus.REJECTED: return 'REIMBURSEMENT.STATUS_REJECTED';
            default: return value;
          }
        }
      },
      {
        key: 'approvalDate',
        label: 'REIMBURSEMENT.APPROVAL_DATE',
        type: 'date',
        sortable: true,
        width: '12%'
      },
      {
        key: 'actions',
        label: 'COMMON.ACTIONS',
        type: 'action',
        sortable: false,
        width: '9%',
        actions: [
          {
            label: 'COMMON.VIEW',
            icon: 'visibility',
            color: 'primary',
            action: (item: Reimbursement) => this.onView(item)
          },
          {
            label: 'COMMON.EDIT',
            icon: 'edit',
            color: 'primary',
            action: (item: Reimbursement) => this.onEdit(item),
            hidden: (item: Reimbursement) => item.status !== ReimbursementStatus.DRAFT
          },
          {
            label: 'COMMON.DELETE',
            icon: 'delete',
            color: 'warn',
            action: (item: Reimbursement) => this.onDelete(item),
            hidden: (item: Reimbursement) => item.status !== ReimbursementStatus.DRAFT
          }
        ]
      }
    ],
    pageSizeOptions: [5, 10, 25, 50],
    defaultPageSize: 10,
    showSearch: true,
    showExport: true,
    showBulkActions: false,
    searchPlaceholder: 'REIMBURSEMENT.SEARCH_PLACEHOLDER',
    showAddButton: true
  };

  data: Reimbursement[] = [];
  loading = false;
  totalItems = 0;
  currentPage = 0;
  pageSize = 10;
  currentSearch = '';

  constructor(
    private reimbursementService: ReimbursementService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar,
    private translate: TranslateService
  ) {}

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    this.loading = true;
    this.reimbursementService.getAll({
      page: this.currentPage,
      size: this.pageSize
    }).subscribe({
      next: (response: any) => {
        if (response.content) {
          this.data = response.content;
          this.totalItems = response.totalElements;
        }
        this.loading = false;
      },
      error: (error: any) => {
        console.error('Error loading reimbursements:', error);
        this.snackBar.open(
          this.translate.instant('ERROR.LOAD_REIMBURSEMENTS'),
          this.translate.instant('ACTIONS.CLOSE'),
          { duration: 3000 }
        );
        this.loading = false;
      }
    });
  }

  onPageChange(event: { page: number; size: number }): void {
    this.currentPage = event.page;
    this.pageSize = event.size;
    this.loadData();
  }

  onSortChange(event: { column: string; direction: string }): void {
    // Reimbursement service doesn't support sorting, so we just reload
    this.loadData();
  }

  onSearchChange(query: string): void {
    this.currentSearch = query;
    this.currentPage = 0;
    // For now, just reload all data since search is not implemented in service
    this.loadData();
  }

  onAdd(): void {
    const dialogRef = this.dialog.open(ReimbursementInputNewComponent, {
      width: '1000px',
      data: { mode: 'create' }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadData();
      }
    });
  }

  onView(item: Reimbursement): void {
    const dialogRef = this.dialog.open(ReimbursementInputNewComponent, {
      width: '1000px',
      data: { mode: 'view', reimbursement: item }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadData();
      }
    });
  }

  onEdit(item: Reimbursement): void {
    const dialogRef = this.dialog.open(ReimbursementInputNewComponent, {
      width: '1000px',
      data: { mode: 'edit', reimbursement: item }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadData();
      }
    });
  }

  onDelete(item: Reimbursement): void {
    if (confirm(this.translate.instant('REIMBURSEMENT.DELETE_CONFIRM'))) {
      this.reimbursementService.delete(item.id!).subscribe({
        next: () => {
          this.snackBar.open(
            this.translate.instant('REIMBURSEMENT.DELETE_SUCCESS'),
            this.translate.instant('ACTIONS.CLOSE'),
            { duration: 3000 }
          );
          this.loadData();
        },
        error: (error: any) => {
          console.error('Error deleting reimbursement:', error);
          this.snackBar.open(
            this.translate.instant('ERROR.DELETE_REIMBURSEMENT'),
            this.translate.instant('ACTIONS.CLOSE'),
            { duration: 3000 }
          );
        }
      });
    }
  }

  onExport(): void {
    // TODO: Implement export functionality
    console.log('Export reimbursements');
  }
} 