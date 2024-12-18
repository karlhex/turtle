import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Person } from '../../models/person.model';

@Component({
  selector: 'app-person-dialog',
  templateUrl: './person-dialog.component.html',
  styleUrls: ['./person-dialog.component.scss']
})
export class PersonDialogComponent {
  form: FormGroup;

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<PersonDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { person: Person | null }
  ) {
    this.form = this.fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      mobilePhone: [''],
      workPhone: [''],
      homePhone: [''],
      companyName: [''],
      department: [''],
      position: [''],
      email: ['', [Validators.email]],
      address: ['']
    });

    if (data.person) {
      this.form.patchValue({
        firstName: data.person.firstName,
        lastName: data.person.lastName,
        address: data.person.address || '',
        email: data.person.email || '',
        mobilePhone: data.person.mobilePhone || '',
        workPhone: data.person.workPhone || '',
        homePhone: data.person.homePhone || '',
        companyName: data.person.companyName || '',
        department: data.person.department || '',
        position: data.person.position || ''
      });
    }
  }

  onSubmit(): void {
    if (this.form.valid) {
      const formValue = this.form.value;
      const person: Person = {
        ...this.data.person,
        firstName: formValue.firstName,
        lastName: formValue.lastName,
        address: formValue.address || undefined,
        email: formValue.email || undefined,
        mobilePhone: formValue.mobilePhone || undefined,
        workPhone: formValue.workPhone || undefined,
        homePhone: formValue.homePhone || undefined,
        companyName: formValue.companyName || undefined,
        department: formValue.department || undefined,
        position: formValue.position || undefined
      };
      this.dialogRef.close(person);
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}
