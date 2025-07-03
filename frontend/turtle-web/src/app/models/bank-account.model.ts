import { Currency } from './currency.model';
import { BankAccountType } from '../types/bank-account-type.enum';

export interface BankAccount {
  id?: number;
  name: string; // 账户名称
  accountNo: string; // 账号
  bankName: string; // 银行名称
  balance: number; // 余额
  currency: Currency; // 币种
  companyId: number; // 所属公司ID
  type: BankAccountType; // 账户类型
  bankBranch: string; // 开户行支行
  description?: string; // 描述
  active: boolean; // 是否启用
  swiftCode?: string; // SWIFT代码
  contactPerson?: string; // 联系人
  contactPhone?: string; // 联系电话
  createdAt?: Date;
  updatedAt?: Date;
}
