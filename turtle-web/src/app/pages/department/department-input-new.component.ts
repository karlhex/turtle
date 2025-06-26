import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { TranslateService } from '@ngx-translate/core';
import { DepartmentService } from '../../services/department.service';
import { Department } from '../../models/department.model';
import { InputPageConfig, FormFieldConfig } from '../../components/input-page/input-page.component';

@Component({
  selector: 'app-department-input-new',
  templateUrl: './department-input-new.component.html',
  styleUrls: ['./department-input-new.component.scss'],
})
export class DepartmentInputNewComponent implements OnInit {
  config: InputPageConfig = {
    title: 'DEPARTMENT.TITLE',
    layout: 'auto-column',
    autoColumnThreshold: 4,
    showSaveButton: true,
    showCancelButton: true,
    showResetButton: true,
    autoSave: false,
    fields: [
      {
        key: 'name',
        label: 'DEPARTMENT.NAME',
        type: 'text',
        required: true,
        placeholder: 'DEPARTMENT.NAME_PLACEHOLDER',
        width: 2
      },
      {
        key: 'code',
        label: 'DEPARTMENT.CODE',
        type: 'text',
        required: true,
        placeholder: 'DEPARTMENT.CODE_PLACEHOLDER',
        width: 2
      },
      {
        key: 'description',
        label: 'DEPARTMENT.DESCRIPTION',
        type: 'textarea',
        required: false,
        placeholder: 'DEPARTMENT.DESCRIPTION_PLACEHOLDER',
        rows: 3,
        width: 4
      },
      {
        key: 'isActive',
        label: 'DEPARTMENT.STATUS',
        type: 'checkbox',
        required: false,
        width: 4
      }
    ]
  };

  initialData: any = {};
  loading = false;

  constructor(
    private departmentService: DepartmentService,
    private dialogRef: MatDialogRef<DepartmentInputNewComponent>,
    private snackBar: MatSnackBar,
    private translate: TranslateService,
    @Inject(MAT_DIALOG_DATA) public data: { mode: 'create' | 'edit'; department?: Department }
  ) {
    if (data.mode === 'edit' && data.department) {
      this.initialData = { ...data.department };
    }
  }

  ngOnInit(): void {
    // 初始化逻辑
  }

  onSave(data: any): void {
    if (this.validateForm(data)) {
      this.loading = true;
      const department = data;

      const request = this.data.mode === 'create'
        ? this.departmentService.createDepartment(department)
        : this.departmentService.updateDepartment(this.data.department!.id!, department);

      request.subscribe({
        next: response => {
          if (response.code === 200) {
            this.snackBar.open(
              this.translate.instant(
                this.data.mode === 'create' ? 'DEPARTMENT.CREATE_SUCCESS' : 'DEPARTMENT.UPDATE_SUCCESS'
              ),
              this.translate.instant('ACTIONS.CLOSE'),
              { duration: 3000 }
            );
            this.dialogRef.close(response.data);
          } else {
            this.snackBar.open(
              response.message || this.translate.instant('ERROR.SAVE_DEPARTMENT'),
              this.translate.instant('ACTIONS.CLOSE'),
              { duration: 3000 }
            );
          }
          this.loading = false;
        },
        error: error => {
          console.error('Error saving department:', error);
          this.snackBar.open(
            this.translate.instant('ERROR.SAVE_DEPARTMENT'),
            this.translate.instant('ACTIONS.CLOSE'),
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
    if (!data.name || data.name.trim() === '') {
      this.snackBar.open(
        this.translate.instant('DEPARTMENT.NAME_REQUIRED'),
        this.translate.instant('ACTIONS.CLOSE'),
        { duration: 3000 }
      );
      return false;
    }

    if (!data.code || data.code.trim() === '') {
      this.snackBar.open(
        this.translate.instant('DEPARTMENT.CODE_REQUIRED'),
        this.translate.instant('ACTIONS.CLOSE'),
        { duration: 3000 }
      );
      return false;
    }

    return true;
  }
} 