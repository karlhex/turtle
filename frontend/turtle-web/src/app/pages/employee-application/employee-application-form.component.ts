import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { EmployeeApplicationService } from '../../services/employee-application.service';
import {
  EmployeeApplication,
  Gender,
  IdType,
  ApplicationStatus,
  EmployeeContractType,
  ApplicationEducation,
  ApplicationJobHistory,
  ApplicationCertification
} from '../../models/employee-application.model';
import { InputPageConfig } from '../../components/input-page/input-page.component';

@Component({
  selector: 'app-employee-application-form',
  templateUrl: './employee-application-form.component.html',
  styleUrls: ['./employee-application-form.component.scss']
})
export class EmployeeApplicationFormComponent implements OnInit {

  basicInfoConfig: InputPageConfig = {
    title: '员工入职申请',
    subtitle: '请如实填写以下信息，我们会在3个工作日内处理您的申请。',
    fields: [
      // 基本信息
      {
        key: 'name',
        label: '姓名',
        type: 'text',
        required: true,
        placeholder: '请输入您的姓名',
        width: 2
      },
      {
        key: 'email',
        label: '邮箱',
        type: 'email',
        required: true,
        placeholder: '请输入邮箱地址',
        width: 2
      },
      {
        key: 'phone',
        label: '手机号',
        type: 'text',
        required: false,
        placeholder: '请输入手机号',
        pattern: '^1[3-9]\\d{9}$',
        width: 2
      },
      {
        key: 'birthday',
        label: '生日',
        type: 'date',
        required: false,
        width: 2
      },
      {
        key: 'gender',
        label: '性别',
        type: 'select',
        required: false,
        width: 2,
        options: [
          { value: Gender.MALE, label: '男' },
          { value: Gender.FEMALE, label: '女' },
          { value: Gender.OTHER, label: '其他' }
        ]
      },
      {
        key: 'ethnicity',
        label: '民族',
        type: 'text',
        required: false,
        placeholder: '请输入民族',
        width: 2
      },
      {
        key: 'idType',
        label: '证件类型',
        type: 'select',
        required: false,
        width: 2,
        options: [
          { value: IdType.ID_CARD, label: '身份证' },
          { value: IdType.PASSPORT, label: '护照' },
          { value: IdType.OTHER, label: '其他' }
        ]
      },
      {
        key: 'idNumber',
        label: '证件号码',
        type: 'text',
        required: true,
        placeholder: '请输入证件号码',
        width: 2
      },
      {
        key: 'emergencyContactName',
        label: '紧急联系人姓名',
        type: 'text',
        required: false,
        placeholder: '请输入紧急联系人姓名',
        width: 2
      },
      {
        key: 'emergencyContactPhone',
        label: '紧急联系人电话',
        type: 'text',
        required: false,
        placeholder: '请输入紧急联系人电话',
        pattern: '^1[3-9]\\d{9}$',
        width: 2
      },
      // 社保公积金信息
      {
        key: 'socialSecurityNumber',
        label: '社保号',
        type: 'text',
        required: false,
        placeholder: '请输入社保号',
        width: 2
      },
      {
        key: 'providentFundNumber',
        label: '公积金账号',
        type: 'text',
        required: false,
        placeholder: '请输入公积金账号',
        width: 2
      },
      {
        key: 'bankAccount',
        label: '银行账号',
        type: 'text',
        required: false,
        placeholder: '请输入银行账号',
        width: 2
      },
      {
        key: 'bankName',
        label: '开户银行',
        type: 'text',
        required: false,
        placeholder: '请输入开户银行',
        width: 2
      },
      // 求职信息
      {
        key: 'desiredPosition',
        label: '期望职位',
        type: 'text',
        required: false,
        placeholder: '请输入期望职位',
        width: 2
      },
      {
        key: 'expectedSalary',
        label: '期望薪资',
        type: 'text',
        required: false,
        placeholder: '如：8000-12000',
        width: 2
      },
      {
        key: 'preferredContractType',
        label: '期望合同类型',
        type: 'select',
        required: false,
        width: 2,
        options: [
          { value: EmployeeContractType.FIXED_TERM, label: '固定期限' },
          { value: EmployeeContractType.NON_FIXED_TERM, label: '无固定期限' },
          { value: EmployeeContractType.INTERNSHIP, label: '实习' },
          { value: EmployeeContractType.PART_TIME, label: '兼职' },
          { value: EmployeeContractType.PROBATION, label: '试用期' }
        ]
      },
      {
        key: 'selfIntroduction',
        label: '自我介绍',
        type: 'textarea',
        required: false,
        placeholder: '请简单介绍您的个人情况、兴趣爱好等',
        rows: 4,
        width: 4
      },
    ],
    layout: 'two-column',
    showSaveButton: false,
    showCancelButton: false,
    showResetButton: false
  };

  // 保留完整的配置以避免其他地方的引用错误
  config = this.basicInfoConfig;

  initialData: any = {};
  loading = false;
  saving = false;

  // 表单数据存储
  formData: any = {};

  // 结构化数据数组
  educations: ApplicationEducation[] = [];
  jobHistories: ApplicationJobHistory[] = [];
  certifications: ApplicationCertification[] = [];

  constructor(
    private employeeApplicationService: EmployeeApplicationService,
    private snackBar: MatSnackBar,
    private router: Router
  ) {}

  ngOnInit(): void {
    // Component initialization
  }

  onSave(_data: any): void {
    this.submitForm();
  }

  submitForm(): void {
    // 从input-page组件获取表单数据
    const formData = this.getFormData();
    if (!this.isFormValid()) {
      return;
    }

    this.saving = true;

    // 先检查身份证号和邮箱
    this.checkDuplicates(formData).then((canSubmit) => {
      if (canSubmit) {
        this.submitApplication(formData);
      } else {
        this.saving = false;
      }
    });
  }

  private getFormData(): any {
    // 合并初始数据和表单变更数据
    return {
      ...this.initialData,
      ...this.formData
    };
  }

  isFormValid(): boolean {
    const currentData = this.getFormData();

    // 检查必填字段
    const requiredFields = ['name', 'email', 'idNumber'];
    for (const field of requiredFields) {
      if (!currentData[field] || currentData[field].toString().trim() === '') {
        return false;
      }
    }

    // 检查邮箱格式
    if (currentData.email && !this.isValidEmail(currentData.email)) {
      return false;
    }

    return true;
  }

  onCancel(): void {
    this.router.navigate(['/']);
  }

  onReset(): void {
    // 重置所有数据
    this.formData = {};
    this.educations = [];
    this.jobHistories = [];
    this.certifications = [];
    this.initialData = {};
  }

  onFieldChange(key: string, value: any): void {
    // 更新表单数据
    this.formData[key] = value;
  }

  onValidationChange(_valid: boolean, _errors: any): void {
    // Handle validation changes if needed
  }

  // 教育经历管理
  onEducationEdited(educations: ApplicationEducation[]): void {
    this.educations = educations;
  }

  // 工作经历管理
  onJobHistoryEdited(jobHistories: ApplicationJobHistory[]): void {
    this.jobHistories = jobHistories;
  }

  // 证书管理
  onCertificationEdited(certifications: ApplicationCertification[]): void {
    this.certifications = certifications;
  }

  private isValidEmail(email: string): boolean {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
  }

  // 将结构化教育数据转换为字符串格式
  private convertEducationsToString(): string {
    if (this.educations.length === 0) {
      return '';
    }

    return this.educations.map(edu => {
      const parts = [];
      if (edu.school) parts.push(`School: ${edu.school}`);
      if (edu.degree) parts.push(`Degree: ${edu.degree}`);
      if (edu.major) parts.push(`Major: ${edu.major}`);
      if (edu.startDate && edu.endDate) {
        parts.push(`Period: ${edu.startDate} to ${edu.endDate}`);
      }
      if (edu.remarks) parts.push(`Remarks: ${edu.remarks}`);
      return parts.join(', ');
    }).join('\n\n');
  }

  // 将结构化工作经历数据转换为字符串格式
  private convertJobHistoriesToString(): string {
    if (this.jobHistories.length === 0) {
      return '';
    }

    return this.jobHistories.map(job => {
      const parts = [];
      if (job.companyName) parts.push(`Company: ${job.companyName}`);
      if (job.position) parts.push(`Position: ${job.position}`);
      if (job.department) parts.push(`Department: ${job.department}`);
      if (job.startDate) {
        const endDate = job.endDate ? job.endDate : 'Present';
        parts.push(`Period: ${job.startDate} to ${endDate}`);
      }
      if (job.jobDescription) parts.push(`Description: ${job.jobDescription}`);
      if (job.achievements) parts.push(`Achievements: ${job.achievements}`);
      if (job.leavingReason) parts.push(`Leaving Reason: ${job.leavingReason}`);
      if (job.remarks) parts.push(`Remarks: ${job.remarks}`);
      return parts.join(', ');
    }).join('\n\n');
  }

  // 将结构化证书数据转换为字符串格式
  private convertCertificationsToString(): string {
    if (this.certifications.length === 0) {
      return '';
    }

    return this.certifications.map(cert => {
      const parts = [];
      if (cert.name) parts.push(`Certificate: ${cert.name}`);
      if (cert.issuer) parts.push(`Issuer: ${cert.issuer}`);
      if (cert.issueDate) parts.push(`Issue Date: ${cert.issueDate}`);
      if (cert.expiryDate) parts.push(`Expiry Date: ${cert.expiryDate}`);
      if (cert.certificateNumber) parts.push(`Certificate Number: ${cert.certificateNumber}`);
      if (cert.remarks) parts.push(`Remarks: ${cert.remarks}`);
      return parts.join(', ');
    }).join('\n\n');
  }

  private async checkDuplicates(formValue: any): Promise<boolean> {
    try {
      // 检查身份证号
      const idCheckResult = await this.employeeApplicationService.checkIdNumber(formValue.idNumber).toPromise();
      if (idCheckResult?.data) {
        this.snackBar.open('该身份证号已存在申请', '关闭', { duration: 3000 });
        return false;
      }

      // 检查邮箱是否有待处理申请
      const emailCheckResult = await this.employeeApplicationService.checkPendingEmail(formValue.email).toPromise();
      if (emailCheckResult?.data) {
        this.snackBar.open('该邮箱已有待处理的申请', '关闭', { duration: 3000 });
        return false;
      }

      return true;
    } catch (error) {
      console.error('检查重复信息失败:', error);
      this.snackBar.open('检查信息失败，请重试', '关闭', { duration: 3000 });
      return false;
    }
  }

  private submitApplication(formValue: any): void {
    const application: EmployeeApplication = {
      ...formValue,
      status: ApplicationStatus.PENDING,
      // 为了后端兼容性，转换为字符串格式
      educationBackground: this.convertEducationsToString(),
      workExperience: this.convertJobHistoriesToString(),
      certifications: this.convertCertificationsToString()
    };

    // 保留结构化数据在内存中，用于前端状态管理
    // 注意：暂时不发送到后端，因为后端还未支持结构化字段

    this.employeeApplicationService.submitApplication(application).subscribe({
      next: (response) => {
        if (response.code === 200) {
          this.snackBar.open('申请提交成功！', '关闭', { duration: 3000 });
          this.router.navigate(['/employee-application/my-applications']);
        } else {
          this.snackBar.open(response.message || '提交失败', '关闭', { duration: 3000 });
        }
        this.saving = false;
      },
      error: (error) => {
        console.error('提交申请失败:', error);
        this.snackBar.open('提交失败，请重试', '关闭', { duration: 3000 });
        this.saving = false;
      }
    });
  }
}