import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { TranslateService } from '@ngx-translate/core';
import { CompanyService } from '../../services/company.service';
import { Company } from '../../models/company.model';
import { CompanyInputNewComponent } from './company-input-new.component';
import { CompanyType } from '../../types/company-type.enum';
import { ConfirmDialogComponent } from '../../components/confirmdialog/confirm-dialog.component';
import { ListPageConfig, ColumnConfig, ActionConfig, BulkActionConfig } from '../../components/list-page/list-page.component';

@Component({
  selector: 'app-company-list-new',
  templateUrl: './company-list-new.component.html',
  styleUrls: ['./company-list-new.component.scss'],
})
export class CompanyListNewComponent implements OnInit {
  config: ListPageConfig = {
    title: 'COMPANY.TITLE',
    showSearch: true,
    searchPlaceholder: 'COMPANY.SEARCH_PLACEHOLDER',
    showBulkActions: true,
    showExport: true,
    showAddButton: true,
    defaultPageSize: 10,
    pageSizeOptions: [5, 10, 25, 50],
    columns: [
      {
        key: 'fullName',
        label: 'COMPANY.FULL_NAME',
        type: 'text',
        sortable: true,
        width: '200px'
      },
      {
        key: 'shortName',
        label: 'COMPANY.SHORT_NAME',
        type: 'text',
        sortable: true,
        width: '150px'
      },
      {
        key: 'address',
        label: 'COMPANY.ADDRESS',
        type: 'text',
        sortable: false,
        width: '250px'
      },
      {
        key: 'phone',
        label: 'COMPANY.PHONE',
        type: 'text',
        sortable: false,
        width: '120px'
      },
      {
        key: 'email',
        label: 'COMPANY.EMAIL',
        type: 'text',
        sortable: false,
        width: '180px'
      },
      {
        key: 'type',
        label: 'COMPANY.TYPE',
        type: 'text',
        sortable: true,
        width: '100px'
      },
      {
        key: 'active',
        label: 'COMPANY.STATUS',
        type: 'boolean',
        sortable: true,
        width: '80px'
      },
      {
        key: 'actions',
        label: 'ACTIONS.TITLE',
        type: 'action',
        sortable: false,
        width: '120px',
        actions: [
          {
            label: 'ACTIONS.EDIT',
            icon: 'edit',
            color: 'primary',
            action: (row: Company) => this.onEdit(row)
          },
          {
            label: 'ACTIONS.DELETE',
            icon: 'delete',
            color: 'warn',
            action: (row: Company) => this.onDelete(row)
          },
          {
            label: 'COMPANY.TOGGLE_STATUS',
            icon: 'toggle_on',
            color: 'accent',
            action: (row: Company) => this.onToggleStatus(row)
          },
          {
            label: 'COMPANY.SET_PRIMARY',
            icon: 'star',
            color: 'primary',
            action: (row: Company) => this.onSetPrimary(row),
            hidden: (row: Company) => row.type === CompanyType.PRIMARY
          }
        ]
      }
    ]
  };

  bulkActions: BulkActionConfig[] = [
    {
      label: 'ACTIONS.DELETE_SELECTED',
      icon: 'delete',
      color: 'warn',
      action: (items: Company[]) => this.onBulkDelete(items)
    },
    {
      label: 'COMPANY.ACTIVATE_SELECTED',
      icon: 'check_circle',
      color: 'primary',
      action: (items: Company[]) => this.onBulkActivate(items)
    },
    {
      label: 'COMPANY.DEACTIVATE_SELECTED',
      icon: 'cancel',
      color: 'accent',
      action: (items: Company[]) => this.onBulkDeactivate(items)
    }
  ];

  data: Company[] = [];
  loading = false;
  totalItems = 0;
  selectedItems: Company[] = [];
  searchValue = '';

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
    this.companyService.getCompanies({
      page: 0,
      size: 1000, // 获取所有数据用于演示
      search: this.searchValue
    }).subscribe({
      next: response => {
        console.log('response: ', response);
        if (response.code === 200) {
          this.data = response.data.content.sort((a: Company, b: Company) =>
            a.type === CompanyType.PRIMARY ? -1 : 1
          );
          this.totalItems = response.data.totalElements;
        }
        this.loading = false;
      },
      error: error => {
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

  onSearch(value: string): void {
    this.searchValue = value;
    this.loadCompanies();
  }

  onRefresh(): void {
    this.loadCompanies();
  }

  onExport(): void {
    // 实现导出功能
    this.snackBar.open('导出功能待实现', '关闭', { duration: 2000 });
  }

  onAdd(): void {
    const dialogRef = this.dialog.open(CompanyInputNewComponent, {
      width: '800px',
      data: { mode: 'create' }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadCompanies();
      }
    });
  }

  onEdit(company: Company): void {
    const dialogRef = this.dialog.open(CompanyInputNewComponent, {
      width: '800px',
      data: { mode: 'edit', company }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadCompanies();
      }
    });
  }

  onDelete(company: Company): void {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: {
        title: 'COMPANY.DELETE_TITLE',
        message: 'COMPANY.CONFIRM_DELETE',
        confirmText: 'ACTIONS.DELETE',
        cancelText: 'ACTIONS.CANCEL'
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.companyService.deleteCompany(company.id!).subscribe({
          next: () => {
            this.snackBar.open(
              this.translate.instant('COMPANY.DELETE_SUCCESS'),
              this.translate.instant('ACTIONS.CLOSE'),
              { duration: 3000 }
            );
            this.loadCompanies();
          },
          error: error => {
            console.error('Error deleting company:', error);
            this.snackBar.open(
              this.translate.instant('ERROR.DELETE_COMPANY'),
              this.translate.instant('ACTIONS.CLOSE'),
              { duration: 3000 }
            );
          }
        });
      }
    });
  }

  onToggleStatus(company: Company): void {
    this.companyService.toggleStatus(company.id!).subscribe({
      next: response => {
        if (response.code === 200) {
          this.snackBar.open(
            this.translate.instant('COMPANY.STATUS_UPDATED'),
            this.translate.instant('ACTIONS.CLOSE'),
            { duration: 3000 }
          );
          this.loadCompanies();
        }
      },
      error: error => {
        console.error('Error updating company status:', error);
        this.snackBar.open(
          this.translate.instant('ERROR.UPDATE_STATUS'),
          this.translate.instant('ACTIONS.CLOSE'),
          { duration: 3000 }
        );
      }
    });
  }

  onSetPrimary(company: Company): void {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: {
        title: 'COMPANY.SET_PRIMARY_TITLE',
        message: 'COMPANY.SET_PRIMARY_CONFIRM',
        confirmText: 'ACTIONS.CONFIRM',
        cancelText: 'ACTIONS.CANCEL'
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loading = true;
        this.companyService.setPrimary(company.id!).subscribe({
          next: response => {
            if (response.code === 200) {
              this.snackBar.open(
                this.translate.instant('COMPANY.PRIMARY_UPDATED'),
                this.translate.instant('ACTIONS.CLOSE'),
                { duration: 3000 }
              );
              this.loadCompanies();
            }
          },
          error: error => {
            console.error('Error setting company as primary:', error);
            this.snackBar.open(
              error.error?.message || this.translate.instant('ERROR.SET_PRIMARY'),
              this.translate.instant('ACTIONS.CLOSE'),
              { duration: 3000 }
            );
            this.loading = false;
          }
        });
      }
    });
  }

  onBulkAction(action: BulkActionConfig): void {
    if (this.selectedItems.length === 0) return;

    switch (action.label) {
      case 'ACTIONS.DELETE_SELECTED':
        // 批量删除
        this.snackBar.open(`批量删除 ${this.selectedItems.length} 项`, '关闭', { duration: 2000 });
        break;
      case 'COMPANY.ACTIVATE_SELECTED':
        // 批量激活
        this.snackBar.open(`批量激活 ${this.selectedItems.length} 项`, '关闭', { duration: 2000 });
        break;
      case 'COMPANY.DEACTIVATE_SELECTED':
        // 批量停用
        this.snackBar.open(`批量停用 ${this.selectedItems.length} 项`, '关闭', { duration: 2000 });
        break;
    }
  }

  onBulkDelete(items: Company[]): void {
    this.snackBar.open(`批量删除 ${items.length} 项`, '关闭', { duration: 2000 });
  }

  onBulkActivate(items: Company[]): void {
    this.snackBar.open(`批量激活 ${items.length} 项`, '关闭', { duration: 2000 });
  }

  onBulkDeactivate(items: Company[]): void {
    this.snackBar.open(`批量停用 ${items.length} 项`, '关闭', { duration: 2000 });
  }

  onPageChange(event: any): void {
    // 处理分页
    console.log('Page change:', event);
  }

  onSortChange(event: any): void {
    // 处理排序
    console.log('Sort change:', event);
  }

  onSelectItem(item: Company, event: any): void {
    if (event.checked) {
      this.selectedItems.push(item);
    } else {
      this.selectedItems = this.selectedItems.filter(i => i.id !== item.id);
    }
  }

  onSelectAll(event: any): void {
    if (event.checked) {
      this.selectedItems = [...this.data];
    } else {
      this.selectedItems = [];
    }
  }

  isSelected(item: Company): boolean {
    return this.selectedItems.some(i => i.id === item.id);
  }

  getColumnValue(row: Company, column: ColumnConfig): any {
    switch (column.key) {
      case 'type':
        return this.translate.instant(`COMPANY.TYPE.${row.type}`);
      case 'active':
        return row.active;
      default:
        return row[column.key as keyof Company];
    }
  }

  getActionDisabled(action: ActionConfig, row: Company): boolean {
    return false;
  }

  getActionHidden(action: ActionConfig, row: Company): boolean {
    if (action.label === 'COMPANY.SET_PRIMARY') {
      return row.type === CompanyType.PRIMARY;
    }
    return false;
  }

  getBulkActionDisabled(action: BulkActionConfig): boolean {
    return this.selectedItems.length === 0;
  }

  getInputValue(event: Event): string {
    const target = event.target as HTMLInputElement;
    return target ? target.value : '';
  }
} 