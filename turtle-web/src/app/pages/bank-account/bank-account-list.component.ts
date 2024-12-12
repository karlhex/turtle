import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatTableDataSource } from '@angular/material/table';
import { TranslateService } from '@ngx-translate/core';
import { BankAccountService } from '../../services/bank-account.service';
import { BankAccount } from '../../models/bank-account.model';
import { BankAccountDialogComponent } from './bank-account-dialog.component';
import { PageEvent } from '@angular/material/paginator';

@Component({
  selector: 'app-bank-account-list',
  templateUrl: './bank-account-list.component.html',
  styleUrls: ['./bank-account-list.component.scss']
})
export class BankAccountListComponent implements OnInit {
  displayedColumns: string[] = [
    'name',
    'accountNo',
    'bankName',
    'active',
    'actions'
  ];
  dataSource = new MatTableDataSource<BankAccount>();
  loading = false;
  totalElements = 0;
  pageSize = 10;
  pageIndex = 0;
  searchText = '';

  constructor(
    private bankAccountService: BankAccountService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar,
    private translate: TranslateService
  ) {}

  ngOnInit(): void {
    this.loadBankAccounts();
  }

  loadBankAccounts(): void {
    this.loading = true;
    this.bankAccountService
      .getBankAccounts({
        page: this.pageIndex,
        size: this.pageSize,
        search: this.searchText
      })
      .subscribe({
        next: (response) => {
          if (response.code === 200) {
            this.dataSource.data = response.data.content;
            this.totalElements = response.data.totalElements;
          }
          this.loading = false;
        },
        error: () => {
          this.loading = false;
          this.snackBar.open(
            this.translate.instant('COMMON.ERROR.LOAD_FAILED'),
            this.translate.instant('COMMON.CLOSE'),
            { duration: 3000 }
          );
        }
      });
  }

  onSearch(searchText: string): void {
    this.searchText = searchText;
    this.pageIndex = 0;
    this.loadBankAccounts();
  }

  onPageChange(event: PageEvent): void {
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;
    this.loadBankAccounts();
  }

  onAdd(): void {
    const dialogRef = this.dialog.open(BankAccountDialogComponent, {
      width: '600px',
      data: { mode: 'create' }
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result && result.code === 200) {
        this.loadBankAccounts();
      }
    });
  }

  onEdit(bankAccount: BankAccount): void {
    const dialogRef = this.dialog.open(BankAccountDialogComponent, {
      width: '600px',
      data: { mode: 'edit', bankAccount }
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result && result.code === 200) {
        this.loadBankAccounts();
      }
    });
  }

  onToggleStatus(bankAccount: BankAccount): void {
    this.bankAccountService.toggleStatus(bankAccount.id!).subscribe({
      next: (response) => {
        if (response.code === 200) {
          this.snackBar.open(
            this.translate.instant('COMMON.SUCCESS.UPDATE'),
            this.translate.instant('COMMON.CLOSE'),
            { duration: 3000 }
          );
          this.loadBankAccounts();
        }
      },
      error: () => {
        this.snackBar.open(
          this.translate.instant('COMMON.ERROR.UPDATE_FAILED'),
          this.translate.instant('COMMON.CLOSE'),
          { duration: 3000 }
        );
      }
    });
  }

  onDelete(bankAccount: BankAccount): void {
    if (confirm(this.translate.instant('COMMON.CONFIRM.DELETE'))) {
      this.bankAccountService.deleteBankAccount(bankAccount.id!).subscribe({
        next: (response) => {
          if (response.code === 200) {
            this.snackBar.open(
              this.translate.instant('COMMON.SUCCESS.DELETE'),
              this.translate.instant('COMMON.CLOSE'),
              { duration: 3000 }
            );
            this.loadBankAccounts();
          }
        },
        error: () => {
          this.snackBar.open(
            this.translate.instant('COMMON.ERROR.DELETE_FAILED'),
            this.translate.instant('COMMON.CLOSE'),
            { duration: 3000 }
          );
        }
      });
    }
  }
}
