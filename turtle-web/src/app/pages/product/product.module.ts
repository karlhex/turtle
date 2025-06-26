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

import { ProductListComponent } from './product-list.component';
import { ProductDialogComponent } from './product-dialog.component';
import { ProductListNewComponent } from './product-list-new.component';
import { ProductInputNewComponent } from './product-input-new.component';
import { BaseListModule } from '../../components/base-list/base-list.module';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatTabsModule } from '@angular/material/tabs';
import { SharedModule } from '../../shared/shared.module';

@NgModule({
  declarations: [
    ProductListComponent, 
    ProductDialogComponent,
    ProductListNewComponent,
    ProductInputNewComponent
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
    MatSlideToggleModule,
    MatTabsModule,
    TranslateModule,
    BaseListModule,
    SharedModule,
  ],
  exports: [
    ProductListComponent, 
    ProductDialogComponent,
    ProductListNewComponent,
    ProductInputNewComponent
  ],
})
export class ProductModule {}
