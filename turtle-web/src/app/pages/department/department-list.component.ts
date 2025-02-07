import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator, PageEvent, MatPaginatorModule } from '@angular/material/paginator';
import { MatSort, Sort, MatSortModule } from '@angular/material/sort';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { DepartmentService } from '../../services/department.service';
import { DepartmentDialogComponent } from './department-dialog.component';
import { debounceTime, distinctUntilChanged, Subject } from 'rxjs';
import { MatSnackBar } from '@angular/material/snack-bar';
import { TranslateService, TranslateModule } from '@ngx-translate/core';
import { ConfirmDialogComponent } from '../../components/confirmdialog/confirm-dialog.component';
import { Department } from '../../models/department.model';

@Component({
  selector: 'app-department-list',
  templateUrl: './department-list.component.html',
  styleUrls: ['./department-list.component.scss'],
})
export class DepartmentListComponent implements OnInit {
  displayedColumns: string[] = ['name', 'code', 'description', 'parentId', 'managerId', 'isActive', 'actions'];
  dataSource = new MatTableDataSource<Department>();
  isLoading = false;
  totalElements = 0;
  pageSize = 10;
  pageIndex = 0;
  searchQuery = '';
  private searchSubject = new Subject<string>();

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(
    private departmentService: DepartmentService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar,
    private translate: TranslateService
  ) {
    this.setupSearch();
  }

  ngOnInit() {
    this.loadDepartments();
  }

  setupSearch() {
    this.searchSubject.pipe(
      debounceTime(300),
      distinctUntilChanged()
    ).subscribe(() => {
      this.pageIndex = 0;
      this.loadDepartments();
    });
  }

  onSearch(event: Event) {
    const value = (event.target as HTMLInputElement).value;
    this.searchQuery = value;
    this.searchSubject.next(value);
  }

  onSearchChange(searchText: string) {
    this.searchQuery = searchText;
    this.searchSubject.next(searchText);
  }

  onPageChange(event: PageEvent) {
    this.pageSize = event.pageSize;
    this.pageIndex = event.pageIndex;
    this.loadDepartments();
  }

  loadDepartments() {
    this.isLoading = true;
    const request = this.searchQuery
      ? this.departmentService.searchDepartments(this.searchQuery, this.pageIndex, this.pageSize)
      : this.departmentService.getDepartments(this.pageIndex, this.pageSize);

    request.subscribe({
      next: (response) => {
        if (response.code === 200) {
          this.dataSource.data = response.data.content;
          this.totalElements = response.data.totalElements;
        } else {
          this.showError('Failed to load departments: ' + response.message);
        }
        this.isLoading = false;
      },
      error: (error) => {
        this.showError('Error loading departments');
        this.isLoading = false;
      }
    });
  }

  onAdd() {
    const dialogRef = this.dialog.open(DepartmentDialogComponent, {
      width: '50%',
      data: {
        department: {},
        mode: 'create'
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.isLoading = true;
        this.departmentService.createDepartment(result).subscribe({
          next: (response) => {
            if (response.code === 200) {
              this.showSuccess('Department created successfully');
              this.loadDepartments();
            } else {
              this.showError('Failed to create department: ' + response.message);
            }
            this.isLoading = false;
          },
          error: (error) => {
            this.showError('Error creating department');
            this.isLoading = false;
          }
        });
      }
    });
  }

  onEdit(department: Department) {
    const dialogRef = this.dialog.open(DepartmentDialogComponent, {
      width: '50%',
      data: {
        department: { ...department },
        mode: 'edit'
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.isLoading = true;
        this.departmentService.updateDepartment(department.id!, result).subscribe({
          next: (response) => {
            if (response.code === 200) {
              this.showSuccess('Department updated successfully');
              this.loadDepartments();
            } else {
              this.showError('Failed to update department: ' + response.message);
            }
            this.isLoading = false;
          },
          error: (error) => {
            this.showError('Error updating department');
            this.isLoading = false;
          }
        });
      }
    });
  }

  onDelete(department: Department) {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      width: '400px',
      data: {
        title: this.translate.instant('department.message.deleteConfirm'),
        message: department.name
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.departmentService.deleteDepartment(department.id!).subscribe({
          next: () => {
            this.loadDepartments();
            this.snackBar.open(
              this.translate.instant('department.message.deleteSuccess'),
              'OK',
              { duration: 3000 }
            );
          },
          error: (error) => {
            console.error('Error deleting department:', error);
            this.snackBar.open(error.message, 'OK', { duration: 3000 });
          }
        });
      }
    });
  }

  getStatusChipClass(isActive: boolean): string {
    return isActive ? 'status-active' : 'status-inactive';
  }

  private showSuccess(message: string) {
    this.snackBar.open(message, 'Close', {
      duration: 3000,
      panelClass: ['success-snackbar']
    });
  }

  private showError(message: string) {
    this.snackBar.open(message, 'Close', {
      duration: 5000,
      panelClass: ['error-snackbar']
    });
  }
}
