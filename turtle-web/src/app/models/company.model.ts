import { BankAccount } from './bank-account.model';
import { TaxInfo } from './tax-info.model';
import { CompanyType } from '../types/company-type.enum';
import { BaseModel } from './base.model';
import { Contact } from './contact.model';

export interface Company extends BaseModel {
  fullName: string;        // 公司全称
  shortName?: string;      // 公司简称
  address: string;         // 公司地址
  phone: string;           // 公司电话
  email?: string;         // 公司邮件
  website?: string;       // 公司网站
  bankAccounts?: BankAccount[];  // 公司银行账户列表
  taxInfo?: TaxInfo;      // 税务信息
  active: boolean;         // 是否启用
  remarks?: string;        // 备注
  type: CompanyType;      // 公司类型
  contacts?: Contact[];    // 联系人列表
}
