import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientModule, HTTP_INTERCEPTORS, HttpClient } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './pages/login/login.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { EmployeeDialogComponent } from './pages/employee/employee-dialog.component';
import { DepartmentDialogComponent } from './pages/department/department-dialog.component';
import { EducationDialogComponent } from './pages/employee/education-dialog.component';
import { JobHistoryDialogComponent } from './pages/employee/job-history-dialog.component';

// Services and Interceptors
import { AuthInterceptor } from './interceptors/auth.interceptor';

// Shared Module
import { SharedModule } from './shared/shared.module';

// Translation
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';

// Person Components
import { PersonInputComponent } from './components/person-input/person-input.component';
import { PersonDialogComponent } from './components/person-input/person-dialog.component';

// Standalone Components
import { DepartmentListComponent } from './pages/department/department-list.component';
import { UserManagementComponent } from './pages/user-management/user-management.component';
import { UserEmployeeMappingComponent } from './pages/user-employee-mapping/user-employee-mapping.component';
import { EmployeeListComponent } from './pages/employee/employee-list.component';
import { ActionComponent } from './components/action/action.component';
import { BaseDialogComponent } from '@components/base-dialog/base-dialog.component';
import { ContractListComponent } from './pages/contract/contract-list.component';
import { ContractDialogComponent } from './pages/contract/contract-dialog.component';
import { CurrencyListComponent } from './pages/currency/currency-list.component';
import { CurrencyDialogComponent } from './pages/currency/currency-dialog.component';
import { TaxInfoListComponent } from './pages/tax-info/tax-info-list.component';
import { TaxInfoDialogComponent } from './pages/tax-info/tax-info-dialog.component';
import { SidebarMenuComponent } from './components/sidebar-menu/sidebar-menu.component';
import { CompanyDialogComponent } from '@pages/company/company-dialog.component';
import { CompanyListComponent } from '@pages/company/company-list.component';
import { ContractItemDialogComponent } from '@pages/contract/contract-item-dialog.component';
import { ContractItemListComponent } from '@pages/contract/contract-item-list.component';
import { ProductListComponent } from './pages/product/product-list.component';
import { ProductDialogComponent } from './pages/product/product-dialog.component';
import { ContractDownPaymentDialogComponent } from '@pages/contract/contract-down-payment-dialog.component';
import { ContractDownPaymentListComponent } from '@pages/contract/contract-down-payment-list.component';
import { ProjectDialogComponent } from './pages/project/project-dialog.component';
import { ProjectListComponent } from './pages/project/project-list.component';
import { BaseListComponent } from './components/base-list/base-list.component';
import { PaymentTotalPipe } from './pipes/payment-total.pipe';
import { ContractInvoiceDialogComponent } from './pages/contract/contract-invoice-dialog.component';
import { ContractInvoiceListComponent } from './pages/contract/contract-invoice-list.component';
import { BankAccountDialogComponent } from './pages/bank-account/bank-account-dialog.component';
import { BankAccountListComponent } from './pages/bank-account/bank-account-list.component';

// AoT requires an exported function for factories
export function HttpLoaderFactory(http: HttpClient) {
  return new TranslateHttpLoader(http, './assets/i18n/', '.json');
}

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    DashboardComponent,
    EmployeeDialogComponent,
    DepartmentDialogComponent,
    EducationDialogComponent,
    JobHistoryDialogComponent,
    DepartmentListComponent,
    UserManagementComponent,
    UserEmployeeMappingComponent,
    EmployeeListComponent,
    ActionComponent,
    BaseDialogComponent,
    ContractListComponent,
    ContractDialogComponent,
    ContractItemDialogComponent,
    ContractItemListComponent,
    ContractDownPaymentDialogComponent,
    ContractDownPaymentListComponent,
    ContractInvoiceDialogComponent,
    ContractInvoiceListComponent,
    CurrencyListComponent,
    CurrencyDialogComponent,
    TaxInfoListComponent,
    TaxInfoDialogComponent,
    BankAccountDialogComponent,
    BankAccountListComponent,
    SidebarMenuComponent,
    CompanyDialogComponent,
    CompanyListComponent,
    ProductListComponent,
    ProductDialogComponent,
    ProjectDialogComponent,
    ProjectListComponent,
    BaseListComponent,
    PaymentTotalPipe,
    PersonInputComponent,
    PersonDialogComponent
  ],
  imports: [

    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    AppRoutingModule,
    SharedModule,
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
