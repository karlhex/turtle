import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { TranslateService } from '@ngx-translate/core';
import { ListPageConfig } from '../../components/list-page/list-page.component';
import { EmployeeJobHistory } from '../../models/employee.model';
import { EmployeeService } from '../../services/employee.service';
import { JobHistoryInputNewComponent } from './job-history-input-new.component';

@Component({
  selector: 'app-job-history-list-new',
  templateUrl: './job-history-list-new.component.html',
  styleUrls: ['./job-history-list-new.component.scss']
})
export class JobHistoryListNewComponent implements OnInit {
  @Input() employeeId!: number;
  @Input() jobHistories: EmployeeJobHistory[] = [];
  @Input() editMode = false;
  @Output() jobHistoryEdited = new EventEmitter<EmployeeJobHistory[]>();

  config: ListPageConfig = {
    title: 'EMPLOYEE.JOB_HISTORY.TITLE',
    columns: [
      {
        key: 'companyName',
        label: 'EMPLOYEE.JOB_HISTORY.COMPANY',
        type: 'text',
        sortable: true,
        width: '20%'
      },
      {
        key: 'position',
        label: 'EMPLOYEE.JOB_HISTORY.POSITION',
        type: 'text',
        sortable: true,
        width: '20%'
      },
      {
        key: 'department',
        label: 'EMPLOYEE.JOB_HISTORY.DEPARTMENT',
        type: 'text',
        sortable: true,
        width: '15%'
      },
      {
        key: 'startDate',
        label: 'EMPLOYEE.JOB_HISTORY.START_DATE',
        type: 'date',
        sortable: true,
        width: '15%'
      },
      {
        key: 'endDate',
        label: 'EMPLOYEE.JOB_HISTORY.END_DATE',
        type: 'date',
        sortable: true,
        width: '15%',
        formatter: (value: string) => value || 'Present'
      },
      {
        key: 'jobDescription',
        label: 'EMPLOYEE.JOB_HISTORY.DESCRIPTION',
        type: 'text',
        sortable: false,
        width: '10%',
        formatter: (value: string) => value || '-'
      },
      {
        key: 'actions',
        label: 'COMMON.ACTIONS',
        type: 'action',
        sortable: false,
        width: '10%',
        actions: [
          {
            label: 'COMMON.EDIT',
            icon: 'edit',
            color: 'primary',
            action: (item: EmployeeJobHistory) => this.onEdit(item),
            hidden: () => !this.editMode
          },
          {
            label: 'COMMON.DELETE',
            icon: 'delete',
            color: 'warn',
            action: (item: EmployeeJobHistory) => this.onDelete(item),
            hidden: () => !this.editMode
          }
        ]
      }
    ],
    pageSizeOptions: [5, 10, 25],
    defaultPageSize: 10,
    showSearch: true,
    showExport: false,
    showBulkActions: false,
    searchPlaceholder: 'EMPLOYEE.JOB_HISTORY.SEARCH.PLACEHOLDER',
    showAddButton: this.editMode
  };

  data: EmployeeJobHistory[] = [];
  loading = false;

  constructor(
    private employeeService: EmployeeService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar,
    private translate: TranslateService
  ) {}

  ngOnInit(): void {
    this.data = this.jobHistories;
    this.config.showAddButton = this.editMode;
  }

  ngOnChanges(): void {
    this.data = this.jobHistories;
    this.config.showAddButton = this.editMode;
  }

  onAdd(): void {
    const dialogRef = this.dialog.open(JobHistoryInputNewComponent, {
      width: '800px',
      data: { mode: 'create', employeeId: this.employeeId }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.jobHistories = [...this.jobHistories, result];
        this.jobHistoryEdited.emit(this.jobHistories);
        this.data = this.jobHistories;
      }
    });
  }

  onEdit(item: EmployeeJobHistory): void {
    const dialogRef = this.dialog.open(JobHistoryInputNewComponent, {
      width: '800px',
      data: { mode: 'edit', jobHistory: item, employeeId: this.employeeId }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.jobHistories = this.jobHistories.map(j => (j.id === result.id ? result : j));
        this.jobHistoryEdited.emit(this.jobHistories);
        this.data = this.jobHistories;
      }
    });
  }

  onDelete(item: EmployeeJobHistory): void {
    if (!item.id) {
      console.error('Cannot delete job history record: ID is undefined');
      return;
    }

    if (confirm(this.translate.instant('EMPLOYEE.JOB_HISTORY.DELETE_CONFIRM'))) {
      // Note: EmployeeService doesn't have deleteJobHistory method, so we'll just remove from local array
      this.jobHistories = this.jobHistories.filter(j => j.id !== item.id);
      this.jobHistoryEdited.emit(this.jobHistories);
      this.data = this.jobHistories;
      this.snackBar.open(
        this.translate.instant('EMPLOYEE.JOB_HISTORY.DELETE_SUCCESS'),
        this.translate.instant('ACTIONS.CLOSE'),
        { duration: 3000 }
      );
    }
  }

  onPageChange(event: { page: number; size: number }): void {
    // Not needed for embedded list
  }

  onSortChange(event: { column: string; direction: string }): void {
    // Not needed for embedded list
  }

  onSearchChange(query: string): void {
    // Not needed for embedded list
  }

  onExport(): void {
    // Not needed for embedded list
  }
} 