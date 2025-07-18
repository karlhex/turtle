import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatTableModule } from '@angular/material/table';
import { MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatTooltipModule } from '@angular/material/tooltip';
import { TranslateModule } from '@ngx-translate/core';

import { DepartmentListComponent } from './department-list.component';
import { DepartmentDialogComponent } from './department-dialog.component';
import { DepartmentListNewComponent } from './department-list-new.component';
import { DepartmentInputNewComponent } from './department-input-new.component';
import { BaseListModule } from '../../components/base-list/base-list.module';
import { ActionModule } from '@app/components/action/action.module';
import { SharedModule } from '../../shared/shared.module';

@NgModule({
  declarations: [
    DepartmentListComponent, 
    DepartmentDialogComponent,
    DepartmentListNewComponent,
    DepartmentInputNewComponent
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
    MatButtonModule,
    MatIconModule,
    MatMenuModule,
    MatProgressBarModule,
    MatTooltipModule,
    TranslateModule,
    BaseListModule,
    SharedModule,
  ],
  exports: [
    DepartmentListComponent, 
    DepartmentDialogComponent,
    DepartmentListNewComponent,
    DepartmentInputNewComponent
  ],
})
export class DepartmentModule {}
