import { Component, Inject, OnInit, ViewChild, ElementRef } from '@angular/core';
import { FormBuilder, FormGroup, Validators, FormArray } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { User } from '../../../services/user.service';
import { RoleService } from '../../../services/role.service';
import { Role } from '../../../models/role.model';

@Component({
  selector: 'app-user-edit-dialog',
  templateUrl: './user-edit-dialog.component.html',
  styleUrls: ['./user-edit-dialog.component.scss'],
})
export class UserEditDialogComponent implements OnInit {
  userForm: FormGroup;
  availableRoles: Role[] = [];
  isEditMode: boolean;
  isLoading = true;
  @ViewChild('firstInput') firstInput!: ElementRef;

  constructor(
    private fb: FormBuilder,
    private roleService: RoleService,
    public dialogRef: MatDialogRef<UserEditDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { user?: User }
  ) {
    this.isEditMode = !!data.user;

    // Extract role names from the user's roles if they exist
    const roleNames = data.user?.roleNames?.map(role => 'ROLE_' + role) || [];
    console.log('Initial user data:', data.user);
    console.log('Initial roleNames:', roleNames);

    this.userForm = this.fb.group({
      id: [data.user?.id],
      username: [data.user?.username || '', [Validators.required]],
      email: [data.user?.email || '', [Validators.required, Validators.email]],
      roleNames: [roleNames, [Validators.required]],
    });
  }

  ngOnInit(): void {
    // 设置对话框的aria标签
    this.dialogRef.addPanelClass('user-edit-dialog');

    // 加载角色列表
    this.roleService.getAllRoles().subscribe({
      next: response => {
        console.log('Loaded roles:', response.data);
        this.availableRoles = response.data;
        this.isLoading = false;
      },
      error: error => {
        console.error('Failed to load roles:', error);
        this.isLoading = false;
      },
    });
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

  // 获取角色显示名称（去除ROLE_前缀）
  getRoleDisplayName(role: Role): string {
    return role.description || role.name.replace('ROLE_', '');
  }

  onRoleChange(event: any): void {
    const roleNames = this.userForm.get('roleNames') as FormArray;
    if (event.checked) {
      roleNames.push(this.fb.control(event.source.value));
    } else {
      const index = roleNames.controls.findIndex(control => control.value === event.source.value);
      if (index !== -1) {
        roleNames.removeAt(index);
      }
    }
  }
}
