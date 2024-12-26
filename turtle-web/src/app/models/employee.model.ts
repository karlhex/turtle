import { Department } from './department.model';
import { Person } from './person.model';
import { User } from './user.model';

export enum EmployeeStatus {
    APPLICATION = 'APPLICATION',
    ACTIVE = 'ACTIVE',
    RESIGNED = 'RESIGNED',
    SUSPENDED = 'SUSPENDED'
}

export enum EmployeeRole {
    HR_SPECIALIST = 'HR_SPECIALIST',
    FINANCIAL_MANAGER = 'FINANCIAL_MANAGER',
    DEPARTMENT_MANAGER = 'DEPARTMENT_MANAGER',
    REGULAR_EMPLOYEE = 'REGULAR_EMPLOYEE'
}

export interface EmployeeEducation {
    id?: number;
    school: string;
    major: string;
    degree: string;
    startDate: Date;
    endDate: Date;
    remarks?: string;
}

export interface EmployeeAttendance {
    id?: number;
    attendanceDate: Date;
    checkInTime: Date;
    checkOutTime: Date;
    status: string;
    remarks?: string;
}

export interface EmployeeLeave {
    id?: number;
    startDate: Date;
    endDate: Date;
    leaveType: string;
    reason: string;
    status: string;
    remarks?: string;
}

export interface EmployeeJobHistory {
    id?: number;
    employeeId: number;
    companyName: string;
    position: string;
    startDate: string;
    endDate?: string;
    department: string;
    jobDescription?: string;
    achievements?: string;
    leavingReason?: string;
    referenceContact?: string;
    remarks?: string;
  }
  
export interface Employee {
    id?: number;
    name: string;
    employeeNumber: string;
    email?: string;
    phone?: string;
    department?: Department;
    position?: string;
    status: EmployeeStatus;
    hireDate?: Date;
    leaveDate?: Date;
    remarks?: string;
    emergencyContactName?: string;
    emergencyContactPhone?: string;
    birthday?: Date;
    gender?: string;
    ethnicity?: string;
    contractType?: string;
    contractDuration?: number;
    contractStartDate?: Date;
    idType?: string;
    idNumber: string;
    user?: User;
    educations?: EmployeeEducation[];
    attendances?: EmployeeAttendance[];
    leaves?: EmployeeLeave[];
    jobHistories?: EmployeeJobHistory[];
    role?: EmployeeRole;
}
