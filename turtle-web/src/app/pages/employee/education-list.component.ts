import { Component, Input, Output, EventEmitter } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { EducationDialogComponent } from './education-dialog.component';
import { EmployeeService } from '../../services/employee.service';
import { EmployeeEducation as Education } from '../../models/employee.model';

@Component({
  selector: 'app-education-list',
  templateUrl: './education-list.component.html',
  styleUrls: ['./education-list.component.scss'],
})
export class EducationListComponent {
  @Input() employeeId!: number;
  @Input() educations: Education[] = [];
  @Input() editMode = false;
  @Output() educationEdited = new EventEmitter<Education[]>();

  displayedColumns: string[] = [
    'institution',
    'degree',
    'major',
    'startDate',
    'endDate',
    'actions',
  ];

  constructor(private dialog: MatDialog, private employeeService: EmployeeService) {}

  onAddEducation(): void {
    const dialogRef = this.dialog.open(EducationDialogComponent, {
      width: '600px',
      data: { mode: 'add', employeeId: this.employeeId },
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.educations = [...this.educations, result];
        console.log('Updated educations', this.educations);
        this.educationEdited.emit(this.educations);
      }
    });
  }

  onEditEducation(education: Education): void {
    const dialogRef = this.dialog.open(EducationDialogComponent, {
      width: '600px',
      data: { mode: 'edit', education, employeeId: this.employeeId },
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.educations = this.educations.map(e => (e.id === result.id ? result : e));
        this.educationEdited.emit(this.educations);
      }
    });
  }

  onDeleteEducation(education: Education): void {
    if (!education.id) {
      console.error('Cannot delete education record: ID is undefined');
      return;
    }
    if (confirm('Are you sure you want to delete this education record?')) {
      this.educations = this.educations.filter(e => e.id !== education.id);
      this.educationEdited.emit(this.educations);
    }
  }
}
