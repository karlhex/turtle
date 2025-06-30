import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { TranslateService } from '@ngx-translate/core';
import { ListPageConfig } from '../../components/list-page/list-page.component';
import { Employee, EmployeeStatus } from '../../models/employee.model';
import { EmployeeService } from '../../services/employee.service';
import { EmployeeInputNewComponent } from './employee-input-new.component';

@Component({
  selector: 'app-employee-list-new',
  templateUrl: './employee-list-new.component.html',
  styleUrls: ['./employee-list-new.component.scss']
})
export class EmployeeListNewComponent implements OnInit {
  config: ListPageConfig = {
    title: 'EMPLOYEE.TITLE',
    columns: [
      {
        key: 'employeeNumber',
        label: 'EMPLOYEE.LIST.ID',
        type: 'text',
        sortable: true,
        width: '12%'
      },
      {
        key: 'name',
        label: 'EMPLOYEE.LIST.NAME',
        type: 'text',
        sortable: true,
        width: '15%'
      },
      {
        key: 'email',
        label: 'EMPLOYEE.LIST.EMAIL',
        type: 'text',
        sortable: true,
        width: '18%'
      },
      {
        key: 'department',
        label: 'EMPLOYEE.LIST.DEPARTMENT',
        type: 'text',
        sortable: true,
        width: '15%',
        formatter: (value: any) => value?.name || '-'
      },
      {
        key: 'position',
        label: 'EMPLOYEE.LIST.POSITION',
        type: 'text',
        sortable: true,
        width: '15%',
        formatter: (value: any) => value?.name || '-'
      },
      {
        key: 'hireDate',
        label: 'EMPLOYEE.LIST.HIRE_DATE',
        type: 'date',
        sortable: true,
        width: '12%'
      },
      {
        key: 'status',
        label: 'EMPLOYEE.LIST.STATUS',
        type: 'text',
        sortable: true,
        width: '10%',
        formatter: (value: EmployeeStatus) => {
          switch (value) {
            case EmployeeStatus.APPLICATION: return this.translate.instant('employee.status.application');
            case EmployeeStatus.ACTIVE: return this.translate.instant('employee.status.active');
            case EmployeeStatus.RESIGNED: return this.translate.instant('employee.status.resigned');
            case EmployeeStatus.SUSPENDED: return this.translate.instant('employee.status.suspended');
            default: return value;
          }
        }
      },
      {
        key: 'actions',
        label: 'COMMON.ACTIONS',
        type: 'action',
        sortable: false,
        width: '12%',
        actions: [
          {
            label: 'COMMON.VIEW',
            icon: 'visibility',
            color: 'primary',
            action: (item: Employee) => this.onView(item)
          },
          {
            label: 'COMMON.EDIT',
            icon: 'edit',
            color: 'primary',
            action: (item: Employee) => this.onEdit(item)
          },
          {
            label: 'COMMON.DELETE',
            icon: 'delete',
            color: 'warn',
            action: (item: Employee) => this.onDelete(item),
            hidden: (item: Employee) => item.status === EmployeeStatus.ACTIVE
          }
        ]
      }
    ],
    pageSizeOptions: [5, 10, 25, 50],
    defaultPageSize: 10,
    showSearch: true,
    showExport: true,
    showBulkActions: false,
    searchPlaceholder: 'EMPLOYEE.SEARCH.PLACEHOLDER',
    showAddButton: true
  };

  data: Employee[] = [];
  loading = false;
  totalItems = 0;
  currentPage = 0;
  pageSize = 10;
  currentSearch = '';

  constructor(
    private employeeService: EmployeeService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar,
    private translate: TranslateService
  ) {}

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    this.loading = true;
    this.employeeService.getEmployees(this.currentPage, this.pageSize).subscribe({
      next: (response: any) => {
        if (response.code === 200) {
          this.data = response.data.content || response.data;
          this.totalItems = response.data.totalElements || this.data.length;
        }
        this.loading = false;
      },
      error: (error: any) => {
        console.error('Error loading employees:', error);
        this.snackBar.open(
          this.translate.instant('ERROR.LOAD_EMPLOYEES'),
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
    // Employee service doesn't support sorting, so we just reload
    this.loadData();
  }

  onSearchChange(query: string): void {
    this.currentSearch = query;
    this.currentPage = 0;
    if (query.trim()) {
      this.searchEmployees(query);
    } else {
      this.loadData();
    }
  }

  private searchEmployees(query: string): void {
    this.loading = true;
    this.employeeService.search(query, this.currentPage, this.pageSize).subscribe({
      next: (response: any) => {
        if (response.code === 200) {
          this.data = response.data.content || response.data;
          this.totalItems = response.data.totalElements || this.data.length;
        }
        this.loading = false;
      },
      error: (error: any) => {
        console.error('Error searching employees:', error);
        this.snackBar.open(
          this.translate.instant('ERROR.SEARCH_EMPLOYEES'),
          this.translate.instant('ACTIONS.CLOSE'),
          { duration: 3000 }
        );
        this.loading = false;
      }
    });
  }

  onAdd(): void {
    const dialogRef = this.dialog.open(EmployeeInputNewComponent, {
      width: '1200px',
      data: { mode: 'create' }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadData();
      }
    });
  }

  onView(item: Employee): void {
    const dialogRef = this.dialog.open(EmployeeInputNewComponent, {
      width: '1200px',
      data: { mode: 'view', employee: item }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadData();
      }
    });
  }

  onEdit(item: Employee): void {
    const dialogRef = this.dialog.open(EmployeeInputNewComponent, {
      width: '1200px',
      data: { mode: 'edit', employee: item }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadData();
      }
    });
  }

  onDelete(item: Employee): void {
    if (confirm(this.translate.instant('EMPLOYEE.DELETE_CONFIRM'))) {
      this.employeeService.deleteEmployee(item.id!).subscribe({
        next: () => {
          this.snackBar.open(
            this.translate.instant('EMPLOYEE.DELETE_SUCCESS'),
            this.translate.instant('ACTIONS.CLOSE'),
            { duration: 3000 }
          );
          this.loadData();
        },
        error: (error: any) => {
          console.error('Error deleting employee:', error);
          this.snackBar.open(
            this.translate.instant('ERROR.DELETE_EMPLOYEE'),
            this.translate.instant('ACTIONS.CLOSE'),
            { duration: 3000 }
          );
        }
      });
    }
  }

  onExport(): void {
    // TODO: Implement export functionality
    console.log('Export employees');
  }
} 