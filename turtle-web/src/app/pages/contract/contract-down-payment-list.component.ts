import { Component, Input, Output, EventEmitter } from '@angular/core';
import { ContractDownPayment } from '../../models/contract-down-payment.model';
import { Currency } from '../../models/currency.model';
import { DebitCreditType } from '../../types/debit-credit-type.enum';

@Component({
  selector: 'app-contract-down-payment-list',
  templateUrl: './contract-down-payment-list.component.html',
  styleUrls: ['./contract-down-payment-list.component.scss']
})
export class ContractDownPaymentListComponent {
  @Input() items: ContractDownPayment[] = [];
  @Input() currency?: Currency;
  @Output() itemDeleted = new EventEmitter<ContractDownPayment>();
  @Output() itemEdited = new EventEmitter<ContractDownPayment>();
  @Output() paymentStatusChanged = new EventEmitter<{item: ContractDownPayment, status: boolean}>();

  displayedColumns: string[] = [
    'paymentInstruction',
    'debitCreditType',
    'plannedDate',
    'actualDate',
    'amount',
    'bankAccountNo',
    'bankName',
    'paymentStatus',
    'remarks',
    'actions'
  ];

  editItem(item: ContractDownPayment): void {
    this.itemEdited.emit(item);
  }

  deleteItem(item: ContractDownPayment): void {
    this.itemDeleted.emit(item);
  }

  onPaymentStatusChange(item: ContractDownPayment, event: any): void {
    this.paymentStatusChanged.emit({ item, status: event.checked });
  }
}
