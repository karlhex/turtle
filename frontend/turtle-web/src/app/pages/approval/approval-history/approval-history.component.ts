import { Component, OnInit, AfterViewInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute } from '@angular/router';
import { EmployeeApplicationService } from '../../../services/employee-application.service';

interface ApprovalRecord {
  taskName: string;
  assigneeName: string;
  startTime: Date;
  endTime?: Date;
  duration: string;
  decision: string;
  comments: string;
  stepType: string;
  applicationId: number;
}

@Component({
  selector: 'app-approval-history',
  templateUrl: './approval-history.component.html',
  styleUrls: ['./approval-history.component.scss']
})
export class ApprovalHistoryComponent implements OnInit, AfterViewInit {

  displayedColumns: string[] = [
    'stepType', 'assigneeName', 'startTime', 'endTime', 
    'duration', 'decision', 'comments'
  ];

  dataSource = new MatTableDataSource<ApprovalRecord>();
  loading = true;
  applicationId?: number;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(
    private employeeApplicationService: EmployeeApplicationService,
    private snackBar: MatSnackBar,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    // 如果有applicationId参数，显示单个申请的历史
    this.route.queryParams.subscribe(params => {
      if (params['applicationId']) {
        this.applicationId = +params['applicationId'];
        this.loadApprovalHistory(this.applicationId);
      } else {
        // 显示所有审批历史（可以后续扩展）
        this.loadAllApprovalHistory();
      }
    });
  }

  ngAfterViewInit(): void {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  loadApprovalHistory(applicationId: number): void {
    this.loading = true;

    this.employeeApplicationService.getApprovalHistory(applicationId).subscribe({
      next: (response) => {
        if (response.code === 200) {
          const records: ApprovalRecord[] = (response.data || []).map(record => ({
            taskName: record.taskName || '',
            assigneeName: record.assigneeName || record.assignee || '',
            startTime: new Date(record.startTime || new Date()),
            endTime: record.endTime ? new Date(record.endTime) : undefined,
            duration: record.formattedDuration || this.formatDuration(record.durationInMillis),
            decision: this.getDecisionLabel(record.decision),
            comments: record.comments || '-',
            stepType: this.mapStepType(record.stepType),
            applicationId: record.applicationId || applicationId
          }));
          this.dataSource.data = records;
        } else {
          this.snackBar.open('加载失败：' + response.message, '关闭', { duration: 3000 });
        }
        this.loading = false;
      },
      error: (error) => {
        console.error('加载审批历史失败:', error);
        this.snackBar.open('加载失败，请重试', '关闭', { duration: 3000 });
        this.loading = false;
      }
    });
  }

  loadAllApprovalHistory(): void {
    // 暂时显示空状态，后续可以扩展显示所有审批历史
    this.loading = false;
    this.dataSource.data = [];
  }

  mapStepType(stepType: string): string {
    if (!stepType) return '未知步骤';
    
    switch (stepType) {
      case 'HR_REVIEW':
        return 'HR初审';
      case 'DEPT_APPROVAL':
        return '部门审批';
      case 'GM_APPROVAL':
        return '总经理审批';
      case 'SUPPLEMENTARY_WAIT':
        return '等待补充资料';
      default:
        return stepType;
    }
  }

  getDecisionLabel(decision: string): string {
    if (!decision) return '处理中';
    
    switch (decision.toUpperCase()) {
      case 'APPROVED':
        return '批准';
      case 'REJECTED':
        return '拒绝';
      case 'SUPPLEMENTARY_REQUIRED':
        return '要求补充资料';
      case 'PENDING':
        return '待处理';
      default:
        return decision;
    }
  }

  formatDuration(durationInMillis?: number): string {
    if (!durationInMillis) {
      return '-';
    }
    
    const seconds = Math.floor(durationInMillis / 1000);
    const minutes = Math.floor(seconds / 60);
    const hours = Math.floor(minutes / 60);
    const days = Math.floor(hours / 24);
    
    if (days > 0) {
      return `${days}天${hours % 24}小时`;
    } else if (hours > 0) {
      return `${hours}小时${minutes % 60}分钟`;
    } else if (minutes > 0) {
      return `${minutes}分钟`;
    } else {
      return `${seconds}秒`;
    }
  }

  formatDate(date?: Date): string {
    if (!date) return '-';
    
    return new Date(date).toLocaleString('zh-CN', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit'
    });
  }

  getDecisionClass(decision: string): string {
    switch (decision) {
      case '批准':
        return 'decision-approved';
      case '拒绝':
        return 'decision-rejected';
      case '要求补充资料':
        return 'decision-supplementary';
      case '待处理':
      case '处理中':
        return 'decision-pending';
      default:
        return '';
    }
  }

  getStepClass(stepType: string): string {
    switch (stepType) {
      case 'HR初审':
        return 'step-hr';
      case '部门审批':
        return 'step-dept';
      case '总经理审批':
        return 'step-gm';
      default:
        return 'step-default';
    }
  }

  applyFilter(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  refresh(): void {
    if (this.applicationId) {
      this.loadApprovalHistory(this.applicationId);
    } else {
      this.loadAllApprovalHistory();
    }
  }
}