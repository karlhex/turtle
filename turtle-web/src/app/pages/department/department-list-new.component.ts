import { Component, OnInit, OnDestroy } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { TranslateService } from '@ngx-translate/core';
import { Subject, takeUntil } from 'rxjs';
import { DepartmentService } from '../../services/department.service';
import { Department } from '../../models/department.model';
import { DepartmentInputNewComponent } from './department-input-new.component';
import { ListPageConfig, ColumnConfig, ActionConfig } from '../../components/list-page/list-page.component';
import { ConfirmDialogComponent } from '../../components/confirmdialog/confirm-dialog.component';

@Component({
  selector: 'app-department-list-new',
  templateUrl: './department-list-new.component.html',
  styleUrls: ['./department-list-new.component.scss']
})
export class DepartmentListNewComponent implements OnInit, OnDestroy {
  config: ListPageConfig = {
    title: 'DEPARTMENT.TITLE',
    columns: [
      {
        key: 'name',
        label: 'DEPARTMENT.NAME',
        type: 'text',
        sortable: true,
        width: '20%'
      },
      {
        key: 'code',
        label: 'DEPARTMENT.CODE',
        type: 'text',
        sortable: true,
        width: '15%'
      },
      {
        key: 'description',
        label: 'DEPARTMENT.DESCRIPTION',
        type: 'text',
        sortable: false,
        width: '25%'
      },
      {
        key: 'isActive',
        label: 'DEPARTMENT.STATUS',
        type: 'boolean',
        sortable: true,
        width: '10%',
        formatter: (value: boolean) => value ? 'DEPARTMENT.ACTIVE' : 'DEPARTMENT.INACTIVE'
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
            action: (item: Department) => this.onEdit(item)
          },
          {
            label: 'ACTIONS.DELETE',
            icon: 'delete',
            color: 'warn',
            action: (item: Department) => this.onDelete(item)
          }
        ]
      }
    ],
    pageSizeOptions: [10, 20, 50],
    defaultPageSize: 20,
    showSearch: true,
    showExport: true,
    showBulkActions: true,
    searchPlaceholder: 'DEPARTMENT.SEARCH.PLACEHOLDER',
    showAddButton: true
  };

  data: Department[] = [];
  loading = false;
  totalItems = 0;
  currentPage = 0;
  pageSize = 20;
  searchText = '';

  private destroy$ = new Subject<void>();

  constructor(
    private departmentService: DepartmentService,
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
      ? this.departmentService.searchDepartments(this.searchText, this.currentPage, this.pageSize)
      : this.departmentService.getDepartments(this.currentPage, this.pageSize);
    
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
          console.error('Error loading departments:', error);
          this.snackBar.open(
            this.translate.instant('ERROR.LOAD_DEPARTMENTS'),
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
    console.log('Export departments');
  }

  onAdd(): void {
    const dialogRef = this.dialog.open(DepartmentInputNewComponent, {
      width: '800px',
      data: { mode: 'create' }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadData();
      }
    });
  }

  onEdit(department: Department): void {
    const dialogRef = this.dialog.open(DepartmentInputNewComponent, {
      width: '800px',
      data: { mode: 'edit', department }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadData();
      }
    });
  }

  onDelete(department: Department): void {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      width: '400px',
      data: {
        title: 'DEPARTMENT.DELETE_CONFIRM_TITLE',
        message: 'DEPARTMENT.DELETE_CONFIRM_MESSAGE',
        confirmText: 'ACTIONS.DELETE',
        cancelText: 'ACTIONS.CANCEL'
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.departmentService.deleteDepartment(department.id!).subscribe({
          next: response => {
            if (response.code === 200) {
              this.snackBar.open(
                this.translate.instant('DEPARTMENT.DELETE_SUCCESS'),
                this.translate.instant('ACTIONS.CLOSE'),
                { duration: 3000 }
              );
              this.loadData();
            }
          },
          error: error => {
            console.error('Error deleting department:', error);
            this.snackBar.open(
              this.translate.instant('ERROR.DELETE_DEPARTMENT'),
              this.translate.instant('ACTIONS.CLOSE'),
              { duration: 3000 }
            );
          }
        });
      }
    });
  }
} 