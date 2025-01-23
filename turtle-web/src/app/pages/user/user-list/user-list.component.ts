import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { MatDialog } from '@angular/material/dialog';
import { UserService, User } from '../../../services/user.service';
import { SnackBarService } from '../../../services/snack-bar.service';
import { ConfirmDialogComponent } from '../../../components/confirm-dialog/confirm-dialog.component';
import { UserDialogComponent } from '../user-dialog/user-dialog.component';
import { ResetPasswordComponent } from '../../../components/reset-password/reset-password.component';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.scss']
})
export class UserListComponent implements OnInit {
  displayedColumns: string[] = ['id', 'username', 'email', 'status', 'roles', 'actions'];
  dataSource: MatTableDataSource<User>;

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(
    private userService: UserService,
    private dialog: MatDialog,
    private snackBar: SnackBarService
  ) {
    this.dataSource = new MatTableDataSource();
  }

  ngOnInit() {
    this.loadUsers();
  }

  loadUsers() {
    this.userService.getUsers().subscribe({
      next: (response) => {
        this.dataSource.data = response.data.content;
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
      },
      error: (error) => {
        console.error('Error loading users:', error);
        this.snackBar.error('加载用户列表失败');
      }
    });
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  openCreateDialog() {
    const dialogRef = this.dialog.open(UserDialogComponent, {
      width: '600px',
      data: { mode: 'create' }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadUsers();
      }
    });
  }

  editUser(user: User) {
    const dialogRef = this.dialog.open(UserDialogComponent, {
      width: '600px',
      data: { mode: 'edit', user }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadUsers();
      }
    });
  }

  resetPassword(user: User) {
    const dialogRef = this.dialog.open(ResetPasswordComponent, {
      width: '500px',
      data: { userId: user.id }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadUsers();
      }
    });
  }

  forcePasswordChange(user: User) {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      width: '400px',
      data: {
        title: '强制修改密码',
        message: `确定要强制用户 ${user.username} 在下次登录时修改密码吗？`
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.userService.forcePasswordChange(user.id).subscribe({
          next: () => {
            this.snackBar.success('操作成功');
            this.loadUsers();
          },
          error: (error) => {
            console.error('Error forcing password change:', error);
            this.snackBar.error('操作失败');
          }
        });
      }
    });
  }

  toggleUserStatus(user: User) {
    const newStatus = user.status === 'ACTIVE' ? 'INACTIVE' : 'ACTIVE';
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      width: '400px',
      data: {
        title: `${newStatus === 'ACTIVE' ? '启用' : '禁用'}用户`,
        message: `确定要${newStatus === 'ACTIVE' ? '启用' : '禁用'}用户 ${user.username} 吗？`
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        const updatedUser = { ...user, status: newStatus };
        this.userService.updateUser(user.id, updatedUser).subscribe({
          next: () => {
            this.snackBar.success('操作成功');
            this.loadUsers();
          },
          error: (error) => {
            console.error('Error updating user status:', error);
            this.snackBar.error('操作失败');
          }
        });
      }
    });
  }

  deleteUser(user: User) {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      width: '400px',
      data: {
        title: '删除用户',
        message: `确定要删除用户 ${user.username} 吗？此操作不可撤销。`
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.userService.deleteUser(user.id).subscribe({
          next: () => {
            this.snackBar.success('用户删除成功');
            this.loadUsers();
          },
          error: (error) => {
            console.error('Error deleting user:', error);
            this.snackBar.error('用户删除失败');
          }
        });
      }
    });
  }
}
