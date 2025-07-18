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

import { BankAccountListComponent } from './bank-account-list.component';
import { BankAccountDialogComponent } from './bank-account-dialog.component';
import { BankAccountListNewComponent } from './bank-account-list-new.component';
import { BankAccountInputNewComponent } from './bank-account-input-new.component';
import { BaseListModule } from '../../components/base-list/base-list.module';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { SharedModule } from '../../shared/shared.module';

@NgModule({
  declarations: [
    BankAccountListComponent, 
    BankAccountDialogComponent,
    BankAccountListNewComponent,
    BankAccountInputNewComponent
  ],
  imports: [
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
    MatAutocompleteModule,
    MatProgressSpinnerModule,
    MatSlideToggleModule,
    TranslateModule,
    BaseListModule,
    SharedModule,
  ],
  exports: [
    BankAccountListComponent, 
    BankAccountDialogComponent,
    BankAccountListNewComponent,
    BankAccountInputNewComponent
  ],
})
export class BankAccountModule {}
