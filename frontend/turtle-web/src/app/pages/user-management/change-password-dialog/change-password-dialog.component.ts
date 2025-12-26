import { Component, OnInit, Optional } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { UserService, ExpiredPasswordChangeRequest } from '../../../services/user.service';
import { ChangePasswordRequest } from '../../../models/change-password-request.model';
import { MatSnackBar } from '@angular/material/snack-bar';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-change-password-dialog',
  templateUrl: './change-password-dialog.component.html',
  styleUrls: ['./change-password-dialog.component.scss'],
})
export class ChangePasswordDialogComponent implements OnInit {
  changePasswordForm: FormGroup;
  isPasswordExpired = false;
  pendingUsername = '';

  constructor(
    private fb: FormBuilder,
    @Optional() private dialogRef: MatDialogRef<ChangePasswordDialogComponent>,
    private router: Router,
    private userService: UserService,
    private snackBar: MatSnackBar,
    private translate: TranslateService
  ) {
    this.changePasswordForm = this.fb.group(
      {
        currentPassword: ['', Validators.required],
        newPassword: ['', [Validators.required, Validators.minLength(8)]],
        confirmPassword: ['', Validators.required],
      },
      { validator: this.passwordMatchValidator }
    );
  }

  ngOnInit(): void {
    // Check if this is a password expiry scenario
    this.pendingUsername = sessionStorage.getItem('pendingPasswordChange') || '';
    this.isPasswordExpired = !!this.pendingUsername;
    
    if (this.isPasswordExpired) {
      // Show a message about password expiry
      this.snackBar.open(
        '您的密码已过期，请设置新密码',
        '关闭',
        { duration: 5000 }
      );
    }
  }

  passwordMatchValidator(form: FormGroup) {
    const newPassword = form.get('newPassword');
    const confirmPassword = form.get('confirmPassword');

    if (newPassword?.value !== confirmPassword?.value) {
      confirmPassword?.setErrors({ passwordMismatch: true });
    } else {
      confirmPassword?.setErrors(null);
    }
  }

  onSubmit() {
    if (this.changePasswordForm.valid) {
      if (this.isPasswordExpired) {
        // Handle expired password scenario
        const expiredPasswordChangeRequest: ExpiredPasswordChangeRequest = {
          username: this.pendingUsername,
          currentPassword: this.changePasswordForm.get('currentPassword')?.value,
          newPassword: this.changePasswordForm.get('newPassword')?.value,
          confirmPassword: this.changePasswordForm.get('confirmPassword')?.value,
        };

        this.userService.changeExpiredPassword(expiredPasswordChangeRequest).subscribe({
          next: response => {
            // Check if the API response indicates success or failure
            if (response.code === 200 || response.code === 0) {
              this.snackBar.open(
                '密码修改成功',
                '关闭',
                { duration: 3000 }
              );
              
              // Clear pending password change
              sessionStorage.removeItem('pendingPasswordChange');
              // Redirect to login after password change
              this.router.navigate(['/login']);
            } else {
              // Handle API error (non-200 code but HTTP 200 status)
              this.snackBar.open(
                response.message || '密码修改失败',
                '关闭',
                { duration: 3000 }
              );
            }
          },
          error: error => {
            this.snackBar.open(
              error.error?.message || '密码修改失败',
              '关闭',
              { duration: 3000 }
            );
          },
        });
      } else {
        // Handle normal password change scenario
        const changePasswordRequest: ChangePasswordRequest = {
          currentPassword: this.changePasswordForm.get('currentPassword')?.value,
          newPassword: this.changePasswordForm.get('newPassword')?.value,
          confirmPassword: this.changePasswordForm.get('confirmPassword')?.value,
        };

        this.userService.changeUserPassword(changePasswordRequest).subscribe({
          next: response => {
            // Check if the API response indicates success or failure
            if (response.code === 200 || response.code === 0) {
              this.snackBar.open(
                this.translate.instant('changePassword.success'),
                this.translate.instant('common.close'),
                { duration: 3000 }
              );
              
              if (this.dialogRef) {
                this.dialogRef.close(true);
              }
            } else {
              // Handle API error (non-200 code but HTTP 200 status)
              this.snackBar.open(
                response.message || this.translate.instant('changePassword.error'),
                this.translate.instant('common.close'),
                { duration: 3000 }
              );
            }
          },
          error: error => {
            this.snackBar.open(
              error.error?.message || this.translate.instant('changePassword.error'),
              this.translate.instant('common.close'),
              { duration: 3000 }
            );
          },
        });
      }
    }
  }

  onCancel() {
    if (this.isPasswordExpired) {
      // If password is expired, redirect to login instead of closing
      this.router.navigate(['/login']);
    } else if (this.dialogRef) {
      this.dialogRef.close(false);
    }
  }
  
  get isDialog(): boolean {
    return !!this.dialogRef;
  }
}
