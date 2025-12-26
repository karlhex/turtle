import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ApplicationEducation } from '../../models/employee-application.model';

@Component({
  selector: 'app-application-education-dialog',
  templateUrl: './application-education-dialog.component.html',
  styleUrls: ['./application-education-dialog.component.scss']
})
export class ApplicationEducationDialogComponent implements OnInit {
  educationForm: FormGroup;
  isEditMode: boolean;

  degreeOptions = [
    '博士',
    '硕士',
    '本科',
    '专科',
    '高中',
    '中专',
    '其他'
  ];

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<ApplicationEducationDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { mode: 'add' | 'edit'; education?: ApplicationEducation }
  ) {
    this.isEditMode = data.mode === 'edit';
    this.educationForm = this.createForm();
  }

  ngOnInit(): void {
    if (this.isEditMode && this.data.education) {
      this.educationForm.patchValue({
        school: this.data.education.school,
        major: this.data.education.major,
        degree: this.data.education.degree,
        startDate: this.data.education.startDate,
        endDate: this.data.education.endDate,
        remarks: this.data.education.remarks
      });
    }
  }

  private createForm(): FormGroup {
    return this.fb.group({
      school: ['', [Validators.required]],
      major: ['', [Validators.required]],
      degree: ['', [Validators.required]],
      startDate: ['', [Validators.required]],
      endDate: ['', [Validators.required]],
      remarks: ['']
    });
  }

  onSave(): void {
    if (this.educationForm.valid) {
      const formValue = this.educationForm.value;

      const education: ApplicationEducation = {
        school: formValue.school,
        major: formValue.major,
        degree: formValue.degree,
        startDate: formValue.startDate,
        endDate: formValue.endDate,
        remarks: formValue.remarks
      };

      this.dialogRef.close(education);
    } else {
      this.markFormGroupTouched();
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }

  private markFormGroupTouched(): void {
    Object.keys(this.educationForm.controls).forEach(key => {
      this.educationForm.get(key)?.markAsTouched();
    });
  }

  getErrorMessage(fieldName: string): string {
    const control = this.educationForm.get(fieldName);
    if (control?.hasError('required')) {
      return `${this.getFieldLabel(fieldName)}不能为空`;
    }
    return '';
  }

  private getFieldLabel(fieldName: string): string {
    const labels: { [key: string]: string } = {
      school: '学校',
      major: '专业',
      degree: '学历',
      startDate: '开始日期',
      endDate: '结束日期'
    };
    return labels[fieldName] || fieldName;
  }
}