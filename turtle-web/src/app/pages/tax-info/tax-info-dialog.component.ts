import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { TranslateService } from '@ngx-translate/core';
import { TaxInfoService } from '../../services/tax-info.service';
import { TaxInfo } from '../../models/tax-info.model';

@Component({
  selector: 'app-tax-info-dialog',
  templateUrl: './tax-info-dialog.component.html',
  styleUrls: ['./tax-info-dialog.component.scss']
})
export class TaxInfoDialogComponent {
  form: FormGroup;
  loading = false;

  constructor(
    private fb: FormBuilder,
    private taxInfoService: TaxInfoService,
    private dialogRef: MatDialogRef<TaxInfoDialogComponent>,
    private snackBar: MatSnackBar,
    private translate: TranslateService,
    @Inject(MAT_DIALOG_DATA) public data: { mode: 'create' | 'edit', taxInfo?: TaxInfo }
  ) {
    this.form = this.fb.group({
      fullName: ['', [Validators.required]],
      address: ['', [Validators.required]],
      phone: ['', [Validators.required]],
      bankName: ['', [Validators.required]],
      bankCode: ['', [Validators.required]],
      bankAccount: ['', [Validators.required]],
      taxNo: ['', [Validators.required]],
      remarks: ['']
    });

    if (data.mode === 'edit' && data.taxInfo) {
      this.form.patchValue(data.taxInfo);
    }
  }

  onSubmit(): void {
    if (this.form.valid) {
      this.loading = true;
      const taxInfo = this.form.value;

      const request = this.data.mode === 'create'
        ? this.taxInfoService.createTaxInfo(taxInfo)
        : this.taxInfoService.updateTaxInfo(this.data.taxInfo!.id!, taxInfo);

      request.subscribe({
        next: () => {
          this.dialogRef.close(true);
          this.snackBar.open(
            this.translate.instant(this.data.mode === 'create' 
              ? 'SUCCESS.TAX_INFO_CREATED' 
              : 'SUCCESS.TAX_INFO_UPDATED'),
            this.translate.instant('ACTIONS.CLOSE'),
            { duration: 3000 }
          );
        },
        error: (error) => {
          console.error('Error saving tax info:', error);
          this.loading = false;
          this.snackBar.open(
            this.translate.instant('ERROR.SAVING_TAX_INFO'),
            this.translate.instant('ACTIONS.CLOSE'),
            { duration: 3000 }
          );
        }
      });
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}
