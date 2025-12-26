import { Component, Input, Output, EventEmitter } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ApplicationJobHistory } from '../../models/employee-application.model';
import { ApplicationJobHistoryDialogComponent } from './application-job-history-dialog.component';

@Component({
  selector: 'app-application-job-history-list',
  templateUrl: './application-job-history-list.component.html',
  styleUrls: ['./application-job-history-list.component.scss'],
})
export class ApplicationJobHistoryListComponent {
  @Input() jobHistories: ApplicationJobHistory[] = [];
  @Input() editable = true;
  @Output() jobHistoriesChange = new EventEmitter<ApplicationJobHistory[]>();

  displayedColumns: string[] = [
    'companyName',
    'position',
    'department',
    'startDate',
    'endDate',
    'actions',
  ];

  constructor(private dialog: MatDialog) {}

  onAddJobHistory(): void {
    const dialogRef = this.dialog.open(ApplicationJobHistoryDialogComponent, {
      width: '700px',
      data: { mode: 'add' },
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.jobHistories = [...this.jobHistories, result];
        this.jobHistoriesChange.emit(this.jobHistories);
      }
    });
  }

  onEditJobHistory(jobHistory: ApplicationJobHistory, index: number): void {
    const dialogRef = this.dialog.open(ApplicationJobHistoryDialogComponent, {
      width: '700px',
      data: { mode: 'edit', jobHistory: { ...jobHistory } },
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.jobHistories = this.jobHistories.map((j, i) => (i === index ? result : j));
        this.jobHistoriesChange.emit(this.jobHistories);
      }
    });
  }

  onDeleteJobHistory(index: number): void {
    if (confirm('确定要删除这条工作经历吗？')) {
      this.jobHistories = this.jobHistories.filter((_, i) => i !== index);
      this.jobHistoriesChange.emit(this.jobHistories);
    }
  }

  formatDate(dateString?: string): string {
    if (!dateString) return '-';
    return new Date(dateString).toLocaleDateString('zh-CN');
  }

  getDateRangeDisplay(startDate: string, endDate?: string): string {
    const start = this.formatDate(startDate);
    const end = endDate ? this.formatDate(endDate) : '至今';
    return `${start} - ${end}`;
  }
}