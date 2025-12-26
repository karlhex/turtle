import { Component, Input, Output, EventEmitter } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ApplicationEducation } from '../../models/employee-application.model';
import { ApplicationEducationDialogComponent } from './application-education-dialog.component';

@Component({
  selector: 'app-application-education-list',
  templateUrl: './application-education-list.component.html',
  styleUrls: ['./application-education-list.component.scss'],
})
export class ApplicationEducationListComponent {
  @Input() educations: ApplicationEducation[] = [];
  @Input() editable = true;
  @Output() educationsChange = new EventEmitter<ApplicationEducation[]>();

  displayedColumns: string[] = [
    'school',
    'degree',
    'major',
    'startDate',
    'endDate',
    'actions',
  ];

  constructor(private dialog: MatDialog) {}

  onAddEducation(): void {
    const dialogRef = this.dialog.open(ApplicationEducationDialogComponent, {
      width: '600px',
      data: { mode: 'add' },
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.educations = [...this.educations, result];
        this.educationsChange.emit(this.educations);
      }
    });
  }

  onEditEducation(education: ApplicationEducation, index: number): void {
    const dialogRef = this.dialog.open(ApplicationEducationDialogComponent, {
      width: '600px',
      data: { mode: 'edit', education: { ...education } },
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.educations = this.educations.map((e, i) => (i === index ? result : e));
        this.educationsChange.emit(this.educations);
      }
    });
  }

  onDeleteEducation(index: number): void {
    if (confirm('确定要删除这条教育经历吗？')) {
      this.educations = this.educations.filter((_, i) => i !== index);
      this.educationsChange.emit(this.educations);
    }
  }

  formatDate(dateString: string): string {
    if (!dateString) return '-';
    return new Date(dateString).toLocaleDateString('zh-CN');
  }
}