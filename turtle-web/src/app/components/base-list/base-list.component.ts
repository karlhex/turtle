import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { TranslateModule } from '@ngx-translate/core';
import { MatPaginator, MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { ViewChild } from '@angular/core';
import { MatProgressBarModule } from '@angular/material/progress-bar';

/**
 * @swagger
 * components:
 *   schemas:
 *     BaseListComponent:
 *       description: A reusable base component for displaying lists with search, pagination and add functionality
 *       type: object
 *       properties:
 *         title:
 *           type: string
 *           description: The title displayed at the top of the list
 *         placeholder:
 *           type: string
 *           description: Placeholder text for the search input field
 *         loading:
 *           type: boolean
 *           description: Flag indicating if the list is currently loading data
 *         pageSize:
 *           type: number
 *           description: Number of items to display per page
 *         totalElements:
 *           type: number
 *           description: Total number of items in the list
 *         searchText:
 *           type: string
 *           description: Current search query text
 *         pageIndex:
 *           type: number
 *           description: Current page index (zero-based)
 *       events:
 *         search:
 *           description: Emitted when user performs a search
 *           payload: string
 *         add:
 *           description: Emitted when user clicks the add button
 *           payload: void
 *         page:
 *           description: Emitted when user changes page
 *           payload: PageEvent
 */
@Component({
  selector: 'app-base-list',
  templateUrl: './base-list.component.html',
  styleUrls: ['./base-list.component.scss'],
})
export class BaseListComponent {
  /** Title displayed at the top of the list */
  @Input() title = '';

  /** Placeholder text for the search input field */
  @Input() placeholder = 'Search...';

  /** Flag indicating if the list is currently loading data */
  @Input() loading = false;

  /** Number of items to display per page */
  @Input() pageSize = 10;

  /** Total number of items in the list */
  @Input() totalElements = 0;

  /** Event emitted when user performs a search */
  @Output() search = new EventEmitter<string>();

  /** Event emitted when user clicks the add button */
  @Output() add = new EventEmitter<void>();

  /** Event emitted when user changes page */
  @Output() page = new EventEmitter<PageEvent>();

  /** Reference to the MatPaginator component */
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  /** Current search query text */
  searchText = '';

  /** Current page index (zero-based) */
  pageIndex = 0;

  /**
   * Handles search action
   * Emits search event with current search text and resets to first page
   */
  onSearch(): void {
    this.search.emit(this.searchText);
    if (this.paginator) {
      this.paginator.firstPage();
    }
  }

  /**
   * Handles add action
   * Emits add event
   */
  onAdd(): void {
    this.add.emit();
  }

  /**
   * Handles page change events
   * @param event - The page change event containing page size and index
   */
  onPageChange(event: PageEvent) {
    this.page.emit(event);
  }
}
