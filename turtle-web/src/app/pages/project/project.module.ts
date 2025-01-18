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
import { MatTabsModule } from '@angular/material/tabs';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatChipsModule } from '@angular/material/chips';

import { ProjectListComponent } from './project-list.component';
import { ProjectDialogComponent } from './project-dialog.component';
import { BaseListModule } from '../../components/base-list/base-list.module';
import { SharedModule } from '../../shared/shared.module';
import { ActionModule } from '@app/components/action/action.module';

@NgModule({
  declarations: [
    ProjectListComponent,
    ProjectDialogComponent
  ],
  imports: [
    SharedModule,
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
    MatTabsModule,
    MatAutocompleteModule,
    MatChipsModule,
    TranslateModule,
    BaseListModule
  ],
  exports: [
    ProjectListComponent,
    ProjectDialogComponent
  ]
})
export class ProjectModule { }
