import { Component, Input, TemplateRef, ViewChild } from '@angular/core';
import { NG_VALUE_ACCESSOR } from '@angular/forms';
import { Contract } from '@app/models/contract.model';
import { BaseFilterSelectInputComponent } from '../base-filter-select-input/base-filter-select-input.component';

@Component({
  selector: 'app-contract-filter-select-input',
  templateUrl: '../base-filter-select-input/base-filter-select-input.component.html',
  styleUrls: ['../base-filter-select-input/base-filter-select-input.component.scss'],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: ContractFilterSelectInputComponent,
      multi: true,
    },
  ],
})
export class ContractFilterSelectInputComponent extends BaseFilterSelectInputComponent<Contract> {
  @Input() override label = 'common.select_contract';
  @ViewChild('optionTemplate', { static: true }) override optionTemplate!: TemplateRef<any>;

  @Input() set contracts(value: Contract[]) {
    this.items = value;
  }
  get contracts(): Contract[] {
    return this.items;
  }

  override displayFn(contract: Contract): string {
    return contract ? contract.contractNo : '';
  }

  override getSearchFields(contract: Contract): (string | undefined)[] {
    return [
      contract.contractNo,
      contract.title,
      contract.buyerCompany?.fullName,
      contract.sellerCompany?.fullName,
    ];
  }
}
