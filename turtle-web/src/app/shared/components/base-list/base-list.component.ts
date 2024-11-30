import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-base-list',
  templateUrl: './base-list.component.html',
  styleUrls: ['./base-list.component.scss']
})
export class BaseListComponent {
  @Input() title: string = '';
  @Input() placeholder: string = 'Search...';
  @Output() search = new EventEmitter<string>();
  @Output() add = new EventEmitter<void>();

  searchText: string = '';

  onSearch(): void {
    this.search.emit(this.searchText);
  }

  onAdd(): void {
    this.add.emit();
  }
}
