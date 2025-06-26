import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { TranslateService } from '@ngx-translate/core';
import { InputPageConfig } from '../../components/input-page/input-page.component';
import { User, CreateUserDto } from '../../services/user.service';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-user-input-new',
  templateUrl: './user-input-new.component.html',
  styleUrls: ['./user-input-new.component.scss']
})
export class UserInputNewComponent implements OnInit {
  config: InputPageConfig = {
    title: 'USER.TITLE',
    fields: [
      {
        key: 'username',
        label: 'USER.USERNAME',
        type: 'text',
        required: true,
        placeholder: 'USER.USERNAME_PLACEHOLDER',
        width: 2
      },
      {
        key: 'email',
        label: 'USER.EMAIL',
        type: 'email',
        required: true,
        placeholder: 'USER.EMAIL_PLACEHOLDER',
        width: 2
      },
      {
        key: 'password',
        label: 'USER.PASSWORD',
        type: 'password',
        required: true,
        placeholder: 'USER.PASSWORD_PLACEHOLDER',
        width: 2,
        hidden: this.data.mode === 'edit'
      },
      {
        key: 'status',
        label: 'USER.STATUS',
        type: 'select',
        required: false,
        width: 2,
        options: [
          { value: 'ACTIVE', label: 'USER.STATUS_ACTIVE' },
          { value: 'INACTIVE', label: 'USER.STATUS_INACTIVE' },
          { value: 'LOCKED', label: 'USER.STATUS_LOCKED' }
        ]
      },
      {
        key: 'roleNames',
        label: 'USER.ROLES',
        type: 'select',
        required: true,
        width: 4,
        multiple: true,
        options: [
          { value: 'ADMIN', label: 'USER.ROLE_ADMIN' },
          { value: 'USER', label: 'USER.ROLE_USER' },
          { value: 'MANAGER', label: 'USER.ROLE_MANAGER' }
        ]
      }
    ],
    layout: 'two-column',
    showSaveButton: true,
    showCancelButton: true,
    showResetButton: true,
    saveButtonText: 'COMMON.SAVE',
    cancelButtonText: 'COMMON.CANCEL',
    resetButtonText: 'COMMON.RESET'
  };

  initialData: any = {};
  loading = false;

  constructor(
    private userService: UserService,
    private dialogRef: MatDialogRef<UserInputNewComponent>,
    private snackBar: MatSnackBar,
    private translate: TranslateService,
    @Inject(MAT_DIALOG_DATA) public data: { mode: 'create' | 'edit'; user?: User }
  ) {
    if (data.mode === 'edit' && data.user) {
      this.initialData = { 
        ...data.user,
        roleNames: data.user.roleNames || []
      };
    }
  }

  ngOnInit(): void {
    // No additional initialization needed
  }

  onSave(data: any): void {
    if (this.validateForm(data)) {
      this.loading = true;

      if (this.data.mode === 'create') {
        const createUserDto: CreateUserDto = {
          username: data.username,
          email: data.email,
          password: data.password,
          status: data.status || 'ACTIVE',
          roleNames: data.roleNames || []
        };

        this.userService.createUser(createUserDto).subscribe({
          next: (response: any) => {
            if (response.code === 200) {
              this.snackBar.open(
                this.translate.instant('USER.CREATE_SUCCESS'),
                this.translate.instant('ACTIONS.CLOSE'),
                { duration: 3000 }
              );
              this.dialogRef.close(response.data);
            } else {
              this.snackBar.open(
                response.message || this.translate.instant('ERROR.SAVE_USER'),
                this.translate.instant('ACTIONS.CLOSE'),
                { duration: 3000 }
              );
            }
            this.loading = false;
          },
          error: (error: any) => {
            console.error('Error creating user:', error);
            this.snackBar.open(
              this.translate.instant('ERROR.SAVE_USER'),
              this.translate.instant('ACTIONS.CLOSE'),
              { duration: 3000 }
            );
            this.loading = false;
          }
        });
      } else {
        // For edit mode, we need to handle differently since we can't update password here
        const updateData = {
          ...data,
          id: this.data.user!.id
        };
        delete updateData.password; // Remove password from update

        this.userService.updateUser(this.data.user!.id, updateData).subscribe({
          next: (response: any) => {
            if (response.code === 200) {
              this.snackBar.open(
                this.translate.instant('USER.UPDATE_SUCCESS'),
                this.translate.instant('ACTIONS.CLOSE'),
                { duration: 3000 }
              );
              this.dialogRef.close(response.data);
            } else {
              this.snackBar.open(
                response.message || this.translate.instant('ERROR.SAVE_USER'),
                this.translate.instant('ACTIONS.CLOSE'),
                { duration: 3000 }
              );
            }
            this.loading = false;
          },
          error: (error: any) => {
            console.error('Error updating user:', error);
            this.snackBar.open(
              this.translate.instant('ERROR.SAVE_USER'),
              this.translate.instant('ACTIONS.CLOSE'),
              { duration: 3000 }
            );
            this.loading = false;
          }
        });
      }
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }

  onReset(): void {
    console.log('Form reset');
  }

  onFieldChange(key: string, value: any): void {
    console.log('Field change:', key, value);
  }

  onValidationChange(valid: boolean, errors: any): void {
    console.log('Validation change:', valid, errors);
  }

  private validateForm(data: any): boolean {
    if (!data.username || data.username.trim() === '') {
      this.snackBar.open(
        this.translate.instant('USER.USERNAME_REQUIRED'),
        this.translate.instant('ACTIONS.CLOSE'),
        { duration: 3000 }
      );
      return false;
    }

    if (!data.email || data.email.trim() === '') {
      this.snackBar.open(
        this.translate.instant('USER.EMAIL_REQUIRED'),
        this.translate.instant('ACTIONS.CLOSE'),
        { duration: 3000 }
      );
      return false;
    }

    if (this.data.mode === 'create' && (!data.password || data.password.trim() === '')) {
      this.snackBar.open(
        this.translate.instant('USER.PASSWORD_REQUIRED'),
        this.translate.instant('ACTIONS.CLOSE'),
        { duration: 3000 }
      );
      return false;
    }

    if (!data.roleNames || data.roleNames.length === 0) {
      this.snackBar.open(
        this.translate.instant('USER.ROLES_REQUIRED'),
        this.translate.instant('ACTIONS.CLOSE'),
        { duration: 3000 }
      );
      return false;
    }

    return true;
  }
} 