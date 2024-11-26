import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { EmployeeListComponent } from './components/employee/employee-list.component';
import { DepartmentListComponent } from './components/department/department-list.component';
import { UserManagementComponent } from './components/user-management/user-management.component';
import { UserEmployeeMappingComponent } from './components/user-employee-mapping/user-employee-mapping.component';
import { AuthGuard } from './guards/auth.guard';

const routes: Routes = [
  { path: 'login', component: LoginComponent },
  {
    path: '',
    canActivate: [AuthGuard],
    children: [
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
      { path: 'dashboard', component: DashboardComponent },
      { path: 'employees', component: EmployeeListComponent },
      { path: 'departments', component: DepartmentListComponent },
      { path: 'users', component: UserManagementComponent },
      { path: 'user-employee-mapping', component: UserEmployeeMappingComponent },
      // Placeholder routes for future implementation
      { path: 'positions', component: DashboardComponent },
      { path: 'attendance', component: DashboardComponent },
      { path: 'leave', component: DashboardComponent },
      { path: 'payroll', component: DashboardComponent },
    ]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
