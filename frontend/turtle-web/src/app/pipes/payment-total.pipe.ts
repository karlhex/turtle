import { Pipe, PipeTransform } from '@angular/core';
import { ContractDownPayment } from '../models/contract-down-payment.model';

@Pipe({
  name: 'paymentTotal',
})
export class PaymentTotalPipe implements PipeTransform {
  transform(payments: ContractDownPayment[] | undefined): number {
    if (!payments) {
      return 0;
    }
    return payments.reduce((total, payment) => total + payment.amount, 0);
  }
}
