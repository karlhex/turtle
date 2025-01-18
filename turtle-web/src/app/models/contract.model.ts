import { ContractDownPayment } from './contract-down-payment.model';
import { ContractItem } from './contract-item.model';
import { ContractStatus } from '../types/contract-status.enum';
import { ContractType } from '../types/contract-type.enum';
import { Currency } from './currency.model';
import { Company } from './company.model';
import { Invoice } from './invoice.model';
import { Person } from './person.model';
import { BaseModel } from './base.model';

export interface Contract extends BaseModel {
  contractNo: string;
  title: string;
  buyerCompany: Company;
  sellerCompany: Company;
  type: ContractType;
  signingDate: Date;
  startDate: Date;
  endDate: Date;
  contactPerson?: Person;
  projectId?: number;
  totalAmount: number;
  currency: Currency;
  status: ContractStatus;
  projectNo?: string;
  description?: string;
  remarks?: string;
  paymentTerms?: string;
  deliveryTerms?: string;
  warrantyTerms?: string;
  filePath?: string;
  items?: ContractItem[];
  downPayments?: ContractDownPayment[];
  invoices?: Invoice[];
}

export interface ContractQuery {
  page: number;
  size: number;
  search?: string;
  type?: ContractType;
  status?: ContractStatus;
  startDate?: Date;
  endDate?: Date;
  projectId?: number;
}
