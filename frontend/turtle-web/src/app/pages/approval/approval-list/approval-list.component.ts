import { Component, OnInit, AfterViewInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { ApprovalService, ApprovalRequest } from '../../../services/approval.service';
import { EmployeeApplicationService } from '../../../services/employee-application.service';

@Component({
  selector: 'app-approval-list',
  templateUrl: './approval-list.component.html',
  styleUrls: ['./approval-list.component.scss']
})
export class ApprovalListComponent implements OnInit, AfterViewInit {

  displayedColumns: string[] = [
    'id', 'requestType', 'applicantName', 'submitTime', 
    'currentStep', 'status', 'priority', 'actions'
  ];

  dataSource = new MatTableDataSource<ApprovalRequest>();
  loading = true;
  
  // 筛选选项
  requestTypes = [
    { value: 'EMPLOYEE_APPLICATION', label: '员工入职申请' },
    { value: 'REIMBURSEMENT', label: '报销申请' },
    { value: 'CONTRACT', label: '合同审批' },
    { value: 'LEAVE', label: '请假申请' },
    { value: 'OTHER', label: '其他申请' }
  ];

  statusOptions = [
    { value: 'PENDING', label: '待审批' },
    { value: 'IN_PROGRESS', label: '审批中' },
    { value: 'APPROVED', label: '已批准' },
    { value: 'REJECTED', label: '已拒绝' },
    { value: 'CANCELLED', label: '已取消' }
  ];

  priorityOptions = [
    { value: 'HIGH', label: '高' },
    { value: 'MEDIUM', label: '中' },
    { value: 'LOW', label: '低' }
  ];

  selectedRequestType = '';
  selectedStatus = '';
  selectedPriority = '';

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(
    private approvalService: ApprovalService,
    private employeeApplicationService: EmployeeApplicationService,
    private snackBar: MatSnackBar,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.loadApprovalRequests();
  }

  ngAfterViewInit(): void {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  loadApprovalRequests(): void {
    this.loading = true;

    // 优先加载员工申请数据，后续可以扩展其他类型
    this.loadEmployeeApplications();
  }

  private loadEmployeeApplications(): void {
    // 根据筛选条件加载员工申请数据
    let observable;
    if (this.selectedStatus) {
      observable = this.employeeApplicationService.getApplicationsByStatus(this.selectedStatus as any);
    } else {
      observable = this.employeeApplicationService.getAllApplications();
    }

    observable.subscribe({
      next: (response) => {
        if (response.code === 200) {
          const employeeApplications = response.data.content || response.data || [];
          
          // 将员工申请数据转换为统一的审批请求格式
          const approvalRequests: ApprovalRequest[] = employeeApplications.map((app: any) => ({
            id: app.id,
            applicationId: app.id,
            applicantName: app.name,
            applicantEmail: app.email,
            requestType: 'EMPLOYEE_APPLICATION',
            status: this.mapApplicationStatus(app.status),
            submitTime: new Date(app.submittedAt || app.createdAt),
            currentStep: this.getCurrentStep(app.status),
            priority: 'MEDIUM', // 默认优先级，可以根据实际需求调整
            description: `员工入职申请 - ${app.desiredPosition || '未指定职位'}`
          }));

          // 应用筛选
          let filteredData = approvalRequests;
          if (this.selectedRequestType && this.selectedRequestType !== 'EMPLOYEE_APPLICATION') {
            filteredData = []; // 如果选择了其他类型，暂时显示空数据
          }
          if (this.selectedPriority) {
            filteredData = filteredData.filter(item => item.priority === this.selectedPriority);
          }

          this.dataSource.data = filteredData;
        } else {
          this.snackBar.open('加载失败：' + response.message, '关闭', { duration: 3000 });
        }
        this.loading = false;
      },
      error: (error) => {
        console.error('加载审批请求失败:', error);
        this.snackBar.open('加载失败，请重试', '关闭', { duration: 3000 });
        this.loading = false;
      }
    });
  }

  private mapApplicationStatus(status: string): string {
    // 将员工申请状态映射到统一的审批状态
    switch (status) {
      case 'SUBMITTED':
        return 'PENDING';
      case 'UNDER_REVIEW':
      case 'VALIDATED':
        return 'IN_PROGRESS';
      case 'APPROVED':
        return 'APPROVED';
      case 'REJECTED':
        return 'REJECTED';
      case 'SUPPLEMENTARY_REQUIRED':
        return 'PENDING';
      default:
        return 'PENDING';
    }
  }

  private getCurrentStep(status: string): string {
    // 根据状态返回当前步骤描述
    switch (status) {
      case 'SUBMITTED':
        return 'HR初审';
      case 'UNDER_REVIEW':
        return '审核中';
      case 'VALIDATED':
        return '部门审批';
      case 'APPROVED':
        return '已完成';
      case 'REJECTED':
        return '已拒绝';
      case 'SUPPLEMENTARY_REQUIRED':
        return '需要补充资料';
      default:
        return '未知状态';
    }
  }

  processApproval(request: ApprovalRequest): void {
    // 根据请求类型导航到不同的处理页面
    switch (request.requestType) {
      case 'EMPLOYEE_APPLICATION':
        this.processEmployeeApplicationApproval(request);
        break;
      case 'REIMBURSEMENT':
        this.router.navigate(['/reimbursement/approve', request.applicationId]);
        break;
      case 'CONTRACT':
        this.router.navigate(['/contract/approve', request.applicationId]);
        break;
      default:
        this.snackBar.open('暂不支持该类型的审批处理', '关闭', { duration: 3000 });
    }
  }

  private processEmployeeApplicationApproval(request: ApprovalRequest): void {
    // 根据当前步骤决定处理方式
    switch (request.currentStep) {
      case 'HR初审':
      case '审核中':
      case '需要补充资料':
        // 导航到HR审核页面
        this.router.navigate(['/employee-application/hr/review', request.applicationId]);
        break;
      case '部门审批':
        // 导航到部门经理审批页面，或者统一的工作流审批页面
        this.router.navigate(['/approval/task', request.applicationId], {
          queryParams: { type: 'EMPLOYEE_APPLICATION' }
        });
        break;
      case '已完成':
      case '已拒绝':
        // 查看详情页面
        this.viewDetails(request);
        break;
      default:
        this.snackBar.open('未知的审批步骤，无法处理', '关闭', { duration: 3000 });
    }
  }

  viewDetails(request: ApprovalRequest): void {
    // 根据请求类型导航到详情页面
    switch (request.requestType) {
      case 'EMPLOYEE_APPLICATION':
        this.router.navigate(['/employee-application/hr/view', request.applicationId]);
        break;
      case 'REIMBURSEMENT':
        this.router.navigate(['/reimbursement/view', request.applicationId]);
        break;
      case 'CONTRACT':
        this.router.navigate(['/contract/view', request.applicationId]);
        break;
      default:
        this.snackBar.open('暂不支持查看该类型的详情', '关闭', { duration: 3000 });
    }
  }

  viewHistory(request: ApprovalRequest): void {
    // 根据请求类型使用不同的历史查看方式
    switch (request.requestType) {
      case 'EMPLOYEE_APPLICATION':
        // 使用员工申请的审批历史API
        this.router.navigate(['/approval/history'], { 
          queryParams: { 
            applicationId: request.applicationId,
            requestType: request.requestType
          } 
        });
        break;
      default:
        this.router.navigate(['/approval/history'], { 
          queryParams: { 
            applicationId: request.applicationId,
            requestType: request.requestType
          } 
        });
    }
  }

  getRequestTypeLabel(type: string): string {
    const requestType = this.requestTypes.find(t => t.value === type);
    return requestType ? requestType.label : type;
  }

  getStatusLabel(status: string): string {
    const statusOption = this.statusOptions.find(s => s.value === status);
    return statusOption ? statusOption.label : status;
  }

  getStatusClass(status: string): string {
    switch (status) {
      case 'PENDING':
        return 'status-pending';
      case 'IN_PROGRESS':
        return 'status-in-progress';
      case 'APPROVED':
        return 'status-approved';
      case 'REJECTED':
        return 'status-rejected';
      case 'CANCELLED':
        return 'status-cancelled';
      default:
        return '';
    }
  }

  getPriorityLabel(priority: string): string {
    const priorityOption = this.priorityOptions.find(p => p.value === priority);
    return priorityOption ? priorityOption.label : priority;
  }

  getPriorityClass(priority: string): string {
    switch (priority) {
      case 'HIGH':
        return 'priority-high';
      case 'MEDIUM':
        return 'priority-medium';
      case 'LOW':
        return 'priority-low';
      default:
        return '';
    }
  }

  formatDate(date: Date): string {
    if (!date) return '-';
    
    return new Date(date).toLocaleString('zh-CN', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit'
    });
  }

  applyFilter(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  onFilterChange(): void {
    this.loadApprovalRequests();
  }

  refresh(): void {
    this.loadApprovalRequests();
  }

  exportData(): void {
    // 导出数据功能
    this.snackBar.open('导出功能开发中...', '关闭', { duration: 2000 });
  }
}