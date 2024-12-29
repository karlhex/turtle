export interface Role {
  id: number;
  name: string;
  description: string;
  isSystem: boolean;
}

export interface CreateRoleRequest {
  name: string;
  description: string;
  isSystem: boolean;
}

export interface UpdateRoleRequest {
  name: string;
  description: string;
  isSystem: boolean;
}
