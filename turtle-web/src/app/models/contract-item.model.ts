import { Product } from "./product.model";

export interface ContractItem {
  id?: number;
  contractId?: number;
  product: Product;
  productName: string;  // 产品名称，用于显示
  modelNumber?: string; // 型号
  quantity: number;     // 数量
  unitPrice: number;    // 单价
  totalAmount: number;  // 总金额
  remarks?: string;
  createdAt?: Date;
  updatedAt?: Date;
}
