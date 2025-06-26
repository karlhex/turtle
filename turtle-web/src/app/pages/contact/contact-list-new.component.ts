import { Component, OnInit, OnDestroy } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { TranslateService } from '@ngx-translate/core';
import { Subject, takeUntil } from 'rxjs';
import { ContactService } from '../../services/contact.service';
import { Contact } from '../../models/contact.model';
import { ContactInputNewComponent } from './contact-input-new.component';
import { ListPageConfig, ColumnConfig, ActionConfig } from '../../components/list-page/list-page.component';
import { ConfirmDialogComponent } from '../../components/confirmdialog/confirm-dialog.component';

@Component({
  selector: 'app-contact-list-new',
  templateUrl: './contact-list-new.component.html',
  styleUrls: ['./contact-list-new.component.scss']
})
export class ContactListNewComponent implements OnInit, OnDestroy {
  config: ListPageConfig = {
    title: 'CONTACT.TITLE',
    columns: [
      {
        key: 'firstName',
        label: 'CONTACT.FIRST_NAME',
        type: 'text',
        sortable: true,
        width: '15%'
      },
      {
        key: 'lastName',
        label: 'CONTACT.LAST_NAME',
        type: 'text',
        sortable: true,
        width: '15%'
      },
      {
        key: 'mobilePhone',
        label: 'CONTACT.MOBILE_PHONE',
        type: 'text',
        sortable: false,
        width: '15%'
      },
      {
        key: 'email',
        label: 'CONTACT.EMAIL',
        type: 'text',
        sortable: false,
        width: '20%'
      },
      {
        key: 'companyName',
        label: 'CONTACT.COMPANY',
        type: 'text',
        sortable: false,
        width: '15%'
      },
      {
        key: 'title',
        label: 'CONTACT.TITLE',
        type: 'text',
        sortable: false,
        width: '10%'
      },
      {
        key: 'active',
        label: 'CONTACT.STATUS',
        type: 'boolean',
        sortable: true,
        width: '10%',
        formatter: (value: boolean) => value ? 'CONTACT.ACTIVE' : 'CONTACT.INACTIVE'
      },
      {
        key: 'action',
        label: 'ACTIONS.ACTIONS',
        type: 'action',
        sortable: false,
        width: '10%',
        actions: [
          {
            label: 'ACTIONS.EDIT',
            icon: 'edit',
            color: 'primary',
            action: (item: Contact) => this.onEdit(item)
          },
          {
            label: 'ACTIONS.DELETE',
            icon: 'delete',
            color: 'warn',
            action: (item: Contact) => this.onDelete(item)
          }
        ]
      }
    ],
    pageSizeOptions: [10, 20, 50],
    defaultPageSize: 20,
    showSearch: true,
    showExport: true,
    showBulkActions: true,
    searchPlaceholder: 'CONTACT.SEARCH_PLACEHOLDER',
    showAddButton: true
  };

  data: Contact[] = [];
  loading = false;
  totalItems = 0;
  currentPage = 0;
  pageSize = 20;
  searchText = '';

  private destroy$ = new Subject<void>();

  constructor(
    private contactService: ContactService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar,
    private translate: TranslateService
  ) {}

  ngOnInit(): void {
    this.loadData();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  loadData(): void {
    this.loading = true;
    this.contactService.getContacts(this.currentPage, this.pageSize, this.searchText)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: response => {
          if (response.code === 200) {
            this.data = response.data.content;
            this.totalItems = response.data.totalElements;
          }
          this.loading = false;
        },
        error: error => {
          console.error('Error loading contacts:', error);
          this.snackBar.open(
            this.translate.instant('ERROR.LOAD_CONTACTS'),
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
    // 实现排序逻辑
    console.log('Sort:', event);
    this.loadData();
  }

  onSearchChange(searchText: string): void {
    this.searchText = searchText;
    this.currentPage = 0;
    this.loadData();
  }

  onRefresh(): void {
    this.loadData();
  }

  onExport(): void {
    // 实现导出逻辑
    console.log('Export contacts');
  }

  onAdd(): void {
    const dialogRef = this.dialog.open(ContactInputNewComponent, {
      width: '800px',
      data: { mode: 'create' }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadData();
      }
    });
  }

  onEdit(contact: Contact): void {
    const dialogRef = this.dialog.open(ContactInputNewComponent, {
      width: '800px',
      data: { mode: 'edit', contact }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadData();
      }
    });
  }

  onDelete(contact: Contact): void {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      width: '400px',
      data: {
        title: 'CONTACT.DELETE_CONFIRM_TITLE',
        message: 'CONTACT.DELETE_CONFIRM_MESSAGE',
        confirmText: 'ACTIONS.DELETE',
        cancelText: 'ACTIONS.CANCEL'
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.contactService.deleteContact(contact.id!).subscribe({
          next: response => {
            if (response.code === 200) {
              this.snackBar.open(
                this.translate.instant('CONTACT.DELETE_SUCCESS'),
                this.translate.instant('ACTIONS.CLOSE'),
                { duration: 3000 }
              );
              this.loadData();
            }
          },
          error: error => {
            console.error('Error deleting contact:', error);
            this.snackBar.open(
              this.translate.instant('ERROR.DELETE_CONTACT'),
              this.translate.instant('ACTIONS.CLOSE'),
              { duration: 3000 }
            );
          }
        });
      }
    });
  }
} 