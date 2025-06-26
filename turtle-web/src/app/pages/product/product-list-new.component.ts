import { Component, OnInit, OnDestroy } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { TranslateService } from '@ngx-translate/core';
import { Subject, takeUntil } from 'rxjs';
import { ProductService } from '../../services/product.service';
import { Product } from '../../models/product.model';
import { ProductInputNewComponent } from './product-input-new.component';
import { ListPageConfig, ColumnConfig, ActionConfig } from '../../components/list-page/list-page.component';
import { ConfirmDialogComponent } from '../../components/confirmdialog/confirm-dialog.component';

@Component({
  selector: 'app-product-list-new',
  templateUrl: './product-list-new.component.html',
  styleUrls: ['./product-list-new.component.scss']
})
export class ProductListNewComponent implements OnInit, OnDestroy {
  config: ListPageConfig = {
    title: 'PRODUCT.TITLE',
    columns: [
      {
        key: 'name',
        label: 'PRODUCT.NAME',
        type: 'text',
        sortable: true,
        width: '20%'
      },
      {
        key: 'modelNumber',
        label: 'PRODUCT.MODEL_NUMBER',
        type: 'text',
        sortable: true,
        width: '15%'
      },
      {
        key: 'manufacturer.fullName',
        label: 'PRODUCT.MANUFACTURER',
        type: 'text',
        sortable: false,
        width: '20%',
        formatter: (value: any) => value || '-'
      },
      {
        key: 'type',
        label: 'PRODUCT.TYPE',
        type: 'text',
        sortable: true,
        width: '10%',
        formatter: (value: string) => `PRODUCT.TYPE.${value}`
      },
      {
        key: 'unit',
        label: 'PRODUCT.UNIT',
        type: 'text',
        sortable: false,
        width: '10%'
      },
      {
        key: 'active',
        label: 'PRODUCT.STATUS',
        type: 'boolean',
        sortable: true,
        width: '10%',
        formatter: (value: boolean) => value ? 'PRODUCT.ACTIVE' : 'PRODUCT.INACTIVE'
      },
      {
        key: 'action',
        label: 'ACTIONS.ACTIONS',
        type: 'action',
        sortable: false,
        width: '15%',
        actions: [
          {
            label: 'ACTIONS.EDIT',
            icon: 'edit',
            color: 'primary',
            action: (item: Product) => this.onEdit(item)
          },
          {
            label: 'ACTIONS.DELETE',
            icon: 'delete',
            color: 'warn',
            action: (item: Product) => this.onDelete(item)
          }
        ]
      }
    ],
    pageSizeOptions: [10, 20, 50],
    defaultPageSize: 20,
    showSearch: true,
    showExport: true,
    showBulkActions: true,
    searchPlaceholder: 'PRODUCT.SEARCH_PLACEHOLDER',
    showAddButton: true
  };

  data: Product[] = [];
  loading = false;
  totalItems = 0;
  currentPage = 0;
  pageSize = 20;
  searchText = '';

  private destroy$ = new Subject<void>();

  constructor(
    private productService: ProductService,
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
    const request = this.searchText 
      ? this.productService.search(this.searchText, this.currentPage, this.pageSize)
      : this.productService.getProducts(this.currentPage, this.pageSize);
    
    request.pipe(takeUntil(this.destroy$))
      .subscribe({
        next: response => {
          if (response.code === 200) {
            this.data = response.data.content;
            this.totalItems = response.data.totalElements;
          }
          this.loading = false;
        },
        error: error => {
          console.error('Error loading products:', error);
          this.snackBar.open(
            this.translate.instant('ERROR.LOAD_PRODUCTS'),
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
    console.log('Export products');
  }

  onAdd(): void {
    const dialogRef = this.dialog.open(ProductInputNewComponent, {
      width: '800px',
      data: { mode: 'create' }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadData();
      }
    });
  }

  onEdit(product: Product): void {
    const dialogRef = this.dialog.open(ProductInputNewComponent, {
      width: '800px',
      data: { mode: 'edit', product }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadData();
      }
    });
  }

  onDelete(product: Product): void {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      width: '400px',
      data: {
        title: 'PRODUCT.DELETE_CONFIRM_TITLE',
        message: 'PRODUCT.DELETE_CONFIRM_MESSAGE',
        confirmText: 'ACTIONS.DELETE',
        cancelText: 'ACTIONS.CANCEL'
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.productService.delete(product.id!).subscribe({
          next: (response: any) => {
            if (response.code === 200) {
              this.snackBar.open(
                this.translate.instant('PRODUCT.DELETE_SUCCESS'),
                this.translate.instant('ACTIONS.CLOSE'),
                { duration: 3000 }
              );
              this.loadData();
            }
          },
          error: (error: any) => {
            console.error('Error deleting product:', error);
            this.snackBar.open(
              this.translate.instant('ERROR.DELETE_PRODUCT'),
              this.translate.instant('ACTIONS.CLOSE'),
              { duration: 3000 }
            );
          }
        });
      }
    });
  }
} 