import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { TranslateService } from '@ngx-translate/core';
import { ContactService } from '../../services/contact.service';
import { CompanyService } from '../../services/company.service';
import { Contact } from '../../models/contact.model';
import { Company } from '../../models/company.model';
import { InputPageConfig, FormFieldConfig } from '../../components/input-page/input-page.component';

@Component({
  selector: 'app-contact-input-new',
  templateUrl: './contact-input-new.component.html',
  styleUrls: ['./contact-input-new.component.scss'],
})
export class ContactInputNewComponent implements OnInit {
  config: InputPageConfig = {
    title: 'CONTACT.TITLE',
    layout: 'auto-column',
    autoColumnThreshold: 8,
    showSaveButton: true,
    showCancelButton: true,
    showResetButton: true,
    autoSave: false,
    fields: [
      {
        key: 'firstName',
        label: 'CONTACT.FIRST_NAME',
        type: 'text',
        required: true,
        placeholder: 'CONTACT.FIRST_NAME_PLACEHOLDER',
        width: 2
      },
      {
        key: 'lastName',
        label: 'CONTACT.LAST_NAME',
        type: 'text',
        required: true,
        placeholder: 'CONTACT.LAST_NAME_PLACEHOLDER',
        width: 2
      },
      {
        key: 'gender',
        label: 'CONTACT.GENDER',
        type: 'select',
        required: false,
        options: [
          { value: 'MALE', label: 'CONTACT.GENDER.MALE' },
          { value: 'FEMALE', label: 'CONTACT.GENDER.FEMALE' },
          { value: 'OTHER', label: 'CONTACT.GENDER.OTHER' }
        ],
        width: 2
      },
      {
        key: 'mobilePhone',
        label: 'CONTACT.MOBILE_PHONE',
        type: 'text',
        required: true,
        placeholder: 'CONTACT.MOBILE_PHONE_PLACEHOLDER',
        width: 2
      },
      {
        key: 'workPhone',
        label: 'CONTACT.WORK_PHONE',
        type: 'text',
        required: false,
        placeholder: 'CONTACT.WORK_PHONE_PLACEHOLDER',
        width: 2
      },
      {
        key: 'homePhone',
        label: 'CONTACT.HOME_PHONE',
        type: 'text',
        required: false,
        placeholder: 'CONTACT.HOME_PHONE_PLACEHOLDER',
        width: 2
      },
      {
        key: 'email',
        label: 'CONTACT.EMAIL',
        type: 'email',
        required: false,
        placeholder: 'CONTACT.EMAIL_PLACEHOLDER',
        width: 2
      },
      {
        key: 'companyId',
        label: 'CONTACT.COMPANY',
        type: 'select',
        required: false,
        options: [],
        width: 2
      },
      {
        key: 'department',
        label: 'CONTACT.DEPARTMENT',
        type: 'text',
        required: false,
        placeholder: 'CONTACT.DEPARTMENT_PLACEHOLDER',
        width: 2
      },
      {
        key: 'title',
        label: 'CONTACT.TITLE',
        type: 'text',
        required: false,
        placeholder: 'CONTACT.TITLE_PLACEHOLDER',
        width: 2
      },
      {
        key: 'address',
        label: 'CONTACT.ADDRESS',
        type: 'textarea',
        required: false,
        placeholder: 'CONTACT.ADDRESS_PLACEHOLDER',
        rows: 2,
        width: 4
      },
      {
        key: 'birthDate',
        label: 'CONTACT.BIRTH_DATE',
        type: 'date',
        required: false,
        width: 2
      },
      {
        key: 'nationality',
        label: 'CONTACT.NATIONALITY',
        type: 'text',
        required: false,
        placeholder: 'CONTACT.NATIONALITY_PLACEHOLDER',
        width: 2
      },
      {
        key: 'nativePlace',
        label: 'CONTACT.NATIVE_PLACE',
        type: 'text',
        required: false,
        placeholder: 'CONTACT.NATIVE_PLACE_PLACEHOLDER',
        width: 2
      },
      {
        key: 'ethnicity',
        label: 'CONTACT.ETHNICITY',
        type: 'text',
        required: false,
        placeholder: 'CONTACT.ETHNICITY_PLACEHOLDER',
        width: 2
      },
      {
        key: 'maritalStatus',
        label: 'CONTACT.MARITAL_STATUS',
        type: 'select',
        required: false,
        options: [
          { value: 'SINGLE', label: 'CONTACT.MARITAL_STATUS.SINGLE' },
          { value: 'MARRIED', label: 'CONTACT.MARITAL_STATUS.MARRIED' },
          { value: 'DIVORCED', label: 'CONTACT.MARITAL_STATUS.DIVORCED' },
          { value: 'WIDOWED', label: 'CONTACT.MARITAL_STATUS.WIDOWED' }
        ],
        width: 2
      },
      {
        key: 'religion',
        label: 'CONTACT.RELIGION',
        type: 'text',
        required: false,
        placeholder: 'CONTACT.RELIGION_PLACEHOLDER',
        width: 2
      },
      {
        key: 'university',
        label: 'CONTACT.UNIVERSITY',
        type: 'text',
        required: false,
        placeholder: 'CONTACT.UNIVERSITY_PLACEHOLDER',
        width: 2
      },
      {
        key: 'major',
        label: 'CONTACT.MAJOR',
        type: 'text',
        required: false,
        placeholder: 'CONTACT.MAJOR_PLACEHOLDER',
        width: 2
      },
      {
        key: 'graduationYear',
        label: 'CONTACT.GRADUATION_YEAR',
        type: 'text',
        required: false,
        placeholder: 'CONTACT.GRADUATION_YEAR_PLACEHOLDER',
        width: 2
      },
      {
        key: 'degree',
        label: 'CONTACT.DEGREE',
        type: 'text',
        required: false,
        placeholder: 'CONTACT.DEGREE_PLACEHOLDER',
        width: 2
      },
      {
        key: 'hobbies',
        label: 'CONTACT.HOBBIES',
        type: 'textarea',
        required: false,
        placeholder: 'CONTACT.HOBBIES_PLACEHOLDER',
        rows: 2,
        width: 4
      },
      {
        key: 'remarks',
        label: 'CONTACT.REMARKS',
        type: 'textarea',
        required: false,
        placeholder: 'CONTACT.REMARKS_PLACEHOLDER',
        rows: 3,
        width: 4
      },
      {
        key: 'active',
        label: 'CONTACT.STATUS',
        type: 'checkbox',
        required: false,
        width: 4
      }
    ]
  };

  initialData: any = {};
  loading = false;
  companies: Company[] = [];

  constructor(
    private contactService: ContactService,
    private companyService: CompanyService,
    private dialogRef: MatDialogRef<ContactInputNewComponent>,
    private snackBar: MatSnackBar,
    private translate: TranslateService,
    @Inject(MAT_DIALOG_DATA) public data: { mode: 'create' | 'edit'; contact?: Contact }
  ) {
    if (data.mode === 'edit' && data.contact) {
      this.initialData = { ...data.contact };
    }
  }

  ngOnInit(): void {
    this.loadCompanies();
  }

  private loadCompanies(): void {
    this.companyService.getAllActive().subscribe({
      next: response => {
        if (response.code === 200) {
          this.companies = response.data;
          // 更新companyId字段的选项
          const companyField = this.config.fields.find(f => f.key === 'companyId');
          if (companyField) {
            companyField.options = this.companies.map(company => ({
              value: company.id,
              label: company.fullName
            }));
          }
        }
      },
      error: error => console.error('Error loading companies:', error)
    });
  }

  onSave(data: any): void {
    if (this.validateForm(data)) {
      this.loading = true;
      const contact = data;

      const request = this.data.mode === 'create'
        ? this.contactService.createContact(contact)
        : this.contactService.updateContact(this.data.contact!.id!, contact);

      request.subscribe({
        next: response => {
          if (response.code === 200) {
            this.snackBar.open(
              this.translate.instant(
                this.data.mode === 'create' ? 'CONTACT.CREATE_SUCCESS' : 'CONTACT.UPDATE_SUCCESS'
              ),
              this.translate.instant('ACTIONS.CLOSE'),
              { duration: 3000 }
            );
            this.dialogRef.close(response.data);
          } else {
            this.snackBar.open(
              response.message || this.translate.instant('ERROR.SAVE_CONTACT'),
              this.translate.instant('ACTIONS.CLOSE'),
              { duration: 3000 }
            );
          }
          this.loading = false;
        },
        error: error => {
          console.error('Error saving contact:', error);
          this.snackBar.open(
            this.translate.instant('ERROR.SAVE_CONTACT'),
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
    console.log('Field change:', key, value);
  }

  onValidationChange(valid: boolean, errors: any): void {
    console.log('Validation change:', valid, errors);
  }

  private validateForm(data: any): boolean {
    if (!data.firstName || data.firstName.trim() === '') {
      this.snackBar.open(
        this.translate.instant('CONTACT.FIRST_NAME_REQUIRED'),
        this.translate.instant('ACTIONS.CLOSE'),
        { duration: 3000 }
      );
      return false;
    }

    if (!data.lastName || data.lastName.trim() === '') {
      this.snackBar.open(
        this.translate.instant('CONTACT.LAST_NAME_REQUIRED'),
        this.translate.instant('ACTIONS.CLOSE'),
        { duration: 3000 }
      );
      return false;
    }

    if (!data.mobilePhone || data.mobilePhone.trim() === '') {
      this.snackBar.open(
        this.translate.instant('CONTACT.MOBILE_PHONE_REQUIRED'),
        this.translate.instant('ACTIONS.CLOSE'),
        { duration: 3000 }
      );
      return false;
    }

    if (data.email && !this.isValidEmail(data.email)) {
      this.snackBar.open(
        this.translate.instant('CONTACT.EMAIL_INVALID'),
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