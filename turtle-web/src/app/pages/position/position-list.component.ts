import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatTable } from '@angular/material/table';
import { PageEvent } from '@angular/material/paginator';
import { Position } from '../../models/position.model';
import { PositionService } from '../../services/position.service';
import { PositionDialogComponent } from './position-dialog.component';
import { BaseListComponent } from '../../components/base-list/base-list.component';
import { TranslateService } from '@ngx-translate/core';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-position-list',
  templateUrl: './position-list.component.html',
  styleUrls: ['./position-list.component.scss']
})
export class PositionListComponent implements OnInit {
  @ViewChild(MatTable) table!: MatTable<Position>;
  @ViewChild(BaseListComponent) baseList!: BaseListComponent;

  displayedColumns: string[] = [
    'name',
    'code',
    'level',
    'isActive',
    'actions'
  ];
  
  dataSource: Position[] = [];
  loading = false;
  totalElements = 0;
  pageSize = 10;
  pageIndex = 0;

  constructor(
    private positionService: PositionService,
    private dialog: MatDialog,
    private translate: TranslateService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    this.loading = true;
    this.positionService.getPositions(this.pageIndex, this.pageSize)
      .subscribe({
        next: (response) => {
          console.log(response);
          if (response.code !== 200) {
            this.showError('position.load_error');
            return;
          }
          this.dataSource = response.data.content;
          this.totalElements = response.data.totalElements;
          this.loading = false;
          console.log(this.dataSource);
        },
        error: (error) => {
          console.error('Error loading positions:', error);
          this.loading = false;
          this.showError('position.load_error');
        }
      });
  }

  onPageChange(event: PageEvent): void {
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;
    this.loadData();
  }

  onSearch(query: string): void {
    // Implement search functionality when backend supports it
    console.log('Search query:', query);
  }

  onAdd(): void {
    const dialogRef = this.dialog.open(PositionDialogComponent, {
      width: '600px',
      data: { mode: 'create' }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadData();
      }
    });
  }

  onEdit(position: Position): void {
    const dialogRef = this.dialog.open(PositionDialogComponent, {
      width: '600px',
      data: { mode: 'edit', position }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadData();
      }
    });
  }

  onDelete(position: Position): void {
    if (confirm(this.translate.instant('position.delete_confirm'))) {
      this.positionService.deletePosition(position.id).subscribe({
        next: () => {
          this.loadData();
          this.showSuccess('position.delete_success');
        },
        error: (error) => {
          console.error('Error deleting position:', error);
          this.showError('position.delete_error');
        }
      });
    }
  }

  private showSuccess(message: string): void {
    this.snackBar.open(this.translate.instant(message), 'OK', {
      duration: 3000,
      horizontalPosition: 'end',
      verticalPosition: 'top'
    });
  }

  private showError(message: string): void {
    this.snackBar.open(this.translate.instant(message), 'OK', {
      duration: 5000,
      horizontalPosition: 'end',
      verticalPosition: 'top',
      panelClass: ['error-snackbar']
    });
  }
}
