import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialogComponent } from '../confirmdialog/confirm-dialog.component';
import { User, UserService } from '../../services/user.service';
import { HttpErrorResponse } from '@angular/common/http';
import { UserEmployeeMappingComponent } from '../user-employee-mapping/user-employee-mapping.component';

@Component({
  selector: 'app-user-management',
  templateUrl: './user-management.component.html',
  styleUrls: ['./user-management.component.scss']
})
export class UserManagementComponent implements OnInit {
  users: User[] = [];
  userForm!: FormGroup;
  isModalVisible = false;
  modalTitle = '';
  loading = false;
  availableRoles = ['ROLE_USER', 'ROLE_ADMIN'];
  displayedColumns: string[] = ['id', 'username', 'email', 'roles', 'actions'];

  constructor(
    private userService: UserService,
    private fb: FormBuilder,
    private snackBar: MatSnackBar,
    private dialog: MatDialog
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

  loadUsers(): void {
    this.loading = true;
    this.userService.getUsers().subscribe({
      next: (response) => {
        this.users = response.data;
        this.loading = false;
      },
      error: (error: HttpErrorResponse) => {
        this.loading = false;
        this.snackBar.open('加载用户列表失败', '关闭', {
          duration: 3000,
          horizontalPosition: 'center',
          verticalPosition: 'top'
        });
      }
    });
  }

  showCreateModal(): void {
    this.modalTitle = '新建用户';
    this.userForm.reset();
    this.userForm.patchValue({
      roleNames: ['ROLE_USER'] // Default role for new users
    });
    this.userForm.get('password')?.setValidators([Validators.required]);
    this.isModalVisible = true;
  }

  showEditModal(user: User): void {
    this.modalTitle = '编辑用户';
    this.userForm.patchValue({
      id: user.id,
      username: user.username,
      email: user.email,
      roleNames: user.roleNames || []
    });
    this.userForm.get('password')?.clearValidators();
    this.userForm.get('password')?.updateValueAndValidity();
    this.isModalVisible = true;
  }

  showEmployeeMapping(user: User): void {
    const dialogRef = this.dialog.open(UserEmployeeMappingComponent, {
      width: '800px',
      data: { userId: user.id }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.snackBar.open('员工关联已更新', '关闭', {
          duration: 3000,
          horizontalPosition: 'center',
          verticalPosition: 'top'
        });
      }
    });
  }

  confirmDelete(user: User): void {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: {
        title: '确认删除',
        message: `确定要删除用户 ${user.username} 吗？`
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loading = true;
        this.userService.deleteUser(user.id).subscribe({
          next: () => {
            this.loading = false;
            this.loadUsers();
            this.snackBar.open('删除成功', '关闭', { duration: 3000 });
          },
          error: (error: HttpErrorResponse) => {
            this.loading = false;
            this.snackBar.open('删除失败', '关闭', { duration: 3000 });
          }
        });
      }
    });
  }

  handleOk(): void {
    if (this.userForm.valid) {
      this.loading = true;
      const userData = this.userForm.value;
      const operation = userData.id
        ? this.userService.updateUser(userData.id, userData)
        : this.userService.createUser(userData);

      operation.subscribe({
        next: () => {
          this.loading = false;
          this.isModalVisible = false;
          this.loadUsers();
          this.snackBar.open(
            userData.id ? '更新成功' : '创建成功',
            '关闭',
            { duration: 3000 }
          );
        },
        error: (error: HttpErrorResponse) => {
          this.loading = false;
          this.snackBar.open(
            userData.id ? '更新失败' : '创建失败',
            '关闭',
            { duration: 3000 }
          );
        }
      });
    }
  }

  handleCancel(): void {
    this.isModalVisible = false;
    this.userForm.reset();
  }
}
