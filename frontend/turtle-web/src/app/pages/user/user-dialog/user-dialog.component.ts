import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-user-dialog',
  template: `
    <div>
      <h2 mat-dialog-title>用户管理</h2>
      <div mat-dialog-content>
        <p>用户管理功能开发中...</p>
      </div>
      <div mat-dialog-actions align="end">
        <button mat-button mat-dialog-close>关闭</button>
      </div>
    </div>
  `
})
export class UserDialogComponent {
  form: FormGroup;

  constructor(
    public dialogRef: MatDialogRef<UserDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private fb: FormBuilder
  ) {
    this.form = this.fb.group({
      name: ['', Validators.required]
    });
  }

  onSave(): void {
    if (this.form.valid) {
      this.dialogRef.close(this.form.value);
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}