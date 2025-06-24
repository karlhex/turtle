import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientModule, HTTP_INTERCEPTORS, HttpClient } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatDialogModule } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { MatDividerModule } from '@angular/material/divider';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatMenuModule } from '@angular/material/menu';
import { MatButtonModule } from '@angular/material/button';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './pages/login/login.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { GuestDashboardComponent } from './pages/guest/guest-dashboard.component';
import { TranslateLoader, TranslateModule } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { SidebarMenuComponent } from './components/sidebar-menu/sidebar-menu.component';

// Services and Interceptors
import { AuthInterceptor } from './interceptors/auth.interceptor';

// Feature Modules
import { InventoryModule } from './pages/inventory/inventory.module';
import { ContractModule } from './pages/contract/contract.module';
import { EmployeeModule } from './pages/employee/employee.module';
import { CompanyModule } from './pages/company/company.module';
import { CurrencyModule } from './pages/currency/currency.module';
import { ReimbursementModule } from './pages/reimbursement/reimbursement.module';
import { RolePermissionModule } from './pages/role-permission/role-permission.module';
import { ProductModule } from './pages/product/product.module';
import { ProjectModule } from './pages/project/project.module';
import { DepartmentModule } from './pages/department/department.module';
import { TaxInfoModule } from './pages/tax-info/tax-info.module';
import { BankAccountModule } from './pages/bank-account/bank-account.module';
import { UserManagementModule } from './pages/user-management/user-management.module';

// Standalone Components
import { CompanyFilterSelectInputModule } from './components/company-filter-select-input/company-filter-select-input.module';
import { PersonInputModule } from './components/person-input/person-input.module';
import { ActionModule } from './components/action/action.module';
import { BaseListModule } from './components/base-list/base-list.module';
import { SharedModule } from './shared/shared.module';
import { ContactModule } from './pages/contact/contact.module';
import { PositionModule } from './pages/position/position.module';

// AoT requires an exported function for factories
export function HttpLoaderFactory(http: HttpClient) {
  return new TranslateHttpLoader(http, './assets/i18n/', '.json');
}

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    DashboardComponent,
    GuestDashboardComponent,
    SidebarMenuComponent,
  ],
  imports: [
    // Feature Modules
    CompanyFilterSelectInputModule,
    PersonInputModule,
    PositionModule,
    ActionModule,
    SharedModule,
    InventoryModule,
    ContractModule,
    EmployeeModule,
    CompanyModule,
    CurrencyModule,
    ReimbursementModule,
    RolePermissionModule,
    ProductModule,
    ProjectModule,
    DepartmentModule,
    TaxInfoModule,
    BankAccountModule,
    UserManagementModule,
    ContactModule,
    BaseListModule,

    // Angular
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    AppRoutingModule,

    // Material
    MatDialogModule,
    MatIconModule,
    MatListModule,
    MatDividerModule,
    MatSidenavModule,
    MatToolbarModule,
    MatMenuModule,
    MatButtonModule,

    // Translation
    TranslateModule.forRoot({
      defaultLanguage: 'zh',
      loader: {
        provide: TranslateLoader,
        useFactory: HttpLoaderFactory,
        deps: [HttpClient],
      },
    }),
  ],
  providers: [{ provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true }],
  bootstrap: [AppComponent],
})
export class AppModule {}
