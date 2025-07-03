import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatTableDataSource } from '@angular/material/table';
import { TranslateService } from '@ngx-translate/core';
import { ProductService } from '../../services/product.service';
import { Product } from '../../models/product.model';
import { ProductDialogComponent } from './product-dialog.component';
import { PageEvent } from '@angular/material/paginator';

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.scss'],
})
export class ProductListComponent implements OnInit {
  displayedColumns: string[] = [
    'name',
    'modelNumber',
    'manufacturer',
    'type',
    'unit',
    'price',
    'description',
    'active',
    'actions',
  ];
  dataSource = new MatTableDataSource<Product>();
  loading = false;
  totalElements = 0;
  pageSize = 10;
  pageIndex = 0;
  searchText = '';

  constructor(
    private productService: ProductService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar,
    private translate: TranslateService
  ) {}

  ngOnInit(): void {
    this.loadProducts();
  }

  loadProducts(): void {
    this.loading = true;
    this.productService.getProducts(this.pageIndex, this.pageSize, this.searchText).subscribe({
      next: response => {
        if (response.code === 200 && response.data) {
          this.dataSource.data = response.data.content;
          this.totalElements = response.data.totalElements;
        }
        this.loading = false;
      },
      error: error => {
        console.error('Error loading products:', error);
        this.snackBar.open(
          this.translate.instant('ERROR.LOAD_PRODUCTS'),
          this.translate.instant('COMMON.CLOSE'),
          { duration: 3000 }
        );
        this.loading = false;
      },
    });
  }

  onSearch(query: string): void {
    this.searchText = query;
    this.pageIndex = 0;
    this.loadProducts();
  }

  onAdd(): void {
    const dialogRef = this.dialog.open(ProductDialogComponent, {
      width: '600px',
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadProducts();
      }
    });
  }

  onEdit(product: Product): void {
    const dialogRef = this.dialog.open(ProductDialogComponent, {
      width: '600px',
      data: product,
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadProducts();
      }
    });
  }

  onDelete(product: Product): void {
    if (confirm(this.translate.instant('product.confirm_delete'))) {
      this.loading = true;
      this.productService.delete(product.id!).subscribe({
        next: response => {
          if (response.code === 200) {
            this.snackBar.open(
              this.translate.instant('SUCCESS.DELETE_PRODUCT'),
              this.translate.instant('COMMON.CLOSE'),
              { duration: 3000 }
            );
            this.loadProducts();
          }
          this.loading = false;
        },
        error: error => {
          console.error('Error deleting product:', error);
          this.snackBar.open(
            this.translate.instant('ERROR.DELETE_PRODUCT'),
            this.translate.instant('COMMON.CLOSE'),
            { duration: 3000 }
          );
          this.loading = false;
        },
      });
    }
  }

  onToggleStatus(product: Product): void {
    this.loading = true;
    this.productService.toggleStatus(product.id!).subscribe({
      next: response => {
        if (response.code === 200) {
          this.snackBar.open(
            this.translate.instant('SUCCESS.UPDATE_PRODUCT_STATUS'),
            this.translate.instant('COMMON.CLOSE'),
            { duration: 3000 }
          );
          this.loadProducts();
        }
        this.loading = false;
      },
      error: error => {
        console.error('Error updating product status:', error);
        this.snackBar.open(
          this.translate.instant('ERROR.UPDATE_PRODUCT_STATUS'),
          this.translate.instant('COMMON.CLOSE'),
          { duration: 3000 }
        );
        this.loading = false;
        product.active = !product.active; // Revert the toggle
      },
    });
  }

  onPageChange(event: PageEvent): void {
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;
    this.loadProducts();
  }
}
