import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { DepartmentService } from '../../services/department.service';
import { EmployeeService } from '../../services/employee.service';
import { Employee } from '@models/employee.model';
import { TranslateService } from '@ngx-translate/core';
import { Department } from '../../models/department.model';

@Component({
  selector: 'app-department-dialog',
  templateUrl: './department-dialog.component.html',
  styleUrls: ['./department-dialog.component.scss'],
})
export class DepartmentDialogComponent implements OnInit {
  departmentForm: FormGroup;
  departments: Department[] = [];
  employees: Employee[] = [];

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<DepartmentDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { department: Department },
    private departmentService: DepartmentService,
    private employeeService: EmployeeService,
    private snackBar: MatSnackBar,
    private translate: TranslateService
  ) {
    this.departmentForm = this.fb.group({
      name: ['', [Validators.required]],
      code: ['', [Validators.required]],
      description: ['', [Validators.required]],
      parentId: [null],
      managerId: [null],
      isActive: [true],
    });
  }

  ngOnInit() {
    this.loadDepartments();
    this.loadEmployees();

    if (this.data.department) {
      this.departmentForm.patchValue({
        name: this.data.department.name,
        code: this.data.department.code,
        description: this.data.department.description,
        parentId: this.data.department.parentId,
        managerId: this.data.department.managerId,
        isActive: this.data.department.isActive,
      });
    }
  }

  loadDepartments() {
    this.departmentService.getDepartments().subscribe({
      next: response => {
        if (response.data?.content) {
          this.departments = response.data.content.filter(
            dept => !this.data.department || dept.id !== this.data.department.id
          );
        }
      },
      error: error => {
        console.error('Error loading departments:', error);
        this.snackBar.open(error.message, 'OK', { duration: 3000 });
      },
    });
  }

  loadEmployees() {
    this.employeeService.getEmployees().subscribe({
      next: response => {
        if (response.data?.content) {
          this.employees = response.data.content;
        }
      },
      error: error => {
        console.error('Error loading employees:', error);
        this.snackBar.open(error.message, 'OK', { duration: 3000 });
      },
    });
  }

  onSubmit() {
    if (this.departmentForm.valid) {
      const department: Department = {
        ...this.data.department,
        ...this.departmentForm.value,
      };
      console.log(department);

      const request = this.data.department?.id
        ? this.departmentService.updateDepartment(this.data.department.id, department)
        : this.departmentService.createDepartment(department);

      request.subscribe({
        next: response => {
          if (response.data) {
            this.snackBar.open(this.translate.instant('department.message.saveSuccess'), 'OK', {
              duration: 3000,
            });
            this.dialogRef.close(response.data);
          }
        },
        error: error => {
          console.error('Error saving department:', error);
          this.snackBar.open(error.message, 'OK', { duration: 3000 });
        },
      });
    }
  }

  onCancel() {
    this.dialogRef.close();
  }
}
