import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatTable } from '@angular/material/table';
import { PageEvent } from '@angular/material/paginator';
import { MatSnackBar } from '@angular/material/snack-bar';
import { TranslateService } from '@ngx-translate/core';
import { Contact } from '../../models/contact.model';
import { ContactService } from '../../services/contact.service';
import { ContactDialogComponent } from './contact-dialog.component';
import { BaseListComponent } from '../../components/base-list/base-list.component';
import { ConfirmDialogComponent } from '../../components/confirmdialog/confirm-dialog.component';

@Component({
  selector: 'app-contact-list',
  templateUrl: './contact-list.component.html',
  styleUrls: ['./contact-list.component.scss']
})
export class ContactListComponent implements OnInit {
  @ViewChild(MatTable) table!: MatTable<Contact>;
  @ViewChild(BaseListComponent) baseList!: BaseListComponent;

  displayedColumns: string[] = [
    'name',
    'email',
    'mobilePhone',
    'companyName',
    'department',
    'active',
    'actions'
  ];
  
  dataSource: Contact[] = [];
  loading = false;
  totalElements = 0;
  pageSize = 10;
  pageIndex = 0;
  searchText = '';

  constructor(
    private contactService: ContactService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar,
    private translate: TranslateService
  ) {}

  ngOnInit(): void {
    this.loadContacts();
  }

  loadContacts(event?: PageEvent): void {
    this.loading = true;
    if (event) {
      this.pageIndex = event.pageIndex;
      this.pageSize = event.pageSize;
    }

    this.contactService.getContacts(this.pageIndex, this.pageSize, this.searchText)
      .subscribe({
        next: (response) => {
          console.log('Response:', response);
          console.log('Data:', response.data);
          console.log('Content:', response.data?.content);
          if (response.code === 200) {
            // 检查response.data是否是数组
            if (Array.isArray(response.data)) {
              this.dataSource = response.data;
              this.totalElements = response.data.length;
            } 
            // 检查response.data是否是分页对象
            else if (response.data && response.data.content) {
              this.dataSource = response.data.content;
              this.totalElements = response.data.totalElements;
            }
            // 如果都不是，可能数据直接在response中
            else if (Array.isArray(response)) {
              this.dataSource = response;
              this.totalElements = response.length;
            }
          }
          this.loading = false;
        },
        error: (error) => {
          console.error('Error fetching contacts:', error);
          this.loading = false;
        }
      });
  }

  onSearch(searchText: string): void {
    this.searchText = searchText;
    this.pageIndex = 0;
    this.loadContacts();
  }

  onPageChange(event: PageEvent): void {
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;
    this.loadContacts(event);
  }

  onAdd(): void {
    const dialogRef = this.dialog.open(ContactDialogComponent, {
      width: '900px',
      data: { mode: 'create' }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadContacts();
      }
    });
  }

  onEdit(contact: Contact): void {
    const dialogRef = this.dialog.open(ContactDialogComponent, {
      width: '900px',
      data: { mode: 'edit', contact }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadContacts();
      }
    });
  }

  onDelete(contact: Contact): void {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: {
        title: 'CONTACT.DELETE_TITLE',
        message: 'CONTACT.DELETE_CONFIRM',
        confirmText: 'ACTIONS.DELETE',
        cancelText: 'ACTIONS.CANCEL'
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loading = true;
        this.contactService.deleteContact(contact.id!).subscribe({
          next: (response) => {
            if (response.code === 200) {
              this.snackBar.open(
                this.translate.instant('CONTACT.DELETE_SUCCESS'),
                this.translate.instant('ACTIONS.CLOSE'),
                { duration: 3000 }
              );
              this.loadContacts();
            }
          },
          error: (error) => {
            console.error('Error deleting contact:', error);
            this.snackBar.open(
              this.translate.instant('ERROR.DELETE_CONTACT'),
              this.translate.instant('ACTIONS.CLOSE'),
              { duration: 3000 }
            );
            this.loading = false;
          }
        });
      }
    });
  }

  onToggleStatus(contact: Contact): void {
    this.contactService.toggleStatus(contact.id!).subscribe({
      next: (response) => {
        if (response.code === 200) {
          this.snackBar.open(
            this.translate.instant('CONTACT.STATUS_UPDATED'),
            this.translate.instant('ACTIONS.CLOSE'),
            { duration: 3000 }
          );
          this.loadContacts();
        }
      },
      error: (error) => {
        console.error('Error updating contact status:', error);
        this.snackBar.open(
          this.translate.instant('ERROR.UPDATE_STATUS'),
          this.translate.instant('ACTIONS.CLOSE'),
          { duration: 3000 }
        );
      }
    });
  }

  getFullName(contact: Contact): string {
    return `${contact.lastName || ''} ${contact.firstName || ''}`.trim();
  }
}
