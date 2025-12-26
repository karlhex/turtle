import { EmployeeContractType } from '../types/employee-contract-type.enum';

// Re-export for convenience
export { EmployeeContractType } from '../types/employee-contract-type.enum';

// 申请人教育经历接口
export interface ApplicationEducation {
  id?: number;
  school: string;
  major: string;
  degree: string;
  startDate: string;
  endDate: string;
  remarks?: string;
}

// 申请人工作经历接口
export interface ApplicationJobHistory {
  id?: number;
  companyName: string;
  position: string;
  startDate: string;
  endDate?: string;
  department?: string;
  jobDescription?: string;
  achievements?: string;
  leavingReason?: string;
  referenceContact?: string;
  remarks?: string;
}

// 申请人证书接口
export interface ApplicationCertification {
  id?: number;
  name: string;
  issuer: string;
  issueDate: string;
  expiryDate?: string;
  certificateNumber?: string;
  remarks?: string;
}

export interface EmployeeApplication {
  id?: number;
  status?: ApplicationStatus;

  // === 申请人基本信息 ===
  name: string;
  email: string;
  phone?: string;
  birthday?: string;
  gender?: Gender;
  ethnicity?: string;
  idType?: IdType;
  idNumber: string;
  emergencyContactName?: string;
  emergencyContactPhone?: string;

  // === 社保公积金信息 ===
  socialSecurityNumber?: string;
  providentFundNumber?: string;
  bankAccount?: string;
  bankName?: string;

  // === 求职信息 ===
  desiredPosition?: string;
  expectedSalary?: string;
  preferredContractType?: EmployeeContractType;
  selfIntroduction?: string;

  // === 后端实际字段（字符串类型） ===
  workExperience?: string;
  educationBackground?: string;
  certifications?: string;

  // === 前端结构化数据（仅用于表单UI，不发送给后端） ===
  educations?: ApplicationEducation[];
  jobHistories?: ApplicationJobHistory[];
  certificationsStructured?: ApplicationCertification[];

  // === 关联用户信息 ===
  applicantUserId?: number;
  applicantUserName?: string;
  reviewerUserId?: number;
  reviewerUserName?: string;

  // === 审核信息 ===
  reviewComments?: string;
  submittedAt?: string;
  reviewedAt?: string;

  // === 转换信息 ===
  convertedToEmployee?: boolean;
  convertedEmployeeId?: number;
  convertedAt?: string;

  // === 时间戳 ===
  createdAt?: string;
  updatedAt?: string;
}

export enum ApplicationStatus {
  PENDING = 'PENDING',
  VALIDATED = 'VALIDATED',
  APPROVED = 'APPROVED',
  REJECTED = 'REJECTED',
  UNDER_REVIEW = 'UNDER_REVIEW'
}

export enum Gender {
  MALE = 'MALE',
  FEMALE = 'FEMALE',
  OTHER = 'OTHER'
}

export enum IdType {
  ID_CARD = 'ID_CARD',
  PASSPORT = 'PASSPORT',
  OTHER = 'OTHER'
}

export const ApplicationStatusLabels = {
  [ApplicationStatus.PENDING]: '待审批',
  [ApplicationStatus.VALIDATED]: 'HR总监已审批',
  [ApplicationStatus.APPROVED]: '已通过',
  [ApplicationStatus.REJECTED]: '已拒绝',
  [ApplicationStatus.UNDER_REVIEW]: '审核中'
};

export const GenderLabels = {
  [Gender.MALE]: '男',
  [Gender.FEMALE]: '女',
  [Gender.OTHER]: '其他'
};

export const IdTypeLabels = {
  [IdType.ID_CARD]: '身份证',
  [IdType.PASSPORT]: '护照',
  [IdType.OTHER]: '其他'
};

export const EmployeeContractTypeLabels = {
  [EmployeeContractType.FIXED_TERM]: '固定期限',
  [EmployeeContractType.NON_FIXED_TERM]: '无固定期限',
  [EmployeeContractType.INTERNSHIP]: '实习',
  [EmployeeContractType.PART_TIME]: '兼职',
  [EmployeeContractType.PROBATION]: '试用期'
};