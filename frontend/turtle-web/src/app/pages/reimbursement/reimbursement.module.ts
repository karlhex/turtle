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

import { ReimbursementListComponent } from './reimbursement-list/reimbursement-list.component';
import { ReimbursementDialogComponent } from './reimbursement-dialog/reimbursement-dialog.component';
import { ReimbursementListNewComponent } from './reimbursement-list-new.component';
import { ReimbursementInputNewComponent } from './reimbursement-input-new.component';
import { BaseListModule } from '../../components/base-list/base-list.module';
import { ActionModule } from '@app/components/action/action.module';
import { SharedModule } from '../../shared/shared.module';

@NgModule({
  declarations: [
    ReimbursementListComponent, 
    ReimbursementDialogComponent,
    ReimbursementListNewComponent,
    ReimbursementInputNewComponent
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
    TranslateModule,
    BaseListModule,
    SharedModule,
  ],
  exports: [
    ReimbursementListComponent, 
    ReimbursementDialogComponent,
    ReimbursementListNewComponent,
    ReimbursementInputNewComponent
  ],
})
export class ReimbursementModule {}
