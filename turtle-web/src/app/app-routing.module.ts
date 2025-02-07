import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { GuestDashboardComponent } from './pages/guest/guest-dashboard.component';
import { EmployeeListComponent } from './pages/employee/employee-list.component';
import { DepartmentListComponent } from './pages/department/department-list.component';
import { UserManagementComponent } from './pages/user-management/user-management.component';
import { UserEmployeeMappingComponent } from './pages/user-management/user-employee-mapping/user-employee-mapping.component';
import { ContractListComponent } from './pages/contract/contract-list.component';
import { CurrencyListComponent } from './pages/currency/currency-list.component';
import { TaxInfoListComponent } from './pages/tax-info/tax-info-list.component';
import { CompanyListComponent } from './pages/company/company-list.component';
import { ProductListComponent } from './pages/product/product-list.component';
import { ProjectListComponent } from './pages/project/project-list.component';
import { BankAccountListComponent } from './pages/bank-account/bank-account-list.component';
import { ReimbursementListComponent } from './pages/reimbursement/reimbursement-list/reimbursement-list.component';
import { RolePermissionListComponent } from './pages/role-permission/role-permission-list.component';
import { ChangePasswordDialogComponent } from './pages/user-management/change-password-dialog/change-password-dialog.component';
import { AuthGuard } from './guards/auth.guard';
import { InventoryListComponent } from './pages/inventory/inventory-list.component';
import { ContactListComponent } from './pages/contact/contact-list.component';
import { PositionListComponent } from './pages/position/position-list.component';

const routes: Routes = [
  { path: 'login', component: LoginComponent },
  {
    path: '',
    canActivate: [AuthGuard],
    children: [
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
      { path: 'dashboard', component: DashboardComponent },
      { path: 'guest-dashboard', component: GuestDashboardComponent },
      { path: 'employees', component: EmployeeListComponent },
      { path: 'departments', component: DepartmentListComponent },
      { path: 'users', component: UserManagementComponent },
      { path: 'user-employee-mapping', component: UserEmployeeMappingComponent },
      { path: 'contracts', component: ContractListComponent },
      { path: 'currencies', component: CurrencyListComponent },
      { path: 'tax-infos', component: TaxInfoListComponent },
      { path: 'companies', component: CompanyListComponent },
      { path: 'products', component: ProductListComponent },
      { path: 'projects', component: ProjectListComponent },
      { path: 'bank-accounts', component: BankAccountListComponent },
      { path: 'reimbursements', component: ReimbursementListComponent },
      { path: 'role-permissions', component: RolePermissionListComponent },
      { path: 'change-password', component: ChangePasswordDialogComponent },
      { path: 'inventories', component: InventoryListComponent },
      { path: 'contacts', component: ContactListComponent },
      { path: 'positions', component: PositionListComponent },

      // Placeholder routes for future implementation
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
