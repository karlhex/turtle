import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { TranslateModule } from '@ngx-translate/core';

@Component({
  selector: 'app-base-list',
  templateUrl: './base-list.component.html',
  styleUrls: ['./base-list.component.scss'],
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    TranslateModule
  ]
})
export class BaseListComponent<T> {
  @Input() title: string = '';
  @Input() placeholder: string = 'Search...';
  @Output() search = new EventEmitter<string>();
  @Output() add = new EventEmitter<void>();

  searchText: string = '';
  items: T[] = [];
  pageIndex: number = 0;
  pageSize: number = 10;
  totalItems: number = 0;

  onSearch(): void {
    this.search.emit(this.searchText);
  }

  onAdd(): void {
    this.add.emit();
  }
}
