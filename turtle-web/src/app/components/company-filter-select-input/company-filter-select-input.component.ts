import { Component, Input, TemplateRef, ViewChild } from '@angular/core';
import { NG_VALUE_ACCESSOR } from '@angular/forms';
import { Company } from '@app/models/company.model';
import { BaseFilterSelectInputComponent } from '../base-filter-select-input/base-filter-select-input.component';

@Component({
  selector: 'app-company-filter-select-input',
  templateUrl: '../base-filter-select-input/base-filter-select-input.component.html',
  styleUrls: ['../base-filter-select-input/base-filter-select-input.component.scss'],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: CompanyFilterSelectInputComponent,
      multi: true,
    },
  ],
})
export class CompanyFilterSelectInputComponent extends BaseFilterSelectInputComponent<Company> {
  @Input() override label: string = 'common.select_company';
  @ViewChild('optionTemplate', { static: true }) override optionTemplate!: TemplateRef<any>;

  @Input() set companies(value: Company[]) {
    this.items = value;
  }
  get companies(): Company[] {
    return this.items;
  }

  override displayFn(company: Company): string {
    return company ? company.fullName : '';
  }

  override getSearchFields(company: Company): (string | undefined)[] {
    return [company.fullName, company.shortName];
  }
}
