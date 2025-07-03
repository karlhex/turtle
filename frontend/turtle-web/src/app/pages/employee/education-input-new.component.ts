import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { TranslateService } from '@ngx-translate/core';
import { InputPageConfig } from '../../components/input-page/input-page.component';
import { EmployeeEducation } from '../../models/employee.model';
import { EmployeeService } from '../../services/employee.service';

@Component({
  selector: 'app-education-input-new',
  templateUrl: './education-input-new.component.html',
  styleUrls: ['./education-input-new.component.scss']
})
export class EducationInputNewComponent implements OnInit {
  config: InputPageConfig = {
    title: 'EMPLOYEE.EDUCATION.TITLE',
    fields: [
      {
        key: 'school',
        label: 'EMPLOYEE.EDUCATION.FORM.SCHOOL',
        type: 'text',
        required: true,
        placeholder: 'EMPLOYEE.EDUCATION.FORM.SCHOOL_PLACEHOLDER',
        width: 2
      },
      {
        key: 'major',
        label: 'EMPLOYEE.EDUCATION.FORM.MAJOR',
        type: 'text',
        required: true,
        placeholder: 'EMPLOYEE.EDUCATION.FORM.MAJOR_PLACEHOLDER',
        width: 2
      },
      {
        key: 'degree',
        label: 'EMPLOYEE.EDUCATION.FORM.DEGREE',
        type: 'text',
        required: true,
        placeholder: 'EMPLOYEE.EDUCATION.FORM.DEGREE_PLACEHOLDER',
        width: 2
      },
      {
        key: 'startDate',
        label: 'EMPLOYEE.EDUCATION.FORM.START_DATE',
        type: 'date',
        required: true,
        width: 2
      },
      {
        key: 'endDate',
        label: 'EMPLOYEE.EDUCATION.FORM.END_DATE',
        type: 'date',
        required: false,
        width: 2
      },
      {
        key: 'remarks',
        label: 'EMPLOYEE.EDUCATION.FORM.REMARKS',
        type: 'textarea',
        required: false,
        placeholder: 'EMPLOYEE.EDUCATION.FORM.REMARKS_PLACEHOLDER',
        rows: 3,
        width: 4
      }
    ],
    layout: 'two-column',
    showSaveButton: true,
    showCancelButton: true,
    showResetButton: true,
    saveButtonText: 'COMMON.SAVE',
    cancelButtonText: 'COMMON.CANCEL',
    resetButtonText: 'COMMON.RESET'
  };

  initialData: any = {};
  loading = false;

  constructor(
    private employeeService: EmployeeService,
    private dialogRef: MatDialogRef<EducationInputNewComponent>,
    private snackBar: MatSnackBar,
    private translate: TranslateService,
    @Inject(MAT_DIALOG_DATA) public data: { 
      mode: 'create' | 'edit'; 
      education?: EmployeeEducation; 
      employeeId: number 
    }
  ) {
    if (data.mode === 'edit' && data.education) {
      this.initialData = {
        ...data.education,
        startDate: data.education.startDate ? new Date(data.education.startDate) : null,
        endDate: data.education.endDate ? new Date(data.education.endDate) : null
      };
    } else {
      // Create mode - set default values
      this.initialData = {};
    }
  }

  ngOnInit(): void {
    // Component initialization
  }

  onSave(data: any): void {
    if (this.validateForm(data)) {
      this.loading = true;

      // Convert dates to ISO string and prepare request data
      const requestData = {
        ...data,
        startDate: data.startDate instanceof Date ? data.startDate.toISOString().split('T')[0] : data.startDate,
        endDate: data.endDate instanceof Date ? data.endDate.toISOString().split('T')[0] : data.endDate
      };

      if (this.data.mode === 'create') {
        this.employeeService.addEducation(this.data.employeeId, requestData).subscribe({
          next: (response: any) => {
            this.snackBar.open(
              this.translate.instant('EMPLOYEE.EDUCATION.CREATE_SUCCESS'),
              this.translate.instant('ACTIONS.CLOSE'),
              { duration: 3000 }
            );
            this.dialogRef.close(response.data);
            this.loading = false;
          },
          error: (error: any) => {
            console.error('Error creating education:', error);
            this.snackBar.open(
              this.translate.instant('ERROR.SAVE_EDUCATION'),
              this.translate.instant('ACTIONS.CLOSE'),
              { duration: 3000 }
            );
            this.loading = false;
          }
        });
      } else {
        this.employeeService.updateEducation(this.data.employeeId, this.data.education!.id!, requestData).subscribe({
          next: (response: any) => {
            this.snackBar.open(
              this.translate.instant('EMPLOYEE.EDUCATION.UPDATE_SUCCESS'),
              this.translate.instant('ACTIONS.CLOSE'),
              { duration: 3000 }
            );
            this.dialogRef.close(response.data);
            this.loading = false;
          },
          error: (error: any) => {
            console.error('Error updating education:', error);
            this.snackBar.open(
              this.translate.instant('ERROR.SAVE_EDUCATION'),
              this.translate.instant('ACTIONS.CLOSE'),
              { duration: 3000 }
            );
            this.loading = false;
          }
        });
      }
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
    if (!data.school) {
      this.snackBar.open(
        this.translate.instant('EMPLOYEE.EDUCATION.VALIDATION.REQUIRED.SCHOOL'),
        this.translate.instant('ACTIONS.CLOSE'),
        { duration: 3000 }
      );
      return false;
    }

    if (!data.major) {
      this.snackBar.open(
        this.translate.instant('EMPLOYEE.EDUCATION.VALIDATION.REQUIRED.MAJOR'),
        this.translate.instant('ACTIONS.CLOSE'),
        { duration: 3000 }
      );
      return false;
    }

    if (!data.degree) {
      this.snackBar.open(
        this.translate.instant('EMPLOYEE.EDUCATION.VALIDATION.REQUIRED.DEGREE'),
        this.translate.instant('ACTIONS.CLOSE'),
        { duration: 3000 }
      );
      return false;
    }

    if (!data.startDate) {
      this.snackBar.open(
        this.translate.instant('EMPLOYEE.EDUCATION.VALIDATION.REQUIRED.START_DATE'),
        this.translate.instant('ACTIONS.CLOSE'),
        { duration: 3000 }
      );
      return false;
    }

    if (data.endDate && data.startDate && new Date(data.endDate) < new Date(data.startDate)) {
      this.snackBar.open(
        this.translate.instant('EMPLOYEE.EDUCATION.VALIDATION.END_DATE_BEFORE_START'),
        this.translate.instant('ACTIONS.CLOSE'),
        { duration: 3000 }
      );
      return false;
    }

    return true;
  }
} 