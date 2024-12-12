import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { TranslateService } from '@ngx-translate/core';
import { CompanyService } from '../../services/company.service';
import { Company } from '../../models/company.model';
import { TaxInfo } from '../../models/tax-info.model';
import { Person } from '../../models/person.model';
import { Observable } from 'rxjs';
import { map, startWith } from 'rxjs/operators';
import { TaxInfoService } from '../../services/tax-info.service';
import { PersonService } from '../../services/person.service';

@Component({
  selector: 'app-company-dialog',
  templateUrl: './company-dialog.component.html',
  styleUrls: ['./company-dialog.component.scss']
})
export class CompanyDialogComponent implements OnInit {
  form: FormGroup;
  loading = false;
  taxInfos: TaxInfo[] = [];
  persons: Person[] = [];
  filteredTaxInfos!: Observable<TaxInfo[]>;
  filteredBusinessContacts!: Observable<Person[]>;
  filteredTechnicalContacts!: Observable<Person[]>;

  constructor(
    private fb: FormBuilder,
    private companyService: CompanyService,
    private taxInfoService: TaxInfoService,
    private personService: PersonService,
    private dialogRef: MatDialogRef<CompanyDialogComponent>,
    private snackBar: MatSnackBar,
    private translate: TranslateService,
    @Inject(MAT_DIALOG_DATA) public data: { mode: 'create' | 'edit', company?: Company }
  ) {
    this.form = this.fb.group({
      fullName: ['', [Validators.required]],
      shortName: [''],
      address: ['', [Validators.required]],
      phone: ['', [Validators.required]],
      email: ['', [Validators.email]],
      website: [''],
      isPrimary: [false],
      active: [true],
      remarks: [''],
      taxInfo: [null],
      businessContact: [null],
      technicalContact: [null]
    });

    if (data.mode === 'edit' && data.company) {
      this.form.patchValue(data.company);
    }

    // Bind the display functions to this context
    this.displayPerson = this.displayPerson.bind(this);
    this.displayTaxInfo = this.displayTaxInfo.bind(this);

    this.setupAutoComplete();
  }

  ngOnInit(): void {
    this.loadTaxInfos();
    this.loadPersons();
  }

  private loadTaxInfos(): void {
    this.taxInfoService.getAllActive().subscribe(response => {
      if (response.code === 200) {
        this.taxInfos = response.data;
      }
    });
  }

  private loadPersons(): void {
    this.personService.getAll().subscribe(response => {
      if (response.code === 200) {
        this.persons = response.data;
      }
    });
  }

  private setupAutoComplete(): void {
    // TaxInfo autocomplete
    this.filteredTaxInfos = this.form.get('taxInfo')!.valueChanges.pipe(
      startWith(''),
      map(value => {
        const name = typeof value === 'string' ? value : value?.fullName;
        return name ? this._filterTaxInfos(name) : this.taxInfos.slice();
      })
    );

    // Business contact autocomplete
    this.filteredBusinessContacts = this.form.get('businessContact')!.valueChanges.pipe(
      startWith(''),
      map(value => {
        const name = typeof value === 'string' ? value : this._getPersonFullName(value);
        return name ? this._filterPersons(name) : this.persons.slice();
      })
    );

    // Technical contact autocomplete
    this.filteredTechnicalContacts = this.form.get('technicalContact')!.valueChanges.pipe(
      startWith(''),
      map(value => {
        const name = typeof value === 'string' ? value : this._getPersonFullName(value);
        return name ? this._filterPersons(name) : this.persons.slice();
      })
    );
  }

  private _filterTaxInfos(name: string): TaxInfo[] {
    const filterValue = name.toLowerCase();
    return this.taxInfos.filter(taxInfo => 
      taxInfo.fullName.toLowerCase().includes(filterValue));
  }

  private _filterPersons(name: string): Person[] {
    const filterValue = name.toLowerCase();
    return this.persons.filter(person => 
      this._getPersonFullName(person).toLowerCase().includes(filterValue));
  }

  private _getPersonFullName(person: Person): string {
    return person ? `${person.firstName} ${person.lastName}` : '';
  }

  displayTaxInfo(taxInfo: TaxInfo): string {
    return taxInfo?.fullName || '';
  }

  displayPerson(person: Person | string | null): string {
    if (!person) return '';
    if (typeof person === 'string') return person;
    return this._getPersonFullName(person);
  }

  onSubmit(): void {
    if (this.form.valid) {
      this.loading = true;
      const company = this.form.value;

      const request = this.data.mode === 'create'
        ? this.companyService.createCompany(company)
        : this.companyService.updateCompany(this.data.company!.id!, company);

      request.subscribe({
        next: (response) => {
          this.snackBar.open(
            this.translate.instant(
              this.data.mode === 'create'
                ? 'COMPANY.CREATE_SUCCESS'
                : 'COMPANY.UPDATE_SUCCESS'
            ),
            this.translate.instant('ACTIONS.CLOSE'),
            { duration: 3000 }
          );
          this.dialogRef.close(response.data);
        },
        error: (error) => {
          console.error('Error saving company:', error);
          this.snackBar.open(
            this.translate.instant('ERROR.SAVE_COMPANY'),
            this.translate.instant('ACTIONS.CLOSE'),
            { duration: 3000 }
          );
          this.loading = false;
        }
      });
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}
