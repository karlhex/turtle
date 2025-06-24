import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Currency } from '../../models/currency.model';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-currency-dialog',
  templateUrl: './currency-dialog.component.html',
  styleUrls: ['./currency-dialog.component.scss'],
})
export class CurrencyDialogComponent implements OnInit {
  currencyForm: FormGroup;
  isEditMode: boolean;

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<CurrencyDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { currency: Currency; mode: 'create' | 'edit' },
    private snackBar: MatSnackBar,
    private translate: TranslateService
  ) {
    this.isEditMode = data.mode === 'edit';
    this.currencyForm = this.fb.group({
      code: ['', [Validators.required, Validators.maxLength(10)]],
      name: ['', [Validators.required, Validators.maxLength(100)]],
      symbol: ['', [Validators.required, Validators.maxLength(5)]],
      decimalPlaces: [2, [Validators.required, Validators.min(0), Validators.max(10)]],
      country: ['', [Validators.maxLength(100)]],
      exchangeRate: [{ value: null, disabled: this.isEditMode }, [Validators.min(0)]],
      active: [true],
      isBaseCurrency: [{ value: false, disabled: true }],
    });
  }

  ngOnInit() {
    if (this.data.currency) {
      // When editing, patch all values except exchangeRate
      const formValue = { ...this.data.currency };
      delete formValue.exchangeRate; // Exchange rate can only be set during creation
      this.currencyForm.patchValue(formValue);

      // Disable code field in edit mode
      if (this.isEditMode) {
        this.currencyForm.get('code')?.disable();
      }
    }
  }

  onSubmit() {
    if (this.currencyForm.valid) {
      const formValue = this.currencyForm.getRawValue();
      const currency: Currency = {
        ...this.data.currency,
        ...formValue,
      };

      // For new currencies, if no exchange rate is provided and it's not base currency,
      // set a default exchange rate of 1
      if (!this.isEditMode && !currency.exchangeRate && !currency.isBaseCurrency) {
        currency.exchangeRate = 1;
      }

      this.dialogRef.close(currency);
    }
  }

  onCancel() {
    this.dialogRef.close();
  }
}
