import { Component, Inject, OnInit } from '@angular/core';
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
import { InputPageConfig, FormFieldConfig } from '../../components/input-page/input-page.component';

@Component({
  selector: 'app-company-input-new',
  templateUrl: './company-input-new.component.html',
  styleUrls: ['./company-input-new.component.scss'],
})
export class CompanyInputNewComponent implements OnInit {
  config: InputPageConfig = {
    title: 'COMPANY.TITLE',
    layout: 'auto-column',
    autoColumnThreshold: 6,
    showSaveButton: true,
    showCancelButton: true,
    showResetButton: true,
    autoSave: false,
    fields: [
      {
        key: 'fullName',
        label: 'COMPANY.FULL_NAME',
        type: 'text',
        required: true,
        placeholder: 'COMPANY.FULL_NAME_PLACEHOLDER',
        width: 2
      },
      {
        key: 'shortName',
        label: 'COMPANY.SHORT_NAME',
        type: 'text',
        required: false,
        placeholder: 'COMPANY.SHORT_NAME_PLACEHOLDER',
        width: 2
      },
      {
        key: 'website',
        label: 'COMPANY.WEBSITE',
        type: 'text',
        required: false,
        placeholder: 'COMPANY.WEBSITE_PLACEHOLDER',
        width: 2
      },
      {
        key: 'phone',
        label: 'COMPANY.PHONE',
        type: 'text',
        required: true,
        placeholder: 'COMPANY.PHONE_PLACEHOLDER',
        width: 2
      },
      {
        key: 'email',
        label: 'COMPANY.EMAIL',
        type: 'email',
        required: false,
        placeholder: 'COMPANY.EMAIL_PLACEHOLDER',
        width: 2
      },
      {
        key: 'type',
        label: 'COMPANY.TYPE',
        type: 'select',
        required: true,
        options: [
          { value: CompanyType.CUSTOMER, label: 'COMPANY.TYPE.CUSTOMER' },
          { value: CompanyType.SUPPLIER, label: 'COMPANY.TYPE.SUPPLIER' },
          { value: CompanyType.PROSPECT, label: 'COMPANY.TYPE.PROSPECT' }
        ],
        width: 2
      },
      {
        key: 'address',
        label: 'COMPANY.ADDRESS',
        type: 'textarea',
        required: true,
        placeholder: 'COMPANY.ADDRESS_PLACEHOLDER',
        rows: 3,
        width: 4
      },
      {
        key: 'remarks',
        label: 'COMPANY.REMARKS',
        type: 'textarea',
        required: false,
        placeholder: 'COMPANY.REMARKS_PLACEHOLDER',
        rows: 3,
        width: 4
      },
      {
        key: 'active',
        label: 'COMPANY.STATUS',
        type: 'checkbox',
        required: false,
        width: 4
      }

    ]
  };

  initialData: any = {};
  loading = false;
  taxInfos: TaxInfo[] = [];
  persons: Person[] = [];
  filteredTaxInfos!: Observable<TaxInfo[]>;

  constructor(
    private companyService: CompanyService,
    private taxInfoService: TaxInfoService,
    private personService: PersonService,
    private dialogRef: MatDialogRef<CompanyInputNewComponent>,
    private snackBar: MatSnackBar,
    private translate: TranslateService,
    @Inject(MAT_DIALOG_DATA) public data: { mode: 'create' | 'edit'; company?: Company }
  ) {
    if (data.mode === 'edit' && data.company) {
      this.initialData = { ...data.company };
    }
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

  onSave(data: any): void {
    if (this.validateForm(data)) {
      this.loading = true;
      const company = data;

      const request = this.data.mode === 'create'
        ? this.companyService.createCompany(company)
        : this.companyService.updateCompany(this.data.company!.id!, company);

      request.subscribe({
        next: response => {
          if (response.code === 200) {
            this.snackBar.open(
              this.translate.instant(
                this.data.mode === 'create' ? 'COMPANY.CREATE_SUCCESS' : 'COMPANY.UPDATE_SUCCESS'
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
        error: error => {
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

  onReset(): void {
    console.log('Form reset');
  }

  onFieldChange(key: string, value: any): void {
    console.log('Field changed:', key, value);
  }

  onValidationChange(valid: boolean, errors: any): void {
    console.log('Validation changed:', valid, errors);
  }

  private validateForm(data: any): boolean {
    if (!data.fullName || !data.address || !data.phone) {
      this.snackBar.open(
        this.translate.instant('ERROR.REQUIRED_FIELDS'),
        this.translate.instant('ACTIONS.CLOSE'),
        { duration: 3000 }
      );
      return false;
    }

    if (data.email && !this.isValidEmail(data.email)) {
      this.snackBar.open(
        this.translate.instant('ERROR.INVALID_EMAIL'),
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