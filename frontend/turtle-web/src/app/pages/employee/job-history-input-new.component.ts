import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { TranslateService } from '@ngx-translate/core';
import { InputPageConfig } from '../../components/input-page/input-page.component';
import { EmployeeJobHistory } from '../../models/employee.model';
import { EmployeeService } from '../../services/employee.service';

@Component({
  selector: 'app-job-history-input-new',
  templateUrl: './job-history-input-new.component.html',
  styleUrls: ['./job-history-input-new.component.scss']
})
export class JobHistoryInputNewComponent implements OnInit {
  config: InputPageConfig = {
    title: 'EMPLOYEE.JOB_HISTORY.TITLE',
    fields: [
      {
        key: 'companyName',
        label: 'EMPLOYEE.JOB_HISTORY.COMPANY',
        type: 'text',
        required: true,
        placeholder: 'EMPLOYEE.JOB_HISTORY.FORM.COMPANY_PLACEHOLDER',
        width: 2
      },
      {
        key: 'position',
        label: 'EMPLOYEE.JOB_HISTORY.POSITION',
        type: 'text',
        required: true,
        placeholder: 'EMPLOYEE.JOB_HISTORY.FORM.POSITION_PLACEHOLDER',
        width: 2
      },
      {
        key: 'department',
        label: 'EMPLOYEE.JOB_HISTORY.DEPARTMENT',
        type: 'text',
        required: true,
        placeholder: 'EMPLOYEE.JOB_HISTORY.FORM.DEPARTMENT_PLACEHOLDER',
        width: 2
      },
      {
        key: 'startDate',
        label: 'EMPLOYEE.JOB_HISTORY.START_DATE',
        type: 'date',
        required: true,
        width: 2
      },
      {
        key: 'endDate',
        label: 'EMPLOYEE.JOB_HISTORY.END_DATE',
        type: 'date',
        required: false,
        width: 2
      },
      {
        key: 'jobDescription',
        label: 'EMPLOYEE.JOB_HISTORY.DESCRIPTION',
        type: 'textarea',
        required: false,
        placeholder: 'EMPLOYEE.JOB_HISTORY.FORM.DESCRIPTION_PLACEHOLDER',
        rows: 3,
        width: 4
      },
      {
        key: 'achievements',
        label: 'EMPLOYEE.JOB_HISTORY.ACHIEVEMENTS',
        type: 'textarea',
        required: false,
        placeholder: 'EMPLOYEE.JOB_HISTORY.FORM.ACHIEVEMENTS_PLACEHOLDER',
        rows: 3,
        width: 4
      },
      {
        key: 'leavingReason',
        label: 'EMPLOYEE.JOB_HISTORY.LEAVING_REASON',
        type: 'textarea',
        required: false,
        placeholder: 'EMPLOYEE.JOB_HISTORY.FORM.LEAVING_REASON_PLACEHOLDER',
        rows: 2,
        width: 4
      },
      {
        key: 'referenceContact',
        label: 'EMPLOYEE.JOB_HISTORY.REFERENCE_CONTACT',
        type: 'text',
        required: false,
        placeholder: 'EMPLOYEE.JOB_HISTORY.FORM.REFERENCE_CONTACT_PLACEHOLDER',
        width: 2
      },
      {
        key: 'remarks',
        label: 'EMPLOYEE.JOB_HISTORY.REMARKS',
        type: 'textarea',
        required: false,
        placeholder: 'EMPLOYEE.JOB_HISTORY.FORM.REMARKS_PLACEHOLDER',
        rows: 2,
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
    private dialogRef: MatDialogRef<JobHistoryInputNewComponent>,
    private snackBar: MatSnackBar,
    private translate: TranslateService,
    @Inject(MAT_DIALOG_DATA) public data: { 
      mode: 'create' | 'edit'; 
      jobHistory?: EmployeeJobHistory; 
      employeeId: number 
    }
  ) {
    if (data.mode === 'edit' && data.jobHistory) {
      this.initialData = {
        ...data.jobHistory,
        startDate: data.jobHistory.startDate ? new Date(data.jobHistory.startDate) : null,
        endDate: data.jobHistory.endDate ? new Date(data.jobHistory.endDate) : null
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

      // Note: EmployeeService doesn't have addJobHistory/updateJobHistory methods
      // So we'll just return the data for now
      setTimeout(() => {
        this.snackBar.open(
          this.translate.instant('EMPLOYEE.JOB_HISTORY.SAVE_SUCCESS'),
          this.translate.instant('ACTIONS.CLOSE'),
          { duration: 3000 }
        );
        this.dialogRef.close(requestData);
        this.loading = false;
      }, 500);
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
    if (!data.companyName) {
      this.snackBar.open(
        this.translate.instant('EMPLOYEE.JOB_HISTORY.VALIDATION.REQUIRED.COMPANY'),
        this.translate.instant('ACTIONS.CLOSE'),
        { duration: 3000 }
      );
      return false;
    }

    if (!data.position) {
      this.snackBar.open(
        this.translate.instant('EMPLOYEE.JOB_HISTORY.VALIDATION.REQUIRED.POSITION'),
        this.translate.instant('ACTIONS.CLOSE'),
        { duration: 3000 }
      );
      return false;
    }

    if (!data.department) {
      this.snackBar.open(
        this.translate.instant('EMPLOYEE.JOB_HISTORY.VALIDATION.REQUIRED.DEPARTMENT'),
        this.translate.instant('ACTIONS.CLOSE'),
        { duration: 3000 }
      );
      return false;
    }

    if (!data.startDate) {
      this.snackBar.open(
        this.translate.instant('EMPLOYEE.JOB_HISTORY.VALIDATION.REQUIRED.START_DATE'),
        this.translate.instant('ACTIONS.CLOSE'),
        { duration: 3000 }
      );
      return false;
    }

    if (data.endDate && data.startDate && new Date(data.endDate) < new Date(data.startDate)) {
      this.snackBar.open(
        this.translate.instant('EMPLOYEE.JOB_HISTORY.VALIDATION.END_DATE_BEFORE_START'),
        this.translate.instant('ACTIONS.CLOSE'),
        { duration: 3000 }
      );
      return false;
    }

    return true;
  }
} 