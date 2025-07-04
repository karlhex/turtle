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

import { InventoryListComponent } from './inventory-list.component';
import { InventoryDialogComponent } from './inventory-dialog.component';
import { InventoryListNewComponent } from './inventory-list-new.component';
import { InventoryInputNewComponent } from './inventory-input-new.component';
import { CompanyFilterSelectInputModule } from '../../components/company-filter-select-input/company-filter-select-input.module';
import { BaseListModule } from '../../components/base-list/base-list.module';
import { EmployeeFilterSelectInputModule } from '@app/components/employee-filter-select-input/employee-filter-select-input.module';
import { ContractFilterSelectInputModule } from '@app/components/contract-filter-select-input/contract-filter-select-input.module';
import { MaterialModule } from '@app/shared/material.module';
import { ProductFilterSelectInputModule } from '@app/components/product-filter-select-input/product-filter-select-input.module';
import { ProjectFilterSelectInputModule } from '@app/components/project-filter-select-input/project-filter-select-input.module';
import { SharedModule } from '../../shared/shared.module';

@NgModule({
  declarations: [
    InventoryListComponent, 
    InventoryDialogComponent,
    InventoryListNewComponent,
    InventoryInputNewComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    MaterialModule,
    BaseListModule,
    ProjectFilterSelectInputModule,
    ProductFilterSelectInputModule,
    ContractFilterSelectInputModule,
    EmployeeFilterSelectInputModule,
    CompanyFilterSelectInputModule,
    SharedModule,
  ],
  exports: [
    InventoryListComponent, 
    InventoryDialogComponent,
    InventoryListNewComponent,
    InventoryInputNewComponent
  ],
})
export class InventoryModule {}
