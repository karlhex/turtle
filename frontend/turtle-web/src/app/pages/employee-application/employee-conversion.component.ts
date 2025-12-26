import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatDialog } from '@angular/material/dialog';
import { EmployeeApplicationService } from '../../services/employee-application.service';
import { EmployeeService } from '../../services/employee.service';
import { DepartmentService } from '../../services/department.service';
import { PositionService } from '../../services/position.service';
import { SequenceService } from '../../services/sequence.service';
import { RoleService } from '../../services/role.service';
import {
  EmployeeApplication,
  ApplicationStatus,
  ApplicationStatusLabels,
  GenderLabels,
  IdTypeLabels,
  EmployeeContractTypeLabels,
  EmployeeContractType
} from '../../models/employee-application.model';
import { Department } from '../../models/department.model';
import { Position } from '../../models/position.model';
import { EmployeeStatus } from '../../models/employee.model';
import { Role } from '../../models/role.model';

@Component({
  selector: 'app-employee-conversion',
  templateUrl: './employee-conversion.component.html',
  styleUrls: ['./employee-conversion.component.scss']
})
export class EmployeeConversionComponent implements OnInit {

  application?: EmployeeApplication;
  conversionForm: FormGroup;
  loading = true;
  submitting = false;
  applicationId!: number;

  departments: Department[] = [];
  positions: Position[] = [];
  roles: Role[] = [];
  selectedRoles: number[] = [];
  
  // 标签映射
  ApplicationStatusLabels = ApplicationStatusLabels;
  GenderLabels = GenderLabels;
  IdTypeLabels = IdTypeLabels;
  EmployeeContractTypeLabels = EmployeeContractTypeLabels;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private fb: FormBuilder,
    private dialog: MatDialog,
    private snackBar: MatSnackBar,
    private employeeApplicationService: EmployeeApplicationService,
    private employeeService: EmployeeService,
    private departmentService: DepartmentService,
    private positionService: PositionService,
    private sequenceService: SequenceService,
    private roleService: RoleService
  ) {
    this.conversionForm = this.fb.group({
      // 员工基本信息
      employeeNumber: ['', [Validators.required, Validators.pattern(/^[A-Z0-9]{6,10}$/)]],
      
      // 职务信息
      departmentId: ['', [Validators.required]],
      positionId: ['', [Validators.required]],
      
      // 合同信息
      contractType: [EmployeeContractType.FIXED_TERM, [Validators.required]],
      contractStartDate: ['', [Validators.required]],
      contractEndDate: [''],
      probationPeriod: [3, [Validators.min(0), Validators.max(12)]],
      
      // 薪酬信息
      basicSalary: [0, [Validators.min(0)]],
      
      // 入职日期
      hireDate: ['', [Validators.required]],
      
      // 备注
      conversionNotes: [''],
      
      // 角色
      roles: [[]]
    });

    // 监听合同类型变化
    this.conversionForm.get('contractType')?.valueChanges.subscribe(value => {
      this.updateContractEndDateValidation(value);
    });
  }

  ngOnInit(): void {
    this.applicationId = Number(this.route.snapshot.paramMap.get('id'));
    this.loadData();
  }

  private loadData(): void {
    // 并行加载数据
    Promise.all([
      this.loadApplication(),
      this.loadDepartments(),
      this.loadPositions(),
      this.loadRoles()
    ]).then(() => {
      this.loading = false;
      this.initializeFormWithApplicationData();
    }).catch(error => {
      console.error('加载数据失败:', error);
      this.snackBar.open('加载数据失败，请重试', '关闭', { duration: 3000 });
      this.loading = false;
      this.goBack();
    });
  }

  private loadApplication(): Promise<void> {
    return new Promise((resolve, reject) => {
      this.employeeApplicationService.getApplication(this.applicationId).subscribe({
        next: (response) => {
          if (response.code === 200) {
            this.application = response.data;
            
            // 检查申请是否可以转换
            if (!this.canConvert()) {
              this.snackBar.open('该申请当前状态不允许转换为员工', '关闭', { duration: 3000 });
              this.goBack();
              reject('Cannot convert application');
              return;
            }
            resolve();
          } else {
            reject(response.message);
          }
        },
        error: (error) => {
          reject(error);
        }
      });
    });
  }

  private loadDepartments(): Promise<void> {
    return new Promise((resolve, reject) => {
      this.departmentService.getDepartments().subscribe({
        next: (response) => {
          if (response.code === 200) {
            this.departments = response.data.content || response.data;
            resolve();
          } else {
            reject(response.message);
          }
        },
        error: (error) => {
          reject(error);
        }
      });
    });
  }

  private loadPositions(): Promise<void> {
    return new Promise((resolve, reject) => {
      this.positionService.getPositions().subscribe({
        next: (response) => {
          if (response.code === 200) {
            this.positions = response.data.content || response.data;
            resolve();
          } else {
            reject(response.message);
          }
        },
        error: (error) => {
          reject(error);
        }
      });
    });
  }

  private loadRoles(): Promise<void> {
    return new Promise((resolve, reject) => {
      this.roleService.getAllRoles().subscribe({
        next: (response) => {
          if (response.code === 200) {
            this.roles = response.data || [];
            resolve();
          } else {
            reject(response.message);
          }
        },
        error: (error) => {
          reject(error);
        }
      });
    });
  }

  private initializeFormWithApplicationData(): void {
    if (!this.application) return;

    // 生成建议的员工编号
    this.generateEmployeeNumber();
    
    // 设置今天为默认入职日期
    const today = new Date();
    
    this.conversionForm.patchValue({
      contractType: this.application.preferredContractType || EmployeeContractType.FIXED_TERM,
      contractStartDate: today,
      hireDate: today,
      probationPeriod: 3 // 默认3个月试用期
    });

    // 如果申请中有期望职位，尝试匹配
    if (this.application.desiredPosition) {
      const matchedPosition = this.positions.find(p => 
        p.name.includes(this.application?.desiredPosition || '') || 
        (this.application?.desiredPosition || '').includes(p.name)
      );
      if (matchedPosition) {
        this.conversionForm.patchValue({
          positionId: matchedPosition.id
        });
      }
    }
  }

  private generateEmployeeNumber(): void {
    this.sequenceService.getNextSequence('EMPLOYEE').subscribe({
      next: (response) => {
        if (response.code === 200) {
          this.conversionForm.patchValue({
            employeeNumber: response.data
          });
        } else {
          // 如果获取序列号失败，使用备用方案
          const fallbackNumber = this.generateFallbackEmployeeNumber();
          this.conversionForm.patchValue({
            employeeNumber: fallbackNumber
          });
          console.warn('获取员工编号失败，使用备用编号:', fallbackNumber);
        }
      },
      error: (error) => {
        console.error('获取员工编号失败:', error);
        // 使用备用方案
        const fallbackNumber = this.generateFallbackEmployeeNumber();
        this.conversionForm.patchValue({
          employeeNumber: fallbackNumber
        });
      }
    });
  }

  private generateFallbackEmployeeNumber(): string {
    const year = new Date().getFullYear().toString().substr(-2);
    const month = (new Date().getMonth() + 1).toString().padStart(2, '0');
    const random = Math.floor(Math.random() * 9999).toString().padStart(4, '0');
    return `EMP${year}${month}${random}`;
  }

  private updateContractEndDateValidation(contractType: EmployeeContractType): void {
    const contractEndDateControl = this.conversionForm.get('contractEndDate');
    
    if (contractType === EmployeeContractType.FIXED_TERM || 
        contractType === EmployeeContractType.INTERNSHIP ||
        contractType === EmployeeContractType.PART_TIME) {
      contractEndDateControl?.setValidators([Validators.required]);
    } else {
      contractEndDateControl?.clearValidators();
    }
    
    contractEndDateControl?.updateValueAndValidity();
  }

  canConvert(): boolean {
    if (!this.application) return false;

    // 只有已审核通过且未转换的申请可以转换为员工
    return this.application.status === ApplicationStatus.APPROVED && !this.application.convertedToEmployee;
  }

  getContractEndDateHint(): string {
    const contractType = this.conversionForm.get('contractType')?.value;
    
    switch (contractType) {
      case EmployeeContractType.NON_FIXED_TERM:
        return '无固定期限合同无需填写结束日期';
      case EmployeeContractType.FIXED_TERM:
        return '固定期限合同必须填写结束日期';
      case EmployeeContractType.INTERNSHIP:
        return '实习合同必须填写结束日期';
      case EmployeeContractType.PART_TIME:
        return '兼职合同必须填写结束日期';
      case EmployeeContractType.PROBATION:
        return '试用期合同建议填写结束日期';
      default:
        return '';
    }
  }

  onDepartmentChange(): void {
    const departmentId = this.conversionForm.get('departmentId')?.value;
    
    // 当部门改变时，过滤职位列表
    if (departmentId) {
      // 这里可以根据部门过滤职位，如果position模型有departmentId字段
      // 暂时不过滤，显示所有职位
    }
    
    // 清空职位选择
    this.conversionForm.patchValue({ positionId: '' });
  }

  previewEmployeeData(): any {
    const formValue = this.conversionForm.value;
    const app = this.application;

    if (!app) {
      throw new Error('Application data is required for employee conversion');
    }

    // Calculate contract duration and ensure contractEndDate is NOT included
    const employeeData = {
      name: app.name,
      employeeNumber: formValue.employeeNumber,
      email: app.email,
      phone: app.phone,
      birthday: app.birthday,
      gender: app.gender,
      ethnicity: app.ethnicity,
      idType: app.idType,
      idNumber: app.idNumber || '',
      emergencyContactName: app.emergencyContactName,
      emergencyContactPhone: app.emergencyContactPhone,
      departmentId: formValue.departmentId,
      positionId: formValue.positionId,
      contractType: formValue.contractType,
      contractStartDate: formValue.contractStartDate,
      contractDuration: this.calculateContractDuration(formValue.contractStartDate, formValue.contractEndDate),
      hireDate: formValue.hireDate,
      status: EmployeeStatus.ACTIVE,
      roles: formValue.roles || [],
      // 转换EmployeeApplication字符串数据为Employee所需的结构化数组
      educations: this.parseEducationBackground(app.educationBackground),
      jobHistories: this.parseWorkExperience(app.workExperience)
      // 注意：certifications暂时不支持，因为后端EmployeeDTO中没有certifications字段
    };

    // Explicitly remove contractEndDate if it somehow got included
    delete (employeeData as any).contractEndDate;

    return employeeData;
  }

  private parseEducationBackground(educationText?: string): any[] {
    if (!educationText || educationText.trim() === '') {
      return [];
    }

    // 尝试解析格式化的教育背景文本
    // 格式：School: XXX, Degree: XXX, Major: XXX, Period: XXX to XXX
    const educations = educationText.split('\n\n').map(section => {
      const education: any = {};

      const schoolMatch = section.match(/School:\s*([^,\n]+)/);
      if (schoolMatch) education.school = schoolMatch[1];

      const degreeMatch = section.match(/Degree:\s*([^,\n]+)/);
      if (degreeMatch) education.degree = degreeMatch[1];

      const majorMatch = section.match(/Major:\s*([^,\n]+)/);
      if (majorMatch) education.major = majorMatch[1];

      const timeMatch = section.match(/Period:\s*([^,\n]+)\s*to\s*([^,\n]+)/);
      if (timeMatch) {
        education.startDate = this.formatDateForBackend(timeMatch[1]);
        education.endDate = this.formatDateForBackend(timeMatch[2]);
      }

      const remarksMatch = section.match(/Remarks:\s*([^,\n]+)/);
      if (remarksMatch) education.remarks = remarksMatch[1];

      return education;
    }).filter(edu => edu.school || edu.degree || edu.major);

    return educations;
  }

  private parseWorkExperience(workText?: string): any[] {
    if (!workText || workText.trim() === '') {
      return [];
    }

    // 尝试解析格式化的工作经历文本
    // 格式：Company: XXX, Position: XXX, Department: XXX, Period: XXX to XXX
    const jobHistories = workText.split('\n\n').map(section => {
      const job: any = {};

      const companyMatch = section.match(/Company:\s*([^,\n]+)/);
      if (companyMatch) job.companyName = companyMatch[1];

      const positionMatch = section.match(/Position:\s*([^,\n]+)/);
      if (positionMatch) job.position = positionMatch[1];

      const deptMatch = section.match(/Department:\s*([^,\n]+)/);
      if (deptMatch) job.department = deptMatch[1];

      const timeMatch = section.match(/Period:\s*([^,\n]+)\s*to\s*([^,\n]+)/);
      if (timeMatch) {
        job.startDate = this.formatDateForBackend(timeMatch[1]);
        job.endDate = timeMatch[2] === 'Present' ? null : this.formatDateForBackend(timeMatch[2]);
      }

      const descMatch = section.match(/Description:\s*([^,\n]+)/);
      if (descMatch) job.jobDescription = descMatch[1];

      const achieveMatch = section.match(/Achievements:\s*([^,\n]+)/);
      if (achieveMatch) job.achievements = achieveMatch[1];

      const reasonMatch = section.match(/Leaving Reason:\s*([^,\n]+)/);
      if (reasonMatch) job.leavingReason = reasonMatch[1];

      const remarksMatch = section.match(/Remarks:\s*([^,\n]+)/);
      if (remarksMatch) job.remarks = remarksMatch[1];

      return job;
    }).filter(job => job.companyName || job.position);

    return jobHistories;
  }

  private formatDateForBackend(dateStr: string): string | null {
    if (!dateStr || dateStr.trim() === '') {
      return null;
    }

    try {
      // 尝试解析各种可能的日期格式
      let date: Date;

      // 处理 YYYY-MM-DD 格式
      if (/^\d{4}-\d{2}-\d{2}$/.test(dateStr)) {
        date = new Date(dateStr);
      }
      // 处理 YYYY/MM/DD 格式
      else if (/^\d{4}\/\d{2}\/\d{2}$/.test(dateStr)) {
        date = new Date(dateStr.replace(/\//g, '-'));
      }
      // 处理 YYYY年MM月DD日 格式
      else if (/^\d{4}年\d{1,2}月\d{1,2}日$/.test(dateStr)) {
        const match = dateStr.match(/^(\d{4})年(\d{1,2})月(\d{1,2})日$/);
        if (match) {
          const year = match[1];
          const month = match[2].padStart(2, '0');
          const day = match[3].padStart(2, '0');
          date = new Date(`${year}-${month}-${day}`);
        } else {
          return null;
        }
      }
      // 处理 YYYY.MM.DD 格式
      else if (/^\d{4}\.\d{2}\.\d{2}$/.test(dateStr)) {
        date = new Date(dateStr.replace(/\./g, '-'));
      }
      // 其他格式尝试直接解析
      else {
        date = new Date(dateStr);
      }

      // 检查日期是否有效
      if (isNaN(date.getTime())) {
        console.warn('Invalid date format:', dateStr);
        return null;
      }

      // 返回 YYYY-MM-DD 格式的字符串，后端会自动转换为 LocalDate
      return date.toISOString().split('T')[0];
    } catch (error) {
      console.error('Error parsing date:', dateStr, error);
      return null;
    }
  }

  submitConversion(): void {
    if (this.conversionForm.invalid) {
      this.markFormGroupTouched(this.conversionForm);
      return;
    }

    this.submitting = true;
    
    const employeeData = this.previewEmployeeData();

    this.employeeApplicationService.approveAndConvertToEmployee(this.applicationId, employeeData).subscribe({
      next: (response) => {
        if (response.code === 200) {
          this.snackBar.open(`申请已成功转换为员工记录！员工编号：${response.data.employeeNumber}`, '关闭', {
            duration: 5000,
            panelClass: ['success-snackbar']
          });

          // 重新加载申请数据以获取最新的转换状态
          this.reloadApplicationData(response.data);
        } else {
          this.snackBar.open('转换失败：' + response.message, '关闭', {
            duration: 3000,
            panelClass: ['error-snackbar']
          });
          this.submitting = false;
        }
      },
      error: (error) => {
        console.error('转换员工失败:', error);
        this.snackBar.open('转换失败，请重试', '关闭', { 
          duration: 3000,
          panelClass: ['error-snackbar']
        });
        this.submitting = false;
      }
    });
  }

  private markFormGroupTouched(formGroup: FormGroup): void {
    Object.keys(formGroup.controls).forEach(key => {
      const control = formGroup.get(key);
      control?.markAsTouched();
    });
  }

  private reloadApplicationData(_employeeData: any): void {
    // 重新加载申请数据以获取最新状态
    this.employeeApplicationService.getApplication(this.applicationId).subscribe({
      next: (response) => {
        if (response.code === 200) {
          this.application = response.data;
          console.log('Application reloaded after conversion:', this.application);

          // 延迟跳转，让用户看到转换状态变化
          setTimeout(() => {
            this.goBack();
          }, 3000);
        } else {
          console.error('Failed to reload application data:', response.message);
          // 即使重新加载失败，也要跳转回去
          setTimeout(() => {
            this.goBack();
          }, 2000);
        }
        this.submitting = false;
      },
      error: (error) => {
        console.error('Error reloading application data:', error);
        // 即使重新加载失败，也要跳转回去
        setTimeout(() => {
          this.goBack();
        }, 2000);
        this.submitting = false;
      }
    });
  }

  generateNewEmployeeNumber(): void {
    this.generateEmployeeNumber();
    this.snackBar.open('正在生成新的员工编号...', '关闭', { duration: 2000 });
  }

  goBack(): void {
    this.router.navigate(['/employee-application/hr/list']);
  }

  formatDate(dateString: string | undefined): string {
    if (!dateString) return '-';
    
    const date = new Date(dateString);
    return date.toLocaleDateString('zh-CN', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit'
    });
  }

  getStatusColor(status: string | undefined): string {
    switch (status) {
      case 'APPROVED':
        return '#4caf50';
      default:
        return '#666';
    }
  }

  // Helper methods for template
  getDepartmentName(departmentId: number): string {
    const department = this.departments.find(d => d.id === departmentId);
    return department?.name || '-';
  }

  getPositionName(positionId: number): string {
    const position = this.positions.find(p => p.id === positionId);
    return position?.name || '-';
  }

  getContractTypeLabel(contractType: EmployeeContractType): string {
    return EmployeeContractTypeLabels[contractType] || '-';
  }

  getFormValue(controlName: string): any {
    return this.conversionForm.get(controlName)?.value;
  }

  getApplicationStatusLabel(): string {
    if (!this.application?.status) {
      return '-';
    }
    return ApplicationStatusLabels[this.application.status] || '-';
  }

  getConversionStatusLabel(): string {
    if (!this.application) {
      return '-';
    }

    if (this.application.convertedToEmployee) {
      return `已转换为员工 (ID: ${this.application.convertedEmployeeId})`;
    }

    return '未转换';
  }

  isAlreadyConverted(): boolean {
    console.log(this.application?.convertedToEmployee);
    return this.application?.convertedToEmployee === true;
  }

  calculateContractDuration(startDate: Date | string, endDate: Date | string): number | null {
    if (!startDate || !endDate) {
      return null;
    }

    const start = new Date(startDate);
    const end = new Date(endDate);
    
    if (start >= end) {
      return null;
    }

    // 计算月份差
    const yearDiff = end.getFullYear() - start.getFullYear();
    const monthDiff = end.getMonth() - start.getMonth();
    const dayDiff = end.getDate() - start.getDate();
    
    let totalMonths = yearDiff * 12 + monthDiff;
    
    // 如果结束日期的日期小于开始日期的日期，说明不足一个月
    if (dayDiff < 0) {
      totalMonths -= 1;
    }
    
    return Math.max(0, totalMonths);
  }

  getSelectedRolesLabel(): string {
    const selectedRoleIds = this.conversionForm.get('roles')?.value || [];
    if (selectedRoleIds.length === 0) {
      return '-';
    }
    
    const selectedRoles = this.roles.filter(role => selectedRoleIds.includes(role.id));
    return selectedRoles.map(role => role.description || role.name).join(', ');
  }
}