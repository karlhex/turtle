import { Component, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatDialog } from '@angular/material/dialog';
import { PageEvent } from '@angular/material/paginator';
import { MatSort, Sort, MatSortModule } from '@angular/material/sort';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { Employee, EmployeeService } from '@services/employee.service';
import { EmployeeDialogComponent } from './employee-dialog.component';
import { MatSnackBar } from '@angular/material/snack-bar';

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
    'isActive',
    'actions'
  ];
  dataSource = new MatTableDataSource<Employee>();
  totalElements = 0;
  pageSize = 10;
  isLoading = false;
  searchQuery = '';

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

  onSearch(searchText: string) {
    this.searchQuery = searchText;
    this.loadEmployees(0);

  }

  onPageChange(event: PageEvent) {
    this.pageSize = event.pageSize;
    this.loadEmployees(event.pageIndex);
  }

  onSortChange(sort: Sort) {
    this.loadEmployees(0);
  }

  openAddDialog(): void {
    const dialogRef = this.dialog.open(EmployeeDialogComponent, {
      width: '600px',
      data: { mode: 'add' }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.employeeService.createEmployee(result).subscribe({
          next: () => {
            this.loadEmployees();
            this.snackBar.open('Employee added successfully', 'Close', {
              duration: 3000,
              horizontalPosition: 'center',
              verticalPosition: 'top',
            });
          },
          error: (error) => {
            this.handleError(error);
          }
        });
      }
    });
  }

  openEditDialog(employee: Employee): void {
    if (!employee.id) {
      this.snackBar.open('Cannot edit employee: Missing ID', 'Close', {
        duration: 3000,
        horizontalPosition: 'center',
        verticalPosition: 'top',
      });
      return;
    }

    const dialogRef = this.dialog.open(EmployeeDialogComponent, {
      width: '600px',
      data: { mode: 'edit', employee }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.employeeService.updateEmployee(employee.id as number, result).subscribe({
          next: () => {
            this.loadEmployees();
            this.snackBar.open('Employee updated successfully', 'Close', {
              duration: 3000,
              horizontalPosition: 'center',
              verticalPosition: 'top',
            });
          },
          error: (error) => {
            this.handleError(error);
          }
        });
      }
    });
  }

  deleteEmployee(employee: Employee): void {
    if (!employee.id) {
      this.snackBar.open('Cannot delete employee: Missing ID', 'Close', {
        duration: 3000,
        horizontalPosition: 'center',
        verticalPosition: 'top',
      });
      return;
    }

    if (confirm('Are you sure you want to delete this employee?')) {
      this.employeeService.deleteEmployee(employee.id as number).subscribe({
        next: () => {
          this.loadEmployees();
          this.snackBar.open('Employee deleted successfully', 'Close', {
            duration: 3000,
            horizontalPosition: 'center',
            verticalPosition: 'top',
          });
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
