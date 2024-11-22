import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatSort, Sort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Employee, EmployeeService } from '../../services/employee.service';
import { EmployeeDialogComponent } from './employee-dialog.component';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-employee-list',
  templateUrl: './employee-list.component.html',
  styleUrls: ['./employee-list.component.scss']
})
export class EmployeeListComponent implements OnInit {
  displayedColumns: string[] = [
    'employeeNumber',
    'name',
    'email',
    'department',
    'position',
    'hireDate',
    'isActive',
    'actions'
  ];
  dataSource = new MatTableDataSource<Employee>();
  totalElements = 0;
  pageSize = 10;
  isLoading = false;
  searchQuery = '';

  @ViewChild(MatPaginator) paginator!: MatPaginator;
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
      this.employeeService.searchEmployees(this.searchQuery, page, this.pageSize)
        .subscribe({
          next: (response) => {
            this.handleEmployeeResponse(response);
          },
          error: (error) => {
            this.handleError(error);
          }
        });
    } else {
      this.employeeService.getEmployees(page, this.pageSize, sort)
        .subscribe({
          next: (response) => {
            this.handleEmployeeResponse(response);
          },
          error: (error) => {
            this.handleError(error);
          }
        });
    }
  }

  private handleEmployeeResponse(response: any) {
    if (response.data) {
      this.dataSource.data = response.data.content;
      this.totalElements = response.data.totalElements;
    }
    this.isLoading = false;
  }

  private handleError(error: any) {
    console.error('Error:', error);
    this.snackBar.open('An error occurred while loading employees', 'Close', {
      duration: 3000,
      horizontalPosition: 'center',
      verticalPosition: 'top',
    });
    this.isLoading = false;
  }

  onSearch(event: Event) {
    const target = event.target as HTMLInputElement;
    this.searchQuery = target.value;
    this.loadEmployees(0);
    if (this.paginator) {
      this.paginator.firstPage();
    }
  }

  onPageChange(event: PageEvent) {
    this.pageSize = event.pageSize;
    this.loadEmployees(event.pageIndex);
  }

  onSortChange(sort: Sort) {
    this.loadEmployees(0);
  }

  onAdd() {
    const dialogRef = this.dialog.open(EmployeeDialogComponent, {
      width: '50%',
      data: {
        employee: {},
        mode: 'edit'
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.employeeService.createEmployee(result).subscribe({
          next: () => {
            this.snackBar.open('Employee created successfully', 'Close', {
              duration: 3000,
              horizontalPosition: 'center',
              verticalPosition: 'top',
            });
            this.loadEmployees(0);
          },
          error: (error) => {
            this.handleError(error);
          }
        });
      }
    });
  }

  onView(employee: Employee) {
    this.dialog.open(EmployeeDialogComponent, {
      width: '50%',
      data: {
        employee,
        mode: 'view'
      }
    });
  }

  onEdit(employee: Employee) {
    const dialogRef = this.dialog.open(EmployeeDialogComponent, {
      width: '50%',
      data: {
        employee,
        mode: 'edit'
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.employeeService.updateEmployee(employee.id!, result).subscribe({
          next: () => {
            this.snackBar.open('Employee updated successfully', 'Close', {
              duration: 3000,
              horizontalPosition: 'center',
              verticalPosition: 'top',
            });
            this.loadEmployees(this.paginator?.pageIndex || 0);
          },
          error: (error) => {
            this.handleError(error);
          }
        });
      }
    });
  }

  onDelete(employee: Employee) {
    if (confirm(`Are you sure you want to delete employee ${employee.name}?`)) {
      this.employeeService.deleteEmployee(employee.id!).subscribe({
        next: () => {
          this.snackBar.open('Employee deleted successfully', 'Close', {
            duration: 3000,
            horizontalPosition: 'center',
            verticalPosition: 'top',
          });
          this.loadEmployees(this.paginator?.pageIndex || 0);
        },
        error: (error) => {
          this.handleError(error);
        }
      });
    }
  }

  getDepartmentName(employee: Employee): string {
    return employee.department?.name || '-';
  }

  getStatusChipClass(isActive: boolean): string {
    return isActive ? 'status-active' : 'status-inactive';
  }
}
