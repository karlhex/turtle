import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';

// Angular Material
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatIconModule } from '@angular/material/icon';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatDialogModule } from '@angular/material/dialog';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatChipsModule } from '@angular/material/chips';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatMenuModule } from '@angular/material/menu';
import { MatTabsModule } from '@angular/material/tabs';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';

// Components
import { EmployeeApplicationFormComponent } from './employee-application-form.component';
import { MyApplicationsComponent } from './my-applications.component';
import { HrApplicationListComponent } from './hr-application-list.component';
import { ApplicationViewComponent } from './application-view.component';
import { ApplicationReviewComponent } from './application-review.component';
import { EmployeeConversionComponent } from './employee-conversion.component';
import { ApprovedApplicationsListComponent } from './approved-applications-list.component';

// New structured data components
import { ApplicationEducationListComponent } from './application-education-list.component';
import { ApplicationEducationDialogComponent } from './application-education-dialog.component';
import { ApplicationJobHistoryListComponent } from './application-job-history-list.component';
import { ApplicationJobHistoryDialogComponent } from './application-job-history-dialog.component';
import { ApplicationCertificationListComponent } from './application-certification-list.component';
import { ApplicationCertificationDialogComponent } from './application-certification-dialog.component';
import { ApplicationTimelineComponent } from './application-timeline/application-timeline.component';

// Routing
import { EmployeeApplicationRoutingModule } from './employee-application-routing.module';

// Shared
import { SharedModule } from '../../shared/shared.module';

@NgModule({
  declarations: [
    EmployeeApplicationFormComponent,
    MyApplicationsComponent,
    HrApplicationListComponent,
    ApplicationViewComponent,
    ApplicationReviewComponent,
    EmployeeConversionComponent,
    ApprovedApplicationsListComponent,

    // New structured data components
    ApplicationEducationListComponent,
    ApplicationEducationDialogComponent,
    ApplicationJobHistoryListComponent,
    ApplicationJobHistoryDialogComponent,
    ApplicationCertificationListComponent,
    ApplicationCertificationDialogComponent,
    ApplicationTimelineComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    FormsModule,
    EmployeeApplicationRoutingModule,
    SharedModule,
    
    // Material modules
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatSelectModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatIconModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatProgressSpinnerModule,
    MatSnackBarModule,
    MatDialogModule,
    MatToolbarModule,
    MatProgressBarModule,
    MatTooltipModule,
    MatChipsModule,
    MatExpansionModule,
    MatMenuModule,
    MatTabsModule,
    MatCheckboxModule,
    MatSlideToggleModule
  ]
})
export class EmployeeApplicationModule { }