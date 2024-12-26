import { Component, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatDialog } from '@angular/material/dialog';
import { PageEvent } from '@angular/material/paginator';
import { MatSort, Sort, MatSortModule } from '@angular/material/sort';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { EmployeeService } from '@services/employee.service';
import { EmployeeDialogComponent } from './employee-dialog.component';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Employee, EmployeeStatus } from '@models/employee.model';

@Component({
  selector: 'app-employee-list',
  templateUrl: './employee-list.component.html',
  styleUrls: ['./employee-list.component.scss'],
})
export class EmployeeListComponent implements OnInit {
  displayedColumns: string[] = [
    'employeeNumber',
    'name',
    'email',
    'department',
    'position',
    'hireDate',
    'status',
    'actions'
  ];
  dataSource = new MatTableDataSource<Employee>();
  totalElements = 0;
  pageSize = 10;
  isLoading = false;
  searchQuery = '';
  employeeStatus = EmployeeStatus;

  @ViewChild(MatSort) sort!: MatSort;

  constructor(
    private employeeService: EmployeeService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit() {
    this.loadEmployees();
  }

  loadEmployees(page: number = 0) {
    this.isLoading = true;
    const sort = this.sort?.active ? { 
      sortBy: this.sort.active, 
      direction: this.sort.direction.toUpperCase() as 'ASC' | 'DESC' 
    } : undefined;

    if (this.searchQuery) {
      this.employeeService.search(this.searchQuery, page, this.pageSize)
        .subscribe({
          next: (response) => {
            this.dataSource.data = response.data.content;
            this.totalElements = response.data.totalElements;
            this.isLoading = false;
          },
          error: (error) => {
            console.error('Error loading employees:', error);
            this.snackBar.open('加载员工列表失败', '关闭', { duration: 3000 });
            this.isLoading = false;
          }
        });
    } else {
      this.employeeService.getEmployees(page, this.pageSize, sort?.sortBy ? { sortBy: sort.sortBy, direction: sort.direction } : undefined)
        .subscribe({
          next: (response) => {
            this.dataSource.data = response.data.content;
            this.totalElements = response.data.totalElements;
            this.isLoading = false;
          },
          error: (error) => {
            console.error('Error loading employees:', error);
            this.snackBar.open('加载员工列表失败', '关闭', { duration: 3000 });
            this.isLoading = false;
          }
        });
    }
  }

  onPageChange(event: PageEvent) {
    this.loadEmployees(event.pageIndex);
  }

  onSortChange(sort: Sort) {
    this.loadEmployees();
  }

  onSearch(query: string) {
    this.searchQuery = query;
    this.loadEmployees();
  }

  openAddDialog() {
    const dialogRef = this.dialog.open(EmployeeDialogComponent, {
      width: '600px',
      data: { mode: 'add' }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadEmployees();
      }
    });
  }

  onEdit(employee: Employee) {
    const dialogRef = this.dialog.open(EmployeeDialogComponent, {
      width: '600px',
      data: { mode: 'edit', employee }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadEmployees();
      }
    });
  }

  onDelete(employee: Employee) {
    if (confirm('确定要删除该员工吗？')) {
      this.employeeService.deleteEmployee(employee.id!).subscribe({
        next: () => {
          this.snackBar.open('删除成功', '关闭', { duration: 3000 });
          this.loadEmployees();
        },
        error: (error) => {
          console.error('Error deleting employee:', error);
          this.snackBar.open('删除失败', '关闭', { duration: 3000 });
        }
      });
    }
  }

  getDepartmentName(employee: Employee): string {
    return employee.department?.name || '-';
  }

  getStatusChipClass(status: EmployeeStatus): string {
    switch (status) {
      case EmployeeStatus.ACTIVE:
        return 'status-active';
      case EmployeeStatus.RESIGNED:
        return 'status-resigned';
      case EmployeeStatus.SUSPENDED:
        return 'status-suspended';
      case EmployeeStatus.APPLICATION:
        return 'status-application';
      default:
        return 'status-inactive';
    }
  }
}
