import { Component, Input, Output, EventEmitter, OnInit, OnDestroy, OnChanges, SimpleChanges } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { MatSort } from '@angular/material/sort';
import { MatPaginator } from '@angular/material/paginator';
import { ViewChild } from '@angular/core';
import { Subject, takeUntil } from 'rxjs';

export interface ListPageConfig {
  title: string;
  columns: ColumnConfig[];
  pageSizeOptions: number[];
  defaultPageSize: number;
  showSearch: boolean;
  showExport: boolean;
  showBulkActions: boolean;
  searchPlaceholder?: string;
  showAddButton?: boolean;
}

export interface ColumnConfig {
  key: string;
  label: string;
  type: 'text' | 'number' | 'date' | 'boolean' | 'action' | 'custom';
  sortable: boolean;
  width?: string;
  formatter?: (value: any) => string;
  actions?: ActionConfig[];
}

export interface ActionConfig {
  label: string;
  icon: string;
  color: 'primary' | 'accent' | 'warn';
  action: (item: any) => void;
  disabled?: (item: any) => boolean;
  hidden?: (item: any) => boolean;
}

export interface BulkActionConfig {
  label: string;
  icon: string;
  color: 'primary' | 'accent' | 'warn';
  action: (items: any[]) => void;
  disabled?: (items: any[]) => boolean;
}

@Component({
  selector: 'app-list-page',
  templateUrl: './list-page.component.html',
  styleUrls: ['./list-page.component.scss']
})
export class ListPageComponent implements OnInit, OnDestroy, OnChanges {
  @Input() config!: ListPageConfig;
  @Input() data: any[] = [];
  @Input() loading = false;
  @Input() totalItems = 0;
  @Input() bulkActions: BulkActionConfig[] = [];
  @Input() onAdd?: () => void;

  @Output() pageChange = new EventEmitter<{ page: number; size: number }>();
  @Output() sortChange = new EventEmitter<{ column: string; direction: string }>();
  @Output() searchChange = new EventEmitter<string>();
  @Output() refresh = new EventEmitter<void>();
  @Output() export = new EventEmitter<void>();

  @ViewChild(MatSort) sort!: MatSort;
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  dataSource = new MatTableDataSource<any>([]);
  displayedColumns: string[] = [];
  selectedItems: any[] = [];
  searchValue = '';

  private destroy$ = new Subject<void>();

  ngOnInit(): void {
    this.initializeColumns();
    this.updateDataSource();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['data'] && !changes['data'].firstChange) {
      this.updateDataSource();
    }
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private initializeColumns(): void {
    this.displayedColumns = this.config.columns.map(col => col.key);
  }

  private updateDataSource(): void {
    this.dataSource.data = this.data;
    if (this.sort) {
      this.dataSource.sort = this.sort;
    }
    if (this.paginator) {
      this.dataSource.paginator = this.paginator;
    }
  }

  onSearch(value: string): void {
    this.searchValue = value;
    this.searchChange.emit(value);
  }

  onPageChange(event: any): void {
    this.pageChange.emit({
      page: event.pageIndex,
      size: event.pageSize
    });
  }

  onSortChange(event: any): void {
    this.sortChange.emit({
      column: event.active,
      direction: event.direction
    });
  }

  onRefresh(): void {
    this.refresh.emit();
  }

  onExport(): void {
    this.export.emit();
  }

  onBulkAction(action: BulkActionConfig): void {
    if (this.selectedItems.length > 0) {
      action.action(this.selectedItems);
    }
  }

  onSelectAll(event: any): void {
    if (event.checked) {
      this.selectedItems = [...this.data];
    } else {
      this.selectedItems = [];
    }
  }

  onSelectItem(item: any, event: any): void {
    if (event.checked) {
      this.selectedItems.push(item);
    } else {
      this.selectedItems = this.selectedItems.filter(i => i !== item);
    }
  }

  isSelected(item: any): boolean {
    return this.selectedItems.includes(item);
  }

  getColumnValue(item: any, column: ColumnConfig): any {
    const value = item[column.key];
    return column.formatter ? column.formatter(value) : value;
  }

  getBulkActionDisabled(action: BulkActionConfig): boolean {
    return action.disabled ? action.disabled(this.selectedItems) : this.selectedItems.length === 0;
  }

  getActionDisabled(action: ActionConfig, item: any): boolean {
    return action.disabled ? action.disabled(item) : false;
  }

  getActionHidden(action: ActionConfig, item: any): boolean {
    return action.hidden ? action.hidden(item) : false;
  }

  getInputValue(event: Event): string {
    return (event.target as HTMLInputElement)?.value ?? '';
  }
}
