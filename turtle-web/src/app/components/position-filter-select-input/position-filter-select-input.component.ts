import { Component, Input, TemplateRef, ViewChild } from '@angular/core';
import { NG_VALUE_ACCESSOR } from '@angular/forms';
import { Position } from '@app/models/position.model';
import { BaseFilterSelectInputComponent } from '../base-filter-select-input/base-filter-select-input.component';

@Component({
  selector: 'app-position-filter-select-input',
  templateUrl: '../base-filter-select-input/base-filter-select-input.component.html',
  styleUrls: ['../base-filter-select-input/base-filter-select-input.component.scss'],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: PositionFilterSelectInputComponent,
      multi: true
    }
  ]
})
export class PositionFilterSelectInputComponent extends BaseFilterSelectInputComponent<Position> {
  @Input() override label: string = 'common.select_position';
  @ViewChild('optionTemplate', { static: true }) override optionTemplate!: TemplateRef<any>;

  @Input() set positions(value: Position[]) {
    this.items = value;
  }
  get positions(): Position[] {
    return this.items;
  }

  override displayFn(position: Position): string {
    return position ? position.name + ' (' + position.code + ')' : '';
  }

  override getSearchFields(position: Position): (string | undefined)[] {
    return [position.name, position.code];
  }
}
