import { Component, Inject, OnInit, ViewChild, ElementRef } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { User } from '../../../services/user.service';

@Component({
  selector: 'app-user-edit-dialog',
  templateUrl: './user-edit-dialog.component.html',
  styleUrls: ['./user-edit-dialog.component.scss']
})
export class UserEditDialogComponent implements OnInit {
  userForm: FormGroup;
  availableRoles = ['ROLE_USER', 'ROLE_ADMIN'];
  isEditMode: boolean;
  @ViewChild('firstInput') firstInput!: ElementRef;

  constructor(
    private fb: FormBuilder,
    public dialogRef: MatDialogRef<UserEditDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { user?: User }
  ) {
    this.isEditMode = !!data.user;
    this.userForm = this.fb.group({
      id: [data.user?.id],
      username: [data.user?.username || '', [Validators.required]],
      email: [data.user?.email || '', [Validators.required, Validators.email]],
      password: ['', this.isEditMode ? [] : [Validators.required]],
      roleNames: [data.user?.roleNames || [], [Validators.required]]
    });
  }

  ngOnInit(): void {
    // 设置对话框的aria标签
    this.dialogRef.addPanelClass('user-edit-dialog');
  }

  ngAfterViewInit(): void {
    // 确保对话框打开时第一个输入框获得焦点
    setTimeout(() => {
      if (this.firstInput) {
        this.firstInput.nativeElement.focus();
      }
    });
  }

  onSubmit(): void {
    if (this.userForm.valid) {
      this.dialogRef.close(this.userForm.value);
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}
