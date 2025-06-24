import { ProductType } from '../types/product-type.enum';
import { Company } from './company.model';
import { BaseModel } from './base.model';

export interface Product extends BaseModel {
  name: string;
  modelNumber: string;
  manufacturer: Company;
  type: ProductType;
  unit: string;
  description?: string;
  specifications?: string;
  remarks?: string;
  active: boolean;
  warrantyPeriod?: number;
}
