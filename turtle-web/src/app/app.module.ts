import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientModule, HTTP_INTERCEPTORS, HttpClient } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatDialogModule } from '@angular/material/dialog';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './pages/login/login.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { GuestDashboardComponent } from './pages/guest/guest-dashboard.component';
import { PaymentTotalPipe } from './pipes/payment-total.pipe';

// Services and Interceptors
import { AuthInterceptor } from './interceptors/auth.interceptor';

// Shared Module
import { SharedModule } from './shared/shared.module';

// Translation
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';

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
import { BaseListModule } from './components/base-list/base-list.module';

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
    PaymentTotalPipe
  ],
  imports: [
    // Feature Modules
    CompanyFilterSelectInputModule,
    PersonInputModule,
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
    BaseListModule,

    // Angular
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    AppRoutingModule,
    SharedModule,
    MatDialogModule,
    TranslateModule.forRoot({
      defaultLanguage: 'zh',
      loader: {
        provide: TranslateLoader,
        useFactory: HttpLoaderFactory,
        deps: [HttpClient]
      }
    })
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
