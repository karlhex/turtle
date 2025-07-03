import { Component, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatSortModule } from '@angular/material/sort';
import { TranslateService } from '@ngx-translate/core';
import { TaxInfoService } from '../../services/tax-info.service';
import { TaxInfo } from '../../models/tax-info.model';
import { BaseListComponent } from '../../components/base-list/base-list.component';
import { TaxInfoDialogComponent } from './tax-info-dialog.component';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { PageEvent } from '@angular/material/paginator';

@Component({
  selector: 'app-tax-info-list',
  templateUrl: './tax-info-list.component.html',
  styleUrls: ['./tax-info-list.component.scss'],
})
export class TaxInfoListComponent implements OnInit {
  displayedColumns: string[] = [
    'fullName',
    'taxNo',
    'bankName',
    'bankAccount',
    'phone',
    'active',
    'actions',
  ];
  dataSource = new MatTableDataSource<TaxInfo>();
  loading = false;
  totalElements = 0;
  pageSize = 10;
  pageIndex = 0;
  searchText = '';

  constructor(
    private taxInfoService: TaxInfoService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar,
    private translate: TranslateService
  ) {}

  ngOnInit(): void {
    this.loadTaxInfos();
  }

  loadTaxInfos(): void {
    this.loading = true;
    this.taxInfoService
      .getTaxInfos({
        page: this.pageIndex,
        size: this.pageSize,
        search: this.searchText || undefined,
      })
      .subscribe({
        next: response => {
          this.dataSource.data = response.data.content;
          this.totalElements = response.data.totalElements;
          this.loading = false;
        },
        error: error => {
          console.error('Error loading tax infos:', error);
          this.loading = false;
          this.snackBar.open(
            this.translate.instant('ERROR.LOADING_TAX_INFOS'),
            this.translate.instant('ACTIONS.CLOSE'),
            { duration: 3000 }
          );
        },
      });
  }

  onSearch(searchText: string): void {
    this.searchText = searchText;
    this.pageIndex = 0;
    this.loadTaxInfos();
  }

  onPageChange(event: PageEvent): void {
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;
    this.loadTaxInfos();
  }

  onAdd(): void {
    const dialogRef = this.dialog.open(TaxInfoDialogComponent, {
      width: '600px',
      data: { mode: 'create' },
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadTaxInfos();
      }
    });
  }

  onEdit(taxInfo: TaxInfo): void {
    const dialogRef = this.dialog.open(TaxInfoDialogComponent, {
      width: '600px',
      data: { mode: 'edit', taxInfo },
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadTaxInfos();
      }
    });
  }

  onToggleStatus(taxInfo: TaxInfo): void {
    this.loading = true;
    this.taxInfoService.toggleStatus(taxInfo.id!).subscribe({
      next: () => {
        this.loadTaxInfos();
        this.snackBar.open(
          this.translate.instant('SUCCESS.STATUS_UPDATED'),
          this.translate.instant('ACTIONS.CLOSE'),
          { duration: 3000 }
        );
      },
      error: error => {
        console.error('Error toggling status:', error);
        this.loading = false;
        this.snackBar.open(
          this.translate.instant('ERROR.UPDATING_STATUS'),
          this.translate.instant('ACTIONS.CLOSE'),
          { duration: 3000 }
        );
      },
    });
  }

  onDelete(taxInfo: TaxInfo): void {
    if (confirm(this.translate.instant('CONFIRM.DELETE_TAX_INFO'))) {
      this.loading = true;
      this.taxInfoService.deleteTaxInfo(taxInfo.id!).subscribe({
        next: () => {
          this.loadTaxInfos();
          this.snackBar.open(
            this.translate.instant('SUCCESS.TAX_INFO_DELETED'),
            this.translate.instant('ACTIONS.CLOSE'),
            { duration: 3000 }
          );
        },
        error: error => {
          console.error('Error deleting tax info:', error);
          this.loading = false;
          this.snackBar.open(
            this.translate.instant('ERROR.DELETING_TAX_INFO'),
            this.translate.instant('ACTIONS.CLOSE'),
            { duration: 3000 }
          );
        },
      });
    }
  }
}
