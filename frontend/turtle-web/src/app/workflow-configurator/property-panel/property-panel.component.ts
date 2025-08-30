import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
// import { WorkflowDesignService } from '../services/workflow-design.service';
import { WorkflowNode, WorkflowNodeType } from '../models/workflow.model';

@Component({
  selector: 'app-property-panel',
  templateUrl: './property-panel.component.html',
  styleUrls: ['./property-panel.component.scss']
})
export class PropertyPanelComponent implements OnInit, OnDestroy {
  selectedNode: WorkflowNode | null = null;
  propertyForm: FormGroup;
  private destroy$ = new Subject<void>();

  // 节点类型配置
  nodeTypeConfig = {
    [WorkflowNodeType.START]: {
      icon: 'play_circle',
      title: '开始节点',
      fields: []
    },
    [WorkflowNodeType.APPROVAL]: {
      icon: 'person',
      title: '审批节点',
      fields: ['label', 'approver', 'department', 'role', 'description']
    },
    [WorkflowNodeType.CONDITION]: {
      icon: 'rule',
      title: '条件节点',
      fields: ['label', 'condition', 'amountThreshold', 'description']
    },
    [WorkflowNodeType.END]: {
      icon: 'stop_circle',
      title: '结束节点',
      fields: []
    }
  };

  // 部门选项
  departments = [
    { value: 'finance', label: '财务部' },
    { value: 'hr', label: '人事部' },
    { value: 'sales', label: '销售部' },
    { value: 'marketing', label: '市场部' },
    { value: 'it', label: '技术部' },
    { value: 'gm', label: '总经理办公室' }
  ];

  // 角色选项
  roles = [
    { value: 'employee', label: '普通员工' },
    { value: 'manager', label: '部门经理' },
    { value: 'director', label: '总监' },
    { value: 'gm', label: '总经理' },
    { value: 'finance_manager', label: '财务经理' },
    { value: 'hr_manager', label: '人事经理' }
  ];

  constructor(
    // private workflowService: WorkflowDesignService,
    private fb: FormBuilder
  ) {
    this.propertyForm = this.fb.group({
      label: ['', [Validators.required, Validators.maxLength(50)]],
      approver: [''],
      department: [''],
      role: [''],
      condition: [''],
      amountThreshold: ['', [Validators.min(0)]],
      description: ['', [Validators.maxLength(200)]]
    });
  }

  ngOnInit() {
    // this.workflowService.selectedNode$
      .pipe(takeUntil(this.destroy$))
      .subscribe(node => {
        this.selectedNode = node;
        if (node) {
          this.updateFormValues(node);
        }
      });

    // 监听表单变化并更新节点
    this.propertyForm.valueChanges
      .pipe(takeUntil(this.destroy$))
      .subscribe(values => {
        if (this.selectedNode) {
          this.updateNodeFromForm(values);
        }
      });
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private updateFormValues(node: WorkflowNode) {
    this.propertyForm.patchValue({
      label: node.label,
      approver: node.properties.approver || '',
      department: node.properties.department || '',
      role: node.properties.role || '',
      condition: node.properties.condition || '',
      amountThreshold: node.properties.amountThreshold || null,
      description: node.properties.description || ''
    }, { emitEvent: false });
  }

  private updateNodeFromForm(values: any) {
    if (!this.selectedNode) return;

    const updatedNode: WorkflowNode = {
      ...this.selectedNode,
      label: values.label,
      properties: {
        ...this.selectedNode.properties,
        approver: values.approver,
        department: values.department,
        role: values.role,
        condition: values.condition,
        amountThreshold: values.amountThreshold,
        description: values.description
      }
    };

    // this.workflowService.updateNode(updatedNode);
  }

  getNodeConfig() {
    if (!this.selectedNode) return null;
    return this.nodeTypeConfig[this.selectedNode.type];
  }

  getVisibleFields() {
    const config = this.getNodeConfig();
    return config ? config.fields : [];
  }

  isFieldVisible(fieldName: string): boolean {
    const fields = this.getVisibleFields();
    return fields.includes(fieldName);
  }

  getNodeTypeIcon() {
    if (!this.selectedNode) return 'help_outline';
    return this.nodeTypeConfig[this.selectedNode.type]?.icon || 'help_outline';
  }

  getNodeTypeTitle() {
    if (!this.selectedNode) return '未选择节点';
    return this.nodeTypeConfig[this.selectedNode.type]?.title || '未知节点';
  }

  deselectNode() {
    // this.workflowService.selectNode(null);
  }

  deleteSelectedNode() {
    if (this.selectedNode) {
      // this.workflowService.removeNode(this.selectedNode.id);
    }
  }

  // 表单验证提示
  getErrorMessage(fieldName: string): string {
    const field = this.propertyForm.get(fieldName);
    if (field?.hasError('required')) {
      return '此字段为必填项';
    }
    if (field?.hasError('maxLength')) {
      return `最多${field.errors?.['maxLength'].requiredLength}个字符`;
    }
    if (field?.hasError('min')) {
      return '金额必须大于等于0';
    }
    return '';
  }

  // 重置节点位置
  resetNodePosition() {
    if (this.selectedNode) {
      const updatedNode = {
        ...this.selectedNode,
        position: { x: 100, y: 100 }
      };
      // this.workflowService.updateNode(updatedNode);
    }
  }
}