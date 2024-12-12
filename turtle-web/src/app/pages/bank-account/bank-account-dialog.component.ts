import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { TranslateService } from '@ngx-translate/core';
import { BankAccountService } from '../../services/bank-account.service';
import { BankAccount } from '../../models/bank-account.model';
import { BankAccountType } from '../../types/bank-account-type.enum';
import { Currency } from '../../models/currency.model';
import { CurrencyService } from '../../services/currency.service';
import { CompanyService } from '../../services/company.service';
import { Company } from '../../models/company.model';
import { Observable } from 'rxjs';
import { map, startWith } from 'rxjs/operators';

@Component({
  selector: 'app-bank-account-dialog',
  templateUrl: './bank-account-dialog.component.html',
  styleUrls: ['./bank-account-dialog.component.scss']
})
export class BankAccountDialogComponent implements OnInit {
  form: FormGroup;
  loading = false;
  currencies: Currency[] = [];
  companies: Company[] = [];
  filteredCompanies: Observable<Company[]>;
  accountTypes = Object.values(BankAccountType);

  constructor(
    private fb: FormBuilder,
    private bankAccountService: BankAccountService,
    private currencyService: CurrencyService,
    private companyService: CompanyService,
    private dialogRef: MatDialogRef<BankAccountDialogComponent>,
    private snackBar: MatSnackBar,
    private translate: TranslateService,
    @Inject(MAT_DIALOG_DATA) public data: { mode: 'create' | 'edit', bankAccount?: BankAccount }
  ) {
    this.form = this.fb.group({
      name: ['', [Validators.required]],
      accountNo: ['', [Validators.required]],
      bankName: ['', [Validators.required]],
      bankBranch: ['', [Validators.required]],
      balance: [0, [Validators.required, Validators.min(0)]],
      currency: [null, [Validators.required]],
      company: [null],
      companyId: [null],
      type: [null, [Validators.required]],
      description: [''],
      active: [true],
      swiftCode: [''],
      contactPerson: [''],
      contactPhone: ['']
    });

    if (data.mode === 'edit' && data.bankAccount) {
      // Find the company by ID and set it in the form
      if (data.bankAccount.companyId) {
        this.companyService.getCompany(data.bankAccount.companyId).subscribe({
          next: (response) => {
            if (response.code === 200 && response.data) {
              this.form.patchValue({
                ...data.bankAccount,
                company: response.data
              });
            }
          }
        });
      } else {
        this.form.patchValue(data.bankAccount);
      }
    }

    // Initialize company filtering
    this.filteredCompanies = this.form.get('company')!.valueChanges.pipe(
      startWith(''),
      map(value => {
        const name = typeof value === 'string' ? value : value?.fullName;
        return name ? this._filterCompanies(name) : this.companies.slice();
      })
    );
  }

  ngOnInit(): void {
    this.loadCurrencies();
    this.loadCompanies();
  }

  private loadCurrencies(): void {
    this.currencyService.getCurrencies({ page: 0, size: 100, active: true }).subscribe(response => {
        if (response.code === 200) {
        this.currencies = response.data.content;
      }
    });
  }

  private loadCompanies(): void {
    this.companyService.getCompanies({ page: 0, size: 100, active: true }).subscribe(response => {
        if (response.code === 200) {
        this.companies = response.data.content;
      }
    });
  }

  private _filterCompanies(value: string): Company[] {
    const filterValue = value.toLowerCase();
    return this.companies.filter(company => 
      company.fullName.toLowerCase().includes(filterValue) ||
      (company.shortName && company.shortName.toLowerCase().includes(filterValue))
    );
  }

  displayCompany(company: Company): string {
    return company ? company.fullName : '';
  }

  onSubmit(): void {
    if (this.form.valid) {
      this.loading = true;
      const bankAccount = this.form.value;

      bankAccount.companyId = bankAccount.company?.id;
      delete bankAccount.company;

      const request = this.data.mode === 'create'
        ? this.bankAccountService.createBankAccount(bankAccount)
        : this.bankAccountService.updateBankAccount(this.data.bankAccount!.id!, bankAccount);

      request.subscribe({
        next: response => {
          console.log("Saving bank account:", response);
          if (response.code === 200) {
            this.snackBar.open(
              this.translate.instant(this.data.mode === 'create' ? 'common.message.create_success' : 'common.message.update_success'),
              this.translate.instant('common.action.close'),
              { duration: 3000 }
            );
            this.dialogRef.close(true);
          } else {
            this.snackBar.open(
              response.message || this.translate.instant('common.message.operation_failed'),
              this.translate.instant('common.action.close'),
              { duration: 3000 }
            );
          }
          this.loading = false;
        },
        error: () => {
          this.snackBar.open(
            this.translate.instant('common.message.operation_failed'),
            this.translate.instant('common.action.close'),
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

  compareCurrency(c1: Currency, c2: Currency): boolean {
    return c1?.id === c2?.id;
  }

  compareCompany(c1: Company, c2: Company): boolean {
    return c1?.id === c2?.id;
  }
}
