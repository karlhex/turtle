import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ContractDownPayment } from '../../models/contract-down-payment.model';
import { Currency } from '../../models/currency.model';
import { DebitCreditType } from '../../types/debit-credit-type.enum';

interface DialogData {
  contractId?: number;
  currency: Currency;
}

@Component({
  selector: 'app-contract-down-payment-dialog',
  templateUrl: './contract-down-payment-dialog.component.html',
  styleUrls: ['./contract-down-payment-dialog.component.scss']
})
export class ContractDownPaymentDialogComponent {
  form: FormGroup;
  debitCreditTypes = Object.values(DebitCreditType);
  loading = false;

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<ContractDownPaymentDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData
  ) {
    this.form = this.fb.group({
      contractId: [data.contractId],
      paymentInstruction: ['', [Validators.required]],
      debitCreditType: [DebitCreditType.DEBIT, [Validators.required]],
      plannedDate: [null, [Validators.required]],
      actualDate: [null],
      amount: [0, [Validators.required, Validators.min(0)]],
      bankAccountNo: [''],
      bankName: [''],
      accountName: [''],
      remarks: [''],
      paymentStatus: [false]
    });
  }

  submit(): void {
    if (this.form.valid) {
      const formValue = this.form.value;
      const downPayment: ContractDownPayment = {
        ...formValue,
        currencyId: this.data.currency.id,
        currencyCode: this.data.currency.code
      };
      this.dialogRef.close(downPayment);
    }
  }

  cancel(): void {
    this.dialogRef.close();
  }
}
