import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { TranslateService } from '@ngx-translate/core';
import { InputPageConfig } from '../../components/input-page/input-page.component';
import { TaxInfo } from '../../models/tax-info.model';
import { TaxInfoService } from '../../services/tax-info.service';

@Component({
  selector: 'app-tax-info-input-new',
  templateUrl: './tax-info-input-new.component.html',
  styleUrls: ['./tax-info-input-new.component.scss']
})
export class TaxInfoInputNewComponent implements OnInit {
  config: InputPageConfig = {
    title: 'TAX_INFO.TITLE',
    fields: [
      {
        key: 'fullName',
        label: 'TAX_INFO.FULL_NAME',
        type: 'text',
        required: true,
        placeholder: 'TAX_INFO.FULL_NAME_PLACEHOLDER',
        width: 2
      },
      {
        key: 'taxNo',
        label: 'TAX_INFO.TAX_NO',
        type: 'text',
        required: true,
        placeholder: 'TAX_INFO.TAX_NO_PLACEHOLDER',
        width: 2
      },
      {
        key: 'address',
        label: 'TAX_INFO.ADDRESS',
        type: 'textarea',
        required: true,
        placeholder: 'TAX_INFO.ADDRESS_PLACEHOLDER',
        rows: 3,
        width: 4
      },
      {
        key: 'phone',
        label: 'TAX_INFO.PHONE',
        type: 'text',
        required: true,
        placeholder: 'TAX_INFO.PHONE_PLACEHOLDER',
        width: 2
      },
      {
        key: 'bankName',
        label: 'TAX_INFO.BANK_NAME',
        type: 'text',
        required: true,
        placeholder: 'TAX_INFO.BANK_NAME_PLACEHOLDER',
        width: 2
      },
      {
        key: 'bankCode',
        label: 'TAX_INFO.BANK_CODE',
        type: 'text',
        required: true,
        placeholder: 'TAX_INFO.BANK_CODE_PLACEHOLDER',
        width: 2
      },
      {
        key: 'bankAccount',
        label: 'TAX_INFO.BANK_ACCOUNT',
        type: 'text',
        required: true,
        placeholder: 'TAX_INFO.BANK_ACCOUNT_PLACEHOLDER',
        width: 2
      },
      {
        key: 'active',
        label: 'TAX_INFO.STATUS',
        type: 'checkbox',
        required: false,
        width: 2
      },
      {
        key: 'remarks',
        label: 'TAX_INFO.REMARKS',
        type: 'textarea',
        required: false,
        placeholder: 'TAX_INFO.REMARKS_PLACEHOLDER',
        rows: 3,
        width: 4
      }
    ],
    layout: 'two-column',
    showSaveButton: true,
    showCancelButton: true,
    showResetButton: true,
    saveButtonText: 'COMMON.SAVE',
    cancelButtonText: 'COMMON.CANCEL',
    resetButtonText: 'COMMON.RESET'
  };

  initialData: any = {};
  loading = false;

  constructor(
    private taxInfoService: TaxInfoService,
    private dialogRef: MatDialogRef<TaxInfoInputNewComponent>,
    private snackBar: MatSnackBar,
    private translate: TranslateService,
    @Inject(MAT_DIALOG_DATA) public data: { mode: 'create' | 'edit'; taxInfo?: TaxInfo }
  ) {
    if (data.mode === 'edit' && data.taxInfo) {
      this.initialData = { ...data.taxInfo };
    }
  }

  ngOnInit(): void {
    // No additional initialization needed
  }

  onSave(data: any): void {
    if (this.validateForm(data)) {
      this.loading = true;
      const taxInfo = data;

      const request = this.data.mode === 'create'
        ? this.taxInfoService.createTaxInfo(taxInfo)
        : this.taxInfoService.updateTaxInfo(this.data.taxInfo!.id!, taxInfo);

      request.subscribe({
        next: (response: any) => {
          if (response.code === 200) {
            this.snackBar.open(
              this.translate.instant(
                this.data.mode === 'create' ? 'TAX_INFO.CREATE_SUCCESS' : 'TAX_INFO.UPDATE_SUCCESS'
              ),
              this.translate.instant('ACTIONS.CLOSE'),
              { duration: 3000 }
            );
            this.dialogRef.close(response.data);
          } else {
            this.snackBar.open(
              response.message || this.translate.instant('ERROR.SAVE_TAX_INFO'),
              this.translate.instant('ACTIONS.CLOSE'),
              { duration: 3000 }
            );
          }
          this.loading = false;
        },
        error: (error: any) => {
          console.error('Error saving tax info:', error);
          this.snackBar.open(
            this.translate.instant('ERROR.SAVE_TAX_INFO'),
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
    if (!data.fullName || data.fullName.trim() === '') {
      this.snackBar.open(
        this.translate.instant('TAX_INFO.FULL_NAME_REQUIRED'),
        this.translate.instant('ACTIONS.CLOSE'),
        { duration: 3000 }
      );
      return false;
    }

    if (!data.taxNo || data.taxNo.trim() === '') {
      this.snackBar.open(
        this.translate.instant('TAX_INFO.TAX_NO_REQUIRED'),
        this.translate.instant('ACTIONS.CLOSE'),
        { duration: 3000 }
      );
      return false;
    }

    if (!data.address || data.address.trim() === '') {
      this.snackBar.open(
        this.translate.instant('TAX_INFO.ADDRESS_REQUIRED'),
        this.translate.instant('ACTIONS.CLOSE'),
        { duration: 3000 }
      );
      return false;
    }

    if (!data.phone || data.phone.trim() === '') {
      this.snackBar.open(
        this.translate.instant('TAX_INFO.PHONE_REQUIRED'),
        this.translate.instant('ACTIONS.CLOSE'),
        { duration: 3000 }
      );
      return false;
    }

    if (!data.bankName || data.bankName.trim() === '') {
      this.snackBar.open(
        this.translate.instant('TAX_INFO.BANK_NAME_REQUIRED'),
        this.translate.instant('ACTIONS.CLOSE'),
        { duration: 3000 }
      );
      return false;
    }

    if (!data.bankCode || data.bankCode.trim() === '') {
      this.snackBar.open(
        this.translate.instant('TAX_INFO.BANK_CODE_REQUIRED'),
        this.translate.instant('ACTIONS.CLOSE'),
        { duration: 3000 }
      );
      return false;
    }

    if (!data.bankAccount || data.bankAccount.trim() === '') {
      this.snackBar.open(
        this.translate.instant('TAX_INFO.BANK_ACCOUNT_REQUIRED'),
        this.translate.instant('ACTIONS.CLOSE'),
        { duration: 3000 }
      );
      return false;
    }

    return true;
  }
} 