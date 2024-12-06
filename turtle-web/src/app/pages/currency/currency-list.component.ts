import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { MatSnackBar } from '@angular/material/snack-bar';
import { TranslateService } from '@ngx-translate/core';
import { debounceTime, distinctUntilChanged, Subject } from 'rxjs';
import { Currency } from '../../models/currency.model';
import { CurrencyService } from '../../services/currency.service';
import { CurrencyDialogComponent } from './currency-dialog.component';
import { ConfirmDialogComponent } from '../../components/confirmdialog/confirm-dialog.component';

@Component({
  selector: 'app-currency-list',
  templateUrl: './currency-list.component.html',
  styleUrls: ['./currency-list.component.scss']
})
export class CurrencyListComponent implements OnInit {
  displayedColumns: string[] = ['code', 'name', 'symbol', 'decimalPlaces', 'exchangeRate', 'active', 'isBaseCurrency', 'actions'];
  dataSource = new MatTableDataSource<Currency>();
  isLoading = false;
  totalElements = 0;
  pageSize = 10;
  pageIndex = 0;
  searchQuery = '';
  private searchSubject = new Subject<string>();

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(
    private currencyService: CurrencyService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar,
    private translate: TranslateService
  ) {
    this.setupSearch();
  }

  ngOnInit() {
    this.loadCurrencies();
  }

  setupSearch() {
    this.searchSubject.pipe(
      debounceTime(300),
      distinctUntilChanged()
    ).subscribe(() => {
      this.pageIndex = 0;
      this.loadCurrencies();
    });
  }

  onSearchChange(searchText: string) {
    this.searchQuery = searchText;
    this.searchSubject.next(searchText);
  }

  onPageChange(event: PageEvent) {
    this.pageSize = event.pageSize;
    this.pageIndex = event.pageIndex;
    this.loadCurrencies();
  }

  loadCurrencies() {
    this.isLoading = true;
    this.currencyService.getCurrencies({
      page: this.pageIndex,
      size: this.pageSize,
      search: this.searchQuery
    }).subscribe({
      next: (response) => {
        if (response.code === 200) {
          this.dataSource.data = response.data.content;
          this.totalElements = response.data.totalElements;
        } else {
          this.showError('Failed to load currencies: ' + response.message);
        }
        this.isLoading = false;
      },
      error: (error) => {
        this.showError('Error loading currencies');
        this.isLoading = false;
      }
    });
  }

  onAdd() {
    const dialogRef = this.dialog.open(CurrencyDialogComponent, {
      width: '50%',
      data: {
        currency: {},
        mode: 'create'
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.isLoading = true;
        this.currencyService.createCurrency(result).subscribe({
          next: (response) => {
            if (response.code === 200) {
              this.showSuccess('Currency created successfully');
              this.loadCurrencies();
            } else {
              this.showError('Failed to create currency: ' + response.message);
            }
            this.isLoading = false;
          },
          error: (error) => {
            this.showError('Error creating currency');
            this.isLoading = false;
          }
        });
      }
    });
  }

  onEdit(currency: Currency) {
    const dialogRef = this.dialog.open(CurrencyDialogComponent, {
      width: '50%',
      data: {
        currency: { ...currency },
        mode: 'edit'
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.isLoading = true;
        this.currencyService.updateCurrency(currency.id!, result).subscribe({
          next: (response) => {
            if (response.code === 200) {
              this.showSuccess('Currency updated successfully');
              this.loadCurrencies();
            } else {
              this.showError('Failed to update currency: ' + response.message);
            }
            this.isLoading = false;
          },
          error: (error) => {
            this.showError('Error updating currency');
            this.isLoading = false;
          }
        });
      }
    });
  }

  onDelete(currency: Currency) {
    if (currency.isBaseCurrency) {
      this.showError('Cannot delete base currency');
      return;
    }

    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      width: '400px',
      data: {
        title: this.translate.instant('currency.message.deleteConfirm'),
        message: currency.name
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.currencyService.deleteCurrency(currency.id!).subscribe({
          next: (response) => {
            if (response.code === 200) {
              this.showSuccess('Currency deleted successfully');
              this.loadCurrencies();
            } else {
              this.showError('Failed to delete currency: ' + response.message);
            }
          },
          error: (error) => {
            this.showError('Error deleting currency');
          }
        });
      }
    });
  }

  onToggleStatus(currency: Currency) {
    if (currency.isBaseCurrency && currency.active) {
      this.showError('Cannot deactivate base currency');
      return;
    }

    this.currencyService.toggleStatus(currency.id!).subscribe({
      next: (response) => {
        if (response.code === 200) {
          this.showSuccess('Currency status updated successfully');
          this.loadCurrencies();
        } else {
          this.showError('Failed to update currency status: ' + response.message);
        }
      },
      error: (error) => {
        this.showError('Error updating currency status');
      }
    });
  }

  onSetBaseCurrency(currency: Currency) {
    if (!currency.active) {
      this.showError('Cannot set inactive currency as base currency');
      return;
    }

    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      width: '400px',
      data: {
        title: this.translate.instant('currency.message.setBaseConfirm'),
        message: currency.name
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.currencyService.setBaseCurrency(currency.id!).subscribe({
          next: (response) => {
            if (response.code === 200) {
              this.showSuccess('Base currency set successfully');
              this.loadCurrencies();
            } else {
              this.showError('Failed to set base currency: ' + response.message);
            }
          },
          error: (error) => {
            this.showError('Error setting base currency');
          }
        });
      }
    });
  }

  getStatusChipClass(isActive: boolean): string {
    return isActive ? 'status-active' : 'status-inactive';
  }

  private showSuccess(message: string) {
    this.snackBar.open(message, 'Close', {
      duration: 3000,
      panelClass: ['success-snackbar']
    });
  }

  private showError(message: string) {
    this.snackBar.open(message, 'Close', {
      duration: 5000,
      panelClass: ['error-snackbar']
    });
  }
}
