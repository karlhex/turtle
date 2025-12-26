import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ApplicationCertification } from '../../models/employee-application.model';

@Component({
  selector: 'app-application-certification-dialog',
  templateUrl: './application-certification-dialog.component.html',
  styleUrls: ['./application-certification-dialog.component.scss']
})
export class ApplicationCertificationDialogComponent implements OnInit {
  certificationForm: FormGroup;
  isEditMode: boolean;
  hasExpiryDate = true;

  commonCertifications = [
    '计算机等级证书',
    '英语四级证书',
    '英语六级证书',
    '雅思证书',
    '托福证书',
    '驾驶证',
    'PMP项目管理证书',
    '会计从业资格证',
    '注册会计师证书',
    '律师执业证书',
    '医师执业证书',
    '护士执业证书',
    '教师资格证',
    'Java认证',
    'Oracle认证',
    'Microsoft认证',
    'AWS认证',
    '其他'
  ];

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<ApplicationCertificationDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { mode: 'add' | 'edit'; certification?: ApplicationCertification }
  ) {
    this.isEditMode = data.mode === 'edit';
    this.certificationForm = this.createForm();
  }

  ngOnInit(): void {
    if (this.isEditMode && this.data.certification) {
      this.hasExpiryDate = !!this.data.certification.expiryDate;
      this.certificationForm.patchValue({
        name: this.data.certification.name,
        issuer: this.data.certification.issuer,
        issueDate: this.data.certification.issueDate,
        expiryDate: this.data.certification.expiryDate,
        certificateNumber: this.data.certification.certificateNumber,
        remarks: this.data.certification.remarks
      });
      this.updateExpiryDateValidation();
    }
  }

  private createForm(): FormGroup {
    return this.fb.group({
      name: ['', [Validators.required]],
      issuer: ['', [Validators.required]],
      issueDate: ['', [Validators.required]],
      expiryDate: [''],
      certificateNumber: [''],
      remarks: ['']
    });
  }

  onHasExpiryDateChange(): void {
    this.updateExpiryDateValidation();
  }

  private updateExpiryDateValidation(): void {
    const expiryDateControl = this.certificationForm.get('expiryDate');

    if (!this.hasExpiryDate) {
      expiryDateControl?.clearValidators();
      expiryDateControl?.setValue('');
    }

    expiryDateControl?.updateValueAndValidity();
  }

  onSave(): void {
    if (this.certificationForm.valid) {
      const formValue = this.certificationForm.value;

      const certification: ApplicationCertification = {
        name: formValue.name,
        issuer: formValue.issuer,
        issueDate: formValue.issueDate,
        expiryDate: this.hasExpiryDate ? formValue.expiryDate : undefined,
        certificateNumber: formValue.certificateNumber,
        remarks: formValue.remarks
      };

      this.dialogRef.close(certification);
    } else {
      this.markFormGroupTouched();
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }

  private markFormGroupTouched(): void {
    Object.keys(this.certificationForm.controls).forEach(key => {
      this.certificationForm.get(key)?.markAsTouched();
    });
  }

  getErrorMessage(fieldName: string): string {
    const control = this.certificationForm.get(fieldName);
    if (control?.hasError('required')) {
      return `${this.getFieldLabel(fieldName)}不能为空`;
    }
    return '';
  }

  private getFieldLabel(fieldName: string): string {
    const labels: { [key: string]: string } = {
      name: '证书名称',
      issuer: '颁发机构',
      issueDate: '颁发日期',
      expiryDate: '有效期',
      certificateNumber: '证书编号'
    };
    return labels[fieldName] || fieldName;
  }
}