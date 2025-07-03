import { Component, OnInit, OnDestroy } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { TranslateService } from '@ngx-translate/core';
import { Subject, takeUntil } from 'rxjs';
import { InventoryService } from '../../services/inventory.service';
import { Inventory } from '../../models/inventory.model';
import { InventoryInputNewComponent } from './inventory-input-new.component';
import { ListPageConfig } from '../../components/list-page/list-page.component';
import { ConfirmDialogComponent } from '../../components/confirmdialog/confirm-dialog.component';

@Component({
  selector: 'app-inventory-list-new',
  templateUrl: './inventory-list-new.component.html',
  styleUrls: ['./inventory-list-new.component.scss']
})
export class InventoryListNewComponent implements OnInit, OnDestroy {
  config: ListPageConfig = {
    title: 'INVENTORY.TITLE',
    columns: [
      {
        key: 'productName',
        label: 'INVENTORY.PRODUCT_NAME',
        type: 'text',
        sortable: true,
        width: '20%'
      },
      {
        key: 'quantity',
        label: 'INVENTORY.QUANTITY',
        type: 'number',
        sortable: true,
        width: '10%'
      },
      {
        key: 'status',
        label: 'INVENTORY.STATUS',
        type: 'text',
        sortable: true,
        width: '10%',
        formatter: (value: string) => `INVENTORY.STATUS.${value}`
      },
      {
        key: 'storageTime',
        label: 'INVENTORY.STORAGE_TIME',
        type: 'date',
        sortable: true,
        width: '15%'
      },
      {
        key: 'borrowedCompanyName',
        label: 'INVENTORY.BORROWED_COMPANY',
        type: 'text',
        sortable: false,
        width: '15%'
      },
      {
        key: 'handlingEmployeeName',
        label: 'INVENTORY.HANDLING_EMPLOYEE',
        type: 'text',
        sortable: false,
        width: '15%'
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
            action: (item: Inventory) => this.onEdit(item)
          },
          {
            label: 'ACTIONS.DELETE',
            icon: 'delete',
            color: 'warn',
            action: (item: Inventory) => this.onDelete(item)
          }
        ]
      }
    ],
    pageSizeOptions: [10, 20, 50],
    defaultPageSize: 20,
    showSearch: true,
    showExport: true,
    showBulkActions: true,
    searchPlaceholder: 'INVENTORY.SEARCH_PLACEHOLDER',
    showAddButton: true
  };

  data: Inventory[] = [];
  loading = false;
  totalItems = 0;
  currentPage = 0;
  pageSize = 20;
  searchText = '';

  private destroy$ = new Subject<void>();

  constructor(
    private inventoryService: InventoryService,
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
    this.inventoryService.getAll(this.currentPage, this.pageSize)
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
          console.error('Error loading inventories:', error);
          this.snackBar.open(
            this.translate.instant('ERROR.LOAD_INVENTORIES'),
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
    console.log('Export inventories');
  }

  onAdd(): void {
    const dialogRef = this.dialog.open(InventoryInputNewComponent, {
      width: '800px',
      data: { mode: 'create' }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadData();
      }
    });
  }

  onEdit(inventory: Inventory): void {
    const dialogRef = this.dialog.open(InventoryInputNewComponent, {
      width: '800px',
      data: { mode: 'edit', inventory }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadData();
      }
    });
  }

  onDelete(inventory: Inventory): void {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      width: '400px',
      data: {
        title: 'INVENTORY.DELETE_CONFIRM_TITLE',
        message: 'INVENTORY.DELETE_CONFIRM_MESSAGE',
        confirmText: 'ACTIONS.DELETE',
        cancelText: 'ACTIONS.CANCEL'
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.inventoryService.delete(inventory.id!).subscribe({
          next: (response: any) => {
            if (response.code === 200) {
              this.snackBar.open(
                this.translate.instant('INVENTORY.DELETE_SUCCESS'),
                this.translate.instant('ACTIONS.CLOSE'),
                { duration: 3000 }
              );
              this.loadData();
            }
          },
          error: (error: any) => {
            console.error('Error deleting inventory:', error);
            this.snackBar.open(
              this.translate.instant('ERROR.DELETE_INVENTORY'),
              this.translate.instant('ACTIONS.CLOSE'),
              { duration: 3000 }
            );
          }
        });
      }
    });
  }
} 