import { BaseModel } from './base.model';
import { Employee } from './employee.model';
import { Project } from './project.model';
import {
  ReimbursementItem,
  CreateReimbursementItemRequest,
  UpdateReimbursementItemRequest,
} from './reimbursement-item.model';
import { ReimbursementStatus } from '../types/reimbursement-status.enum';

export interface Reimbursement extends BaseModel {
  reimbursementNo: string;
  applicationDate: string; // ISO date string
  applicantId: number;
  approverId?: number;
  projectId?: number;
  approvalDate?: string; // ISO date string
  totalAmount: number;
  items: ReimbursementItem[];
  remarks?: string;
  status: ReimbursementStatus;
}

export interface CreateReimbursementRequest {
  applicationDate: string;
  applicantId: number;
  projectId?: number;
  items: CreateReimbursementItemRequest[];
  remarks?: string;
  totalAmount: number;
}

export interface UpdateReimbursementRequest {
  id: number;
  applicationDate: string;
  applicantId: number;
  projectId?: number;
  items: (CreateReimbursementItemRequest | UpdateReimbursementItemRequest)[];
  remarks?: string;
  totalAmount: number;
}

// For API responses
export interface ReimbursementResponse {
  content: Reimbursement[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}

// Search parameters
export interface ReimbursementSearchParams {
  reimbursementNo?: string;
  startDate?: string;
  endDate?: string;
  applicantId?: number;
  approverId?: number;
  projectId?: number;
  page?: number;
  size?: number;
  sort?: string;
}
