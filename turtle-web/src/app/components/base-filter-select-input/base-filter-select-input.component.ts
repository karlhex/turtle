import { Input, OnInit, Directive, TemplateRef } from '@angular/core';
import { FormControl, ControlValueAccessor } from '@angular/forms';
import { Observable } from 'rxjs';
import { map, startWith } from 'rxjs/operators';

interface BaseItem {
  id: any;
}

@Directive()
export abstract class BaseFilterSelectInputComponent<T extends BaseItem> implements OnInit, ControlValueAccessor {
  @Input() set items(value: T[]) {
    this._items = value;
    if (this._initialValue !== undefined) {
      this.writeValue(this._initialValue);
      this._initialValue = undefined;
    }
  }
  get items(): T[] {
    return this._items;
  }
  
  @Input() label: string = 'common.select_item';
  @Input() optionTemplate?: TemplateRef<any>;
  
  itemCtrl = new FormControl();
  filteredItems: Observable<T[]>;
  
  private _items: T[] = [];
  private _initialValue: any;
  protected onChange: (value: any) => void = () => {};
  protected onTouched: () => void = () => {};

  constructor() {
    this.filteredItems = this.itemCtrl.valueChanges.pipe(
      startWith(''),
      map(value => this._filter(value || ''))
    );
  }

  ngOnInit(): void {
    this.itemCtrl.valueChanges.subscribe(value => {
      if (value === null) {
        this.onChange(null);
      } else if (typeof value === 'object') {
        this.onChange(value.id);
      }
      this.onTouched();
    });
  }

  abstract displayFn(item: T): string;

  protected abstract getSearchFields(item: T): (string | undefined)[];

  protected _filter(value: string | T): T[] {
    if (!value) return this.items;
    
    const filterValue = typeof value === 'string' ? value.toLowerCase() : '';
    
    return this.items.filter(item => {
      if (!filterValue) return true;
      
      const searchFields = this.getSearchFields(item)
        .map(field => field?.toLowerCase() || '');

      return searchFields.some(field => field.includes(filterValue));
    });
  }

  writeValue(value: any): void {
    if (!this.items || this.items.length === 0) {
      this._initialValue = value;
      return;
    }

    if (value === null || value === undefined) {
      this.itemCtrl.setValue(null, { emitEvent: false });
    } else {
      const item = this.items.find(i => i.id === value);
      if (item) {
        this.itemCtrl.setValue(item, { emitEvent: false });
      } else {
        console.warn(`Item with id ${value} not found`);
        this.itemCtrl.setValue(null, { emitEvent: false });
      }
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
      this.itemCtrl.disable();
    } else {
      this.itemCtrl.enable();
    }
  }
}
