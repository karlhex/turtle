import { Component, OnInit, ViewChild, ElementRef, HostListener } from '@angular/core';
// import { WorkflowDesignService } from '../services/workflow-design.service';
import { WorkflowNode, WorkflowEdge, WorkflowNodeType } from '../models/workflow.model';

@Component({
  selector: 'app-workflow-canvas',
  templateUrl: './workflow-canvas.component.html',
  styleUrls: ['./workflow-canvas.component.scss']
})
export class WorkflowCanvasComponent implements OnInit {
  @ViewChild('canvas', { static: true }) canvas!: ElementRef;
  @ViewChild('svg', { static: true }) svg!: ElementRef;
  
  nodes: WorkflowNode[] = [];
  edges: WorkflowEdge[] = [];
  selectedNode: WorkflowNode | null = null;
  selectedEdge: WorkflowEdge | null = null;
  
  // 拖拽状态
  isDragging = false;
  draggedNode: WorkflowNode | null = null;
  dragOffset = { x: 0, y: 0 };
  
  // 连线状态
  isConnecting = false;
  connectionSource: string | null = null;
  tempConnection: { x: number; y: number } | null = null;
  
  constructor(// private workflowService: WorkflowDesignService) {}
  
  ngOnInit() {
    // TODO: Subscribe to Flowable workflow state
    console.log('Workflow canvas migrated to Flowable');
  }
  
  // 节点点击事件
  onNodeClick(node: WorkflowNode, event: MouseEvent) {
    event.stopPropagation();
    // this.workflowService.selectNode(node);
    this.selectedEdge = null;
  }
  
  // 节点拖拽开始
  onNodeMouseDown(node: WorkflowNode, event: MouseEvent) {
    this.isDragging = true;
    this.draggedNode = node;
    
    const rect = this.canvas.nativeElement.getBoundingClientRect();
    this.dragOffset = {
      x: event.clientX - rect.left - node.position.x,
      y: event.clientY - rect.top - node.position.y
    };
    
    event.preventDefault();
  }
  
  // 画布拖拽事件
  @HostListener('document:mousemove', ['$event'])
  onMouseMove(event: MouseEvent) {
    if (this.isDragging && this.draggedNode) {
      const rect = this.canvas.nativeElement.getBoundingClientRect();
      const newX = event.clientX - rect.left - this.dragOffset.x;
      const newY = event.clientY - rect.top - this.dragOffset.y;
      
      this.draggedNode.position = { x: newX, y: newY };
      // this.workflowService.updateNode(this.draggedNode);
    }
    
    if (this.isConnecting && this.tempConnection) {
      const rect = this.canvas.nativeElement.getBoundingClientRect();
      this.tempConnection = {
        x: event.clientX - rect.left,
        y: event.clientY - rect.top
      };
    }
  }
  
  // 停止拖拽
  @HostListener('document:mouseup')
  onMouseUp() {
    this.isDragging = false;
    this.draggedNode = null;
  }
  
  // 画布点击事件
  onCanvasClick(event: MouseEvent) {
    if (event.target === this.canvas.nativeElement || event.target === this.svg.nativeElement) {
      // this.workflowService.selectNode(null);
      this.selectedEdge = null;
      this.isConnecting = false;
      this.connectionSource = null;
    }
  }
  
  // 画布放置事件
  onCanvasDrop(event: DragEvent) {
    event.preventDefault();
    
    const nodeType = event.dataTransfer?.getData('nodeType');
    if (!nodeType) return;
    
    const rect = this.canvas.nativeElement.getBoundingClientRect();
    const x = event.clientX - rect.left;
    const y = event.clientY - rect.top;
    
    const newNode: WorkflowNode = {
      id: this.generateId(),
      type: nodeType as any,
      label: this.getDefaultLabel(nodeType),
      position: { x, y },
      properties: {}
    };
    
    // this.workflowService.addNode(newNode);
  }
  
  // 画布拖拽悬停
  onCanvasDragOver(event: DragEvent) {
    event.preventDefault();
  }
  
  // 开始连接节点
  startConnection(nodeId: string) {
    this.isConnecting = true;
    this.connectionSource = nodeId;
    
    // 获取源节点位置作为临时连线的起点
    const sourceNode = this.nodes.find(n => n.id === nodeId);
    if (sourceNode) {
      this.tempConnection = { x: sourceNode.position.x, y: sourceNode.position.y };
    }
  }
  
  // 完成连接
  finishConnection(targetNodeId: string) {
    if (this.connectionSource && this.connectionSource !== targetNodeId) {
      const newEdge: WorkflowEdge = {
        id: this.generateId(),
        source: this.connectionSource,
        target: targetNodeId,
        label: ''
      };
      
      // this.workflowService.addEdge(newEdge);
    }
    
    this.isConnecting = false;
    this.connectionSource = null;
    this.tempConnection = null;
  }
  
  // 删除节点
  deleteNode(nodeId: string) {
    // this.workflowService.removeNode(nodeId);
  }
  
  // 获取节点
  getNodeById(id: string): WorkflowNode | undefined {
    return this.nodes.find(n => n.id === id);
  }
  
  // 获取连线的中点
  getEdgeMidpoint(edge: WorkflowEdge): { x: number; y: number } {
    const source = this.getNodeById(edge.source);
    const target = this.getNodeById(edge.target);
    
    if (!source || !target) return { x: 0, y: 0 };
    
    return {
      x: (source.position.x + target.position.x) / 2,
      y: (source.position.y + target.position.y) / 2
    };
  }
  
  // 获取箭头坐标
  getArrowPoints(edge: WorkflowEdge): string {
    const source = this.getNodeById(edge.source);
    const target = this.getNodeById(edge.target);
    
    if (!source || !target) return '';
    
    const dx = target.position.x - source.position.x;
    const dy = target.position.y - source.position.y;
    const length = Math.sqrt(dx * dx + dy * dy);
    
    if (length === 0) return '';
    
    const unitX = dx / length;
    const unitY = dy / length;
    
    const arrowLength = 10;
    const arrowWidth = 5;
    
    const endX = target.position.x - unitX * 30; // 箭头距离节点中心的偏移
    const endY = target.position.y - unitY * 30;
    
    const p1X = endX - unitX * arrowLength + unitY * arrowWidth;
    const p1Y = endY - unitY * arrowLength - unitX * arrowWidth;
    const p2X = endX - unitX * arrowLength - unitY * arrowWidth;
    const p2Y = endY - unitY * arrowLength + unitX * arrowWidth;
    
    return `${endX},${endY} ${p1X},${p1Y} ${p2X},${p2Y}`;
  }
  
  // 获取节点图标
  getNodeIcon(type: string): string {
    const icons: Record<string, string> = {
      [WorkflowNodeType.START]: 'play_circle',
      [WorkflowNodeType.APPROVAL]: 'person',
      [WorkflowNodeType.CONDITION]: 'rule',
      [WorkflowNodeType.END]: 'stop_circle'
    };
    return icons[type] || 'help_outline';
  }
  
  // 生成唯一ID
  private generateId(): string {
    return 'node_' + Math.random().toString(36).substr(2, 9);
  }
  
  // 获取默认标签
  private getDefaultLabel(type: string): string {
    const labels: Record<string, string> = {
      [WorkflowNodeType.START]: '开始',
      [WorkflowNodeType.APPROVAL]: '审批',
      [WorkflowNodeType.CONDITION]: '条件',
      [WorkflowNodeType.END]: '结束'
    };
    return labels[type] || '节点';
  }
  
  // 获取节点样式类
  getNodeClass(node: WorkflowNode): string {
    let classes = `node-${node.type}`;
    if (this.selectedNode?.id === node.id) {
      classes += ' selected';
    }
    return classes;
  }
  
  // 获取连线样式类
  getEdgeClass(edge: WorkflowEdge): string {
    let classes = 'workflow-edge';
    if (this.selectedEdge?.id === edge.id) {
      classes += ' selected';
    }
    return classes;
  }

  // 获取节点坐标
  getNodeCoordinates(nodeId: string | null): { x: number; y: number } {
    if (!nodeId) return { x: 0, y: 0 };
    const node = this.nodes.find(n => n.id === nodeId);
    return node ? node.position : { x: 0, y: 0 };
  }
}