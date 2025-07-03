import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { TranslateService } from '@ngx-translate/core';
import { CurrencyService } from '../../services/currency.service';
import { Currency } from '../../models/currency.model';
import { InputPageConfig, FormFieldConfig } from '../../components/input-page/input-page.component';

@Component({
  selector: 'app-currency-input-new',
  templateUrl: './currency-input-new.component.html',
  styleUrls: ['./currency-input-new.component.scss'],
})
export class CurrencyInputNewComponent implements OnInit {
  config: InputPageConfig = {
    title: 'CURRENCY.TITLE',
    layout: 'auto-column',
    autoColumnThreshold: 6,
    showSaveButton: true,
    showCancelButton: true,
    showResetButton: true,
    autoSave: false,
    fields: [
      {
        key: 'code',
        label: 'CURRENCY.CODE',
        type: 'text',
        required: true,
        placeholder: 'CURRENCY.CODE_PLACEHOLDER',
        width: 2
      },
      {
        key: 'name',
        label: 'CURRENCY.NAME',
        type: 'text',
        required: true,
        placeholder: 'CURRENCY.NAME_PLACEHOLDER',
        width: 2
      },
      {
        key: 'symbol',
        label: 'CURRENCY.SYMBOL',
        type: 'text',
        required: true,
        placeholder: 'CURRENCY.SYMBOL_PLACEHOLDER',
        width: 2
      },
      {
        key: 'decimalPlaces',
        label: 'CURRENCY.DECIMAL_PLACES',
        type: 'number',
        required: true,
        placeholder: 'CURRENCY.DECIMAL_PLACES_PLACEHOLDER',
        width: 2
      },
      {
        key: 'country',
        label: 'CURRENCY.COUNTRY',
        type: 'text',
        required: false,
        placeholder: 'CURRENCY.COUNTRY_PLACEHOLDER',
        width: 2
      },
      {
        key: 'exchangeRate',
        label: 'CURRENCY.EXCHANGE_RATE',
        type: 'number',
        required: false,
        placeholder: 'CURRENCY.EXCHANGE_RATE_PLACEHOLDER',
        width: 2
      },
      {
        key: 'isBaseCurrency',
        label: 'CURRENCY.BASE_CURRENCY',
        type: 'checkbox',
        required: false,
        width: 2
      },
      {
        key: 'active',
        label: 'CURRENCY.STATUS',
        type: 'checkbox',
        required: false,
        width: 2
      }
    ]
  };

  initialData: any = {};
  loading = false;

  constructor(
    private currencyService: CurrencyService,
    private dialogRef: MatDialogRef<CurrencyInputNewComponent>,
    private snackBar: MatSnackBar,
    private translate: TranslateService,
    @Inject(MAT_DIALOG_DATA) public data: { mode: 'create' | 'edit'; currency?: Currency }
  ) {
    if (data.mode === 'edit' && data.currency) {
      this.initialData = { ...data.currency };
    }
  }

  ngOnInit(): void {
    // 初始化逻辑
  }

  onSave(data: any): void {
    if (this.validateForm(data)) {
      this.loading = true;
      const currency = data;

      const request = this.data.mode === 'create'
        ? this.currencyService.createCurrency(currency)
        : this.currencyService.updateCurrency(this.data.currency!.id!, currency);

      request.subscribe({
        next: response => {
          if (response.code === 200) {
            this.snackBar.open(
              this.translate.instant(
                this.data.mode === 'create' ? 'CURRENCY.CREATE_SUCCESS' : 'CURRENCY.UPDATE_SUCCESS'
              ),
              this.translate.instant('ACTIONS.CLOSE'),
              { duration: 3000 }
            );
            this.dialogRef.close(response.data);
          } else {
            this.snackBar.open(
              response.message || this.translate.instant('ERROR.SAVE_CURRENCY'),
              this.translate.instant('ACTIONS.CLOSE'),
              { duration: 3000 }
            );
          }
          this.loading = false;
        },
        error: error => {
          console.error('Error saving currency:', error);
          this.snackBar.open(
            this.translate.instant('ERROR.SAVE_CURRENCY'),
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
    if (!data.code || data.code.trim() === '') {
      this.snackBar.open(
        this.translate.instant('CURRENCY.CODE_REQUIRED'),
        this.translate.instant('ACTIONS.CLOSE'),
        { duration: 3000 }
      );
      return false;
    }

    if (!data.name || data.name.trim() === '') {
      this.snackBar.open(
        this.translate.instant('CURRENCY.NAME_REQUIRED'),
        this.translate.instant('ACTIONS.CLOSE'),
        { duration: 3000 }
      );
      return false;
    }

    if (!data.symbol || data.symbol.trim() === '') {
      this.snackBar.open(
        this.translate.instant('CURRENCY.SYMBOL_REQUIRED'),
        this.translate.instant('ACTIONS.CLOSE'),
        { duration: 3000 }
      );
      return false;
    }

    if (data.decimalPlaces === null || data.decimalPlaces === undefined) {
      this.snackBar.open(
        this.translate.instant('CURRENCY.DECIMAL_PLACES_REQUIRED'),
        this.translate.instant('ACTIONS.CLOSE'),
        { duration: 3000 }
      );
      return false;
    }

    return true;
  }
} 