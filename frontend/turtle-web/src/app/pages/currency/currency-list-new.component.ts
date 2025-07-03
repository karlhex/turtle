import { Component, OnInit, OnDestroy } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { TranslateService } from '@ngx-translate/core';
import { Subject, takeUntil } from 'rxjs';
import { CurrencyService } from '../../services/currency.service';
import { Currency } from '../../models/currency.model';
import { CurrencyInputNewComponent } from './currency-input-new.component';
import { ListPageConfig, ColumnConfig, ActionConfig } from '../../components/list-page/list-page.component';
import { ConfirmDialogComponent } from '../../components/confirmdialog/confirm-dialog.component';

@Component({
  selector: 'app-currency-list-new',
  templateUrl: './currency-list-new.component.html',
  styleUrls: ['./currency-list-new.component.scss']
})
export class CurrencyListNewComponent implements OnInit, OnDestroy {
  config: ListPageConfig = {
    title: 'CURRENCY.TITLE',
    columns: [
      {
        key: 'code',
        label: 'CURRENCY.CODE',
        type: 'text',
        sortable: true,
        width: '15%'
      },
      {
        key: 'name',
        label: 'CURRENCY.NAME',
        type: 'text',
        sortable: true,
        width: '20%'
      },
      {
        key: 'symbol',
        label: 'CURRENCY.SYMBOL',
        type: 'text',
        sortable: false,
        width: '10%'
      },
      {
        key: 'decimalPlaces',
        label: 'CURRENCY.DECIMAL_PLACES',
        type: 'number',
        sortable: true,
        width: '10%'
      },
      {
        key: 'exchangeRate',
        label: 'CURRENCY.EXCHANGE_RATE',
        type: 'number',
        sortable: true,
        width: '15%',
        formatter: (value: number) => value ? value.toFixed(4) : '-'
      },
      {
        key: 'isBaseCurrency',
        label: 'CURRENCY.BASE_CURRENCY',
        type: 'boolean',
        sortable: true,
        width: '10%',
        formatter: (value: boolean) => value ? 'CURRENCY.YES' : 'CURRENCY.NO'
      },
      {
        key: 'active',
        label: 'CURRENCY.STATUS',
        type: 'boolean',
        sortable: true,
        width: '10%',
        formatter: (value: boolean) => value ? 'CURRENCY.ACTIVE' : 'CURRENCY.INACTIVE'
      },
      {
        key: 'action',
        label: 'ACTIONS.ACTIONS',
        type: 'action',
        sortable: false,
        width: '10%',
        actions: [
          {
            label: 'ACTIONS.EDIT',
            icon: 'edit',
            color: 'primary',
            action: (item: Currency) => this.onEdit(item)
          },
          {
            label: 'ACTIONS.DELETE',
            icon: 'delete',
            color: 'warn',
            action: (item: Currency) => this.onDelete(item)
          }
        ]
      }
    ],
    pageSizeOptions: [10, 20, 50],
    defaultPageSize: 20,
    showSearch: true,
    showExport: true,
    showBulkActions: true,
    searchPlaceholder: 'CURRENCY.SEARCH.PLACEHOLDER',
    showAddButton: true
  };

  data: Currency[] = [];
  loading = false;
  totalItems = 0;
  currentPage = 0;
  pageSize = 20;
  searchText = '';

  private destroy$ = new Subject<void>();

  constructor(
    private currencyService: CurrencyService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar,
    private translate: TranslateService
  ) {}

  ngOnInit(): void {
    this.loadData();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  loadData(): void {
    this.loading = true;
    const params = {
      page: this.currentPage,
      size: this.pageSize,
      search: this.searchText || undefined
    };
    
    this.currencyService.getCurrencies(params)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: response => {
          if (response.code === 200) {
            this.data = response.data.content;
            this.totalItems = response.data.totalElements;
          }
          this.loading = false;
        },
        error: error => {
          console.error('Error loading currencies:', error);
          this.snackBar.open(
            this.translate.instant('ERROR.LOAD_CURRENCIES'),
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
    // 实现排序逻辑
    console.log('Sort:', event);
    this.loadData();
  }

  onSearchChange(searchText: string): void {
    this.searchText = searchText;
    this.currentPage = 0;
    this.loadData();
  }

  onRefresh(): void {
    this.loadData();
  }

  onExport(): void {
    // 实现导出逻辑
    console.log('Export currencies');
  }

  onAdd(): void {
    const dialogRef = this.dialog.open(CurrencyInputNewComponent, {
      width: '800px',
      data: { mode: 'create' }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadData();
      }
    });
  }

  onEdit(currency: Currency): void {
    const dialogRef = this.dialog.open(CurrencyInputNewComponent, {
      width: '800px',
      data: { mode: 'edit', currency }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadData();
      }
    });
  }

  onDelete(currency: Currency): void {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      width: '400px',
      data: {
        title: 'CURRENCY.DELETE_CONFIRM_TITLE',
        message: 'CURRENCY.DELETE_CONFIRM_MESSAGE',
        confirmText: 'ACTIONS.DELETE',
        cancelText: 'ACTIONS.CANCEL'
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.currencyService.deleteCurrency(currency.id!).subscribe({
          next: response => {
            if (response.code === 200) {
              this.snackBar.open(
                this.translate.instant('CURRENCY.DELETE_SUCCESS'),
                this.translate.instant('ACTIONS.CLOSE'),
                { duration: 3000 }
              );
              this.loadData();
            }
          },
          error: error => {
            console.error('Error deleting currency:', error);
            this.snackBar.open(
              this.translate.instant('ERROR.DELETE_CURRENCY'),
              this.translate.instant('ACTIONS.CLOSE'),
              { duration: 3000 }
            );
          }
        });
      }
    });
  }
} 