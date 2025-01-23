import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatTableModule } from '@angular/material/table';
import { MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatTooltipModule } from '@angular/material/tooltip';
import { TranslateModule } from '@ngx-translate/core';

import { EmployeeListComponent } from './employee-list.component';
import { EmployeeDialogComponent } from './employee-dialog.component';
import { EducationListComponent } from './education-list.component';
import { EducationDialogComponent } from './education-dialog.component';
import { JobHistoryListComponent } from './job-history-list.component';
import { JobHistoryDialogComponent } from './job-history-dialog.component';
import { CompanyFilterSelectInputModule } from '../../components/company-filter-select-input/company-filter-select-input.module';
import { BaseListModule } from '../../components/base-list/base-list.module';
import { MatTabsModule } from '@angular/material/tabs';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { ActionModule } from '@app/components/action/action.module';
import { MatChipsModule } from '@angular/material/chips';

@NgModule({
  declarations: [
    EmployeeListComponent,
    EmployeeDialogComponent,
    EducationListComponent,
    EducationDialogComponent,
    JobHistoryListComponent,
    JobHistoryDialogComponent
  ],
  imports: [
    ActionModule,
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    MatTableModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatButtonModule,
    MatIconModule,
    MatMenuModule,
    MatProgressBarModule,
    MatTooltipModule,
    MatTabsModule,
    MatProgressSpinnerModule,
    MatChipsModule,
    TranslateModule,
    BaseListModule,
    CompanyFilterSelectInputModule
  ],
  exports: [
    EmployeeListComponent,
    EmployeeDialogComponent,
    EducationListComponent,
    EducationDialogComponent,
    JobHistoryListComponent,
    JobHistoryDialogComponent
  ]
})
export class EmployeeModule { }
