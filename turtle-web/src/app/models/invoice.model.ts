import { BaseModel } from './base.model';
import { TaxInfo } from './tax-info.model';

export enum InvoiceType {
  GENERAL = 'GENERAL',
  SPECIAL = 'SPECIAL'
}

export interface Invoice extends BaseModel {
  invoiceNo: string;
  buyerTaxInfo: TaxInfo;
  sellerTaxInfo: TaxInfo;
  batchNo: string;
  type: InvoiceType;
  amount: number;
  taxRate: number;
  taxAmount: number;
  totalAmount: number;
  remarks: string;
  invoiceDate: Date;
  verificationCode: string;
  machineCode: string;
  cancelled: boolean;
  cancelDate?: Date;
  cancelReason?: string;
}
