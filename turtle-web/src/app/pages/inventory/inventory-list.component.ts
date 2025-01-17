import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatTable } from '@angular/material/table';
import { PageEvent } from '@angular/material/paginator';
import { Inventory } from '../../models/inventory.model';
import { InventoryService } from '../../services/inventory.service';
import { InventoryDialogComponent } from './inventory-dialog.component';
import { BaseListComponent } from '../../components/base-list/base-list.component';
import { InventoryStatus } from '../../types/inventory-status.enum';
import { InventoryAction } from '../../types/inventory-action.enum';

@Component({
  selector: 'app-inventory-list',
  templateUrl: './inventory-list.component.html',
  styleUrls: ['./inventory-list.component.scss']
})
export class InventoryListComponent implements OnInit {
  @ViewChild(MatTable) table!: MatTable<Inventory>;
  @ViewChild(BaseListComponent) baseList!: BaseListComponent;

  displayedColumns: string[] = [
    'productName',
    'quantity', 
    'purchaseContractNo', 
    'storageTime',
    'license',
    'status',
    'actions'
  ];
  
  dataSource: Inventory[] = [];
  loading = false;
  totalElements = 0;
  pageSize = 10;
  pageIndex = 0;

  constructor(
    private inventoryService: InventoryService,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.loadData();
  }

  onPageChange(event: PageEvent): void {
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;
    this.loadData();
  }
  
  onSearch(searchText: string): void {
    this.pageIndex = 0;
    // TODO: Implement search functionality if needed
    this.loadData();
  }

  loadData(event?: PageEvent): void {
    this.loading = true;
    if (event) {
      this.pageIndex = event.pageIndex;
      this.pageSize = event.pageSize;
    }

    this.inventoryService.getAll(this.pageIndex, this.pageSize)
      .subscribe({
        next: (response) => {
          this.dataSource = response.data.content;
          this.totalElements = response.data.totalElements;
          this.loading = false;
        },
        error: (error) => {
          console.error('Error loading inventories:', error);
          this.loading = false;
        }
      });
  }

  openDialog(inventory?: Inventory, action?: InventoryAction): void {
    const dialogRef = this.dialog.open(InventoryDialogComponent, {
      width: '800px',
      data: {inventory, action}
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadData();
      }
    });
  }

  delete(id: number): void {
    if (confirm('Are you sure you want to delete this inventory?')) {
      this.inventoryService.delete(id).subscribe({
        next: () => {
          this.loadData();
        },
        error: (error) => {
          console.error('Error deleting inventory:', error);
        }
      });
    }
  }

  getInventoryActions(row: Inventory): InventoryAction[] {
    const baseActions = [InventoryAction.VIEW];
    switch (row.status) {
      case InventoryStatus.IN_STOCK:
        return [
          ...baseActions,
          InventoryAction.STORAGE,
          InventoryAction.OUTBOUND,
          InventoryAction.BORROW
        ];
      case InventoryStatus.BORROWED:
        return [
          ...baseActions,
          InventoryAction.RETURN
        ];
      case InventoryStatus.OUT_OF_STOCK:
        return baseActions;
      default:
        return baseActions;
    }
  }

  onAdd(): void {
    this.openDialog(undefined, InventoryAction.STORAGE);
  }

  getActionIcon(action: InventoryAction): string {
    switch (action) {
      case InventoryAction.VIEW: return 'visibility'; // Good for viewing details
      case InventoryAction.STORAGE: return 'warehouse'; // Better represents storage
      case InventoryAction.OUTBOUND: return 'local_shipping'; // Represents outgoing shipments
      case InventoryAction.BORROW: return 'swap_horizontal_circle'; // Better represents borrowing
      case InventoryAction.RETURN: return 'assignment_return'; // Clearly represents returning
      default: return 'help_outline';
    }
  }

  performInventoryAction(action: InventoryAction, row: Inventory): void {
    this.openDialog(row, action);
  }
}
