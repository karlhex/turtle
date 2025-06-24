import { Component, Input, Output, EventEmitter } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { JobHistoryDialogComponent } from './job-history-dialog.component';
import { EmployeeService } from '../../services/employee.service';
import { EmployeeJobHistory as JobHistory } from '../../models/employee.model';

@Component({
  selector: 'app-job-history-list',
  templateUrl: './job-history-list.component.html',
  styleUrls: ['./job-history-list.component.scss'],
})
export class JobHistoryListComponent {
  @Input() employeeId!: number;
  @Input() jobHistories: JobHistory[] = [];
  @Input() editMode: boolean = false;
  @Output() jobHistoryEdited = new EventEmitter<JobHistory[]>();

  displayedColumns: string[] = ['position', 'department', 'startDate', 'endDate', 'actions'];

  constructor(private dialog: MatDialog, private employeeService: EmployeeService) {}

  onAddJobHistory(): void {
    const dialogRef = this.dialog.open(JobHistoryDialogComponent, {
      width: '600px',
      data: { mode: 'add', employeeId: this.employeeId },
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.jobHistories = [...this.jobHistories, result];
        this.jobHistoryEdited.emit(this.jobHistories);
      }
    });
  }

  onEditJobHistory(jobHistory: JobHistory): void {
    const dialogRef = this.dialog.open(JobHistoryDialogComponent, {
      width: '600px',
      data: { mode: 'edit', jobHistory, employeeId: this.employeeId },
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.jobHistories = this.jobHistories.map(j => (j.id === result.id ? result : j));
        this.jobHistoryEdited.emit(this.jobHistories);
      }
    });
  }

  onDeleteJobHistory(jobHistory: JobHistory): void {
    if (!jobHistory.id) {
      console.error('Cannot delete job history record: ID is undefined');
      return;
    }
    if (confirm('Are you sure you want to delete this job history record?')) {
      this.jobHistories = this.jobHistories.filter(j => j.id !== jobHistory.id);
      this.jobHistoryEdited.emit(this.jobHistories);
    }
  }
}
