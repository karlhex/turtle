import { Component, OnInit, Inject, Optional } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { UserEmployeeMappingService, UserEmployeeMapping } from '../../services/user-employee-mapping.service';
import { UserService, User } from '../../services/user.service';
import { EmployeeService } from '../../services/employee.service';
import { ConfirmDialogComponent, ConfirmDialogData } from '../../components/confirmdialog/confirm-dialog.component';
import { MatDialog } from '@angular/material/dialog';
import { finalize } from 'rxjs/operators';
import { TranslateModule } from '@ngx-translate/core';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatTableModule } from '@angular/material/table';
import { MatTooltipModule } from '@angular/material/tooltip';
import { ActionComponent } from '../../components/action/action.component';
 import { Employee } from '@models/employee.model';

@Component({
  selector: 'app-user-employee-mapping',
  templateUrl: './user-employee-mapping.component.html',
  styleUrls: ['./user-employee-mapping.component.scss']
})
export class UserEmployeeMappingComponent implements OnInit {
  mappings: UserEmployeeMapping[] = [];
  unmappedUsers: User[] = [];
  unmappedEmployees: Employee[] = [];
  selectedUser: number | null = null;
  selectedEmployee: number | null = null;
  loading = false;
  displayedColumns: string[] = ['username', 'email', 'employee', 'actions'];
  currentUser: User | null = null;

  constructor(
    private userEmployeeMappingService: UserEmployeeMappingService,
    private userService: UserService,
    private employeeService: EmployeeService,
    private snackBar: MatSnackBar,
    private dialog: MatDialog,
    @Optional() private dialogRef: MatDialogRef<UserEmployeeMappingComponent>,
    @Optional() @Inject(MAT_DIALOG_DATA) private dialogData: { user: User }
  ) {
    if (this.dialogData?.user) {
      this.currentUser = this.dialogData.user;
      this.selectedUser = this.currentUser.id;
      this.displayedColumns = ['employee', 'actions'];
    }
  }

  ngOnInit(): void {
    this.loadData();
  }

  private loadData(): void {
    this.loading = true;

    if (this.currentUser) {
      this.loadUserMappings();
    } else {
      this.loadAllMappings();
    }

    this.loadUnmappedEmployees();
    if (!this.currentUser) {
      this.loadUnmappedUsers();
    }
  }

  private loadAllMappings(): void {
    this.userEmployeeMappingService.getMappings()
      .pipe(finalize(() => this.loading = false))
      .subscribe({
        next: (response) => {
          this.mappings = response.data;
        },
        error: (error) => {
          console.error('Error loading mappings:', error);
          this.showError('加载用户-员工关联失败');
        }
      });
  }

  private loadUserMappings(): void {
    if (!this.currentUser?.id) return;

    this.userEmployeeMappingService.getUserMappings(this.currentUser.id)
      .pipe(finalize(() => this.loading = false))
      .subscribe({
        next: (response) => {
          this.mappings = response.data;
        },
        error: (error) => {
          console.error('Error loading user mappings:', error);
          this.showError('加载用户的员工关联失败');
        }
      });
  }

  private loadUnmappedUsers(): void {
    this.userService.getUnmappedUsers().subscribe({
      next: (response) => {
        this.unmappedUsers = response.data;
      },
      error: (error) => {
        console.error('Error loading unmapped users:', error);
        this.showError('加载未关联用户失败');
      }
    });
  }

  private loadUnmappedEmployees(): void {
    this.employeeService.getUnmappedEmployees().subscribe({
      next: (response) => {
        this.unmappedEmployees = response.data;
      },
      error: (error) => {
        console.error('Error loading unmapped employees:', error);
        this.showError('加载未关联员工失败');
      }
    });
  }

  createMapping(): void {
    if (!this.selectedEmployee || (!this.selectedUser && !this.currentUser)) {
      this.showError('请选择用户和员工');
      return;
    }

    const userId = this.currentUser?.id || this.selectedUser;
    if (!userId) return;

    this.loading = true;
    this.userEmployeeMappingService.createMapping(userId, this.selectedEmployee)
      .pipe(finalize(() => this.loading = false))
      .subscribe({
        next: () => {
          this.showSuccess('用户-员工关联创建成功');
          this.selectedEmployee = null;
          if (!this.currentUser) {
            this.selectedUser = null;
          }
          this.loadData();
        },
        error: (error) => {
          console.error('Error creating mapping:', error);
          this.showError('创建用户-员工关联失败');
        }
      });
  }

  deleteMapping(mapping: UserEmployeeMapping): void {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: {
        title: '确认删除',
        message: `确定要删除用户 "${mapping.username}" 与员工 "${mapping.employeeName}" 的关联吗？`
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loading = true;
        this.userEmployeeMappingService.deleteMapping(mapping.id)
          .pipe(finalize(() => this.loading = false))
          .subscribe({
            next: () => {
              this.showSuccess('用户-员工关联删除成功');
              this.loadData();
            },
            error: (error) => {
              console.error('Error deleting mapping:', error);
              this.showError('删除用户-员工关联失败');
            }
          });
      }
    });
  }

  private showSuccess(message: string): void {
    this.snackBar.open(message, '关闭', {
      duration: 3000,
      horizontalPosition: 'center',
      verticalPosition: 'top',
      panelClass: ['success-snackbar']
    });
  }

  private showError(message: string): void {
    this.snackBar.open(message, '关闭', {
      duration: 5000,
      horizontalPosition: 'center',
      verticalPosition: 'top',
      panelClass: ['error-snackbar']
    });
  }

  close(): void {
    if (this.dialogRef) {
      this.dialogRef.close();
    }
  }
}
