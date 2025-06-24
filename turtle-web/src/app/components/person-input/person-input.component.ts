import { Component, forwardRef, Input, Output, EventEmitter } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { Person } from '../../models/person.model';
import { PersonDialogComponent } from './person-dialog.component';
import { Observable, Subject } from 'rxjs';
import { map, startWith } from 'rxjs/operators';

@Component({
  selector: 'app-person-input',
  templateUrl: './person-input.component.html',
  styleUrls: ['./person-input.component.scss'],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => PersonInputComponent),
      multi: true,
    },
  ],
})
export class PersonInputComponent implements ControlValueAccessor {
  @Input() placeholder = '';
  @Input() required = false;
  @Input() set value(val: Person | null) {
    if (val) {
      this._value = val;
      this.filterValue = this.getPersonDisplayName(val);
      this.onChange(val);
    } else {
      this._value = null;
      this.filterValue = '';
      this.onChange(null);
    }
  }
  get value(): Person | null {
    return this._value;
  }
  @Input() set persons(value: Person[]) {
    this._persons = value;
    this.filteredPersons = this.filterSubject.pipe(
      startWith(''),
      map(filterValue => this.filterPersons(filterValue))
    );
  }
  get persons(): Person[] {
    return this._persons;
  }

  @Output() personAdded = new EventEmitter<Person>();

  private _persons: Person[] = [];
  private _value: Person | null = null;
  disabled = false;
  touched = false;
  filterValue = '';
  filterSubject = new Subject<string>();
  filteredPersons: Observable<Person[]>;

  constructor(private dialog: MatDialog) {
    this.filteredPersons = this.filterSubject.pipe(
      startWith(''),
      map(filterValue => this.filterPersons(filterValue))
    );
  }

  writeValue(value: Person | null): void {
    if (value) {
      this._value = value;
      this.filterValue = this.getPersonDisplayName(value);
    } else {
      this._value = null;
      this.filterValue = '';
    }
  }

  registerOnChange(fn: (value: Person | null) => void): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: () => void): void {
    this.onTouched = fn;
  }

  setDisabledState(isDisabled: boolean): void {
    this.disabled = isDisabled;
  }

  onChange = (value: Person | null) => {};
  onTouched = () => {};

  onFilterChange(event: any): void {
    this.filterValue = event.target.value;
    this.filterSubject.next(this.filterValue);
  }

  selectPerson(person: Person): void {
    this.value = person;
    this.onChange(person);
    this.markAsTouched();
  }

  openPersonDialog(): void {
    const dialogRef = this.dialog.open(PersonDialogComponent, {
      data: { person: this.value },
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.value = result;
        this.onChange(result);
        this.markAsTouched();
        if (!this._persons.find(p => p === result)) {
          this._persons = [...this._persons, result];
          this.personAdded.emit(result);
        }
      }
    });
  }

  markAsTouched() {
    if (!this.touched) {
      this.onTouched();
      this.touched = true;
    }
  }

  private filterPersons(filterValue: string): Person[] {
    if (!filterValue) {
      return [];
    }
    const filter = filterValue.toLowerCase();
    return this._persons.filter(
      person =>
        person.firstName.toLowerCase().includes(filter) ||
        person.lastName.toLowerCase().includes(filter) ||
        `${person.firstName} ${person.lastName}`.toLowerCase().includes(filter) ||
        (person.mobilePhone && person.mobilePhone.toLowerCase().includes(filter)) ||
        (person.workPhone && person.workPhone.toLowerCase().includes(filter)) ||
        (person.homePhone && person.homePhone.toLowerCase().includes(filter)) ||
        (person.companyName && person.companyName.toLowerCase().includes(filter)) ||
        (person.department && person.department.toLowerCase().includes(filter)) ||
        (person.position && person.position.toLowerCase().includes(filter))
    );
  }

  getPersonDisplayName(person: Person): string {
    return `${person.firstName} ${person.lastName}`;
  }
}
