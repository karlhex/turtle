import { NgModule } from '@angular/core';
import { ProjectFilterSelectInputComponent } from './project-filter-select-input.component';
import { BaseFilterSelectInputModule } from '../base-filter-select-input/base-filter-select-input.module';

@NgModule({
  declarations: [ProjectFilterSelectInputComponent],
  imports: [BaseFilterSelectInputModule],
  exports: [ProjectFilterSelectInputComponent]
})
export class ProjectFilterSelectInputModule { }
