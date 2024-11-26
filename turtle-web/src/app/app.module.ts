import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientModule, HTTP_INTERCEPTORS, HttpClient } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './components/login/login.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { EmployeeListComponent } from './components/employee/employee-list.component';
import { EmployeeDialogComponent } from './components/employee/employee-dialog.component';
import { DepartmentListComponent } from './components/department/department-list.component';
import { DepartmentDialogComponent } from './components/department/department-dialog.component';
import { EducationDialogComponent } from './components/employee/education-dialog.component';
import { JobHistoryDialogComponent } from './components/employee/job-history-dialog.component';
import { UserManagementComponent } from './components/user-management/user-management.component';
import { UserEmployeeMappingComponent } from './components/user-employee-mapping/user-employee-mapping.component';

// Services and Interceptors
import { AuthInterceptor } from './interceptors/auth.interceptor';

// Shared Module
import { SharedModule } from './shared/shared.module';

// Translation
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';

// AoT requires an exported function for factories
export function HttpLoaderFactory(http: HttpClient) {
  return new TranslateHttpLoader(http, './assets/i18n/', '.json');
}

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    DashboardComponent,
    EmployeeListComponent,
    EmployeeDialogComponent,
    DepartmentListComponent,
    DepartmentDialogComponent,
    EducationDialogComponent,
    JobHistoryDialogComponent,
    UserManagementComponent,
    UserEmployeeMappingComponent
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
