import { Component, Input, TemplateRef, ViewChild } from '@angular/core';
import { NG_VALUE_ACCESSOR } from '@angular/forms';
import { Product } from '@app/models/product.model';
import { BaseFilterSelectInputComponent } from '../base-filter-select-input/base-filter-select-input.component';

@Component({
  selector: 'app-product-filter-select-input',
  templateUrl: '../base-filter-select-input/base-filter-select-input.component.html',
  styleUrls: ['../base-filter-select-input/base-filter-select-input.component.scss'],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: ProductFilterSelectInputComponent,
      multi: true,
    },
  ],
})
export class ProductFilterSelectInputComponent extends BaseFilterSelectInputComponent<Product> {
  @Input() override label = 'common.select_product';
  @ViewChild('optionTemplate', { static: true }) override optionTemplate!: TemplateRef<any>;

  @Input() set products(value: Product[]) {
    this.items = value;
  }
  get products(): Product[] {
    return this.items;
  }

  override displayFn(product: Product): string {
    return product ? product.name : '';
  }

  override getSearchFields(product: Product): (string | undefined)[] {
    return [product.name, product.modelNumber, product.manufacturer?.fullName];
  }
}
