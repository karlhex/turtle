import { Component, OnInit, AfterViewInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { EmployeeApplicationService } from '../../../services/employee-application.service';

interface PendingTask {
  id: string;
  name: string;
  applicationId: number;
  applicantName: string;
  applicantEmail: string;
  createTime: Date;
  stepType: string;
}

@Component({
  selector: 'app-pending-approvals',
  templateUrl: './pending-approvals.component.html',
  styleUrls: ['./pending-approvals.component.scss']
})
export class PendingApprovalsComponent implements OnInit, AfterViewInit {

  displayedColumns: string[] = [
    'applicationId', 'applicantName', 'applicantEmail', 
    'stepType', 'createTime', 'actions'
  ];

  dataSource = new MatTableDataSource<PendingTask>();
  loading = true;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(
    private employeeApplicationService: EmployeeApplicationService,
    private snackBar: MatSnackBar,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.loadPendingTasks();
  }

  ngAfterViewInit(): void {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  loadPendingTasks(): void {
    this.loading = true;

    this.employeeApplicationService.getMyPendingTasks().subscribe({
      next: (response) => {
        if (response.code === 200) {
          const tasks: PendingTask[] = (response.data || []).map(task => ({
            id: task.id,
            name: task.name,
            applicationId: task.applicationId || 0,
            applicantName: task.applicantName || '',
            applicantEmail: task.applicantEmail || '',
            createTime: new Date(task.createTime || new Date()),
            stepType: this.getStepTypeLabel(task.name)
          }));
          this.dataSource.data = tasks;
        } else {
          this.snackBar.open('加载失败：' + response.message, '关闭', { duration: 3000 });
        }
        this.loading = false;
      },
      error: (error) => {
        console.error('加载待办任务失败:', error);
        this.snackBar.open('加载失败，请重试', '关闭', { duration: 3000 });
        this.loading = false;
      }
    });
  }

  getStepTypeLabel(taskName: string): string {
    if (!taskName) return '未知';
    
    if (taskName.includes('HR') || taskName.includes('初审')) {
      return 'HR初审';
    } else if (taskName.includes('部门') || taskName.includes('经理')) {
      return '部门审批';
    } else if (taskName.includes('总经理') || taskName.includes('总')) {
      return '总经理审批';
    }
    
    return taskName;
  }

  processApproval(task: PendingTask): void {
    if (task.applicationId) {
      this.router.navigate(['/employee-application/hr/review', task.applicationId]);
    } else {
      this.snackBar.open('无法处理该审批任务', '关闭', { duration: 3000 });
    }
  }

  viewApplication(task: PendingTask): void {
    if (task.applicationId) {
      this.router.navigate(['/employee-application/hr/view', task.applicationId]);
    } else {
      this.snackBar.open('无法查看该申请', '关闭', { duration: 3000 });
    }
  }

  refresh(): void {
    this.loadPendingTasks();
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
}