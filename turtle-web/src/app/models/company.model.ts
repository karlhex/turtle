import { BankAccount } from './bank-account.model';
import { TaxInfo } from './tax-info.model';
import { Person } from './person.model';

export interface Company {
  id?: number;
  fullName: string;        // 公司全称
  shortName?: string;      // 公司简称
  address: string;         // 公司地址
  phone: string;           // 公司电话
  email?: string;         // 公司邮件
  website?: string;       // 公司网站
  isPrimary?: boolean;    // 主公司标志
  bankAccounts?: BankAccount[];  // 公司银行账户列表
  taxInfo?: TaxInfo;      // 税务信息
  businessContact?: Person;   // 商务联系人
  technicalContact?: Person;  // 技术联系人
  active: boolean;         // 是否启用
  remarks?: string;        // 备注
  createdAt?: Date;
  updatedAt?: Date;
}
