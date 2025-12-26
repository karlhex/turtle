import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ApprovalListComponent } from './approval-list/approval-list.component';
import { PendingApprovalsComponent } from './pending-approvals/pending-approvals.component';
import { ApprovalHistoryComponent } from './approval-history/approval-history.component';
import { ApprovalTaskComponent } from './approval-task/approval-task.component';
import { AuthGuard } from '../../guards/auth.guard';

const routes: Routes = [
  {
    path: 'list',
    component: ApprovalListComponent,
    canActivate: [AuthGuard],
    data: { title: '统一审批管理' }
  },
  {
    path: 'pending',
    component: PendingApprovalsComponent,
    canActivate: [AuthGuard],
    data: { title: '我的待办审批' }
  },
  {
    path: 'history',
    component: ApprovalHistoryComponent,
    canActivate: [AuthGuard],
    data: { title: '审批历史' }
  },
  {
    path: 'task/:id',
    component: ApprovalTaskComponent,
    canActivate: [AuthGuard],
    data: { title: '审批任务处理' }
  },
  {
    path: '',
    redirectTo: 'list',
    pathMatch: 'full'
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ApprovalRoutingModule { }
