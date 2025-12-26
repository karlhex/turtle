import { NgModule } from '@angular/core';

import { ApprovalRoutingModule } from './approval-routing.module';
import { ApprovalListComponent } from './approval-list/approval-list.component';
import { PendingApprovalsComponent } from './pending-approvals/pending-approvals.component';
import { ApprovalHistoryComponent } from './approval-history/approval-history.component';
import { ApprovalTaskComponent } from './approval-task/approval-task.component';
import { SharedModule } from '../../shared/shared.module';

@NgModule({
  declarations: [
    ApprovalListComponent,
    PendingApprovalsComponent,
    ApprovalHistoryComponent,
    ApprovalTaskComponent
  ],
  imports: [
    ApprovalRoutingModule,
    SharedModule
  ]
})
export class ApprovalModule { }
