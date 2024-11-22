import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Employee } from '../../services/employee.service';
import { DepartmentService, Department } from '../../services/department.service';
import { PersonService, Person } from '../../services/person.service';
import { ApiResponse, PageResponse } from 'src/app/core/models/api.model';
import { debounceTime, distinctUntilChanged, switchMap } from 'rxjs/operators';
import { Subject } from 'rxjs';

@Component({
  selector: 'app-employee-dialog',
  templateUrl: './employee-dialog.component.html',
  styleUrls: ['./employee-dialog.component.scss']
})
export class EmployeeDialogComponent implements OnInit {
  employeeForm!: FormGroup;
  departments: Department[] = [];
  filteredPersons: Person[] = [];
  private searchPersons$ = new Subject<string>();

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<EmployeeDialogComponent>,
    private departmentService: DepartmentService,
    private personService: PersonService,
    @Inject(MAT_DIALOG_DATA) public data: { employee: Employee; mode: 'view' | 'edit' }
  ) {
    this.initForm();
    this.setupPersonSearch();
  }

  ngOnInit(): void {
    this.loadDepartments();
  }

  private loadDepartments(): void {
    this.departmentService.getDepartments(0, 100).subscribe({
      next: (response) => {
        if (response.data?.content) {
          this.departments = response.data.content;
          // After departments are loaded, find and set the correct department
          if (this.data.employee && this.data.employee.department) {
            const selectedDepartment = this.departments.find(
              dept => dept.id === this.data.employee.department.id
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

  private setupPersonSearch(): void {
    this.searchPersons$.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      switchMap(query => this.personService.searchPersons(query))
    ).subscribe({
      next: (response: ApiResponse<Person[]>) => {
        if (response.code === 200 && response.data) {
          this.filteredPersons = response.data;
        }
      },
      error: (error) => {
        console.error('Error searching persons:', error);
      }
    });
  }

  onSearchPerson(event: any): void {
    const query = event.target.value;
    if (query.length >= 2) {
      this.searchPersons$.next(query);
    } else {
      this.filteredPersons = [];
    }
  }

  displayPersonFn(person: Person | null): string {
    if (!person) return '';
    return `${person.firstName} ${person.lastName}`;
  }

  onPersonSelected(person: Person): void {
    const emergencyContact = this.employeeForm.get('emergencyContact');
    if (emergencyContact && person) {
      emergencyContact.patchValue({
        firstName: person.firstName,
        lastName: person.lastName,
        phone: person.phone,
        email: person.email,
        address: person.address
      });
    }
  }

  private initForm(): void {
    this.employeeForm = this.fb.group({
      employeeNumber: ['', [Validators.required]],
      name: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      phone: [''],
      department: [null, [Validators.required]],
      position: ['', [Validators.required]],
      hireDate: [null, [Validators.required]],
      leaveDate: [null],
      birthday: [null],
      gender: [''],
      ethnicity: [''],
      idType: [''],
      idNumber: ['', [Validators.required]],
      contractType: [''],
      contractDuration: [null],
      contractStartDate: [null],
      remarks: [''],
      isActive: [''],
      emergencyContact: this.fb.group({
        person: [null],
        firstName: [''],
        lastName: [''],
        phone: [''],
        email: [''],
        address: ['']
      })
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
      //this.employeeForm.get('employeeNumber')?.disable();
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
}
