import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { TranslateModule } from '@ngx-translate/core';

import { CompanyFilterSelectInputComponent } from './company-filter-select-input.component';

@NgModule({
  declarations: [
    CompanyFilterSelectInputComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatAutocompleteModule,
    MatFormFieldModule,
    MatInputModule,
    TranslateModule
  ],
  exports: [
    CompanyFilterSelectInputComponent
  ]
})
export class CompanyFilterSelectInputModule { }
