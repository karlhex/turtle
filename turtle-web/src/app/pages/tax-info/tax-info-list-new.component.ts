import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { TranslateService } from '@ngx-translate/core';
import { ListPageConfig } from '../../components/list-page/list-page.component';
import { TaxInfo } from '../../models/tax-info.model';
import { TaxInfoService } from '../../services/tax-info.service';
import { TaxInfoInputNewComponent } from './tax-info-input-new.component';

@Component({
  selector: 'app-tax-info-list-new',
  templateUrl: './tax-info-list-new.component.html',
  styleUrls: ['./tax-info-list-new.component.scss']
})
export class TaxInfoListNewComponent implements OnInit {
  config: ListPageConfig = {
    title: 'TAX_INFO.TITLE',
    columns: [
      {
        key: 'fullName',
        label: 'TAX_INFO.FULL_NAME',
        type: 'text',
        sortable: true,
        width: '20%'
      },
      {
        key: 'taxNo',
        label: 'TAX_INFO.TAX_NO',
        type: 'text',
        sortable: true,
        width: '15%'
      },
      {
        key: 'address',
        label: 'TAX_INFO.ADDRESS',
        type: 'text',
        sortable: false,
        width: '25%'
      },
      {
        key: 'phone',
        label: 'TAX_INFO.PHONE',
        type: 'text',
        sortable: false,
        width: '15%'
      },
      {
        key: 'active',
        label: 'TAX_INFO.STATUS',
        type: 'boolean',
        sortable: true,
        width: '10%',
        formatter: (value: boolean) => value ? 'TAX_INFO.ACTIVE' : 'TAX_INFO.INACTIVE'
      },
      {
        key: 'actions',
        label: 'COMMON.ACTIONS',
        type: 'action',
        sortable: false,
        width: '15%',
        actions: [
          {
            label: 'COMMON.EDIT',
            icon: 'edit',
            color: 'primary',
            action: (item: TaxInfo) => this.onEdit(item)
          },
          {
            label: 'COMMON.DELETE',
            icon: 'delete',
            color: 'warn',
            action: (item: TaxInfo) => this.onDelete(item)
          }
        ]
      }
    ],
    pageSizeOptions: [5, 10, 25, 50],
    defaultPageSize: 10,
    showSearch: true,
    showExport: true,
    showBulkActions: false,
    searchPlaceholder: 'TAX_INFO.SEARCH_PLACEHOLDER',
    showAddButton: true
  };

  data: TaxInfo[] = [];
  loading = false;
  totalItems = 0;
  currentPage = 0;
  pageSize = 10;
  currentSearch = '';

  constructor(
    private taxInfoService: TaxInfoService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar,
    private translate: TranslateService
  ) {}

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    this.loading = true;
    this.taxInfoService.getTaxInfos({
      page: this.currentPage,
      size: this.pageSize,
      search: this.currentSearch
    }).subscribe({
      next: (response: any) => {
        if (response.code === 200) {
          this.data = response.data.content;
          this.totalItems = response.data.totalElements;
        }
        this.loading = false;
      },
      error: (error: any) => {
        console.error('Error loading tax info:', error);
        this.snackBar.open(
          this.translate.instant('ERROR.LOAD_TAX_INFO'),
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
    // Tax info service doesn't support sorting, so we just reload
    this.loadData();
  }

  onSearchChange(query: string): void {
    this.currentSearch = query;
    this.currentPage = 0;
    this.loadData();
  }

  onAdd(): void {
    const dialogRef = this.dialog.open(TaxInfoInputNewComponent, {
      width: '800px',
      data: { mode: 'create' }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadData();
      }
    });
  }

  onEdit(item: TaxInfo): void {
    const dialogRef = this.dialog.open(TaxInfoInputNewComponent, {
      width: '800px',
      data: { mode: 'edit', taxInfo: item }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadData();
      }
    });
  }

  onDelete(item: TaxInfo): void {
    if (confirm(this.translate.instant('TAX_INFO.DELETE_CONFIRM'))) {
      this.taxInfoService.deleteTaxInfo(item.id!).subscribe({
        next: (response: any) => {
          if (response.code === 200) {
            this.snackBar.open(
              this.translate.instant('TAX_INFO.DELETE_SUCCESS'),
              this.translate.instant('ACTIONS.CLOSE'),
              { duration: 3000 }
            );
            this.loadData();
          } else {
            this.snackBar.open(
              response.message || this.translate.instant('ERROR.DELETE_TAX_INFO'),
              this.translate.instant('ACTIONS.CLOSE'),
              { duration: 3000 }
            );
          }
        },
        error: (error: any) => {
          console.error('Error deleting tax info:', error);
          this.snackBar.open(
            this.translate.instant('ERROR.DELETE_TAX_INFO'),
            this.translate.instant('ACTIONS.CLOSE'),
            { duration: 3000 }
          );
        }
      });
    }
  }

  onExport(): void {
    // TODO: Implement export functionality
    console.log('Export tax info');
  }
} 