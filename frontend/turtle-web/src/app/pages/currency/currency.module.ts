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

import { CurrencyListComponent } from './currency-list.component';
import { CurrencyDialogComponent } from './currency-dialog.component';
import { CurrencyListNewComponent } from './currency-list-new.component';
import { CurrencyInputNewComponent } from './currency-input-new.component';
import { BaseListModule } from '../../components/base-list/base-list.module';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { ActionModule } from '@app/components/action/action.module';
import { SharedModule } from '../../shared/shared.module';

@NgModule({
  declarations: [
    CurrencyListComponent, 
    CurrencyDialogComponent,
    CurrencyListNewComponent,
    CurrencyInputNewComponent
  ],
  imports: [
    ActionModule,
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    MatSlideToggleModule,
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
    CurrencyListComponent, 
    CurrencyDialogComponent,
    CurrencyListNewComponent,
    CurrencyInputNewComponent
  ],
})
export class CurrencyModule {}
