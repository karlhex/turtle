import { Component, Input, TemplateRef, ViewChild } from '@angular/core';
import { NG_VALUE_ACCESSOR } from '@angular/forms';
import { Employee } from '@app/models/employee.model';
import { BaseFilterSelectInputComponent } from '../base-filter-select-input/base-filter-select-input.component';

@Component({
  selector: 'app-employee-filter-select-input',
  templateUrl: '../base-filter-select-input/base-filter-select-input.component.html',
  styleUrls: ['../base-filter-select-input/base-filter-select-input.component.scss'],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: EmployeeFilterSelectInputComponent,
      multi: true
    }
  ]
})
export class EmployeeFilterSelectInputComponent extends BaseFilterSelectInputComponent<Employee> {
  @Input() override label: string = 'common.select_employee';
  @ViewChild('optionTemplate', { static: true }) override optionTemplate!: TemplateRef<any>;

  @Input() set employees(value: Employee[]) {
    this.items = value;
  }
  get employees(): Employee[] {
    return this.items;
  }

  override displayFn(employee: Employee): string {
    return employee ? employee.name : '';
  }

  override getSearchFields(employee: Employee): (string | undefined)[] {
    return [employee.name, employee.employeeNumber, employee.phone, employee.email, employee.department?.name];
  }
}
