// 节点类型枚举
export enum WorkflowNodeType {
  START = 'start',
  APPROVAL = 'approval',
  CONDITION = 'condition',
  END = 'end'
}

// 节点接口
export interface WorkflowNode {
  id: string;
  type: WorkflowNodeType;
  label: string;
  position: { x: number; y: number };
  properties: {
    approver?: string;
    condition?: string;
    amountThreshold?: number;
    department?: string;
    role?: string;
    description?: string;
  };
}

// 连接线接口
export interface WorkflowEdge {
  id: string;
  source: string;
  target: string;
  label?: string;
  condition?: string;
}

// 工作流配置
export interface WorkflowConfig {
  id: string;
  name: string;
  description: string;
  businessType: string;
  nodes: WorkflowNode[];
  edges: WorkflowEdge[];
  version: number;
  createdAt: Date;
  updatedAt: Date;
  isActive: boolean;
}