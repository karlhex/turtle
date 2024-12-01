export interface Contract {
  id?: number;
  contractNo: string;
  contractName: string;
  contractType: ContractType;
  status: ContractStatus;
  startDate: Date;
  endDate: Date;
  amount: number;
  currency: string;
  remarks?: string;
  items?: ContractItem[];
  downPayments?: ContractDownPayment[];
  createdTime?: Date;
  updatedTime?: Date;
}

export interface ContractItem {
  id?: number;
  name: string;
  quantity: number;
  unitPrice: number;
  amount: number;
  remarks?: string;
}

export interface ContractDownPayment {
  id?: number;
  amount: number;
  paymentDate: Date;
  remarks?: string;
}

export enum ContractType {
  PURCHASE = 'PURCHASE',
  SALES = 'SALES',
  SERVICE = 'SERVICE'
}

export enum ContractStatus {
  DRAFT = 'DRAFT',
  ACTIVE = 'ACTIVE',
  COMPLETED = 'COMPLETED',
  TERMINATED = 'TERMINATED'
}

export interface ContractQuery {
  contractNo?: string;
  contractName?: string;
  contractType?: ContractType;
  status?: ContractStatus;
  startDate?: Date;
  endDate?: Date;
  pageIndex?: number;
  pageSize?: number;
  sortField?: string;
  sortOrder?: string;
}
