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

import { TaxInfoListComponent } from './tax-info-list.component';
import { TaxInfoDialogComponent } from './tax-info-dialog.component';
import { TaxInfoListNewComponent } from './tax-info-list-new.component';
import { TaxInfoInputNewComponent } from './tax-info-input-new.component';
import { BaseListModule } from '../../components/base-list/base-list.module';
import { ActionModule } from '@app/components/action/action.module';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { SharedModule } from '../../shared/shared.module';

@NgModule({
  declarations: [
    TaxInfoListComponent, 
    TaxInfoDialogComponent,
    TaxInfoListNewComponent,
    TaxInfoInputNewComponent
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
    MatSlideToggleModule,
    TranslateModule,
    BaseListModule,
    SharedModule,
  ],
  exports: [
    TaxInfoListComponent, 
    TaxInfoDialogComponent,
    TaxInfoListNewComponent,
    TaxInfoInputNewComponent
  ],
})
export class TaxInfoModule {}
