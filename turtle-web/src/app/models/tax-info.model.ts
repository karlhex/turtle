export interface TaxInfo {
  id?: number;
  fullName: string;        // 公司全称
  address: string;         // 地址
  phone: string;           // 电话
  bankName: string;        // 开户行
  bankCode: string;        // 联行号
  bankAccount: string;     // 银行账号
  taxNo: string;          // 税号
  active: boolean;        // 是否启用
  remarks?: string;        // 备注
  createdAt?: Date;
  updatedAt?: Date;
}
