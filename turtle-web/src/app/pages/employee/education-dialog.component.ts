import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { EmployeeEducation } from '../../services/employee-education.service';

@Component({
  selector: 'app-education-dialog',
  templateUrl: './education-dialog.component.html',
  styleUrls: ['./education-dialog.component.scss'],
})
export class EducationDialogComponent {
  educationForm!: FormGroup;
  mode: 'add' | 'edit' | 'view';

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<EducationDialogComponent>,
    @Inject(MAT_DIALOG_DATA)
    public data: {
      education?: EmployeeEducation;
      employeeId: number;
      mode: 'add' | 'edit' | 'view';
    }
  ) {
    this.mode = data.mode;
    this.initForm();
  }

  private initForm(): void {
    this.educationForm = this.fb.group({
      school: ['', [Validators.required]],
      major: ['', [Validators.required]],
      degree: ['', [Validators.required]],
      startDate: [null, [Validators.required]],
      endDate: [null],
      certificateNumber: [''],
      isHighestDegree: [false],
      remarks: [''],
    });

    if (this.data.education) {
      this.educationForm.patchValue(this.data.education);
    }

    if (this.mode === 'view') {
      this.educationForm.disable();
    }
  }

  onSubmit(): void {
    if (this.educationForm.valid) {
      const formValue = this.educationForm.getRawValue();
      this.dialogRef.close(formValue);
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}
