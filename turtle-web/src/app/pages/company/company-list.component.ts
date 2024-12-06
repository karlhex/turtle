import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatTableDataSource } from '@angular/material/table';
import { TranslateService } from '@ngx-translate/core';
import { CompanyService } from '../../services/company.service';
import { Company } from '../../models/company.model';
import { CompanyDialogComponent } from './company-dialog.component';
import { PageEvent } from '@angular/material/paginator';

@Component({
  selector: 'app-company-list',
  templateUrl: './company-list.component.html',
  styleUrls: ['./company-list.component.scss']
})
export class CompanyListComponent implements OnInit {
  displayedColumns: string[] = [
    'fullName',
    'shortName',
    'address',
    'phone',
    'email',
    'active',
    'actions'
  ];
  dataSource = new MatTableDataSource<Company>();
  loading = false;
  totalElements = 0;
  pageSize = 10;
  pageIndex = 0;
  searchText = '';

  constructor(
    private companyService: CompanyService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar,
    private translate: TranslateService
  ) {}

  ngOnInit(): void {
    this.loadCompanies();
  }

  loadCompanies(): void {
    this.loading = true;
    this.companyService
      .getCompanies({
        page: this.pageIndex,
        size: this.pageSize,
        search: this.searchText
      })
      .subscribe({
        next: (response) => {
          this.dataSource.data = response.data.content;
          this.totalElements = response.data.totalElements;
          this.loading = false;
        },
        error: (error) => {
          console.error('Error loading companies:', error);
          this.loading = false;
          this.snackBar.open(
            this.translate.instant('ERROR.LOAD_COMPANIES'),
            this.translate.instant('ACTIONS.CLOSE'),
            { duration: 3000 }
          );
        }
      });
  }

  onSearch(searchText: string): void {
    this.searchText = searchText;
    this.pageIndex = 0;
    this.loadCompanies();
  }

  onAdd(): void {
    const dialogRef = this.dialog.open(CompanyDialogComponent, {
      width: '600px',
      data: { mode: 'create' }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadCompanies();
      }
    });
  }

  onEdit(company: Company): void {
    const dialogRef = this.dialog.open(CompanyDialogComponent, {
      width: '600px',
      data: { 
        mode: 'edit',
        company: company
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadCompanies();
      }
    });
  }

  onDelete(company: Company): void {
    if (confirm(this.translate.instant('COMPANY.CONFIRM_DELETE'))) {
      this.companyService.deleteCompany(company.id!).subscribe({
        next: () => {
          this.snackBar.open(
            this.translate.instant('COMPANY.DELETE_SUCCESS'),
            this.translate.instant('ACTIONS.CLOSE'),
            { duration: 3000 }
          );
          this.loadCompanies();
        },
        error: (error) => {
          console.error('Error deleting company:', error);
          this.snackBar.open(
            this.translate.instant('ERROR.DELETE_COMPANY'),
            this.translate.instant('ACTIONS.CLOSE'),
            { duration: 3000 }
          );
        }
      });
    }
  }

  onToggleStatus(company: Company): void {
    this.companyService.toggleStatus(company.id!).subscribe({
      next: () => {
        this.snackBar.open(
          this.translate.instant('COMPANY.STATUS_UPDATED'),
          this.translate.instant('ACTIONS.CLOSE'),
          { duration: 3000 }
        );
        this.loadCompanies();
      },
      error: (error) => {
        console.error('Error updating company status:', error);
        this.snackBar.open(
          this.translate.instant('ERROR.UPDATE_STATUS'),
          this.translate.instant('ACTIONS.CLOSE'),
          { duration: 3000 }
        );
      }
    });
  }

  onPageChange(event: PageEvent): void {
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;
    this.loadCompanies();
  }
}
