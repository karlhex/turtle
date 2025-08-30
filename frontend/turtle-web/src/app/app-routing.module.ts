import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { GuestDashboardComponent } from './pages/guest/guest-dashboard.component';
import { EmployeeListNewComponent } from './pages/employee/employee-list-new.component';
import { DepartmentListNewComponent } from './pages/department/department-list-new.component';
import { UserManagementComponent } from './pages/user-management/user-management.component';
import { UserEmployeeMappingComponent } from './pages/user-management/user-employee-mapping/user-employee-mapping.component';
import { ContractListComponent } from './pages/contract/contract-list.component';
import { CurrencyListNewComponent } from './pages/currency/currency-list-new.component';
import { TaxInfoListNewComponent } from './pages/tax-info/tax-info-list-new.component';
import { CompanyListNewComponent } from './pages/company/company-list-new.component';
import { ProductListNewComponent } from './pages/product/product-list-new.component';
import { ProjectListComponent } from './pages/project/project-list.component';
import { BankAccountListNewComponent } from './pages/bank-account/bank-account-list-new.component';
import { ReimbursementListComponent } from './pages/reimbursement/reimbursement-list/reimbursement-list.component';
import { RolePermissionListComponent } from './pages/role-permission/role-permission-list.component';
import { ChangePasswordDialogComponent } from './pages/user-management/change-password-dialog/change-password-dialog.component';
import { AuthGuard } from './guards/auth.guard';
import { InventoryListNewComponent } from './pages/inventory/inventory-list-new.component';
import { ContactListNewComponent } from './pages/contact/contact-list-new.component';
import { PositionListNewComponent } from './pages/position/position-list-new.component';
import { DemoPageComponent } from './pages/demo/demo-page/demo-page.component';
import { FlowableAdminComponent } from './pages/workflow/flowable-admin/flowable-admin.component';

const routes: Routes = [
  { path: 'login', component: LoginComponent },
  {
    path: '',
    canActivate: [AuthGuard],
    children: [
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
      { path: 'dashboard', component: DashboardComponent },
      { path: 'guest-dashboard', component: GuestDashboardComponent },
      { path: 'employees', component: EmployeeListNewComponent },
      { path: 'departments', component: DepartmentListNewComponent },
      { path: 'users', component: UserManagementComponent },
      { path: 'user-employee-mapping', component: UserEmployeeMappingComponent },
      { path: 'contracts', component: ContractListComponent },
      { path: 'currencies', component: CurrencyListNewComponent },
      { path: 'tax-infos', component: TaxInfoListNewComponent },
      { path: 'companies', component: CompanyListNewComponent },
      { path: 'products', component: ProductListNewComponent },
      { path: 'projects', component: ProjectListComponent },
      { path: 'bank-accounts', component: BankAccountListNewComponent },
      { path: 'reimbursements', component: ReimbursementListComponent },
      { path: 'role-permissions', component: RolePermissionListComponent },
      { path: 'change-password', component: ChangePasswordDialogComponent },
      { path: 'inventories', component: InventoryListNewComponent },
      { path: 'contacts', component: ContactListNewComponent },
      { path: 'positions', component: PositionListNewComponent },
      { path: 'demo', component: DemoPageComponent },

      // Workflow configuration - removed (migrated to Flowable BPMN)
      { path: 'workflow/flowable-admin', component: FlowableAdminComponent },

      // Placeholder routes for future implementation
      { path: 'attendance', component: DashboardComponent },
      { path: 'leave', component: DashboardComponent },
      { path: 'payroll', component: DashboardComponent },
    ],
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
