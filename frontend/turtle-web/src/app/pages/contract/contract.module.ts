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
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { PersonInputModule } from '../../components/person-input/person-input.module';

import { ContractListComponent } from './contract-list.component';
import { ContractDialogComponent } from './contract-dialog.component';
import { ContractItemListComponent } from './contract-item-list.component';
import { ContractItemDialogComponent } from './contract-item-dialog.component';
import { CompanyFilterSelectInputModule } from '../../components/company-filter-select-input/company-filter-select-input.module';
import { BaseListModule } from '../../components/base-list/base-list.module';
import { MatTabGroup, MatTabsModule } from '@angular/material/tabs';
import { ContractDownPaymentListComponent } from './contract-down-payment-list.component';
import { ContractDownPaymentDialogComponent } from './contract-down-payment-dialog.component';
import { ContractInvoiceListComponent } from './contract-invoice-list.component';
import { ContractInvoiceDialogComponent } from './contract-invoice-dialog.component';
import { ActionModule } from '@app/components/action/action.module';
import { MatChipsModule } from '@angular/material/chips';
import { MaterialModule } from '@app/shared/material.module';
import { ProjectFilterSelectInputModule } from '@app/components/project-filter-select-input/project-filter-select-input.module';

@NgModule({
  declarations: [
    ContractListComponent,
    ContractDialogComponent,
    ContractItemListComponent,
    ContractDownPaymentListComponent,
    ContractDownPaymentDialogComponent,
    ContractInvoiceListComponent,
    ContractInvoiceDialogComponent,
    ContractItemDialogComponent,
  ],
  imports: [
    CommonModule,
    ActionModule,
    PersonInputModule,
    MaterialModule,
    BaseListModule,
    MatSlideToggleModule,
    ProjectFilterSelectInputModule,
    CompanyFilterSelectInputModule,
  ],
  exports: [
    ContractListComponent,
    ContractDialogComponent,
    ContractItemListComponent,
    ContractDownPaymentListComponent,
    ContractDialogComponent,
    ContractItemDialogComponent,
  ],
})
export class ContractModule {}
