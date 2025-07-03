import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { TranslateService } from '@ngx-translate/core';
import { ListPageConfig } from '../../components/list-page/list-page.component';
import { Position } from '../../models/position.model';
import { PositionService } from '../../services/position.service';
import { PositionInputNewComponent } from './position-input-new.component';

@Component({
  selector: 'app-position-list-new',
  templateUrl: './position-list-new.component.html',
  styleUrls: ['./position-list-new.component.scss']
})
export class PositionListNewComponent implements OnInit {
  config: ListPageConfig = {
    title: 'POSITION.TITLE',
    columns: [
      {
        key: 'name',
        label: 'POSITION.NAME',
        type: 'text',
        sortable: true,
        width: '20%'
      },
      {
        key: 'code',
        label: 'POSITION.CODE',
        type: 'text',
        sortable: true,
        width: '15%'
      },
      {
        key: 'description',
        label: 'POSITION.DESCRIPTION',
        type: 'text',
        sortable: false,
        width: '25%'
      },
      {
        key: 'level',
        label: 'POSITION.LEVEL',
        type: 'number',
        sortable: true,
        width: '10%'
      },
      {
        key: 'isActive',
        label: 'POSITION.STATUS',
        type: 'boolean',
        sortable: true,
        width: '10%',
        formatter: (value: boolean) => value ? 'POSITION.ACTIVE' : 'POSITION.INACTIVE'
      },
      {
        key: 'actions',
        label: 'COMMON.ACTIONS',
        type: 'action',
        sortable: false,
        width: '20%',
        actions: [
          {
            label: 'COMMON.EDIT',
            icon: 'edit',
            color: 'primary',
            action: (item: Position) => this.onEdit(item)
          },
          {
            label: 'COMMON.DELETE',
            icon: 'delete',
            color: 'warn',
            action: (item: Position) => this.onDelete(item)
          }
        ]
      }
    ],
    pageSizeOptions: [5, 10, 25, 50],
    defaultPageSize: 10,
    showSearch: true,
    showExport: true,
    showBulkActions: false,
    searchPlaceholder: 'POSITION.SEARCH_PLACEHOLDER',
    showAddButton: true
  };

  data: Position[] = [];
  loading = false;
  totalItems = 0;
  currentPage = 0;
  pageSize = 10;
  currentSort = { column: 'id', direction: 'ASC' };
  currentSearch = '';

  constructor(
    private positionService: PositionService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar,
    private translate: TranslateService
  ) {}

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    this.loading = true;
    this.positionService.getPositions(this.currentPage, this.pageSize).subscribe({
      next: (response: any) => {
        if (response.code === 200) {
          this.data = response.data.content;
          this.totalItems = response.data.totalElements;
        }
        this.loading = false;
      },
      error: (error: any) => {
        console.error('Error loading positions:', error);
        this.snackBar.open(
          this.translate.instant('ERROR.LOAD_POSITIONS'),
          this.translate.instant('ACTIONS.CLOSE'),
          { duration: 3000 }
        );
        this.loading = false;
      }
    });
  }

  onPageChange(event: { page: number; size: number }): void {
    this.currentPage = event.page;
    this.pageSize = event.size;
    this.loadData();
  }

  onSortChange(event: { column: string; direction: string }): void {
    this.currentSort = event;
    this.loadData();
  }

  onSearchChange(query: string): void {
    this.currentSearch = query;
    this.currentPage = 0;
    this.loadData();
  }

  onAdd(): void {
    const dialogRef = this.dialog.open(PositionInputNewComponent, {
      width: '800px',
      data: { mode: 'create' }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadData();
      }
    });
  }

  onEdit(item: Position): void {
    const dialogRef = this.dialog.open(PositionInputNewComponent, {
      width: '800px',
      data: { mode: 'edit', position: item }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadData();
      }
    });
  }

  onDelete(item: Position): void {
    if (confirm(this.translate.instant('POSITION.DELETE_CONFIRM'))) {
      this.positionService.deletePosition(item.id!).subscribe({
        next: (response: any) => {
          if (response.code === 200) {
            this.snackBar.open(
              this.translate.instant('POSITION.DELETE_SUCCESS'),
              this.translate.instant('ACTIONS.CLOSE'),
              { duration: 3000 }
            );
            this.loadData();
          } else {
            this.snackBar.open(
              response.message || this.translate.instant('ERROR.DELETE_POSITION'),
              this.translate.instant('ACTIONS.CLOSE'),
              { duration: 3000 }
            );
          }
        },
        error: (error: any) => {
          console.error('Error deleting position:', error);
          this.snackBar.open(
            this.translate.instant('ERROR.DELETE_POSITION'),
            this.translate.instant('ACTIONS.CLOSE'),
            { duration: 3000 }
          );
        }
      });
    }
  }

  onExport(): void {
    // TODO: Implement export functionality
    console.log('Export positions');
  }
} 