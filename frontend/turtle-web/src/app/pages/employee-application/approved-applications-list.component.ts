import { Component, OnInit, ViewChild, AfterViewInit } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { EmployeeApplicationService } from '../../services/employee-application.service';
import { 
  EmployeeApplication, 
  ApplicationStatus,
  ApplicationStatusLabels 
} from '../../models/employee-application.model';

@Component({
  selector: 'app-approved-applications-list',
  templateUrl: './approved-applications-list.component.html',
  styleUrls: ['./approved-applications-list.component.scss']
})
export class ApprovedApplicationsListComponent implements OnInit, AfterViewInit {

  displayedColumns: string[] = [
    'id', 'name', 'email', 'desiredPosition', 'submittedAt', 
    'reviewedAt', 'status', 'actions'
  ];
  
  dataSource = new MatTableDataSource<EmployeeApplication>();
  loading = true;
  
  ApplicationStatusLabels = ApplicationStatusLabels;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(
    private employeeApplicationService: EmployeeApplicationService,
    private snackBar: MatSnackBar,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.loadApprovedApplications();
  }

  ngAfterViewInit(): void {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  loadApprovedApplications(): void {
    this.loading = true;
    
    // 加载已批准的申请
    this.employeeApplicationService.getApplicationsByStatus(ApplicationStatus.APPROVED).subscribe({
      next: (response) => {
        if (response.code === 200) {
          // 过滤出还没有转换为员工的申请
          const approvedApplications = (response.data.content || response.data || [])
            .filter((app: any) => 
              app.status === ApplicationStatus.APPROVED && !app.convertedToEmployee
            );
          
          this.dataSource.data = approvedApplications;
        } else {
          this.snackBar.open('加载失败：' + response.message, '关闭', { duration: 3000 });
        }
        this.loading = false;
      },
      error: (error) => {
        console.error('加载已批准申请失败:', error);
        this.snackBar.open('加载失败，请重试', '关闭', { duration: 3000 });
        this.loading = false;
      }
    });
  }

  convertToEmployee(application: EmployeeApplication): void {
    // 导航到员工转换页面
    this.router.navigate(['/employee-application/hr/convert', application.id]);
  }

  viewApplication(application: EmployeeApplication): void {
    // 查看申请详情
    this.router.navigate(['/employee-application/hr/view', application.id]);
  }

  getStatusClass(status: ApplicationStatus): string {
    switch (status) {
      case ApplicationStatus.APPROVED:
        return 'status-approved';
      case ApplicationStatus.REJECTED:
        return 'status-rejected';
      case ApplicationStatus.PENDING:
        return 'status-submitted';
      case ApplicationStatus.UNDER_REVIEW:
        return 'status-under-review';
      case ApplicationStatus.UNDER_REVIEW:
        return 'status-supplementary';
      default:
        return '';
    }
  }

  formatDate(dateString: string | Date): string {
    if (!dateString) return '-';
    
    return new Date(dateString).toLocaleString('zh-CN', {
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

  refresh(): void {
    this.loadApprovedApplications();
  }

  getStatusLabel(status: string): string {
    return this.ApplicationStatusLabels[status as keyof typeof this.ApplicationStatusLabels] || status;
  }
}