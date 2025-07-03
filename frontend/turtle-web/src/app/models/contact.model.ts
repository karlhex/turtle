import { BaseModel } from './base.model';

/**
 * Contact Model Interface
 * 联系人信息接口
 */
export interface Contact extends BaseModel {
  // 基本信息 Basic Information
  firstName?: string; // 名
  lastName?: string; // 姓
  address?: string; // 地址
  email?: string; // 邮箱

  // 联系方式 Contact Information
  mobilePhone?: string; // 手机号
  workPhone?: string; // 工作电话
  homePhone?: string; // 家庭电话

  // 公司信息 Company Information
  companyId?: number; // 所属公司ID
  companyName?: string; // 所属公司名称
  department?: string; // 部门
  title?: string; // 职位

  // 个人信息 Personal Information
  gender?: string; // 性别
  nativePlace?: string; // 籍贯
  ethnicity?: string; // 民族
  maritalStatus?: string; // 婚姻状况
  nationality?: string; // 国籍
  birthDate?: string; // 出生日期
  religion?: string; // 宗教

  // 教育背景 Educational Background
  university?: string; // 毕业院校
  major?: string; // 专业
  graduationYear?: string; // 毕业年份
  degree?: string; // 学位

  // 其他信息 Other Information
  hobbies?: string; // 爱好
  remarks?: string; // 备注
  active: boolean; // 是否启用
}
