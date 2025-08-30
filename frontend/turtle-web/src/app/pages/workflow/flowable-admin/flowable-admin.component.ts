import { Component, OnInit } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatDialog } from '@angular/material/dialog';
import { FlowableService, FlowableProcessDefinition, FlowableProcessInstance, FlowableTask } from '../../../services/flowable.service';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';

@Component({
  selector: 'app-flowable-admin',
  templateUrl: './flowable-admin.component.html',
  styleUrls: ['./flowable-admin.component.scss']
})
export class FlowableAdminComponent implements OnInit {
  selectedTabIndex = 0;
  
  // 流程定义相关
  processDefinitionsDataSource = new MatTableDataSource<FlowableProcessDefinition>();
  processDefinitionsColumns: string[] = ['key', 'name', 'version', 'deploymentId', 'suspended', 'actions'];
  
  // 流程实例相关
  processInstancesDataSource = new MatTableDataSource<FlowableProcessInstance>();
  processInstancesColumns: string[] = ['id', 'processDefinitionKey', 'businessKey', 'startTime', 'startUserId', 'suspended', 'actions'];
  
  // 任务相关
  tasksDataSource = new MatTableDataSource<FlowableTask>();
  tasksColumns: string[] = ['name', 'assignee', 'candidateGroups', 'createTime', 'processDefinitionKey', 'processInstanceId', 'actions'];
  taskFilterUserId = '';
  
  // 调试信息
  debugInfo: any = null;
  showDebugInfo = false;
  
  // 流程图相关
  showProcessDiagram = false;
  processDiagramUrl: SafeUrl | null = null;
  
  // 当前用户ID - 使用数字ID
  currentUserId = '1'; // TODO: 从认证服务获取实际的数字用户ID

  constructor(
    private flowableService: FlowableService,
    private snackBar: MatSnackBar,
    private dialog: MatDialog,
    private sanitizer: DomSanitizer
  ) {}

  ngOnInit(): void {
    this.refreshAll();
  }

  refreshAll(): void {
    this.refreshProcessDefinitions();
    this.refreshProcessInstances();
    this.refreshTasks();
  }

  // 流程定义相关方法
  refreshProcessDefinitions(): void {
    console.log("refreshProcessDefinitions");
    this.flowableService.getProcessDefinitions().subscribe({
      next: (data) => {
        this.processDefinitionsDataSource.data = data || [];
        this.showSuccess('流程定义列表已刷新');
        console.log('Process definitions data:', data);
      },
      error: (error) => {
        console.error('Failed to load process definitions:', error);
        this.showError('加载流程定义失败');
      }
    });
  }

  viewProcessDefinition(processDefinition: FlowableProcessDefinition): void {
    // TODO: 实现流程定义详情查看
    this.showInfo(`查看流程定义: ${processDefinition.name}`);
  }

  startProcessInstance(processDefinitionKey: string): void {
    // 创建一个简单的对话框来收集启动参数
    const businessKey = `test_${Date.now()}`;
    const variables = {
      amount: 1000,
      submitterId: parseInt(this.currentUserId), // 确保是数字
      reimbursementId: Date.now(), // 临时ID用于测试
      businessType: 'REIMBURSEMENT'
    };

    this.flowableService.startProcessInstance(processDefinitionKey, businessKey, variables).subscribe({
      next: (response) => {
        this.showSuccess(`流程实例启动成功: ${processDefinitionKey}`);
        this.refreshProcessInstances();
        this.refreshTasks(); // 刷新任务列表以显示新任务
      },
      error: (error) => {
        console.error('Failed to start process instance:', error);
        this.showError('启动流程实例失败: ' + (error.error?.message || error.message));
      }
    });
  }

  // 流程实例相关方法
  refreshProcessInstances(): void {
    this.flowableService.getProcessInstances().subscribe({
      next: (response) => {
        this.processInstancesDataSource.data = response || [];
        this.showSuccess('流程实例列表已刷新');
      },
      error: (error) => {
        console.error('Failed to load process instances:', error);
        this.showError('加载流程实例失败');
      }
    });
  }

  viewProcessDiagram(processInstanceId: string): void {
    this.showProcessDiagram = true;
    this.processDiagramUrl = null;
    
    this.flowableService.getProcessDiagram(processInstanceId).subscribe({
      next: (blob) => {
        const url = URL.createObjectURL(blob);
        this.processDiagramUrl = this.sanitizer.bypassSecurityTrustUrl(url);
      },
      error: (error) => {
        console.error('Failed to load process diagram:', error);
        this.showError('加载流程图失败');
        this.closeProcessDiagram();
      }
    });
  }

  closeProcessDiagram(): void {
    this.showProcessDiagram = false;
    if (this.processDiagramUrl) {
      // 清理 URL 对象
      const url = (this.processDiagramUrl as any).changingThisBreaksApplicationSecurity;
      if (url && url.startsWith('blob:')) {
        URL.revokeObjectURL(url);
      }
      this.processDiagramUrl = null;
    }
  }

  viewProcessHistory(processInstanceId: string): void {
    this.flowableService.getHistoricProcessInstances(processInstanceId).subscribe({
      next: (response) => {
        console.log('Process history:', response);
        this.showInfo('查看历史记录 (详细实现待完成)');
      },
      error: (error) => {
        console.error('Failed to load process history:', error);
        this.showError('加载历史记录失败');
      }
    });
  }

  toggleProcessInstanceSuspension(instance: FlowableProcessInstance): void {
    const action = instance.suspended ? '激活' : '暂停';
    
    this.flowableService.suspendProcessInstance(instance.id, !instance.suspended).subscribe({
      next: () => {
        this.showSuccess(`流程实例${action}成功`);
        this.refreshProcessInstances();
      },
      error: (error) => {
        console.error(`Failed to ${action} process instance:`, error);
        this.showError(`${action}流程实例失败`);
      }
    });
  }

  deleteProcessInstance(processInstanceId: string): void {
    if (confirm('确定要删除这个流程实例吗？此操作不可逆。')) {
      this.flowableService.deleteProcessInstance(processInstanceId, '用户手动删除').subscribe({
        next: () => {
          this.showSuccess('流程实例删除成功');
          this.refreshProcessInstances();
        },
        error: (error) => {
          console.error('Failed to delete process instance:', error);
          this.showError('删除流程实例失败');
        }
      });
    }
  }

  getInstanceStatusColor(instance: FlowableProcessInstance): string {
    if (instance.ended) {
      return 'accent';
    } else if (instance.suspended) {
      return 'warn';
    } else {
      return 'primary';
    }
  }

  getInstanceStatusText(instance: FlowableProcessInstance): string {
    if (instance.ended) {
      return '已结束';
    } else if (instance.suspended) {
      return '已暂停';
    } else {
      return '运行中';
    }
  }

  // 任务相关方法
  refreshTasks(): void {
    this.flowableService.getTasks(this.currentUserId).subscribe({
      next: (response) => {
        this.tasksDataSource.data = response || [];
        this.showSuccess(`用户ID ${this.currentUserId} 的任务列表已刷新`);
      },
      error: (error) => {
        console.error('Failed to load tasks:', error);
        this.showError('加载任务失败');
      }
    });
  }

  filterTasksByUser(): void {
    if (this.taskFilterUserId.trim()) {
      this.flowableService.getTasks(this.taskFilterUserId).subscribe({
        next: (response) => {
          this.tasksDataSource.data = response || [];
          this.showSuccess(`已按用户ID"${this.taskFilterUserId}"筛选任务`);
        },
        error: (error) => {
          console.error('Failed to filter tasks:', error);
          this.showError('筛选任务失败');
        }
      });
    } else {
      this.refreshTasks();
    }
  }

  loadDebugInfo(): void {
    const userId = this.taskFilterUserId.trim() || this.currentUserId;
    this.flowableService.getDebugTasks(userId).subscribe({
      next: (response) => {
        this.debugInfo = response;
        this.showDebugInfo = true;
        this.showSuccess(`已加载用户ID ${userId} 的调试信息`);
      },
      error: (error) => {
        console.error('Failed to load debug info:', error);
        this.showError('加载调试信息失败');
      }
    });
  }

  toggleDebugInfo(): void {
    this.showDebugInfo = !this.showDebugInfo;
  }

  claimTask(taskId: string, userId?: string): void {
    const userIdToClaim = userId || this.currentUserId;
    this.flowableService.claimTask(taskId, userIdToClaim).subscribe({
      next: () => {
        this.showSuccess(`用户ID ${userIdToClaim} 认领任务成功`);
        this.refreshTasks();
      },
      error: (error) => {
        console.error('Failed to claim task:', error);
        this.showError('认领任务失败: ' + (error.error?.message || error.message));
      }
    });
  }

  claimTaskForCurrentUser(taskId: string): void {
    this.claimTask(taskId, this.currentUserId);
  }

  claimTaskForFilterUser(taskId: string): void {
    if (this.taskFilterUserId.trim()) {
      this.claimTask(taskId, this.taskFilterUserId.trim());
    } else {
      this.claimTask(taskId);
    }
  }

  completeTask(taskId: string, approved: boolean = true): void {
    // 简单的任务完成，实际应该有表单收集用户输入
    const variables = {
      approved: approved,
      comments: approved ? '管理员批准操作' : '管理员拒绝操作',
      approvalDate: new Date().toISOString()
    };

    this.flowableService.completeTask(taskId, variables).subscribe({
      next: () => {
        this.showSuccess(`任务${approved ? '批准' : '拒绝'}成功`);
        this.refreshTasks();
        this.refreshProcessInstances();
      },
      error: (error) => {
        console.error('Failed to complete task:', error);
        this.showError('完成任务失败: ' + (error.error?.message || error.message));
      }
    });
  }

  approveTask(taskId: string): void {
    this.completeTask(taskId, true);
  }

  rejectTask(taskId: string): void {
    this.completeTask(taskId, false);
  }

  setCurrentUser(userId: string): void {
    if (userId && userId.trim()) {
      this.currentUserId = userId.trim();
      this.showSuccess(`当前用户ID已设置为: ${this.currentUserId}`);
      this.refreshTasks();
    }
  }

  viewTaskDetails(task: FlowableTask): void {
    console.log('Task details:', task);
    const details = [
      `任务ID: ${task.id}`,
      `任务名称: ${task.name}`,
      `分配人: ${task.assignee || '未分配'}`,
      `候选组: ${task.candidateGroups?.join(', ') || '无'}`,
      `创建时间: ${task.createTime}`,
      `流程定义: ${task.processDefinitionKey}`,
      `流程实例: ${task.processInstanceId}`
    ];
    
    this.showInfo(`任务详情:\n${details.join('\n')}`);
  }

  // 工具方法 - 检查任务是否可以被认领
  canClaimTask(task: FlowableTask): boolean {
    return !task.assignee && task.candidateGroups && task.candidateGroups.length > 0;
  }

  // 工具方法 - 检查任务是否已分配给用户
  isTaskAssignedToUser(task: FlowableTask, userId: string): boolean {
    return task.assignee === userId;
  }

  // 工具方法 - 获取任务状态文本
  getTaskStatusText(task: FlowableTask): string {
    if (task.assignee) {
      return `已分配给: ${task.assignee}`;
    } else if (task.candidateGroups && task.candidateGroups.length > 0) {
      return `候选组: ${task.candidateGroups.join(', ')}`;
    } else {
      return '未分配';
    }
  }

  // 工具方法
  private showSuccess(message: string): void {
    this.snackBar.open(message, '关闭', {
      duration: 3000,
      panelClass: ['success-snackbar']
    });
  }

  private showError(message: string): void {
    this.snackBar.open(message, '关闭', {
      duration: 5000,
      panelClass: ['error-snackbar']
    });
  }

  private showInfo(message: string): void {
    this.snackBar.open(message, '关闭', {
      duration: 3000,
      panelClass: ['info-snackbar']
    });
  }
}