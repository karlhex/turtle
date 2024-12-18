import { Component, Inject, OnInit, ViewChild, AfterViewInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef, MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { MatAutocompleteSelectedEvent } from '@angular/material/autocomplete';
import { Employee } from '@models/employee.model';
import { DepartmentService, Department } from '../../services/department.service';
import { EmployeeEducationService, EmployeeEducation } from '../../services/employee-education.service';
import { EducationDialogComponent } from './education-dialog.component';
import { ConfirmDialogComponent } from '../../components/confirmdialog/confirm-dialog.component';
import { ApiResponse, PageResponse } from 'src/app/models/api.model';
import { debounceTime, distinctUntilChanged, switchMap } from 'rxjs/operators';
import { Subject } from 'rxjs';
import { EmployeeJobHistoryService, EmployeeJobHistory } from '../../services/employee-job-history.service';
import { JobHistoryDialogComponent } from './job-history-dialog.component';
import { EmployeeContractType } from '../../types/employee-contract-type.enum';
import { Gender } from '../../types/gender.enum';

@Component({
  selector: 'app-employee-dialog',
  templateUrl: './employee-dialog.component.html',
  styleUrls: ['./employee-dialog.component.scss']
})

export class EmployeeDialogComponent implements OnInit, AfterViewInit {
  employeeForm!: FormGroup;
  departments: Department[] = [];
  contractTypes = Object.values(EmployeeContractType);  
  genders = Object.values(Gender);  

  // Education tab
  educations = new MatTableDataSource<EmployeeEducation>([]);
  displayedColumns = ['school', 'major', 'degree', 'startDate', 'endDate', 'actions'];
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  // Job History tab
  jobHistories = new MatTableDataSource<EmployeeJobHistory>([]);
  jobHistoryColumns = ['companyName', 'position', 'startDate', 'endDate', 'remarks', 'actions'];

  // Loading states
  isLoadingEducations = false;
  isLoadingJobHistory = false;

  constructor(
    private fb: FormBuilder,
    private dialog: MatDialog,
    private dialogRef: MatDialogRef<EmployeeDialogComponent>,
    private departmentService: DepartmentService,
    private educationService: EmployeeEducationService,
    private jobHistoryService: EmployeeJobHistoryService,
    @Inject(MAT_DIALOG_DATA) public data: { employee: Employee; mode: 'view' | 'edit' }
  ) {
    this.initForm();
  }

  ngOnInit(): void {
    this.loadDepartments();
    if (this.data.employee.id) {
      this.loadEducations();
      this.loadJobHistory();
    }
  }

  ngAfterViewInit() {
    this.educations.paginator = this.paginator;
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
      employeeNumber: ['', [Validators.required]],
      name: ['', [Validators.required]],
      email: ['', [Validators.email]],
      phone: [''],
      department: [null],
      position: [''],
      hireDate: [null],
      leaveDate: [null],
      birthday: [null],
      gender: [''],
      ethnicity: [''],
      idType: [''],
      idNumber: ['', [Validators.required]],
      contractType: [null],
      contractDuration: [null],
      contractStartDate: [null],
      remarks: [''],
      isActive: [true],
      emergencyContactName: [''],
      emergencyContactPhone: ['']
    });

    // Set initial values
    if (this.data.employee) {
      this.employeeForm.patchValue(this.data.employee);
    }

    // Handle form state based on mode
    if (this.data.mode === 'view') {
      this.employeeForm.disable();
    } else if (this.data.mode === 'edit') {
      this.employeeForm.enable();
    }
  }

  onSubmit(): void {
    if (this.employeeForm.valid) {
      const formValue = this.employeeForm.getRawValue();
      const updatedEmployee: Employee = {
        ...this.data.employee,
        ...formValue,
        hireDate: formValue.hireDate ? new Date(formValue.hireDate).toISOString().split('T')[0] : null,
        leaveDate: formValue.leaveDate ? new Date(formValue.leaveDate).toISOString().split('T')[0] : null,
        birthday: formValue.birthday ? new Date(formValue.birthday).toISOString().split('T')[0] : null,
        contractStartDate: formValue.contractStartDate ? new Date(formValue.contractStartDate).toISOString().split('T')[0] : null
      };
      console.log('in onSubmit', updatedEmployee);
      this.dialogRef.close(updatedEmployee);
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }

  private loadEducations(): void {
    if (!this.data.employee.id) {
      console.error('Cannot load educations for employee without id');
      return;
    }

    this.isLoadingEducations = true;
    this.educationService.getEducations(this.data.employee.id).subscribe({
      next: (response) => {
        if (response.data) {
          this.educations.data = response.data;
        }
        this.isLoadingEducations = false;
      },
      error: (error) => {
        console.error('Error loading educations:', error);
        this.isLoadingEducations = false;
      }
    });
  }

  private loadJobHistory(): void {
    if (!this.data.employee.id) {
      console.error('Cannot load job history for employee without id');
      return;
    }

    this.isLoadingJobHistory = true;
    this.jobHistoryService.getJobHistory(this.data.employee.id).subscribe({
      next: (response) => {
        if (response.code === 200 && response.data) {
          this.jobHistories.data = response.data;
        }
        this.isLoadingJobHistory = false;
      },
      error: (error) => {
        console.error('Error loading job history:', error);
        this.isLoadingJobHistory = false;
      }
    });
  }

  onAddEducation(): void {
    if (!this.data.employee.id) {
      console.error('Cannot add education for employee without id');
      return;
    }

    const dialogRef = this.dialog.open(EducationDialogComponent, {
      width: '600px',
      data: {
        employeeId: this.data.employee.id,
        mode: 'add'
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        console.log(result);
        if (!this.data.employee.educations) {
          this.data.employee.educations = [];
        }
        this.data.employee.educations.push(result);
      }
    });
  }

  onEditEducation(education: EmployeeEducation): void {
    if (!this.data.employee.id) {
      console.error('Cannot edit education for employee without id');
      return;
    }

    if (!education.id) {
      console.error('Cannot edit education without id');
      return;
    }

    const employeeId = this.data.employee.id;
    const educationId = education.id;

    const dialogRef = this.dialog.open(EducationDialogComponent, {
      width: '600px',
      data: {
        employeeId: employeeId,
        education: education,
        mode: 'edit'
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result && employeeId && educationId) {
        this.educationService.updateEducation(employeeId, educationId, result).subscribe({
          next: (response) => {
            if (response.code === 200 && response.data) {
              this.loadEducations();
            }
          },
          error: (error) => {
            console.error('Error updating education:', error);
          }
        });
      }
    });
  }

  onViewEducation(education: EmployeeEducation): void {
    if (!this.data.employee.id) {
      console.error('Cannot view education for employee without id');
      return;
    }

    if (!education.id) {
      console.error('Cannot view education without id');
      return;
    }

    const employeeId = this.data.employee.id;
    this.dialog.open(EducationDialogComponent, {
      width: '600px',
      data: {
        employeeId: employeeId,
        education: education,
        mode: 'view'
      }
    });
  }

  onDeleteEducation(education: EmployeeEducation): void {
    if (!this.data.employee.id) {
      console.error('Cannot delete education for employee without id');
      return;
    }

    if (!education.id) {
      console.error('Cannot delete education without id');
      return;
    }

    const employeeId = this.data.employee.id;
    const educationId = education.id;

    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      width: '400px',
      data: {
        title: 'employee.education.actions.delete',
        message: 'employee.education.messages.confirmDelete',
        confirmText: 'employee.education.actions.confirm',
        cancelText: 'employee.education.actions.cancel'
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result && employeeId && educationId) {
        this.educationService.deleteEducation(employeeId, educationId).subscribe({
          next: () => {
            this.loadEducations();
          },
          error: (error) => {
            console.error('Error deleting education:', error);
          }
        });
      }
    });
  }

  onAddJobHistory(): void {
    if (!this.data.employee.id) {
      console.error('Cannot add job history for employee without id');
      return;
    }

    const dialogRef = this.dialog.open(JobHistoryDialogComponent, {
      width: '600px',
      data: {
        employeeId: this.data.employee.id,
        mode: 'add'
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.jobHistoryService.createJobHistory(this.data.employee.id!, result).subscribe({
          next: (response) => {
            if (response.data) {
              this.jobHistories.data = [...this.jobHistories.data, response.data];
            }
          },
          error: (error) => console.error('Error creating job history:', error)
        });
      }
    });
  }

  onEditJobHistory(jobHistory: EmployeeJobHistory): void {
    if (!this.data.employee.id || !jobHistory.id) {
      console.error('Cannot edit job history: Invalid employee or job history ID');
      return;
    }

    const dialogRef = this.dialog.open(JobHistoryDialogComponent, {
      width: '600px',
      data: {
        jobHistory,
        employeeId: this.data.employee.id,
        mode: 'edit'
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.jobHistoryService.updateJobHistory(this.data.employee.id!, jobHistory.id!, result).subscribe({
          next: (response) => {
            if (response.data) {
              const index = this.jobHistories.data.findIndex(e => e.id === jobHistory.id);
              if (index !== -1) {
                const updatedData = [...this.jobHistories.data];
                updatedData[index] = response.data;
                this.jobHistories.data = updatedData;
              }
            }
          },
          error: (error) => console.error('Error updating job history:', error)
        });
      }
    });
  }

  onViewJobHistory(jobHistory: EmployeeJobHistory): void {
    this.dialog.open(JobHistoryDialogComponent, {
      width: '600px',
      data: {
        jobHistory,
        employeeId: this.data.employee.id,
        mode: 'view'
      }
    });
  }

  onDeleteJobHistory(jobHistory: EmployeeJobHistory): void {
    if (!this.data.employee.id || !jobHistory.id) {
      console.error('Cannot delete job history: Invalid employee or job history ID');
      return;
    }

    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: {
        title: 'employee.jobHistory.dialog.deleteTitle',
        message: 'employee.jobHistory.dialog.deleteMessage'
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.jobHistoryService.deleteJobHistory(this.data.employee.id!, jobHistory.id!).subscribe({
          next: () => {
            this.jobHistories.data = this.jobHistories.data.filter(e => e.id !== jobHistory.id);
          },
          error: (error) => console.error('Error deleting job history:', error)
        });
      }
    });
  }
}
