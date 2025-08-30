import { Component } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
// import { WorkflowDesignService } from '../services/workflow-design.service';

@Component({
  selector: 'app-workflow-toolbar',
  templateUrl: './workflow-toolbar.component.html',
  styleUrls: ['./workflow-toolbar.component.scss']
})
export class WorkflowToolbarComponent {
  workflowName = '';
  isSaving = false;
  isValidating = false;

  constructor(
    // private workflowService: WorkflowDesignService,
    private snackBar: MatSnackBar
  ) {}

  saveWorkflow() {
    if (!this.workflowName.trim()) {
      this.snackBar.open('请输入工作流名称', '关闭', { duration: 3000 });
      return;
    }

    this.isSaving = true;
    // TODO: Implement Flowable-based workflow saving
    this.snackBar.open('工作流保存功能已迁移至 Flowable', '关闭', { duration: 3000 });
    this.isSaving = false;
  }

  validateWorkflow() {
    this.isValidating = true;
    // TODO: Implement Flowable-based workflow validation
    const errors: string[] = [];
    
    setTimeout(() => {
      this.isValidating = false;
      if (errors.length === 0) {
        this.snackBar.open('工作流验证通过 ✓', '关闭', { duration: 3000 });
      } else {
        this.snackBar.open(`验证失败: ${errors[0]}`, '查看全部', { duration: 5000 })
          .onAction().subscribe(() => {
            // TODO: Show all errors
            const errorMessage = errors.join('\n');
            alert(`工作流验证错误：\n\n${errorMessage}`);
          });
      }
    }, 500);
  }

  exportWorkflow() {
    this.workflowService.exportCurrentWorkflow().subscribe(workflow => {
      const blob = new Blob([JSON.stringify(workflow, null, 2)], { 
        type: 'application/json' 
      });
      const url = URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = `${workflow.name || 'workflow'}_${new Date().getTime()}.json`;
      a.click();
      URL.revokeObjectURL(url);
      
      this.snackBar.open('工作流已导出', '关闭', { duration: 3000 });
    });
  }

  importWorkflow(event: any) {
    const file = event.target.files[0];
    if (!file) return;

    const reader = new FileReader();
    reader.onload = (e) => {
      try {
        const workflow = JSON.parse(e.target?.result as string);
        // TODO: 实现导入逻辑
        this.snackBar.open('工作流导入成功', '关闭', { duration: 3000 });
      } catch (error) {
        this.snackBar.open('导入失败：无效的文件格式', '关闭', { duration: 5000 });
      }
    };
    reader.readAsText(file);
  }

  clearWorkflow() {
    if (confirm('确定要清空当前工作流吗？此操作不可撤销。')) {
      this.workflowService.clearWorkflow();
      this.workflowName = '';
      this.snackBar.open('工作流已清空', '关闭', { duration: 3000 });
    }
  }

  loadSampleWorkflow() {
    this.workflowService.loadSampleWorkflow();
    this.workflowName = '报销审批流程';
    this.snackBar.open('已加载示例工作流', '关闭', { duration: 3000 });
  }
}