import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { EmployeeJobHistory } from '../../models/employee.model';

@Component({
  selector: 'app-job-history-dialog',
  templateUrl: './job-history-dialog.component.html',
  styleUrls: ['./job-history-dialog.component.scss']
})
export class JobHistoryDialogComponent implements OnInit {
  jobHistoryForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<JobHistoryDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { 
      jobHistory: EmployeeJobHistory,
      employeeId: number,
      mode: 'view' | 'edit'
    }
  ) {
    this.jobHistoryForm = this.fb.group({
      companyName: ['', Validators.required],
      department: ['', Validators.required],
      position: ['', Validators.required],
      startDate: [null, Validators.required],
      endDate: [null],
      jobDescription: [''],
      achievements: [''],
      leavingReason: [''],
      referenceContact: [''],
      remarks: ['']
    });
  }

  ngOnInit() {
    if (this.data.jobHistory) {
      this.jobHistoryForm.patchValue({
        companyName: this.data.jobHistory.companyName,
        department: this.data.jobHistory.department,
        position: this.data.jobHistory.position,
        startDate: this.data.jobHistory.startDate,
        endDate: this.data.jobHistory.endDate,
        jobDescription: this.data.jobHistory.jobDescription,
        achievements: this.data.jobHistory.achievements,
        leavingReason: this.data.jobHistory.leavingReason,
        referenceContact: this.data.jobHistory.referenceContact,
        remarks: this.data.jobHistory.remarks
      });
    }

    if (this.data.mode === 'view') {
      this.jobHistoryForm.disable();
    }
  }

  onSubmit() {
    if (this.jobHistoryForm.valid) {
      const formValue = this.jobHistoryForm.getRawValue();
      const jobHistory: EmployeeJobHistory = {
        ...this.data.jobHistory,
        employeeId: this.data.employeeId,
        companyName: formValue.companyName,
        department: formValue.department,
        position: formValue.position,
        startDate: formValue.startDate,
        endDate: formValue.endDate,
        jobDescription: formValue.jobDescription,
        achievements: formValue.achievements,
        leavingReason: formValue.leavingReason,
        referenceContact: formValue.referenceContact,
        remarks: formValue.remarks
      };
      this.dialogRef.close(jobHistory);
    }
  }

  onCancel() {
    this.dialogRef.close();
  }
}
