import { Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { EmployeeApplicationService } from '../../services/employee-application.service';
import { 
  EmployeeApplication, 
  ApplicationStatusLabels, 
  GenderLabels, 
  IdTypeLabels, 
  EmployeeContractTypeLabels,
  ApplicationStatus 
} from '../../models/employee-application.model';

@Component({
  selector: 'app-application-review',
  templateUrl: './application-review.component.html',
  styleUrls: ['./application-review.component.scss']
})
export class ApplicationReviewComponent implements OnInit {
  @ViewChild('confirmDialog') confirmDialog!: TemplateRef<any>;

  application?: EmployeeApplication;
  reviewForm: FormGroup;
  loading = true;
  submitting = false;
  applicationId!: number;

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
    private employeeApplicationService: EmployeeApplicationService
  ) {
    this.reviewForm = this.fb.group({
      reviewDecision: ['', [Validators.required]],
      reviewComments: ['']
    });

    // 监听审核决定变化，动态添加验证
    this.reviewForm.get('reviewDecision')?.valueChanges.subscribe(() => {
      this.updateCommentsValidation();
    });
  }

  ngOnInit(): void {
    this.applicationId = Number(this.route.snapshot.paramMap.get('id'));
    this.loadApplication();
  }

  loadApplication(): void {
    this.loading = true;

    this.employeeApplicationService.getApplication(this.applicationId).subscribe({
      next: (response) => {
        if (response.code === 200) {
          this.application = response.data;
          // 检查是否可以审核
          console.log(this.application);
          if (!this.canReview()) {
            this.snackBar.open('该申请当前状态不允许审核', '关闭', { duration: 3000 });
            this.goBack();
          }
        } else {
          this.snackBar.open('加载失败：' + response.message, '关闭', { duration: 3000 });
          this.goBack();
        }
        this.loading = false;
      },
      error: (error) => {
        console.error('加载申请详情失败:', error);
        this.snackBar.open('加载失败，请重试', '关闭', { duration: 3000 });
        this.loading = false;
        this.goBack();
      }
    });
  }

  canReview(): boolean {
    if (!this.application) return false;
    
    // 只有待审批或审核中的申请可以审核
    return this.application.status === ApplicationStatus.PENDING || 
           this.application.status === ApplicationStatus.UNDER_REVIEW;
  }

  requireComments(): boolean {
    const decision = this.reviewForm.get('reviewDecision')?.value;
    // 拒绝或审核中时必须填写意见
    return decision === 'REJECTED' || decision === 'UNDER_REVIEW';
  }

  updateCommentsValidation(): void {
    const commentsControl = this.reviewForm.get('reviewComments');
    
    if (this.requireComments()) {
      commentsControl?.setValidators([Validators.required]);
    } else {
      commentsControl?.clearValidators();
    }
    
    commentsControl?.updateValueAndValidity();
  }

  getReviewHint(): string {
    const decision = this.reviewForm.get('reviewDecision')?.value;
    
    switch (decision) {
      case 'VALIDATED':
        return '可选：添加批准理由或欢迎词';
      case 'APPROVED':
        return '可选：添加最终批准意见';
      case 'REJECTED':
        return '必填：请说明拒绝原因';
      case 'UNDER_REVIEW':
        return '必填：请明确说明需要补充的资料或继续审核的原因';
      default:
        return '请先选择审核决定';
    }
  }

  getCommentsErrorMessage(): string {
    const decision = this.reviewForm.get('reviewDecision')?.value;
    
    switch (decision) {
      case 'REJECTED':
        return '请说明拒绝原因';
      case 'UNDER_REVIEW':
        return '请说明需要补充的资料或继续审核的原因';
      default:
        return '请输入审核意见';
    }
  }

  getActionText(): string {
    const decision = this.reviewForm.get('reviewDecision')?.value;
    
    switch (decision) {
      case 'VALIDATED':
        return 'HR总监审批';
      case 'APPROVED':
        return '最终批准';
      case 'REJECTED':
        return '拒绝';
      case 'UNDER_REVIEW':
        return '转入审核中';
      default:
        return '处理';
    }
  }

  submitReview(): void {
    if (this.reviewForm.invalid) {
      this.markFormGroupTouched(this.reviewForm);
      return;
    }

    // 打开确认对话框
    const dialogRef = this.dialog.open(this.confirmDialog, {
      width: '400px',
      disableClose: true
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result === true) {
        this.performReview();
      }
    });
  }

  private performReview(): void {
    if (!this.application) return;

    this.submitting = true;
    const formValue = this.reviewForm.value;

    const reviewData = {
      applicationId: this.applicationId,
      status: formValue.reviewDecision as ApplicationStatus,
      reviewComments: formValue.reviewComments?.trim() || null
    };

    this.employeeApplicationService.reviewApplication(reviewData).subscribe({
      next: (response) => {
        if (response.code === 200) {
          const actionText = this.getActionText();
          this.snackBar.open(`申请${actionText}成功！`, '关闭', { 
            duration: 3000,
            panelClass: ['success-snackbar']
          });
          this.goBack();
        } else {
          this.snackBar.open('操作失败：' + response.message, '关闭', { 
            duration: 3000,
            panelClass: ['error-snackbar']
          });
        }
        this.submitting = false;
      },
      error: (error) => {
        console.error('审核申请失败:', error);
        this.snackBar.open('操作失败，请重试', '关闭', { 
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

  viewFullDetails(): void {
    this.router.navigate(['/employee-application/hr/view', this.applicationId]);
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
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit'
    });
  }

  getStatusColor(status: string | undefined): string {
    switch (status) {
      case 'PENDING':
        return '#1976d2';
      case 'VALIDATED':
        return '#2196f3';
      case 'UNDER_REVIEW':
        return '#ff9800';
      case 'APPROVED':
        return '#4caf50';
      case 'REJECTED':
        return '#f44336';
      default:
        return '#666';
    }
  }
}