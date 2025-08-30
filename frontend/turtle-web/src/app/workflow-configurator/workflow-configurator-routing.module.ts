import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { WorkflowManagementComponent } from './workflow-management/workflow-management.component';

const routes: Routes = [
  { path: '', component: WorkflowManagementComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class WorkflowConfiguratorRoutingModule { }