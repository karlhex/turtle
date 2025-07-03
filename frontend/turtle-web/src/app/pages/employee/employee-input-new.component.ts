import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { TranslateService } from '@ngx-translate/core';
import { InputPageConfig } from '../../components/input-page/input-page.component';
import { Employee, EmployeeStatus, EmployeeRole, EmployeeEducation, EmployeeJobHistory } from '../../models/employee.model';
import { EmployeeService } from '../../services/employee.service';
import { DepartmentService } from '../../services/department.service';
import { PositionService } from '../../services/position.service';
import { Department } from '../../models/department.model';
import { Position } from '../../models/position.model';

@Component({
  selector: 'app-employee-input-new',
  templateUrl: './employee-input-new.component.html',
  styleUrls: ['./employee-input-new.component.scss']
})
export class EmployeeInputNewComponent implements OnInit {
  config: InputPageConfig = {
    title: 'EMPLOYEE.TITLE',
    fields: [
      {
        key: 'employeeNumber',
        label: 'EMPLOYEE.FORM.BASIC.EMPLOYEE_NUMBER',
        type: 'text',
        required: true,
        placeholder: 'EMPLOYEE.FORM.BASIC.EMPLOYEE_NUMBER_PLACEHOLDER',
        width: 2
      },
      {
        key: 'name',
        label: 'EMPLOYEE.FORM.BASIC.NAME',
        type: 'text',
        required: true,
        placeholder: 'EMPLOYEE.FORM.BASIC.NAME_PLACEHOLDER',
        width: 2
      },
      {
        key: 'email',
        label: 'EMPLOYEE.FORM.BASIC.EMAIL',
        type: 'email',
        required: false,
        placeholder: 'EMPLOYEE.FORM.BASIC.EMAIL_PLACEHOLDER',
        width: 2
      },
      {
        key: 'phone',
        label: 'EMPLOYEE.FORM.BASIC.PHONE',
        type: 'text',
        required: false,
        placeholder: 'EMPLOYEE.FORM.BASIC.PHONE_PLACEHOLDER',
        width: 2
      },
      {
        key: 'departmentId',
        label: 'EMPLOYEE.FORM.DEPARTMENT.DEPARTMENT',
        type: 'select',
        required: false,
        width: 2,
        options: []
      },
      {
        key: 'positionId',
        label: 'EMPLOYEE.FORM.DEPARTMENT.POSITION',
        type: 'select',
        required: false,
        width: 2,
        options: []
      },
      {
        key: 'hireDate',
        label: 'EMPLOYEE.FORM.DEPARTMENT.HIRE_DATE',
        type: 'date',
        required: false,
        width: 2
      },
      {
        key: 'leaveDate',
        label: 'EMPLOYEE.FORM.DEPARTMENT.LEAVE_DATE',
        type: 'date',
        required: false,
        width: 2
      },
      {
        key: 'birthday',
        label: 'EMPLOYEE.FORM.PERSONAL.BIRTHDAY',
        type: 'date',
        required: false,
        width: 2
      },
      {
        key: 'gender',
        label: 'EMPLOYEE.FORM.PERSONAL.GENDER_NAME',
        type: 'select',
        required: false,
        width: 2,
        options: [
          { value: 'male', label: this.translate.instant('employee.form.personal.gender.male') },
          { value: 'female', label: this.translate.instant('employee.form.personal.gender.female') },
          { value: 'other', label: this.translate.instant('employee.form.personal.gender.other') }
        ]
      },
      {
        key: 'idNumber',
        label: 'EMPLOYEE.FORM.PERSONAL.ID_NUMBER',
        type: 'text',
        required: true,
        placeholder: 'EMPLOYEE.FORM.PERSONAL.ID_NUMBER_PLACEHOLDER',
        width: 2
      },
      {
        key: 'ethnicity',
        label: 'EMPLOYEE.FORM.PERSONAL.ETHNICITY',
        type: 'text',
        required: false,
        placeholder: 'EMPLOYEE.FORM.PERSONAL.ETHNICITY_PLACEHOLDER',
        width: 2
      },
      {
        key: 'emergencyContactName',
        label: 'EMPLOYEE.FORM.EMERGENCY.PERSON',
        type: 'text',
        required: false,
        placeholder: 'EMPLOYEE.FORM.EMERGENCY.PERSON_PLACEHOLDER',
        width: 2
      },
      {
        key: 'emergencyContactPhone',
        label: 'EMPLOYEE.FORM.EMERGENCY.PHONE',
        type: 'text',
        required: false,
        placeholder: 'EMPLOYEE.FORM.EMERGENCY.PHONE_PLACEHOLDER',
        width: 2
      },
      {
        key: 'contractType',
        label: 'EMPLOYEE.FORM.CONTRACT.TYPE_NAME',
        type: 'select',
        required: false,
        width: 2,
        options: [
          { value: 'fullTime', label: this.translate.instant('employee.form.contract.type.full_time') },
          { value: 'partTime', label: this.translate.instant('employee.form.contract.type.part_time') },
          { value: 'contractor', label: this.translate.instant('employee.form.contract.type.contractor') },
          { value: 'intern', label: this.translate.instant('employee.form.contract.type.intern') },
        ]
      },
      {
        key: 'contractDuration',
        label: 'EMPLOYEE.FORM.CONTRACT.DURATION',
        type: 'number',
        required: false,
        placeholder: 'EMPLOYEE.FORM.CONTRACT.DURATION_PLACEHOLDER',
        width: 2,
        min: 1,
        max: 120
      },
      {
        key: 'contractStartDate',
        label: 'EMPLOYEE.FORM.CONTRACT.START_DATE',
        type: 'date',
        required: false,
        width: 2
      },
      {
        key: 'status',
        label: 'EMPLOYEE.FORM.CONTRACT.STATUS',
        type: 'select',
        required: true,
        width: 2,
        options: [
          { value: EmployeeStatus.APPLICATION, label: this.translate.instant('types.employeestatus.application') },
          { value: EmployeeStatus.ACTIVE, label: this.translate.instant('types.employeestatus.active') },
          { value: EmployeeStatus.RESIGNED, label: this.translate.instant('types.employeestatus.resigned') },
          { value: EmployeeStatus.SUSPENDED, label: this.translate.instant('types.employeestatus.suspended') }
        ]
      },
      {
        key: 'remarks',
        label: 'EMPLOYEE.FORM.ADDITIONAL.REMARKS',
        type: 'textarea',
        required: false,
        placeholder: 'EMPLOYEE.FORM.ADDITIONAL.REMARKS_PLACEHOLDER',
        rows: 3,
        width: 4
      }
    ],
    layout: 'two-column',
    showSaveButton: true,
    showCancelButton: true,
    showResetButton: true,
    saveButtonText: this.translate.instant('common.button.save'),
    cancelButtonText: this.translate.instant('common.button.cancel'),
    resetButtonText: this.translate.instant('common.button.reset')
  };

  initialData: any = {};
  loading = false;
  departments: Department[] = [];
  positions: Position[] = [];
  
  // Tab data
  educations: EmployeeEducation[] = [];
  jobHistories: EmployeeJobHistory[] = [];
  employeeId: number | null = null;
  isEditMode = false;

  constructor(
    private employeeService: EmployeeService,
    private departmentService: DepartmentService,
    private positionService: PositionService,
    private dialogRef: MatDialogRef<EmployeeInputNewComponent>,
    private snackBar: MatSnackBar,
    private translate: TranslateService,
    @Inject(MAT_DIALOG_DATA) public data: { mode: 'create' | 'edit' | 'view'; employee?: Employee }
  ) {
    if (data.mode === 'edit' && data.employee) {
      this.initialData = { 
        ...data.employee,
        departmentId: data.employee.department?.id,
        positionId: data.employee.position?.id,
        hireDate: data.employee.hireDate ? new Date(data.employee.hireDate) : null,
        leaveDate: data.employee.leaveDate ? new Date(data.employee.leaveDate) : null,
        birthday: data.employee.birthday ? new Date(data.employee.birthday) : null,
        contractStartDate: data.employee.contractStartDate ? new Date(data.employee.contractStartDate) : null
      };
      this.employeeId = data.employee.id!;
      this.educations = data.employee.educations || [];
      this.jobHistories = data.employee.jobHistories || [];
      this.isEditMode = true;
    } else if (data.mode === 'view' && data.employee) {
      this.initialData = { 
        ...data.employee,
        departmentId: data.employee.department?.id,
        positionId: data.employee.position?.id,
        hireDate: data.employee.hireDate ? new Date(data.employee.hireDate) : null,
        leaveDate: data.employee.leaveDate ? new Date(data.employee.leaveDate) : null,
        birthday: data.employee.birthday ? new Date(data.employee.birthday) : null,
        contractStartDate: data.employee.contractStartDate ? new Date(data.employee.contractStartDate) : null
      };
      this.employeeId = data.employee.id!;
      this.educations = data.employee.educations || [];
      this.jobHistories = data.employee.jobHistories || [];
      // Disable form for view mode
      this.config.fields.forEach(field => {
        field.disabled = true;
      });
      this.config.showSaveButton = false;
      this.config.showResetButton = false;
    } else {
      // Create mode - set default values
      this.initialData = {
        status: EmployeeStatus.APPLICATION
      };
      this.isEditMode = false;
    }
  }

  ngOnInit(): void {
    this.loadDepartments();
    this.loadPositions();
  }

  private loadDepartments(): void {
    this.departmentService.getDepartments(0, 1000).subscribe({
      next: (response: any) => {
        if (response.code === 200) {
          this.departments = response.data.content || response.data;
          const departmentField = this.config.fields.find(f => f.key === 'departmentId');
          if (departmentField) {
            departmentField.options = this.departments.map(dept => ({
              value: dept.id,
              label: dept.name
            }));
          }
        }
      },
      error: (error: any) => console.error('Error loading departments:', error)
    });
  }

  private loadPositions(): void {
    this.positionService.getPositions(0, 1000).subscribe({
      next: (response: any) => {
        if (response.code === 200) {
          this.positions = response.data.content || response.data;
          const positionField = this.config.fields.find(f => f.key === 'positionId');
          if (positionField) {
            positionField.options = this.positions.map(pos => ({
              value: pos.id,
              label: pos.name
            }));
          }
        }
      },
      error: (error: any) => console.error('Error loading positions:', error)
    });
  }

  onSave(data: any): void {
    if (this.validateForm(data)) {
      this.loading = true;

      // Convert dates to ISO string and prepare request data
      const requestData = {
        ...data,
        hireDate: data.hireDate instanceof Date ? data.hireDate.toISOString().split('T')[0] : data.hireDate,
        leaveDate: data.leaveDate instanceof Date ? data.leaveDate.toISOString().split('T')[0] : data.leaveDate,
        birthday: data.birthday instanceof Date ? data.birthday.toISOString().split('T')[0] : data.birthday,
        contractStartDate: data.contractStartDate instanceof Date ? data.contractStartDate.toISOString().split('T')[0] : data.contractStartDate,
        educations: this.educations,
        jobHistories: this.jobHistories
      };

      if (this.data.mode === 'create') {
        this.employeeService.createEmployee(requestData).subscribe({
          next: (response: any) => {
            this.snackBar.open(
              this.translate.instant('EMPLOYEE.CREATE_SUCCESS'),
              this.translate.instant('ACTIONS.CLOSE'),
              { duration: 3000 }
            );
            this.dialogRef.close(response);
            this.loading = false;
          },
          error: (error: any) => {
            console.error('Error creating employee:', error);
            this.snackBar.open(
              this.translate.instant('ERROR.SAVE_EMPLOYEE'),
              this.translate.instant('ACTIONS.CLOSE'),
              { duration: 3000 }
            );
            this.loading = false;
          }
        });
      } else {
        this.employeeService.updateEmployee(this.data.employee!.id!, requestData).subscribe({
          next: (response: any) => {
            this.snackBar.open(
              this.translate.instant('EMPLOYEE.UPDATE_SUCCESS'),
              this.translate.instant('ACTIONS.CLOSE'),
              { duration: 3000 }
            );
            this.dialogRef.close(response);
            this.loading = false;
          },
          error: (error: any) => {
            console.error('Error updating employee:', error);
            this.snackBar.open(
              this.translate.instant('ERROR.SAVE_EMPLOYEE'),
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

  // Education tab handlers
  onEducationEdited(educations: EmployeeEducation[]): void {
    this.educations = educations;
  }

  // Job history tab handlers
  onJobHistoryEdited(jobHistories: EmployeeJobHistory[]): void {
    this.jobHistories = jobHistories;
    console.log('Job history edited:', jobHistories);
  }

  private validateForm(data: any): boolean {
    if (!data.employeeNumber) {
      this.snackBar.open(
        this.translate.instant('EMPLOYEE.VALIDATION.REQUIRED.EMPLOYEE_NUMBER'),
        this.translate.instant('ACTIONS.CLOSE'),
        { duration: 3000 }
      );
      return false;
    }

    if (!data.name) {
      this.snackBar.open(
        this.translate.instant('EMPLOYEE.VALIDATION.REQUIRED.NAME'),
        this.translate.instant('ACTIONS.CLOSE'),
        { duration: 3000 }
      );
      return false;
    }

    if (!data.idNumber) {
      this.snackBar.open(
        this.translate.instant('EMPLOYEE.VALIDATION.REQUIRED.ID_NUMBER'),
        this.translate.instant('ACTIONS.CLOSE'),
        { duration: 3000 }
      );
      return false;
    }

    if (data.email && !this.isValidEmail(data.email)) {
      this.snackBar.open(
        this.translate.instant('EMPLOYEE.VALIDATION.EMAIL.INVALID'),
        this.translate.instant('ACTIONS.CLOSE'),
        { duration: 3000 }
      );
      return false;
    }

    return true;
  }

  private isValidEmail(email: string): boolean {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
  }
} 