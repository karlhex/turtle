import { Component, OnInit } from '@angular/core';
// import { WorkflowDesignService } from '../services/workflow-design.service';

@Component({
  selector: 'app-workflow-management',
  templateUrl: './workflow-management.component.html',
  styleUrls: ['./workflow-management.component.scss']
})
export class WorkflowManagementComponent implements OnInit {
  
  constructor(/* private workflowService: WorkflowDesignService */) {}
  
  ngOnInit() {
    console.log('WorkflowManagementComponent initialized');
    // TODO: Load sample Flowable BPMN workflows
    console.log('Workflow management migrated to Flowable');
  }
}