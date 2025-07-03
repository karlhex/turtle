import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientModule, HTTP_INTERCEPTORS, HttpClient, HttpBackend } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

// Material Modules
import { MatDialogModule } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { MatDividerModule } from '@angular/material/divider';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatMenuModule } from '@angular/material/menu';
import { MatButtonModule } from '@angular/material/button';
import { MatTableModule } from '@angular/material/table';
import { MatSortModule } from '@angular/material/sort';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatRadioModule } from '@angular/material/radio';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatStepperModule } from '@angular/material/stepper';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatExpansionModule } from '@angular/material/expansion';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './pages/login/login.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { GuestDashboardComponent } from './pages/guest/guest-dashboard.component';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { MultiTranslateHttpLoader } from 'ngx-translate-multi-http-loader';
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
import { DemoPageComponent } from './pages/demo/demo-page/demo-page.component';

// AoT requires an exported function for factories
export function HttpLoaderFactory(handler: HttpBackend) {
  return new MultiTranslateHttpLoader(handler, [
    { prefix: './assets/i18n/', suffix:'/menu.json' },
    { prefix: './assets/i18n/', suffix:'/common.json' },
    { prefix: './assets/i18n/', suffix:'/types.json' },
    { prefix: './assets/i18n/', suffix:'/contract.json' },
    { prefix: './assets/i18n/', suffix:'/currency.json' },
    { prefix: './assets/i18n/', suffix:'/department.json' },
    { prefix: './assets/i18n/', suffix:'/employee.json' },
    { prefix: './assets/i18n/', suffix:'/inventory.json' },
    { prefix: './assets/i18n/', suffix:'/project.json' },
    { prefix: './assets/i18n/', suffix:'/role-permission.json' },
    { prefix: './assets/i18n/', suffix:'/user-management.json' },
    { prefix: './assets/i18n/', suffix:'/reimbursement.json' },
    { prefix: './assets/i18n/', suffix:'/tax-info.json' },
    { prefix: './assets/i18n/', suffix:'/bank-account.json' },
    { prefix: './assets/i18n/', suffix:'/contact.json' },
    { prefix: './assets/i18n/', suffix:'/position.json' },
    { prefix: './assets/i18n/', suffix:'/user-management.json' },
    { prefix: './assets/i18n/', suffix:'/reimbursement.json' },
    { prefix: './assets/i18n/', suffix:'/tax-info.json' },
    { prefix: './assets/i18n/', suffix:'/bank-account.json' },
  ]);
}

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    DashboardComponent,
    GuestDashboardComponent,
    SidebarMenuComponent,
    DemoPageComponent,
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

    // Material Modules
    MatDialogModule,
    MatIconModule,
    MatListModule,
    MatDividerModule,
    MatSidenavModule,
    MatToolbarModule,
    MatMenuModule,
    MatButtonModule,
    MatTableModule,
    MatSortModule,
    MatPaginatorModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatCheckboxModule,
    MatRadioModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatStepperModule,
    MatProgressSpinnerModule,
    MatTooltipModule,
    MatExpansionModule,

    // Translation
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: HttpLoaderFactory,
        deps: [HttpBackend]
      }
    }),
  ],
  providers: [{ provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true }],
  bootstrap: [AppComponent],
})
export class AppModule {}
