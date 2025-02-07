import { BaseModel } from './base.model';

export interface Position extends BaseModel {
    name: string;
    description?: string;
    code: string;
    isActive?: boolean;
    departmentId?: number;
    departmentName?: string;
    level?: number;
    responsibilities?: string;
}
