import { Component } from '@angular/core';

@Component({
  selector: 'app-node-palette',
  templateUrl: './node-palette.component.html',
  styleUrls: ['./node-palette.component.scss']
})
export class NodePaletteComponent {
  nodeTypes = [
    { 
      type: 'start', 
      label: '开始节点', 
      icon: 'play_circle',
      description: '流程开始'
    },
    { 
      type: 'approval', 
      label: '审批节点', 
      icon: 'person',
      description: '审批操作'
    },
    { 
      type: 'condition', 
      label: '条件节点', 
      icon: 'rule',
      description: '条件判断'
    },
    { 
      type: 'end', 
      label: '结束节点', 
      icon: 'stop_circle',
      description: '流程结束'
    }
  ];

  onDragStart(event: DragEvent, nodeType: string) {
    event.dataTransfer?.setData('nodeType', nodeType);
    event.dataTransfer!.effectAllowed = 'copy';
  }

  onDragEnd(event: DragEvent) {
    // 拖拽结束时的清理工作
  }
}