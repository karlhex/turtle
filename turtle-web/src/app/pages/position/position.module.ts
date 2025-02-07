import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';

import { PositionListComponent } from './position-list.component';
import { PositionDialogComponent } from './position-dialog.component';
import { BaseListModule } from '../../components/base-list/base-list.module';
import { MaterialModule } from '@app/shared/material.module';
import { DepartmentFilterSelectInputModule } from '@app/components/department-filter-select-input/department-filter-select-input.module';

@NgModule({
  declarations: [
    PositionListComponent,
    PositionDialogComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    MaterialModule,
    BaseListModule,
    TranslateModule,
    DepartmentFilterSelectInputModule
  ],
  exports: [
    PositionListComponent,
    PositionDialogComponent
  ]
})
export class PositionModule { }
