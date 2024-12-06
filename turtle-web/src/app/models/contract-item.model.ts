export interface ContractItem {
  id?: number;
  contractId?: number;
  productId: number;
  name: string;
  quantity: number;
  unitPrice: number;
  amount: number;
  modelNumber?: string;
  remarks?: string;
  createdTime?: Date;
  updatedTime?: Date;
}
