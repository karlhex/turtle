import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { TranslateService } from '@ngx-translate/core';
import { ListPageConfig } from '../../components/list-page/list-page.component';
import { User } from '../../services/user.service';
import { UserService } from '../../services/user.service';
import { UserInputNewComponent } from './user-input-new.component';

@Component({
  selector: 'app-user-list-new',
  templateUrl: './user-list-new.component.html',
  styleUrls: ['./user-list-new.component.scss']
})
export class UserListNewComponent implements OnInit {
  config: ListPageConfig = {
    title: 'USER.TITLE',
    columns: [
      {
        key: 'username',
        label: 'USER.USERNAME',
        type: 'text',
        sortable: true,
        width: '15%'
      },
      {
        key: 'email',
        label: 'USER.EMAIL',
        type: 'text',
        sortable: true,
        width: '20%'
      },
      {
        key: 'status',
        label: 'USER.STATUS',
        type: 'text',
        sortable: true,
        width: '10%'
      },
      {
        key: 'roleNames',
        label: 'USER.ROLES',
        type: 'text',
        sortable: false,
        width: '20%',
        formatter: (value: string[]) => value ? value.join(', ') : ''
      },
      {
        key: 'createdAt',
        label: 'USER.CREATED_AT',
        type: 'date',
        sortable: true,
        width: '15%'
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
            action: (item: User) => this.onEdit(item)
          },
          {
            label: 'COMMON.DELETE',
            icon: 'delete',
            color: 'warn',
            action: (item: User) => this.onDelete(item)
          }
        ]
      }
    ],
    pageSizeOptions: [5, 10, 25, 50],
    defaultPageSize: 10,
    showSearch: true,
    showExport: true,
    showBulkActions: false,
    searchPlaceholder: 'USER.SEARCH_PLACEHOLDER',
    showAddButton: true
  };

  data: User[] = [];
  loading = false;
  totalItems = 0;
  currentPage = 0;
  pageSize = 10;
  currentSort = { column: 'id', direction: 'ASC' };
  currentSearch = '';

  constructor(
    private userService: UserService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar,
    private translate: TranslateService
  ) {}

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    this.loading = true;
    this.userService.getUsers(this.currentPage, this.pageSize, {
      sortBy: this.currentSort.column,
      direction: this.currentSort.direction as 'ASC' | 'DESC'
    }).subscribe({
      next: (response: any) => {
        if (response.code === 200) {
          this.data = response.data.content;
          this.totalItems = response.data.totalElements;
        }
        this.loading = false;
      },
      error: (error: any) => {
        console.error('Error loading users:', error);
        this.snackBar.open(
          this.translate.instant('ERROR.LOAD_USERS'),
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
    if (query) {
      this.userService.searchUsers(query, this.currentPage, this.pageSize).subscribe({
        next: (response: any) => {
          if (response.code === 200) {
            this.data = response.data.content;
            this.totalItems = response.data.totalElements;
          }
          this.loading = false;
        },
        error: (error: any) => {
          console.error('Error searching users:', error);
          this.loading = false;
        }
      });
    } else {
      this.loadData();
    }
  }

  onAdd(): void {
    const dialogRef = this.dialog.open(UserInputNewComponent, {
      width: '800px',
      data: { mode: 'create' }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadData();
      }
    });
  }

  onEdit(item: User): void {
    const dialogRef = this.dialog.open(UserInputNewComponent, {
      width: '800px',
      data: { mode: 'edit', user: item }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadData();
      }
    });
  }

  onDelete(item: User): void {
    if (confirm(this.translate.instant('USER.DELETE_CONFIRM'))) {
      this.userService.deleteUser(item.id).subscribe({
        next: (response: any) => {
          if (response.code === 200) {
            this.snackBar.open(
              this.translate.instant('USER.DELETE_SUCCESS'),
              this.translate.instant('ACTIONS.CLOSE'),
              { duration: 3000 }
            );
            this.loadData();
          } else {
            this.snackBar.open(
              response.message || this.translate.instant('ERROR.DELETE_USER'),
              this.translate.instant('ACTIONS.CLOSE'),
              { duration: 3000 }
            );
          }
        },
        error: (error: any) => {
          console.error('Error deleting user:', error);
          this.snackBar.open(
            this.translate.instant('ERROR.DELETE_USER'),
            this.translate.instant('ACTIONS.CLOSE'),
            { duration: 3000 }
          );
        }
      });
    }
  }

  onExport(): void {
    // TODO: Implement export functionality
    console.log('Export users');
  }
} 