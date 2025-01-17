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

import { ContractListComponent } from './contract-list.component';
import { ContractDialogComponent } from './contract-dialog.component';
import { ContractItemListComponent } from './contract-item-list.component';
import { ContractItemDialogComponent } from './contract-item-dialog.component';
import { CompanyFilterSelectInputModule } from '../../components/company-filter-select-input/company-filter-select-input.module';
import { BaseListModule } from '../../components/base-list/base-list.module';

@NgModule({
  declarations: [
    ContractListComponent,
    ContractDialogComponent,
    ContractItemListComponent,
    ContractItemDialogComponent
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
    MatDatepickerModule,
    MatNativeDateModule,
    MatButtonModule,
    MatIconModule,
    MatMenuModule,
    MatProgressBarModule,
    MatTooltipModule,
    TranslateModule,
    BaseListModule,
    CompanyFilterSelectInputModule
  ],
  exports: [
    ContractListComponent,
    ContractDialogComponent,
    ContractItemListComponent,
    ContractItemDialogComponent
  ]
})
export class ContractModule { }
