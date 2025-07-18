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
import { SharedModule } from '../../shared/shared.module';

import { EmployeeListComponent } from './employee-list.component';
import { EmployeeDialogComponent } from './employee-dialog.component';
import { EducationListComponent } from './education-list.component';
import { EducationDialogComponent } from './education-dialog.component';
import { JobHistoryListComponent } from './job-history-list.component';
import { JobHistoryDialogComponent } from './job-history-dialog.component';
import { EmployeeListNewComponent } from './employee-list-new.component';
import { EmployeeInputNewComponent } from './employee-input-new.component';
import { EducationListNewComponent } from './education-list-new.component';
import { EducationInputNewComponent } from './education-input-new.component';
import { JobHistoryListNewComponent } from './job-history-list-new.component';
import { JobHistoryInputNewComponent } from './job-history-input-new.component';
import { CompanyFilterSelectInputModule } from '../../components/company-filter-select-input/company-filter-select-input.module';
import { BaseListModule } from '../../components/base-list/base-list.module';
import { MatTabsModule } from '@angular/material/tabs';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { ActionModule } from '@app/components/action/action.module';
import { MatChipsModule } from '@angular/material/chips';
import { DepartmentFilterSelectInputModule } from '@app/components/department-filter-select-input/department-filter-select-input.module';
import { PositionFilterSelectInputModule } from '@app/components/position-filter-select-input/position-filter-select-input.module';

@NgModule({
  declarations: [
    EmployeeListComponent,
    EmployeeDialogComponent,
    EducationListComponent,
    EducationDialogComponent,
    JobHistoryListComponent,
    JobHistoryDialogComponent,
    EmployeeListNewComponent,
    EmployeeInputNewComponent,
    EducationListNewComponent,
    EducationInputNewComponent,
    JobHistoryListNewComponent,
    JobHistoryInputNewComponent,
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
    CompanyFilterSelectInputModule,
    DepartmentFilterSelectInputModule,
    PositionFilterSelectInputModule,
    SharedModule,
  ],
  exports: [
    EmployeeListComponent,
    EmployeeDialogComponent,
    EducationListComponent,
    EducationDialogComponent,
    JobHistoryListComponent,
    JobHistoryDialogComponent,
    EmployeeListNewComponent,
    EmployeeInputNewComponent,
    EducationListNewComponent,
    EducationInputNewComponent,
    JobHistoryListNewComponent,
    JobHistoryInputNewComponent,
  ],
})
export class EmployeeModule {}
