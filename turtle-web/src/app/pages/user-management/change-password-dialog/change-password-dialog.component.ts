import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { UserService } from '../../../services/user.service';
import { ChangePasswordRequest } from '../../../models/change-password-request.model';
import { MatSnackBar } from '@angular/material/snack-bar';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-change-password-dialog',
  templateUrl: './change-password-dialog.component.html',
  styleUrls: ['./change-password-dialog.component.scss']
})
export class ChangePasswordDialogComponent implements OnInit {
  changePasswordForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<ChangePasswordDialogComponent>,
    private userService: UserService,
    private snackBar: MatSnackBar,
    private translate: TranslateService
  ) {
    this.changePasswordForm = this.fb.group({
      currentPassword: ['', Validators.required],
      newPassword: ['', [Validators.required, Validators.minLength(8)]],
      confirmPassword: ['', Validators.required]
    }, { validator: this.passwordMatchValidator });
  }

  ngOnInit(): void {}

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
      const changePasswordRequest: ChangePasswordRequest = {
        currentPassword: this.changePasswordForm.get('currentPassword')?.value,
        newPassword: this.changePasswordForm.get('newPassword')?.value,
        confirmPassword: this.changePasswordForm.get('confirmPassword')?.value
      };

      this.userService.changeUserPassword(changePasswordRequest).subscribe({
        next: (response) => {
          this.snackBar.open(
            this.translate.instant('changePassword.success'), 
            this.translate.instant('common.close'), 
            { duration: 3000 }
          );
          this.dialogRef.close(true);
        },
        error: (error) => {
          this.snackBar.open(
            error.error?.message || this.translate.instant('changePassword.error'), 
            this.translate.instant('common.close'), 
            { duration: 3000 }
          );
        }
      });
    }
  }

  onCancel() {
    this.dialogRef.close(false);
  }
}
