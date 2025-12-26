import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { EmployeeApplicationService } from '../../services/employee-application.service';
import { EmployeeApplication, ApplicationStatus, ApplicationStatusLabels } from '../../models/employee-application.model';
import { ApplicationStatistics } from '../../models/application-statistics.model';

@Component({
  selector: 'app-hr-application-list',
  templateUrl: './hr-application-list.component.html',
  styleUrls: ['./hr-application-list.component.scss']
})
export class HrApplicationListComponent implements OnInit {

  displayedColumns: string[] = [
    'id', 'name', 'email', 'desiredPosition', 'expectedSalary',
    'status', 'submittedAt', 'actions'
  ];

  dataSource = new MatTableDataSource<EmployeeApplication>();
  loading = true;
  statistics?: ApplicationStatistics;

  // Filter options
  statusFilter: ApplicationStatus | 'ALL' = 'ALL';
  statusOptions = [
    { value: 'ALL', label: '全部状态' },
    { value: ApplicationStatus.PENDING, label: ApplicationStatusLabels[ApplicationStatus.PENDING] },
    { value: ApplicationStatus.UNDER_REVIEW, label: ApplicationStatusLabels[ApplicationStatus.UNDER_REVIEW] },
    { value: ApplicationStatus.APPROVED, label: ApplicationStatusLabels[ApplicationStatus.APPROVED] },
    { value: ApplicationStatus.REJECTED, label: ApplicationStatusLabels[ApplicationStatus.REJECTED] },
    { value: ApplicationStatus.UNDER_REVIEW, label: ApplicationStatusLabels[ApplicationStatus.UNDER_REVIEW] }
  ];

  ApplicationStatusLabels = ApplicationStatusLabels;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(
    private employeeApplicationService: EmployeeApplicationService,
    private snackBar: MatSnackBar,
    private dialog: MatDialog,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.loadApplications();
    this.loadStatistics();
  }

  ngAfterViewInit(): void {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  loadApplications(): void {
    this.loading = true;

    const loadFunction = this.statusFilter === 'ALL'
      ? this.employeeApplicationService.getAllApplications()
      : this.employeeApplicationService.getApplicationsByStatus(this.statusFilter as ApplicationStatus);

    loadFunction.subscribe({
      next: (response) => {
        if (response.code === 200) {
          // Handle paginated response
          const applications = response.data?.content || response.data || [];
          this.dataSource.data = applications;
        } else {
          this.snackBar.open('加载失败：' + response.message, '关闭', { duration: 3000 });
        }
        this.loading = false;
      },
      error: (error) => {
        console.error('加载申请列表失败:', error);
        this.snackBar.open('加载失败，请重试', '关闭', { duration: 3000 });
        this.loading = false;
      }
    });
  }

  loadStatistics(): void {
    this.employeeApplicationService.getApplicationStatistics().subscribe({
      next: (response) => {
        if (response.code === 200) {
          this.statistics = response.data;
        }
      },
      error: (error) => {
        console.error('加载统计信息失败:', error);
      }
    });
  }

  onStatusFilterChange(): void {
    this.loadApplications();
  }

  applyFilter(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  viewApplication(application: EmployeeApplication): void {
    this.router.navigate(['/employee-application/hr/view', application.id]);
  }

  reviewApplication(application: EmployeeApplication): void {
    this.router.navigate(['/employee-application/hr/review', application.id]);
  }

  approveAndConvert(application: EmployeeApplication): void {
    this.router.navigate(['/employee-application/hr/convert', application.id]);
  }

  canReview(application: EmployeeApplication): boolean {
    return application.status === ApplicationStatus.PENDING ||
           application.status === ApplicationStatus.UNDER_REVIEW ;
  }

  canConvert(application: EmployeeApplication): boolean {
    return application.status === ApplicationStatus.APPROVED;
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
    this.loadApplications();
    this.loadStatistics();
  }

  exportApplications(): void {
    // TODO: Implement export functionality
    this.snackBar.open('导出功能开发中...', '关闭', { duration: 2000 });
  }

  getStatusIcon(status: ApplicationStatus | undefined): string {
    switch (status) {
      case ApplicationStatus.PENDING:
        return 'schedule';
      case ApplicationStatus.UNDER_REVIEW:
        return 'rate_review';
      case ApplicationStatus.APPROVED:
        return 'check_circle';
      case ApplicationStatus.REJECTED:
        return 'cancel';
      case ApplicationStatus.UNDER_REVIEW:
        return 'info';
      default:
        return 'help';
    }
  }

  getPendingCount(): number {
    if (!this.statistics) return 0;
    return this.statistics.submittedCount + this.statistics.supplementaryRequiredCount;
  }

  getStatusLabel(status: ApplicationStatus | undefined): string {
    return this.ApplicationStatusLabels[status || ApplicationStatus.PENDING];
  }
}