import { Component, OnInit, OnDestroy } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { TranslateService } from '@ngx-translate/core';
import { Subject, takeUntil } from 'rxjs';
import { BankAccountService } from '../../services/bank-account.service';
import { BankAccount } from '../../models/bank-account.model';
import { BankAccountInputNewComponent } from './bank-account-input-new.component';
import { ListPageConfig } from '../../components/list-page/list-page.component';
import { ConfirmDialogComponent } from '../../components/confirmdialog/confirm-dialog.component';

@Component({
  selector: 'app-bank-account-list-new',
  templateUrl: './bank-account-list-new.component.html',
  styleUrls: ['./bank-account-list-new.component.scss']
})
export class BankAccountListNewComponent implements OnInit, OnDestroy {
  config: ListPageConfig = {
    title: 'BANK_ACCOUNT.TITLE',
    columns: [
      {
        key: 'name',
        label: 'BANK_ACCOUNT.NAME',
        type: 'text',
        sortable: true,
        width: '20%'
      },
      {
        key: 'accountNo',
        label: 'BANK_ACCOUNT.ACCOUNT_NO',
        type: 'text',
        sortable: false,
        width: '15%'
      },
      {
        key: 'bankName',
        label: 'BANK_ACCOUNT.BANK_NAME',
        type: 'text',
        sortable: true,
        width: '15%'
      },
      {
        key: 'type',
        label: 'BANK_ACCOUNT.TYPE',
        type: 'text',
        sortable: true,
        width: '10%',
        formatter: (value: string) => `BANK_ACCOUNT.TYPE.${value}`
      },
      {
        key: 'balance',
        label: 'BANK_ACCOUNT.BALANCE',
        type: 'number',
        sortable: true,
        width: '15%',
        formatter: (value: number) => value ? value.toLocaleString() : '0'
      },
      {
        key: 'currency.symbol',
        label: 'BANK_ACCOUNT.CURRENCY',
        type: 'text',
        sortable: false,
        width: '10%',
        formatter: (value: any) => value || '-'
      },
      {
        key: 'active',
        label: 'BANK_ACCOUNT.STATUS',
        type: 'boolean',
        sortable: true,
        width: '10%',
        formatter: (value: boolean) => value ? 'BANK_ACCOUNT.ACTIVE' : 'BANK_ACCOUNT.INACTIVE'
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
            action: (item: BankAccount) => this.onEdit(item)
          },
          {
            label: 'ACTIONS.DELETE',
            icon: 'delete',
            color: 'warn',
            action: (item: BankAccount) => this.onDelete(item)
          }
        ]
      }
    ],
    pageSizeOptions: [10, 20, 50],
    defaultPageSize: 20,
    showSearch: true,
    showExport: true,
    showBulkActions: true,
    searchPlaceholder: 'BANK_ACCOUNT.SEARCH_PLACEHOLDER',
    showAddButton: true
  };

  data: BankAccount[] = [];
  loading = false;
  totalItems = 0;
  currentPage = 0;
  pageSize = 20;
  searchText = '';

  private destroy$ = new Subject<void>();

  constructor(
    private bankAccountService: BankAccountService,
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
    
    this.bankAccountService.getBankAccounts(params)
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
          console.error('Error loading bank accounts:', error);
          this.snackBar.open(
            this.translate.instant('ERROR.LOAD_BANK_ACCOUNTS'),
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
    console.log('Export bank accounts');
  }

  onAdd(): void {
    const dialogRef = this.dialog.open(BankAccountInputNewComponent, {
      width: '800px',
      data: { mode: 'create' }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadData();
      }
    });
  }

  onEdit(bankAccount: BankAccount): void {
    const dialogRef = this.dialog.open(BankAccountInputNewComponent, {
      width: '800px',
      data: { mode: 'edit', bankAccount }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadData();
      }
    });
  }

  onDelete(bankAccount: BankAccount): void {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      width: '400px',
      data: {
        title: 'BANK_ACCOUNT.DELETE_CONFIRM_TITLE',
        message: 'BANK_ACCOUNT.DELETE_CONFIRM_MESSAGE',
        confirmText: 'ACTIONS.DELETE',
        cancelText: 'ACTIONS.CANCEL'
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.bankAccountService.deleteBankAccount(bankAccount.id!).subscribe({
          next: response => {
            if (response.code === 200) {
              this.snackBar.open(
                this.translate.instant('BANK_ACCOUNT.DELETE_SUCCESS'),
                this.translate.instant('ACTIONS.CLOSE'),
                { duration: 3000 }
              );
              this.loadData();
            }
          },
          error: error => {
            console.error('Error deleting bank account:', error);
            this.snackBar.open(
              this.translate.instant('ERROR.DELETE_BANK_ACCOUNT'),
              this.translate.instant('ACTIONS.CLOSE'),
              { duration: 3000 }
            );
          }
        });
      }
    });
  }
} 