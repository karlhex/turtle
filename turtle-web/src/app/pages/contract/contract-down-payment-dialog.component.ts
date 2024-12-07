import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ContractDownPayment } from '../../models/contract-down-payment.model';
import { Currency } from '../../models/currency.model';
import { DebitCreditType } from '../../types/debit-credit-type.enum';

interface DialogData {
  id?: number;
  contractId?: number;
  contractNo?: string;
  currencyId?: number;
  currencyCode?: string;
  amount?: number;
  debitCreditType?: DebitCreditType;
  plannedDate?: Date;
  actualDate?: Date;
  paymentStatus?: boolean;
  paymentInstruction?: string;
  bankAccountNo?: string;
  bankName?: string;
  accountName?: string;
  remarks?: string;
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
    console.log("Dialog data:", data);
    this.form = this.fb.group({
      id: [data.id],
      contractId: [data.contractId],
      contractNo: [data.contractNo],
      currencyId: [data.currencyId],
      currencyCode: [data.currencyCode],
      amount: [data.amount || null, [Validators.required, Validators.min(0)]],
      debitCreditType: [data.debitCreditType || DebitCreditType.DEBIT, [Validators.required]],
      plannedDate: [data.plannedDate || null, [Validators.required]],
      actualDate: [data.actualDate],
      paymentStatus: [data.paymentStatus || false],
      paymentInstruction: [data.paymentInstruction || '', [Validators.required]],
      bankAccountNo: [data.bankAccountNo || ''],
      bankName: [data.bankName || ''],
      accountName: [data.accountName || ''],
      remarks: [data.remarks || '']
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
