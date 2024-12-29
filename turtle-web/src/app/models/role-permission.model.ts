import { BaseModel } from './base.model';

export interface RolePermission extends BaseModel {
  roleName: string;
  permission: string;
}

export interface RolePermissionCreateRequest {
  roleName: string;
  permission: string;
}

export interface RolePermissionUpdateRequest {
  roleName: string;
  permission: string;
}

export interface RolePermissionResponse {
  data: RolePermission;
  message: string;
  success: boolean;
}

export interface RolePermissionListResponse {
  data: {
    content: RolePermission[];
    totalElements: number;
    totalPages: number;
    size: number;
    number: number;
  };
  message: string;
  success: boolean;
}
