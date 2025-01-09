import { InventoryStatus } from '../types/inventory-status.enum';
import { ShippingMethod } from '../types/shipping-method.enum';

export interface Inventory {
  id?: number;  // Optional as it might be generated on server-side

  productId?: number;
  productName?: string;

  quantity?: number;
  license?: string;

  purchaseContractId?: number;
  purchaseContractNo?: string;

  salesContractId?: number;
  salesContractNo?: string;

  storageTime?: Date;
  outTime?: Date;

  borrowedCompanyId?: number;
  borrowedCompanyName?: string;

  status?: InventoryStatus;
  remarks?: string;

  shippingAddress?: string;
  shippingMethod?: ShippingMethod;
  receiverName?: string;
  expressTrackingNumber?: string;
  receiverPhone?: string;

  handlingEmployeeId?: number;
  handlingEmployeeName?: string;
}
