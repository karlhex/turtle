import { ProductType } from '../types/product-type.enum';
import { Company } from './company.model';

export interface Product {
    id?: number;
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
    createdTime?: Date;
    updatedTime?: Date;
}
