import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { UserService } from '../../services/user.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.scss'],
})
export class ResetPasswordComponent {
  resetForm: FormGroup;
  loading = false;
  tempPassword: string | null = null;

  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private snackBar: MatSnackBar,
    private dialogRef: MatDialogRef<ResetPasswordComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { userId: number }
  ) {
    this.resetForm = this.fb.group({
      confirmReset: [false, [Validators.requiredTrue]],
    });
  }

  onSubmit() {
    if (this.resetForm.valid) {
      this.loading = true;
      this.userService.resetPassword(this.data.userId).subscribe({
        next: response => {
          this.tempPassword = response.data;
          this.snackBar.open('密码重置成功', '关闭', { duration: 3000 });
        },
        error: error => {
          console.error('Error resetting password:', error);
          this.snackBar.open('密码重置失败', '关闭', { duration: 3000 });
        },
        complete: () => {
          this.loading = false;
        },
      });
    }
  }

  onCopyPassword() {
    if (this.tempPassword) {
      navigator.clipboard.writeText(this.tempPassword).then(
        () => {
          this.snackBar.open('临时密码已复制到剪贴板', '关闭', { duration: 3000 });
        },
        err => {
          console.error('Could not copy password:', err);
          this.snackBar.open('复制密码失败', '关闭', { duration: 3000 });
        }
      );
    }
  }

  onClose() {
    this.dialogRef.close();
  }
}
