import { BaseModel } from './base.model';

export interface ReimbursementItem extends BaseModel {
  reimbursementId: number;
  amount: number;
  reason: string;
  occurrenceDate: string; // ISO date string
  remarks?: string;
}

export interface CreateReimbursementItemRequest {
  amount: number;
  reason: string;
  occurrenceDate: string;
  remarks?: string;
}

export interface UpdateReimbursementItemRequest extends CreateReimbursementItemRequest {
  id: number;
}
