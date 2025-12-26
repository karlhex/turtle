import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { EmployeeApplicationFormComponent } from './employee-application-form.component';
import { MyApplicationsComponent } from './my-applications.component';
import { HrApplicationListComponent } from './hr-application-list.component';
import { ApplicationViewComponent } from './application-view.component';
import { ApplicationReviewComponent } from './application-review.component';
import { EmployeeConversionComponent } from './employee-conversion.component';
import { ApprovedApplicationsListComponent } from './approved-applications-list.component';
import { AuthGuard } from '../../guards/auth.guard';

const routes: Routes = [
  // GUEST用户路由
  {
    path: 'new',
    component: EmployeeApplicationFormComponent,
    canActivate: [AuthGuard],
    data: { roles: ['GUEST'], title: '新建入职申请' }
  },
  {
    path: 'my-applications',
    component: MyApplicationsComponent,
    canActivate: [AuthGuard],
    data: { roles: ['GUEST'], title: '我的申请' }
  },
  {
    path: 'edit/:id',
    component: EmployeeApplicationFormComponent,
    canActivate: [AuthGuard],
    data: { roles: ['GUEST'], title: '编辑申请' }
  },

  // HR用户路由
  {
    path: 'hr/list',
    component: HrApplicationListComponent,
    canActivate: [AuthGuard],
    data: { permissions: ['EMPLOYEE_APPLICATION_READ'], title: 'HR申请管理' }
  },
  {
    path: 'hr/approved',
    component: ApprovedApplicationsListComponent,
    canActivate: [AuthGuard],
    data: { permissions: ['EMPLOYEE_APPLICATION_APPROVE'], title: '申请转员工管理' }
  },
  {
    path: 'hr/view/:id',
    component: ApplicationViewComponent,
    canActivate: [AuthGuard],
    data: { permissions: ['EMPLOYEE_APPLICATION_READ'], title: '查看申请详情', readOnly: true }
  },
  {
    path: 'hr/review/:id',
    component: ApplicationReviewComponent,
    canActivate: [AuthGuard],
    data: { permissions: ['EMPLOYEE_APPLICATION_APPROVE'], title: '审核申请', reviewMode: true }
  },
  {
    path: 'hr/convert/:id',
    component: EmployeeConversionComponent,
    canActivate: [AuthGuard],
    data: { permissions: ['EMPLOYEE_APPLICATION_APPROVE'], title: '转换为员工' }
  },

  // 默认重定向
  {
    path: '',
    redirectTo: 'my-applications',
    pathMatch: 'full'
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class EmployeeApplicationRoutingModule { }