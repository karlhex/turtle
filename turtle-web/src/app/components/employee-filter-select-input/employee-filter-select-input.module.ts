import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { TranslateModule } from '@ngx-translate/core';

import { EmployeeFilterSelectInputComponent } from './employee-filter-select-input.component';

@NgModule({
  declarations: [
    EmployeeFilterSelectInputComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    MatAutocompleteModule,
    MatFormFieldModule,
    MatInputModule,
    TranslateModule
  ],
  exports: [
    EmployeeFilterSelectInputComponent
  ]
})
export class EmployeeFilterSelectInputModule { }
