import { ProjectStatus } from '../types/project-status.enum';
import { Employee } from './employee.model';
import { Contract } from './contract.model';
import { BaseModel } from './base.model';

export interface Project extends BaseModel {
  projectName: string; // 项目名称
  projectNo: string; // 项目编号
  startDate: Date; // 开始日期
  endDate: Date; // 结束日期
  status: ProjectStatus; // 项目状态
  manager: Employee; // 项目负责员工
  remarks?: string; // 备注
  contracts?: Contract[]; // 关联的合同列表
}

export interface ProjectQuery {
  page: number;
  size: number;
  search?: string;
  status?: ProjectStatus;
  managerId?: number;
  startDate?: Date;
  endDate?: Date;
}
