import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { TranslateService } from '@ngx-translate/core';
import { BankAccountService } from '../../services/bank-account.service';
import { CurrencyService } from '../../services/currency.service';
import { CompanyService } from '../../services/company.service';
import { BankAccount } from '../../models/bank-account.model';
import { Currency } from '../../models/currency.model';
import { Company } from '../../models/company.model';
import { BankAccountType } from '../../types/bank-account-type.enum';
import { InputPageConfig } from '../../components/input-page/input-page.component';

@Component({
  selector: 'app-bank-account-input-new',
  templateUrl: './bank-account-input-new.component.html',
  styleUrls: ['./bank-account-input-new.component.scss'],
})
export class BankAccountInputNewComponent implements OnInit {
  config: InputPageConfig = {
    title: 'BANK_ACCOUNT.TITLE',
    layout: 'auto-column',
    autoColumnThreshold: 6,
    showSaveButton: true,
    showCancelButton: true,
    showResetButton: true,
    autoSave: false,
    fields: [
      {
        key: 'name',
        label: 'BANK_ACCOUNT.NAME',
        type: 'text',
        required: true,
        placeholder: 'BANK_ACCOUNT.NAME_PLACEHOLDER',
        width: 2
      },
      {
        key: 'accountNo',
        label: 'BANK_ACCOUNT.ACCOUNT_NO',
        type: 'text',
        required: true,
        placeholder: 'BANK_ACCOUNT.ACCOUNT_NO_PLACEHOLDER',
        width: 2
      },
      {
        key: 'bankName',
        label: 'BANK_ACCOUNT.BANK_NAME',
        type: 'text',
        required: true,
        placeholder: 'BANK_ACCOUNT.BANK_NAME_PLACEHOLDER',
        width: 2
      },
      {
        key: 'bankBranch',
        label: 'BANK_ACCOUNT.BANK_BRANCH',
        type: 'text',
        required: false,
        placeholder: 'BANK_ACCOUNT.BANK_BRANCH_PLACEHOLDER',
        width: 2
      },
      {
        key: 'type',
        label: 'BANK_ACCOUNT.TYPE',
        type: 'select',
        required: true,
        options: [
          { value: BankAccountType.COMPANY_BASIC, label: 'BANK_ACCOUNT.TYPE.COMPANY_BASIC' },
          { value: BankAccountType.COMPANY_GENERAL, label: 'BANK_ACCOUNT.TYPE.COMPANY_GENERAL' },
          { value: BankAccountType.COMPANY_OTHER, label: 'BANK_ACCOUNT.TYPE.COMPANY_OTHER' },
          { value: BankAccountType.PERSONAL, label: 'BANK_ACCOUNT.TYPE.PERSONAL' }
        ],
        width: 2
      },
      {
        key: 'balance',
        label: 'BANK_ACCOUNT.BALANCE',
        type: 'number',
        required: true,
        placeholder: 'BANK_ACCOUNT.BALANCE_PLACEHOLDER',
        width: 2
      },
      {
        key: 'currencyId',
        label: 'BANK_ACCOUNT.CURRENCY',
        type: 'select',
        required: true,
        options: [],
        width: 2
      },
      {
        key: 'companyId',
        label: 'BANK_ACCOUNT.COMPANY',
        type: 'select',
        required: true,
        options: [],
        width: 2
      },
      {
        key: 'swiftCode',
        label: 'BANK_ACCOUNT.SWIFT_CODE',
        type: 'text',
        required: false,
        placeholder: 'BANK_ACCOUNT.SWIFT_CODE_PLACEHOLDER',
        width: 2
      },
      {
        key: 'contactPerson',
        label: 'BANK_ACCOUNT.CONTACT_PERSON',
        type: 'text',
        required: false,
        placeholder: 'BANK_ACCOUNT.CONTACT_PERSON_PLACEHOLDER',
        width: 2
      },
      {
        key: 'contactPhone',
        label: 'BANK_ACCOUNT.CONTACT_PHONE',
        type: 'text',
        required: false,
        placeholder: 'BANK_ACCOUNT.CONTACT_PHONE_PLACEHOLDER',
        width: 2
      },
      {
        key: 'description',
        label: 'BANK_ACCOUNT.DESCRIPTION',
        type: 'textarea',
        required: false,
        placeholder: 'BANK_ACCOUNT.DESCRIPTION_PLACEHOLDER',
        rows: 3,
        width: 4
      },
      {
        key: 'active',
        label: 'BANK_ACCOUNT.STATUS',
        type: 'checkbox',
        required: false,
        width: 4
      }
    ]
  };

  initialData: any = {};
  loading = false;
  currencies: Currency[] = [];
  companies: Company[] = [];

  constructor(
    private bankAccountService: BankAccountService,
    private currencyService: CurrencyService,
    private companyService: CompanyService,
    private dialogRef: MatDialogRef<BankAccountInputNewComponent>,
    private snackBar: MatSnackBar,
    private translate: TranslateService,
    @Inject(MAT_DIALOG_DATA) public data: { mode: 'create' | 'edit'; bankAccount?: BankAccount }
  ) {
    if (data.mode === 'edit' && data.bankAccount) {
      this.initialData = { 
        ...data.bankAccount,
        currencyId: data.bankAccount.currency?.id,
        companyId: data.bankAccount.companyId
      };
    }
  }

  ngOnInit(): void {
    this.loadCurrencies();
    this.loadCompanies();
  }

  private loadCurrencies(): void {
    this.currencyService.getCurrencies({ page: 0, size: 1000 }).subscribe({
      next: response => {
        if (response.code === 200) {
          this.currencies = response.data.content;
          const currencyField = this.config.fields.find(f => f.key === 'currencyId');
          if (currencyField) {
            currencyField.options = this.currencies.map(currency => ({
              value: currency.id,
              label: `${currency.code} - ${currency.name}`
            }));
          }
        }
      },
      error: error => console.error('Error loading currencies:', error)
    });
  }

  private loadCompanies(): void {
    this.companyService.getAllActive().subscribe({
      next: response => {
        if (response.code === 200) {
          this.companies = response.data;
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
      const bankAccount = {
        ...data,
        currency: this.currencies.find(c => c.id === data.currencyId)
      };

      const request = this.data.mode === 'create'
        ? this.bankAccountService.createBankAccount(bankAccount)
        : this.bankAccountService.updateBankAccount(this.data.bankAccount!.id!, bankAccount);

      request.subscribe({
        next: response => {
          if (response.code === 200) {
            this.snackBar.open(
              this.translate.instant(
                this.data.mode === 'create' ? 'BANK_ACCOUNT.CREATE_SUCCESS' : 'BANK_ACCOUNT.UPDATE_SUCCESS'
              ),
              this.translate.instant('ACTIONS.CLOSE'),
              { duration: 3000 }
            );
            this.dialogRef.close(response.data);
          } else {
            this.snackBar.open(
              response.message || this.translate.instant('ERROR.SAVE_BANK_ACCOUNT'),
              this.translate.instant('ACTIONS.CLOSE'),
              { duration: 3000 }
            );
          }
          this.loading = false;
        },
        error: error => {
          console.error('Error saving bank account:', error);
          this.snackBar.open(
            this.translate.instant('ERROR.SAVE_BANK_ACCOUNT'),
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
    if (!data.name || data.name.trim() === '') {
      this.snackBar.open(
        this.translate.instant('BANK_ACCOUNT.NAME_REQUIRED'),
        this.translate.instant('ACTIONS.CLOSE'),
        { duration: 3000 }
      );
      return false;
    }

    if (!data.accountNo || data.accountNo.trim() === '') {
      this.snackBar.open(
        this.translate.instant('BANK_ACCOUNT.ACCOUNT_NO_REQUIRED'),
        this.translate.instant('ACTIONS.CLOSE'),
        { duration: 3000 }
      );
      return false;
    }

    if (!data.bankName || data.bankName.trim() === '') {
      this.snackBar.open(
        this.translate.instant('BANK_ACCOUNT.BANK_NAME_REQUIRED'),
        this.translate.instant('ACTIONS.CLOSE'),
        { duration: 3000 }
      );
      return false;
    }

    if (!data.type) {
      this.snackBar.open(
        this.translate.instant('BANK_ACCOUNT.TYPE_REQUIRED'),
        this.translate.instant('ACTIONS.CLOSE'),
        { duration: 3000 }
      );
      return false;
    }

    if (data.balance === null || data.balance === undefined) {
      this.snackBar.open(
        this.translate.instant('BANK_ACCOUNT.BALANCE_REQUIRED'),
        this.translate.instant('ACTIONS.CLOSE'),
        { duration: 3000 }
      );
      return false;
    }

    if (!data.currencyId) {
      this.snackBar.open(
        this.translate.instant('BANK_ACCOUNT.CURRENCY_REQUIRED'),
        this.translate.instant('ACTIONS.CLOSE'),
        { duration: 3000 }
      );
      return false;
    }

    if (!data.companyId) {
      this.snackBar.open(
        this.translate.instant('BANK_ACCOUNT.COMPANY_REQUIRED'),
        this.translate.instant('ACTIONS.CLOSE'),
        { duration: 3000 }
      );
      return false;
    }

    return true;
  }
} 