import { Component, OnInit, ViewChild, AfterViewInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, FormsModule } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator, PageEvent, MatPaginatorModule } from '@angular/material/paginator';
import { MatSort, Sort, MatSortModule } from '@angular/material/sort';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { debounceTime, distinctUntilChanged, Subject } from 'rxjs';
import { TranslateService, TranslateModule } from '@ngx-translate/core';
import { BaseListComponent } from '../../components/base-list/base-list.component';
import { ActionComponent } from '../../components/action/action.component';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatChipsModule } from '@angular/material/chips';
import { MatMenuModule } from '@angular/material/menu';
import { ConfirmDialogComponent } from '../../components/confirmdialog/confirm-dialog.component';
import { User, UserService } from '../../services/user.service';
import { UserEmployeeMappingComponent } from '../user-employee-mapping/user-employee-mapping.component';
import { UserEditDialogComponent } from './user-edit-dialog/user-edit-dialog.component';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-user-management',
  templateUrl: './user-management.component.html',
  styleUrls: ['./user-management.component.scss'],
})
export class UserManagementComponent implements OnInit, AfterViewInit {
  dataSource: MatTableDataSource<User>;
  isLoading = false;
  displayedColumns: string[] = ['id', 'username', 'email', 'roles', 'actions'];
  totalElements = 0;
  pageSize = 10;
  searchQuery = '';

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(
    private userService: UserService,
    private snackBar: MatSnackBar,
    private dialog: MatDialog,
    private translate: TranslateService
  ) {
    this.dataSource = new MatTableDataSource<User>([]);
  }

  ngOnInit(): void {
    this.loadUsers();
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  loadUsers(page: number = 0): void {
    this.isLoading = true;
    const sort = this.sort?.active ? {
      sortBy: this.sort.active,
      direction: this.sort.direction.toUpperCase() as 'ASC' | 'DESC'
    } : undefined;

    if (this.searchQuery) {
      this.userService.searchUsers(this.searchQuery, page, this.pageSize).subscribe({
        next: (response) => {
          this.handleUserResponse(response);
        },
        error: (error) => {
          this.handleError(error);
        }
      });
    } else {
      this.userService.getUsers(page, this.pageSize, sort).subscribe({
        next: (response) => {
          this.handleUserResponse(response);
        },
        error: (error) => {
          this.handleError(error);
        }
      });
    }
  }

  private handleUserResponse(response: any): void {
    if (response.data) {
      this.dataSource.data = response.data.content;
      this.totalElements = response.data.totalElements;
    }
    this.isLoading = false;
  }

  showCreateModal(): void {
    const dialogRef = this.dialog.open(UserEditDialogComponent, {
      data: {}
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.userService.createUser(result).subscribe({
          next: () => {
            this.loadUsers();
            this.snackBar.open(
              this.translate.instant('user.message.create.success'),
              this.translate.instant('common.close'),
              { duration: 3000 }
            );
          },
          error: (error) => {
            this.handleError(error);
          }
        });
      }
    });
  }

  showEditModal(user: User): void {
    const dialogRef = this.dialog.open(UserEditDialogComponent, {
      data: { user }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.userService.updateUser(user.id, result).subscribe({
          next: () => {
            this.loadUsers();
            this.snackBar.open(
              this.translate.instant('user.message.update.success'),
              this.translate.instant('common.close'),
              { duration: 3000 }
            );
          },
          error: (error) => {
            this.handleError(error);
          }
        });
      }
    });
  }

  confirmDelete(user: User): void {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: {
        title: this.translate.instant('user.dialog.delete.title'),
        message: this.translate.instant('user.dialog.delete.message', { username: user.username }),
        confirmText: this.translate.instant('common.delete'),
        cancelText: this.translate.instant('common.cancel')
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.userService.deleteUser(user.id).subscribe({
          next: () => {
            this.loadUsers();
            this.snackBar.open(
              this.translate.instant('user.message.delete.success'),
              this.translate.instant('common.close'),
              { duration: 3000 }
            );
          },
          error: (error) => {
            this.handleError(error);
          }
        });
      }
    });
  }

  showEmployeeMapping(user: User): void {
    const dialogRef = this.dialog.open(UserEmployeeMappingComponent, {
      data: { user },
      width: '600px'
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadUsers();
      }
    });
  }

  handleSearch(query: string): void {
    this.searchQuery = query;
    this.loadUsers();
  }

  onPageChange(event: PageEvent): void {
    this.loadUsers(event.pageIndex);
  }

  onSortChange(sort: Sort): void {
    this.loadUsers();
  }

  private handleError(error: HttpErrorResponse): void {
    let errorMessage = this.translate.instant('common.error.unknown');
    if (error.error?.message) {
      errorMessage = error.error.message;
    }
    this.snackBar.open(errorMessage, this.translate.instant('common.close'), {
      duration: 5000,
      panelClass: ['error-snackbar']
    });
    this.isLoading = false;
  }
}
