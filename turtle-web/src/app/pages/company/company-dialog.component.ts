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
import { CompanyType } from '../../types/company-type.enum';

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
  companyTypes = Object.values(CompanyType).filter(value => value !== CompanyType.PRIMARY);

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
      type: ['', [Validators.required]],
      active: [true],
      remarks: [''],
      taxInfo: [null],
    });

    if (data.mode === 'edit' && data.company) {
      this.form.patchValue(data.company);
    }

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
  }

  private _filterTaxInfos(name: string): TaxInfo[] {
    const filterValue = name.toLowerCase();
    return this.taxInfos.filter(taxInfo => 
      taxInfo.fullName.toLowerCase().includes(filterValue));
  }

  displayTaxInfo(taxInfo: TaxInfo): string {
    return taxInfo?.fullName || '';
  }

  onPersonAdded(person: Person): void {
    this.persons = [...this.persons, person];
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
          if (response.code === 200) {
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
          } else {
            this.snackBar.open(
              response.message || this.translate.instant('ERROR.SAVE_COMPANY'),
              this.translate.instant('ACTIONS.CLOSE'),
              { duration: 3000 }
            );
          }
          this.loading = false;
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
