import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { EmployeeApplicationService } from '../../services/employee-application.service';
import { EmployeeApplication, ApplicationStatusLabels, ApplicationStatus } from '../../models/employee-application.model';

@Component({
  selector: 'app-my-applications',
  templateUrl: './my-applications.component.html',
  styleUrls: ['./my-applications.component.scss']
})
export class MyApplicationsComponent implements OnInit {

  applications: EmployeeApplication[] = [];
  loading = true;
  ApplicationStatusLabels = ApplicationStatusLabels;

  // Table columns
  displayedColumns: string[] = ['id', 'name', 'desiredPosition', 'status', 'submittedAt', 'actions'];

  constructor(
    private employeeApplicationService: EmployeeApplicationService,
    private snackBar: MatSnackBar,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.loadMyApplications();
  }

  loadMyApplications(): void {
    this.loading = true;
    
    this.employeeApplicationService.getMyApplications().subscribe({
      next: (response) => {
        if (response.code === 200) {
          this.applications = response.data || [];
        } else {
          this.snackBar.open('加载失败：' + response.message, '关闭', { duration: 3000 });
        }
        this.loading = false;
      },
      error: (error) => {
        console.error('加载我的申请失败:', error);
        this.snackBar.open('加载失败，请重试', '关闭', { duration: 3000 });
        this.loading = false;
      }
    });
  }

  viewApplication(application: EmployeeApplication): void {
    this.router.navigate(['/employee-application/view', application.id]);
  }

  editApplication(application: EmployeeApplication): void {
    // 只允许编辑未审核或需要补充资料的申请
    if (application.status === ApplicationStatus.PENDING || 
        application.status === ApplicationStatus.UNDER_REVIEW) {
      this.router.navigate(['/employee-application/edit', application.id]);
    } else {
      this.snackBar.open('该申请当前状态不允许编辑', '关闭', { duration: 3000 });
    }
  }

  canEdit(application: EmployeeApplication): boolean {
    return application.status === ApplicationStatus.PENDING || 
           application.status === ApplicationStatus.UNDER_REVIEW;
  }

  getStatusClass(status: ApplicationStatus | undefined): string {
    if (!status) return '';
    
    switch (status) {
      case ApplicationStatus.PENDING:
        return 'status-submitted';
      case ApplicationStatus.UNDER_REVIEW:
        return 'status-under-review';
      case ApplicationStatus.APPROVED:
        return 'status-approved';
      case ApplicationStatus.REJECTED:
        return 'status-rejected';
      case ApplicationStatus.UNDER_REVIEW:
        return 'status-supplementary-required';
      default:
        return '';
    }
  }

  createNewApplication(): void {
    this.router.navigate(['/employee-application/new']);
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

  refresh(): void {
    this.loadMyApplications();
  }

  getStatusLabel(status: ApplicationStatus | undefined): string {
    return this.ApplicationStatusLabels[status || ApplicationStatus.PENDING];
  }
}