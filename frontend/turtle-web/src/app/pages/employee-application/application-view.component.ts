import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { EmployeeApplicationService } from '../../services/employee-application.service';
import { AuthService } from '../../services/auth.service';
import { EmployeeApplication, ApplicationStatus, ApplicationStatusLabels, GenderLabels, IdTypeLabels, EmployeeContractTypeLabels } from '../../models/employee-application.model';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-application-view',
  templateUrl: './application-view.component.html',
  styleUrls: ['./application-view.component.scss']
})
export class ApplicationViewComponent implements OnInit {

  application?: EmployeeApplication;
  loading = true;
  applicationId!: number;
  showFullHistory = false; // 控制是否显示完整历史

  // 标签映射
  ApplicationStatusLabels = ApplicationStatusLabels;
  GenderLabels = GenderLabels;
  IdTypeLabels = IdTypeLabels;
  EmployeeContractTypeLabels = EmployeeContractTypeLabels;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private employeeApplicationService: EmployeeApplicationService,
    private authService: AuthService,
    private snackBar: MatSnackBar
  ) { }

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

  goBack(): void {
    // 根据用户角色返回不同页面
    const userRole = this.getCurrentUserRole();
    if (userRole === 'GUEST') {
      this.router.navigate(['/employee-application/my-applications']);
    } else {
      this.router.navigate(['/employee-application/hr/list']);
    }
  }

  editApplication(): void {
    this.router.navigate(['/employee-application/edit', this.applicationId]);
  }

  reviewApplication(): void {
    this.router.navigate(['/employee-application/hr/review', this.applicationId]);
  }

  convertToEmployee(): void {
    this.router.navigate(['/employee-application/hr/convert', this.applicationId]);
  }

  canEdit(): boolean {
    return this.canEditApplication();
  }

  canReview(): boolean {
    return this.canReviewApplication();
  }

  private getCurrentUserRole(): string {
    // Use the enhanced role detection from AuthService
    let userRole = 'GUEST';
    this.authService.getUserRole().subscribe(role => {
      userRole = role;
    });
    return userRole;
  }

  // Enhanced permission checking methods
  canEditApplication(): boolean {
    const userRole = this.getCurrentUserRole();
    
    // System users can always edit
    if (userRole === 'SYSTEM') {
      return true;
    }
    
    // Guest users can only edit their own applications if status allows
    if (userRole === 'GUEST') {
      return this.application?.status === ApplicationStatus.PENDING || 
             this.application?.status === ApplicationStatus.UNDER_REVIEW;
    }
    
    // Employees with HR permissions can edit applications in certain states
    if (userRole === 'EMPLOYEE') {
      return this.application?.status === ApplicationStatus.PENDING || 
             this.application?.status === ApplicationStatus.UNDER_REVIEW || 
             this.application?.status === ApplicationStatus.VALIDATED;
    }
    
    return false;
  }

  canReviewApplication(): boolean {
    const userRole = this.getCurrentUserRole();
    
    // Only system users and employees can review applications
    if (userRole === 'GUEST') {
      return false;
    }
    
    // Check if application is in a reviewable state
    return this.application?.status === ApplicationStatus.PENDING || 
           this.application?.status === ApplicationStatus.UNDER_REVIEW;
  }

  canConvertToEmployee(): boolean {
    const userRole = this.getCurrentUserRole();
    
    // Only system users and employees can convert applications
    if (userRole === 'GUEST') {
      return false;
    }
    
    // Only approved applications can be converted
    return this.application?.status === ApplicationStatus.APPROVED;
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
      case ApplicationStatus.PENDING:
        return '#1976d2';
      case 'UNDER_REVIEW':
        return '#ff9800';
      case 'APPROVED':
        return '#4caf50';
      case 'REJECTED':
        return '#f44336';
      case ApplicationStatus.UNDER_REVIEW:
        return '#9c27b0';
      default:
        return '#666';
    }
  }

  printApplication(): void {
    // TODO: 实现打印功能
    this.snackBar.open('打印功能开发中...', '关闭', { duration: 2000 });
  }

  exportApplication(): void {
    // TODO: 实现导出功能
    this.snackBar.open('导出功能开发中...', '关闭', { duration: 2000 });
  }

  onHistoryModeChange(): void {
    // 时间线组件会自动响应 showFullHistory 的变化并重新加载数据
    console.log('History mode changed to:', this.showFullHistory ? 'Full' : 'Status Only');
  }
}