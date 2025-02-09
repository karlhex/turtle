import { BaseModel } from './base.model';

export interface Position extends BaseModel {
    name: string;
    description?: string;
    code: string;
    isActive?: boolean;
    level?: number;
    responsibilities?: string;
}
