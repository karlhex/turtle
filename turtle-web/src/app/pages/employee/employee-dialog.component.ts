import { Component, Inject, OnInit, ViewChild, AfterViewInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, FormControl } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef, MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { MatAutocompleteSelectedEvent } from '@angular/material/autocomplete';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Employee, EmployeeJobHistory, EmployeeEducation } from '@models/employee.model';
import { DepartmentService } from '../../services/department.service';
import { EmployeeService } from '../../services/employee.service';
import { EducationDialogComponent } from './education-dialog.component';
import { ConfirmDialogComponent } from '../../components/confirmdialog/confirm-dialog.component';
import { ApiResponse, PageResponse } from 'src/app/models/api.model';
import { debounceTime, distinctUntilChanged, switchMap } from 'rxjs/operators';
import { Subject } from 'rxjs';
import { EmployeeContractType } from '../../types/employee-contract-type.enum';
import { Gender } from '../../types/gender.enum';
import { IdType } from '../../types/id-type.enum';
import { EmployeeStatus } from '../../models/employee.model';
import { Department } from '../../models/department.model';


@Component({
  selector: 'app-employee-dialog',
  templateUrl: './employee-dialog.component.html',
  styleUrls: ['./employee-dialog.component.scss']
})

export class EmployeeDialogComponent implements OnInit {
  employeeForm!: FormGroup;
  isEdit: boolean;
  loading = false;
  isApplication: boolean;
  departments: Department[] = [];
  contractTypes = Object.values(EmployeeContractType);  
  genders = Object.values(Gender);  
  idtypes = Object.values(IdType);
  employeeStatuses = Object.values(EmployeeStatus);

  // Education tab
  educations: EmployeeEducation[] = [];
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  // Job History tab
  jobHistories: EmployeeJobHistory[] = [];

  constructor(
    private dialogRef: MatDialogRef<EmployeeDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: {
      mode: 'create' | 'edit' | 'application';
      employee: any;
    },
    private fb: FormBuilder,
    private employeeService: EmployeeService,
    private departmentService: DepartmentService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar
  ) {
    this.isEdit = data.mode === 'edit';
    this.isApplication = data.mode === 'application';
    this.initForm();
    this.educations = data.employee?.educations || [];
    this.jobHistories = data.employee?.jobHistories || [];
  }

  ngOnInit(): void {
    if (!this.isApplication) {
      this.loadDepartments();
    }
  }

  ngAfterViewInit() {

  }

  onEducationEdit(educations: EmployeeEducation[]): void {
    this.educations = educations;
  }

  onJobHistoryEdit(jobHistories: EmployeeJobHistory[]): void {
    this.jobHistories = jobHistories;
  }

  private loadDepartments(): void {
    this.departmentService.getDepartments(0, 100).subscribe({
      next: (response) => {
        if (response.data?.content) {
          this.departments = response.data.content;
          // After departments are loaded, find and set the correct department
          if (this.data.employee && this.data.employee.department) {
            const selectedDepartment = this.departments.find(
              dept => dept.id === this.data?.employee?.department?.id
            );
            if (selectedDepartment) {
              this.employeeForm.patchValue({ department: selectedDepartment });
            }
          }
        }
      },
      error: (error) => {
        console.error('Error loading departments:', error);
      }
    });
  }

  private initForm(): void {
    this.employeeForm = this.fb.group({
      employeeNumber: ['', Validators.required],
      name: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      phone: [''],
      birthday: [''],
      gender: [''],
      ethnicity: [''],
      idType: [''],
      idNumber: ['', Validators.required],
      emergencyContactName: [''],
      emergencyContactPhone: [''],
      status: ['APPLICATION'],
      remarks: ['']
    });

    if (!this.isApplication) {
      this.employeeForm.addControl('department', new FormControl('', Validators.required));
      this.employeeForm.addControl('position', new FormControl('', Validators.required));
      this.employeeForm.addControl('hireDate', new FormControl('', Validators.required));
      this.employeeForm.addControl('leaveDate', new FormControl(''));
      this.employeeForm.addControl('contractType', new FormControl(''));
      this.employeeForm.addControl('contractDuration', new FormControl(''));
      this.employeeForm.addControl('contractStartDate', new FormControl(''));
    }

    if (this.isEdit || this.data.employee) {
      this.employeeForm.patchValue(this.data.employee);
    }
  }

  submit(): void {
    if (this.employeeForm.valid) {
      const formValue = this.employeeForm.getRawValue();
      const updatedEmployee: Employee = {
        ...this.data.employee,
        ...formValue,
        educations: this.educations,
        jobHistories: this.jobHistories,
        hireDate: formValue.hireDate ? new Date(formValue.hireDate).toISOString().split('T')[0] : null,
        leaveDate: formValue.leaveDate ? new Date(formValue.leaveDate).toISOString().split('T')[0] : null,
        birthday: formValue.birthday ? new Date(formValue.birthday).toISOString().split('T')[0] : null,
        contractStartDate: formValue.contractStartDate ? new Date(formValue.contractStartDate).toISOString().split('T')[0] : null
      };
      const request = this.isEdit
      ? this.employeeService.updateEmployee(updatedEmployee.id!, updatedEmployee)
      : this.employeeService.createEmployee(updatedEmployee);

      console.log('in onSubmit', updatedEmployee);
      request.subscribe({
        next: (response: ApiResponse<Employee>) => {
          console.log(response);
          if (response.code === 200) {
            this.dialogRef.close(response.data);
            this.showSuccess(this.isEdit ? 'Employee updated successfully' : 'Employee created successfully');
          } else {
            this.showError(response.message || 'Failed to save employee');
            this.loading = false;
          }
        },
        error: (error) => {
          this.showError('Error saving employee');
          console.error('Failed to save employee:', error);
          this.loading = false;
        }
      });
    } else {
      Object.keys(this.employeeForm.controls).forEach(key => {
        const control = this.employeeForm.get(key);
        if (control?.invalid) {
          control.markAsTouched();
        }
      });
    }

  }

  onCancel(): void {
    this.dialogRef.close();
  }

  private showError(message: string): void {
    this.snackBar.open(message, 'Close', {
      duration: 5000,
      panelClass: ['error-snackbar']
    });
  }

  private showSuccess(message: string): void {
    this.snackBar.open(message, 'Close', {
      duration: 3000,
      panelClass: ['success-snackbar']
    });
  }  
}
