import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatDialog } from '@angular/material/dialog';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { EmployeeApplicationService } from '../../../services/employee-application.service';
import { ApprovalService } from '../../../services/approval.service';
import { ConfirmDialogComponent } from '../../../components/confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-approval-task',
  templateUrl: './approval-task.component.html',
  styleUrls: ['./approval-task.component.scss']
})
export class ApprovalTaskComponent implements OnInit {

  applicationId: number = 0;
  requestType: string = '';
  applicationData: any = null;
  approvalHistory: any[] = [];
  currentTask: any = null;
  loading = true;
  processing = false;

  approvalForm: FormGroup;

  decisionOptions = [
    { value: 'APPROVED', label: '批准', color: 'primary' },
    { value: 'REJECTED', label: '拒绝', color: 'warn' },
    { value: 'SUPPLEMENTARY_REQUIRED', label: '需要补充资料', color: 'accent' }
  ];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private fb: FormBuilder,
    private employeeApplicationService: EmployeeApplicationService,
    private approvalService: ApprovalService,
    private snackBar: MatSnackBar,
    private dialog: MatDialog
  ) {
    this.approvalForm = this.fb.group({
      decision: ['', Validators.required],
      comments: ['']
    });
  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.applicationId = +params['id'];
    });

    this.route.queryParams.subscribe(params => {
      this.requestType = params['type'] || 'EMPLOYEE_APPLICATION';
    });

    this.loadApplicationData();
    this.loadApprovalHistory();
    this.loadCurrentTask();
  }

  loadApplicationData(): void {
    if (this.requestType === 'EMPLOYEE_APPLICATION') {
      this.employeeApplicationService.getApplication(this.applicationId).subscribe({
        next: (response) => {
          if (response.code === 200) {
            this.applicationData = response.data;
          } else {
            this.snackBar.open('加载申请数据失败：' + response.message, '关闭', { duration: 3000 });
          }
        },
        error: (error) => {
          console.error('加载申请数据失败:', error);
          this.snackBar.open('加载申请数据失败，请重试', '关闭', { duration: 3000 });
        }
      });
    }
  }

  loadApprovalHistory(): void {
    if (this.requestType === 'EMPLOYEE_APPLICATION') {
      this.employeeApplicationService.getApprovalHistory(this.applicationId).subscribe({
        next: (response) => {
          if (response.code === 200) {
            this.approvalHistory = response.data || [];
          }
        },
        error: (error) => {
          console.error('加载审批历史失败:', error);
        }
      });
    }
  }

  loadCurrentTask(): void {
    if (this.requestType === 'EMPLOYEE_APPLICATION') {
      this.employeeApplicationService.getCurrentApprovalTask(this.applicationId).subscribe({
        next: (response) => {
          if (response.code === 200) {
            this.currentTask = response.data;
          }
          this.loading = false;
        },
        error: (error) => {
          console.error('加载当前任务失败:', error);
          this.loading = false;
        }
      });
    }
  }

  processApproval(): void {
    if (this.approvalForm.invalid) {
      this.snackBar.open('请填写完整的审批信息', '关闭', { duration: 3000 });
      return;
    }

    const formData = this.approvalForm.value;
    
    // 确认对话框
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      width: '400px',
      data: {
        title: '确认审批',
        message: `确定要${this.getDecisionLabel(formData.decision)}这个申请吗？`,
        confirmText: '确认',
        cancelText: '取消'
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.submitApproval(formData);
      }
    });
  }

  private submitApproval(formData: any): void {
    this.processing = true;

    if (this.requestType === 'EMPLOYEE_APPLICATION') {
      // 使用员工申请的审核API
      const reviewData = {
        applicationId: this.applicationId,
        status: formData.decision,
        reviewComments: formData.comments
      };

      this.employeeApplicationService.reviewApplication(reviewData).subscribe({
        next: (response) => {
          if (response.code === 200) {
            this.snackBar.open('审批处理成功', '关闭', { duration: 3000 });
            this.router.navigate(['/approval/list']);
          } else {
            this.snackBar.open('审批处理失败：' + response.message, '关闭', { duration: 3000 });
          }
          this.processing = false;
        },
        error: (error) => {
          console.error('审批处理失败:', error);
          this.snackBar.open('审批处理失败，请重试', '关闭', { duration: 3000 });
          this.processing = false;
        }
      });
    } else {
      // 使用统一的审批API
      this.approvalService.processApprovalRequest(
        this.applicationId, 
        formData.decision, 
        formData.comments
      ).subscribe({
        next: (response) => {
          if (response.code === 200) {
            this.snackBar.open('审批处理成功', '关闭', { duration: 3000 });
            this.router.navigate(['/approval/list']);
          } else {
            this.snackBar.open('审批处理失败：' + response.message, '关闭', { duration: 3000 });
          }
          this.processing = false;
        },
        error: (error) => {
          console.error('审批处理失败:', error);
          this.snackBar.open('审批处理失败，请重试', '关闭', { duration: 3000 });
          this.processing = false;
        }
      });
    }
  }

  getDecisionLabel(decision: string): string {
    const option = this.decisionOptions.find(opt => opt.value === decision);
    return option ? option.label : decision;
  }

  goBack(): void {
    this.router.navigate(['/approval/list']);
  }

  formatDate(dateString: string): string {
    if (!dateString) return '-';
    
    return new Date(dateString).toLocaleString('zh-CN', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit'
    });
  }

  getStatusLabel(status: string): string {
    switch (status) {
      case 'SUBMITTED':
        return '已提交';
      case 'UNDER_REVIEW':
        return '审核中';
      case 'VALIDATED':
        return '已验证';
      case 'APPROVED':
        return '已批准';
      case 'REJECTED':
        return '已拒绝';
      case 'SUPPLEMENTARY_REQUIRED':
        return '需要补充资料';
      default:
        return status;
    }
  }

  getStatusClass(status: string): string {
    switch (status) {
      case 'APPROVED':
        return 'status-approved';
      case 'REJECTED':
        return 'status-rejected';
      case 'SUPPLEMENTARY_REQUIRED':
        return 'status-warning';
      default:
        return 'status-pending';
    }
  }
}