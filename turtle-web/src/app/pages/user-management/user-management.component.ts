import { Component, OnInit, ViewChild } from '@angular/core';
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
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-user-management',
  templateUrl: './user-management.component.html',
  styleUrls: ['./user-management.component.scss'],
})
export class UserManagementComponent implements OnInit {
  users: User[] = [];
  userForm!: FormGroup;
  isModalVisible = false;
  modalTitle = '';
  isLoading = false;
  availableRoles = ['ROLE_USER', 'ROLE_ADMIN'];
  displayedColumns: string[] = ['id', 'username', 'email', 'roles', 'actions'];
  totalElements = 0;
  pageSize = 10;
  searchQuery = '';

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(
    private userService: UserService,
    private fb: FormBuilder,
    private snackBar: MatSnackBar,
    private dialog: MatDialog,
    private translate: TranslateService
  ) {
    this.createForm();
  }

  ngOnInit(): void {
    this.loadUsers();
  }

  createForm(): void {
    this.userForm = this.fb.group({
      id: [null],
      username: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required]],
      roleNames: [[], [Validators.required]]
    });
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
      this.users = response.data.content;
      this.totalElements = response.data.totalElements;
    }
    this.isLoading = false;
  }

  private handleError(error: any): void {
    console.error('Error:', error);
    this.snackBar.open(this.translate.instant('user.messages.error'), 'Close', {
      duration: 3000,
      horizontalPosition: 'center',
      verticalPosition: 'top',
    });
    this.isLoading = false;
  }

  public handleSearch(searchText: string): void {
    console.log('Search triggered:', searchText);
    this.searchQuery = searchText;
    this.loadUsers(0);
    if (this.paginator) {
      this.paginator.firstPage();
    }
  }

  onSortChange(sort: Sort): void {
    this.loadUsers(this.paginator?.pageIndex || 0);
  }

  onPageChange(event: PageEvent): void {
    this.loadUsers(event.pageIndex);
  }

  showCreateModal(): void {
    this.userForm.reset();
    this.userForm.get('roleNames')?.setValue([]);
    this.isModalVisible = true;
    this.modalTitle = 'user.dialog.create';
  }

  showEditModal(user: User): void {
    this.userForm.patchValue({
      id: user.id,
      username: user.username,
      email: user.email,
      roleNames: user.roleNames,
      password: ''
    });
    this.isModalVisible = true;
    this.modalTitle = 'user.dialog.edit';
  }

  confirmDelete(user: User): void {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      width: '400px',
      data: {
        title: 'user.dialog.delete',
        message: 'user.dialog.deleteConfirm',
        confirmText: 'common.button.confirm',
        cancelText: 'common.button.cancel'
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result && user.id) {
        this.userService.deleteUser(user.id as number).subscribe({
          next: () => {
            this.loadUsers();
            this.snackBar.open(this.translate.instant('user.messages.deleteSuccess'), 'Close', {
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

  saveUser(): void {
    if (this.userForm.valid) {
      const userData = this.userForm.value;
      const operation = userData.id
        ? this.userService.updateUser(userData.id, userData)
        : this.userService.createUser(userData);

      operation.subscribe({
        next: () => {
          this.loadUsers();
          this.isModalVisible = false;
          this.snackBar.open(
            this.translate.instant(userData.id ? 'user.messages.updateSuccess' : 'user.messages.createSuccess'),
            'Close',
            {
              duration: 3000,
              horizontalPosition: 'center',
              verticalPosition: 'top',
            }
          );
        },
        error: (error) => {
          this.handleError(error);
        }
      });
    }
  }

  showEmployeeMapping(user: User): void {
    const dialogRef = this.dialog.open(UserEmployeeMappingComponent, {
      width: '800px',
      data: { userId: user.id }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.snackBar.open('Employee mapping updated successfully', 'Close', {
          duration: 3000,
          horizontalPosition: 'center',
          verticalPosition: 'top',
        });
      }
    });
  }
}
