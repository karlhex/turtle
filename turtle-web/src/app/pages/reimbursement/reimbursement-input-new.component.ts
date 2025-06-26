import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { TranslateService } from '@ngx-translate/core';
import { InputPageConfig } from '../../components/input-page/input-page.component';
import { Reimbursement, CreateReimbursementRequest, UpdateReimbursementRequest } from '../../models/reimbursement.model';
import { ReimbursementService } from '../../services/reimbursement.service';
import { EmployeeService } from '../../services/employee.service';
import { ProjectService } from '../../services/project.service';
import { Employee } from '../../models/employee.model';
import { Project } from '../../models/project.model';
import { ReimbursementStatus } from '../../types/reimbursement-status.enum';

@Component({
  selector: 'app-reimbursement-input-new',
  templateUrl: './reimbursement-input-new.component.html',
  styleUrls: ['./reimbursement-input-new.component.scss']
})
export class ReimbursementInputNewComponent implements OnInit {
  config: InputPageConfig = {
    title: 'REIMBURSEMENT.TITLE',
    fields: [
      {
        key: 'applicationDate',
        label: 'REIMBURSEMENT.APPLICATION_DATE',
        type: 'date',
        required: true,
        width: 2
      },
      {
        key: 'applicantId',
        label: 'REIMBURSEMENT.APPLICANT',
        type: 'select',
        required: true,
        width: 2,
        options: []
      },
      {
        key: 'projectId',
        label: 'REIMBURSEMENT.PROJECT',
        type: 'select',
        required: false,
        width: 2,
        options: []
      },
      {
        key: 'totalAmount',
        label: 'REIMBURSEMENT.TOTAL_AMOUNT',
        type: 'number',
        required: true,
        placeholder: 'REIMBURSEMENT.TOTAL_AMOUNT_PLACEHOLDER',
        width: 2,
        min: 0,
        step: 0.01
      },
      {
        key: 'status',
        label: 'REIMBURSEMENT.STATUS',
        type: 'select',
        required: true,
        width: 2,
        options: [
          { value: ReimbursementStatus.DRAFT, label: 'REIMBURSEMENT.STATUS_DRAFT' },
          { value: ReimbursementStatus.PENDING, label: 'REIMBURSEMENT.STATUS_PENDING' },
          { value: ReimbursementStatus.APPROVED, label: 'REIMBURSEMENT.STATUS_APPROVED' },
          { value: ReimbursementStatus.REJECTED, label: 'REIMBURSEMENT.STATUS_REJECTED' }
        ]
      },
      {
        key: 'remarks',
        label: 'REIMBURSEMENT.REMARKS',
        type: 'textarea',
        required: false,
        placeholder: 'REIMBURSEMENT.REMARKS_PLACEHOLDER',
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
  employees: Employee[] = [];
  projects: Project[] = [];

  constructor(
    private reimbursementService: ReimbursementService,
    private employeeService: EmployeeService,
    private projectService: ProjectService,
    private dialogRef: MatDialogRef<ReimbursementInputNewComponent>,
    private snackBar: MatSnackBar,
    private translate: TranslateService,
    @Inject(MAT_DIALOG_DATA) public data: { mode: 'create' | 'edit' | 'view'; reimbursement?: Reimbursement }
  ) {
    if (data.mode === 'edit' && data.reimbursement) {
      this.initialData = { 
        ...data.reimbursement,
        applicationDate: data.reimbursement.applicationDate ? new Date(data.reimbursement.applicationDate) : null
      };
    } else if (data.mode === 'view' && data.reimbursement) {
      this.initialData = { 
        ...data.reimbursement,
        applicationDate: data.reimbursement.applicationDate ? new Date(data.reimbursement.applicationDate) : null
      };
      // Disable form for view mode
      this.config.fields.forEach(field => {
        field.disabled = true;
      });
      this.config.showSaveButton = false;
      this.config.showResetButton = false;
    } else {
      // Create mode - set default values
      this.initialData = {
        applicationDate: new Date(),
        status: ReimbursementStatus.DRAFT
      };
    }
  }

  ngOnInit(): void {
    this.loadEmployees();
    this.loadProjects();
  }

  private loadEmployees(): void {
    this.employeeService.getActiveEmployees().subscribe({
      next: (response: any) => {
        if (response.code === 200) {
          this.employees = response.data;
          const applicantField = this.config.fields.find(f => f.key === 'applicantId');
          if (applicantField) {
            applicantField.options = this.employees.map(employee => ({
              value: employee.id,
              label: employee.name
            }));
          }
        }
      },
      error: (error: any) => console.error('Error loading employees:', error)
    });
  }

  private loadProjects(): void {
    this.projectService.getProjects({ page: 0, size: 1000 }).subscribe({
      next: (response: any) => {
        if (response.code === 200) {
          this.projects = response.data.content || response.data;
          const projectField = this.config.fields.find(f => f.key === 'projectId');
          if (projectField) {
            projectField.options = [
              { value: null, label: 'REIMBURSEMENT.NO_PROJECT' },
              ...this.projects.map(project => ({
                value: project.id,
                label: project.projectName
              }))
            ];
          }
        }
      },
      error: (error: any) => console.error('Error loading projects:', error)
    });
  }

  onSave(data: any): void {
    if (this.validateForm(data)) {
      this.loading = true;

      // Convert date to ISO string
      const requestData = {
        ...data,
        applicationDate: data.applicationDate instanceof Date ? data.applicationDate.toISOString().split('T')[0] : data.applicationDate
      };

      if (this.data.mode === 'create') {
        const createRequest: CreateReimbursementRequest = {
          applicationDate: requestData.applicationDate,
          applicantId: requestData.applicantId,
          projectId: requestData.projectId,
          totalAmount: requestData.totalAmount,
          remarks: requestData.remarks,
          items: [] // TODO: Add items management
        };

        this.reimbursementService.create(createRequest).subscribe({
          next: (response: any) => {
            this.snackBar.open(
              this.translate.instant('REIMBURSEMENT.CREATE_SUCCESS'),
              this.translate.instant('ACTIONS.CLOSE'),
              { duration: 3000 }
            );
            this.dialogRef.close(response);
            this.loading = false;
          },
          error: (error: any) => {
            console.error('Error creating reimbursement:', error);
            this.snackBar.open(
              this.translate.instant('ERROR.SAVE_REIMBURSEMENT'),
              this.translate.instant('ACTIONS.CLOSE'),
              { duration: 3000 }
            );
            this.loading = false;
          }
        });
      } else {
        const updateRequest: UpdateReimbursementRequest = {
          id: this.data.reimbursement!.id!,
          applicationDate: requestData.applicationDate,
          applicantId: requestData.applicantId,
          projectId: requestData.projectId,
          totalAmount: requestData.totalAmount,
          remarks: requestData.remarks,
          items: [] // TODO: Add items management
        };

        this.reimbursementService.update(this.data.reimbursement!.id!, updateRequest).subscribe({
          next: (response: any) => {
            this.snackBar.open(
              this.translate.instant('REIMBURSEMENT.UPDATE_SUCCESS'),
              this.translate.instant('ACTIONS.CLOSE'),
              { duration: 3000 }
            );
            this.dialogRef.close(response);
            this.loading = false;
          },
          error: (error: any) => {
            console.error('Error updating reimbursement:', error);
            this.snackBar.open(
              this.translate.instant('ERROR.SAVE_REIMBURSEMENT'),
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
    if (!data.applicationDate) {
      this.snackBar.open(
        this.translate.instant('REIMBURSEMENT.APPLICATION_DATE_REQUIRED'),
        this.translate.instant('ACTIONS.CLOSE'),
        { duration: 3000 }
      );
      return false;
    }

    if (!data.applicantId) {
      this.snackBar.open(
        this.translate.instant('REIMBURSEMENT.APPLICANT_REQUIRED'),
        this.translate.instant('ACTIONS.CLOSE'),
        { duration: 3000 }
      );
      return false;
    }

    if (!data.totalAmount || data.totalAmount <= 0) {
      this.snackBar.open(
        this.translate.instant('REIMBURSEMENT.TOTAL_AMOUNT_REQUIRED'),
        this.translate.instant('ACTIONS.CLOSE'),
        { duration: 3000 }
      );
      return false;
    }

    if (!data.status) {
      this.snackBar.open(
        this.translate.instant('REIMBURSEMENT.STATUS_REQUIRED'),
        this.translate.instant('ACTIONS.CLOSE'),
        { duration: 3000 }
      );
      return false;
    }

    return true;
  }
} 