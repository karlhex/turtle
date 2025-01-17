import { Component, Input, OnInit, forwardRef } from '@angular/core';
import { FormControl, ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import { Observable } from 'rxjs';
import { map, startWith } from 'rxjs/operators';
import { Company } from '../../models/company.model';

@Component({
  selector: 'app-company-filter-select-input',
  templateUrl: './company-filter-select-input.component.html',
  styleUrls: ['./company-filter-select-input.component.scss'],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => CompanyFilterSelectInputComponent),
      multi: true
    }
  ]
})
export class CompanyFilterSelectInputComponent implements OnInit, ControlValueAccessor {
  @Input() companies: Company[] = [];
  @Input() label: string = 'common.select_company';
  
  companyCtrl = new FormControl();
  filteredCompanies: Observable<Company[]>;
  
  private onChange: (value: any) => void = () => {};
  private onTouched: () => void = () => {};

  constructor() {
    this.filteredCompanies = this.companyCtrl.valueChanges.pipe(
      startWith(''),
      map(value => this._filter(value || ''))
    );
  }

  ngOnInit(): void {
    // Subscribe to value changes to emit the company ID
    this.companyCtrl.valueChanges.subscribe(value => {
      if (typeof value === 'object' && value) {
        this.onChange(value.id);
      }
    });
  }

  displayFn(company: Company): string {
    return company && company.fullName ? company.fullName : '';
  }

  private _filter(value: string | Company): Company[] {
    const filterValue = typeof value === 'string' ? value.toLowerCase() : '';
    
    return this.companies.filter(company => {
      const searchFields = [
        company.fullName,
        company.shortName,
        company.phone,
        company.email,
        company.address
      ].map(field => field?.toLowerCase() || '');

      return searchFields.some(field => field.includes(filterValue));
    });
  }

  // ControlValueAccessor implementation
  writeValue(value: any): void {
    if (value) {
      const company = this.companies.find(c => c.id === value);
      this.companyCtrl.setValue(company, { emitEvent: false });
    } else {
      this.companyCtrl.setValue(null, { emitEvent: false });
    }
  }

  registerOnChange(fn: any): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: any): void {
    this.onTouched = fn;
  }

  setDisabledState(isDisabled: boolean): void {
    if (isDisabled) {
      this.companyCtrl.disable();
    } else {
      this.companyCtrl.enable();
    }
  }
}
