import { BaseModel } from './base.model';

export interface Department extends BaseModel {
    name: string;
    description?: string;
    code: string;
    isActive?: boolean;
    parentId?: number;
    managerId?: number;
}
