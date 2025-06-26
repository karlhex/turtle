import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';

import { PositionListComponent } from './position-list.component';
import { PositionDialogComponent } from './position-dialog.component';
import { PositionListNewComponent } from './position-list-new.component';
import { PositionInputNewComponent } from './position-input-new.component';
import { BaseListModule } from '../../components/base-list/base-list.module';
import { MaterialModule } from '@app/shared/material.module';
import { DepartmentFilterSelectInputModule } from '@app/components/department-filter-select-input/department-filter-select-input.module';
import { SharedModule } from '../../shared/shared.module';

@NgModule({
  declarations: [
    PositionListComponent, 
    PositionDialogComponent,
    PositionListNewComponent,
    PositionInputNewComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    MaterialModule,
    BaseListModule,
    TranslateModule,
    SharedModule,
  ],
  exports: [
    PositionListComponent, 
    PositionDialogComponent,
    PositionListNewComponent,
    PositionInputNewComponent
  ],
})
export class PositionModule {}
