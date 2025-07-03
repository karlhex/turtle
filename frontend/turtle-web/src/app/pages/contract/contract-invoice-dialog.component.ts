import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Currency } from '../../models/currency.model';
import { Invoice } from '../../models/invoice.model';
import { InvoiceType } from '../../types/invoice-type.enum';
import { TaxInfo } from '../../models/tax-info.model';
import { Contract } from '../../models/contract.model';
import { Company } from '@app/models/company.model';

interface DialogData {
  item?: Invoice;
  currency?: Currency;
  buyer?: Company;
  seller?: Company;
}

@Component({
  selector: 'app-contract-invoice-dialog',
  templateUrl: './contract-invoice-dialog.component.html',
  styleUrls: ['./contract-invoice-dialog.component.scss'],
})
export class ContractInvoiceDialogComponent implements OnInit {
  form: FormGroup;
  invoiceTypes = Object.values(InvoiceType);
  isEdit: boolean;
  buyerTaxInfo?: TaxInfo;
  sellerTaxInfo?: TaxInfo;

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<ContractInvoiceDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData
  ) {
    this.isEdit = !!data.item;
    this.buyerTaxInfo = data.buyer?.taxInfo;
    this.sellerTaxInfo = data.seller?.taxInfo;

    console.log('Dialog data:', data.buyer, this.sellerTaxInfo);

    // Create form with contract's tax info
    this.form = this.fb.group({
      invoiceNo: ['', Validators.required],
      type: [null, Validators.required],
      amount: [null, [Validators.required, Validators.min(0)]],
      taxRate: [null, [Validators.required, Validators.min(0), Validators.max(100)]],
      taxAmount: [null, [Validators.required, Validators.min(0)]],
      totalAmount: [null, [Validators.required, Validators.min(0)]],
      invoiceDate: [null, Validators.required],
      buyerTaxInfo: [{ value: this.buyerTaxInfo, disabled: true }, Validators.required],
      sellerTaxInfo: [{ value: this.sellerTaxInfo, disabled: true }, Validators.required],
      buyerName: [{ value: this.buyerTaxInfo?.fullName, disabled: true }, Validators.required],
      sellerName: [{ value: this.sellerTaxInfo?.fullName, disabled: true }, Validators.required],
      batchNo: [''],
      verificationCode: [''],
      machineCode: [''],
      cancelled: [false],
      cancelDate: [null],
      remarks: [''],
    });

    if (this.isEdit && data.item) {
      this.form.patchValue(data.item);
    }
  }

  ngOnInit(): void {}

  calculateTaxAmount(): void {
    const amount = this.form.get('amount')?.value;
    const taxRate = this.form.get('taxRate')?.value;

    if (amount && taxRate) {
      const taxAmount = amount * (taxRate / 100);
      const totalAmount = amount + taxAmount;

      this.form.patchValue({
        taxAmount: Number(taxAmount.toFixed(2)),
        totalAmount: Number(totalAmount.toFixed(2)),
      });
    }
  }

  onSubmit(): void {
    if (this.form.valid) {
      const formValue = this.form.getRawValue();
      // Remove display-only fields before submitting
      delete formValue.buyerName;
      delete formValue.sellerName;
      this.dialogRef.close(formValue);
    }
  }
}
