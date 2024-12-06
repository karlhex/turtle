import { DebitCreditType } from '../types/debit-credit-type.enum';

export interface ContractDownPayment {
  id?: number;
  contractId?: number;
  contractNo?: string;
  currencyId: number;
  currencyCode?: string;
  amount: number;
  debitCreditType: DebitCreditType;
  plannedDate: Date;
  actualDate?: Date;
  paymentStatus: boolean;
  remarks?: string;
  createdTime?: Date;
  updatedTime?: Date;
}
