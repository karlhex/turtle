import { Component, Input, TemplateRef, ViewChild } from '@angular/core';
import { NG_VALUE_ACCESSOR } from '@angular/forms';
import { Department } from '@app/models/department.model';
import { BaseFilterSelectInputComponent } from '../base-filter-select-input/base-filter-select-input.component';

@Component({
  selector: 'app-department-filter-select-input',
  templateUrl: '../base-filter-select-input/base-filter-select-input.component.html',
  styleUrls: ['../base-filter-select-input/base-filter-select-input.component.scss'],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: DepartmentFilterSelectInputComponent,
      multi: true
    }
  ]
})
export class DepartmentFilterSelectInputComponent extends BaseFilterSelectInputComponent<Department> {
  @Input() override label: string = 'common.select_department';
  @ViewChild('optionTemplate', { static: true }) override optionTemplate!: TemplateRef<any>;

  @Input() set departments(value: Department[]) {
    this.items = value;
  }
  get departments(): Department[] {
    return this.items;
  }

  override displayFn(department: Department): string {
    return department ? department.name + ' (' + department.code + ')' : '';
  }

  override getSearchFields(department: Department): (string | undefined)[] {
    return [department.name, department.code];
  }
}
