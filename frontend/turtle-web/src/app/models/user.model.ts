export interface User {
  id?: number;
  username: string;
  email: string;
  userType?: 'SYSTEM' | 'EMPLOYEE' | 'GUEST';
  roles?: Set<string>;
  employeeId?: number; // Reference to Employee ID
  employeeName?: string; // Reference to Employee name
  employeeDepartment?: string; // Reference to Employee department
  createdAt?: Date;
  updatedAt?: Date;
}
