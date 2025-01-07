import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { TranslateService } from '@ngx-translate/core';
import { RolePermission } from '../../models/role-permission.model';
import { RolePermissionService } from '../../services/role-permission.service';
import { RoleService } from '../../services/role.service';
import { Role } from '../../models/role.model';

@Component({
  selector: 'app-role-permission-dialog',
  templateUrl: './role-permission-dialog.component.html',
  styleUrls: ['./role-permission-dialog.component.scss']
})
export class RolePermissionDialogComponent implements OnInit {
  form: FormGroup;
  isEdit: boolean;
  loading = false;
  roles: Role[] = [];

  constructor(
    private fb: FormBuilder,
    private rolePermissionService: RolePermissionService,
    private roleService: RoleService,
    private snackBar: MatSnackBar,
    private translate: TranslateService,
    private dialogRef: MatDialogRef<RolePermissionDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: RolePermission
  ) {
    this.isEdit = !!data?.id;
    this.form = this.fb.group({
      roleName: ['', Validators.required],
      transactionPattern: ['', Validators.required],
      description: ['']
    });

    if (this.isEdit) {
      this.form.patchValue(data);
    }
  }

  ngOnInit(): void {
    this.loadRoles();
  }

  loadRoles(): void {
    this.roleService.getAllRoles().subscribe({
      next: (response) => {
        this.roles = response.data;
      },
      error: (error) => {
        console.error('Error loading roles:', error);
      }
    });
  }

  onSubmit(): void {
    if (this.form.valid) {
      this.loading = true;
      const rolePermission = this.form.value;

      const request = this.isEdit ?
        this.rolePermissionService.update(this.data.id, rolePermission) :
        this.rolePermissionService.create(rolePermission);

      request.subscribe({
        next: (response) => {
          this.snackBar.open(
            this.translate.instant(this.isEdit ? 'COMMON.UPDATE_SUCCESS' : 'COMMON.CREATE_SUCCESS'),
            this.translate.instant('COMMON.CLOSE'),
            { duration: 3000 }
          );
          this.dialogRef.close(true);
          this.loading = false;
        },
        error: (error) => {
          console.error('Error saving role permission:', error);
          this.snackBar.open(
            this.translate.instant('COMMON.ERROR_OCCURRED'),
            this.translate.instant('COMMON.CLOSE'),
            { duration: 3000 }
          );
          this.loading = false;
        }
      });
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}
