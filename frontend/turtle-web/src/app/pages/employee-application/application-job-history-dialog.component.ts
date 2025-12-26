import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ApplicationJobHistory } from '../../models/employee-application.model';

@Component({
  selector: 'app-application-job-history-dialog',
  templateUrl: './application-job-history-dialog.component.html',
  styleUrls: ['./application-job-history-dialog.component.scss']
})
export class ApplicationJobHistoryDialogComponent implements OnInit {
  jobHistoryForm: FormGroup;
  isEditMode: boolean;
  isCurrentJob = false;

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<ApplicationJobHistoryDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { mode: 'add' | 'edit'; jobHistory?: ApplicationJobHistory }
  ) {
    this.isEditMode = data.mode === 'edit';
    this.jobHistoryForm = this.createForm();
  }

  ngOnInit(): void {
    if (this.isEditMode && this.data.jobHistory) {
      this.isCurrentJob = !this.data.jobHistory.endDate;
      this.jobHistoryForm.patchValue({
        companyName: this.data.jobHistory.companyName,
        position: this.data.jobHistory.position,
        startDate: this.data.jobHistory.startDate,
        endDate: this.data.jobHistory.endDate,
        department: this.data.jobHistory.department,
        jobDescription: this.data.jobHistory.jobDescription,
        achievements: this.data.jobHistory.achievements,
        leavingReason: this.data.jobHistory.leavingReason,
        referenceContact: this.data.jobHistory.referenceContact,
        remarks: this.data.jobHistory.remarks
      });
      this.updateEndDateValidation();
    }
  }

  private createForm(): FormGroup {
    return this.fb.group({
      companyName: ['', [Validators.required]],
      position: ['', [Validators.required]],
      startDate: ['', [Validators.required]],
      endDate: [''],
      department: [''],
      jobDescription: [''],
      achievements: [''],
      leavingReason: [''],
      referenceContact: [''],
      remarks: ['']
    });
  }

  onCurrentJobChange(): void {
    this.updateEndDateValidation();
  }

  private updateEndDateValidation(): void {
    const endDateControl = this.jobHistoryForm.get('endDate');

    if (this.isCurrentJob) {
      endDateControl?.clearValidators();
      endDateControl?.setValue('');
    } else {
      endDateControl?.setValidators([Validators.required]);
    }

    endDateControl?.updateValueAndValidity();
  }

  onSave(): void {
    if (this.jobHistoryForm.valid) {
      const formValue = this.jobHistoryForm.value;

      const jobHistory: ApplicationJobHistory = {
        companyName: formValue.companyName,
        position: formValue.position,
        startDate: formValue.startDate,
        endDate: this.isCurrentJob ? undefined : formValue.endDate,
        department: formValue.department,
        jobDescription: formValue.jobDescription,
        achievements: formValue.achievements,
        leavingReason: formValue.leavingReason,
        referenceContact: formValue.referenceContact,
        remarks: formValue.remarks
      };

      this.dialogRef.close(jobHistory);
    } else {
      this.markFormGroupTouched();
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }

  private markFormGroupTouched(): void {
    Object.keys(this.jobHistoryForm.controls).forEach(key => {
      this.jobHistoryForm.get(key)?.markAsTouched();
    });
  }

  getErrorMessage(fieldName: string): string {
    const control = this.jobHistoryForm.get(fieldName);
    if (control?.hasError('required')) {
      return `${this.getFieldLabel(fieldName)}不能为空`;
    }
    return '';
  }

  private getFieldLabel(fieldName: string): string {
    const labels: { [key: string]: string } = {
      companyName: '公司名称',
      position: '职位',
      startDate: '开始日期',
      endDate: '结束日期',
      department: '部门'
    };
    return labels[fieldName] || fieldName;
  }
}