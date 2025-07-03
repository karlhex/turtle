import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { TranslateService } from '@ngx-translate/core';
import { ListPageConfig } from '../../components/list-page/list-page.component';
import { EmployeeEducation } from '../../models/employee.model';
import { EmployeeService } from '../../services/employee.service';
import { EducationInputNewComponent } from './education-input-new.component';

@Component({
  selector: 'app-education-list-new',
  templateUrl: './education-list-new.component.html',
  styleUrls: ['./education-list-new.component.scss']
})
export class EducationListNewComponent implements OnInit {
  @Input() employeeId!: number;
  @Input() educations: EmployeeEducation[] = [];
  @Input() editMode = false;
  @Output() educationEdited = new EventEmitter<EmployeeEducation[]>();

  config: ListPageConfig = {
    title: 'EMPLOYEE.EDUCATION.TITLE',
    columns: [
      {
        key: 'school',
        label: 'EMPLOYEE.EDUCATION.SCHOOL',
        type: 'text',
        sortable: true,
        width: '20%'
      },
      {
        key: 'major',
        label: 'EMPLOYEE.EDUCATION.MAJOR',
        type: 'text',
        sortable: true,
        width: '20%'
      },
      {
        key: 'degree',
        label: 'EMPLOYEE.EDUCATION.DEGREE_NAME',
        type: 'text',
        sortable: true,
        width: '15%'
      },
      {
        key: 'startDate',
        label: 'EMPLOYEE.EDUCATION.START_DATE',
        type: 'date',
        sortable: true,
        width: '15%'
      },
      {
        key: 'endDate',
        label: 'EMPLOYEE.EDUCATION.END_DATE',
        type: 'date',
        sortable: true,
        width: '15%'
      },
      {
        key: 'remarks',
        label: 'EMPLOYEE.EDUCATION.REMARKS',
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
            action: (item: EmployeeEducation) => this.onEdit(item),
            hidden: () => !this.editMode
          },
          {
            label: 'COMMON.DELETE',
            icon: 'delete',
            color: 'warn',
            action: (item: EmployeeEducation) => this.onDelete(item),
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
    searchPlaceholder: 'EMPLOYEE.EDUCATION.SEARCH.PLACEHOLDER',
    showAddButton: this.editMode
  };

  data: EmployeeEducation[] = [];
  loading = false;

  constructor(
    private employeeService: EmployeeService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar,
    private translate: TranslateService
  ) {}

  ngOnInit(): void {
    this.data = this.educations;
    this.config.showAddButton = this.editMode;
  }

  ngOnChanges(): void {
    this.data = this.educations;
    this.config.showAddButton = this.editMode;
  }

  onAdd(): void {
    const dialogRef = this.dialog.open(EducationInputNewComponent, {
      width: '800px',
      data: { mode: 'create', employeeId: this.employeeId }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.educations = [...this.educations, result];
        this.educationEdited.emit(this.educations);
        this.data = this.educations;
      }
    });
  }

  onEdit(item: EmployeeEducation): void {
    const dialogRef = this.dialog.open(EducationInputNewComponent, {
      width: '800px',
      data: { mode: 'edit', education: item, employeeId: this.employeeId }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.educations = this.educations.map(e => (e.id === result.id ? result : e));
        this.educationEdited.emit(this.educations);
        this.data = this.educations;
      }
    });
  }

  onDelete(item: EmployeeEducation): void {
    if (!item.id) {
      console.error('Cannot delete education record: ID is undefined');
      return;
    }

    if (confirm(this.translate.instant('EMPLOYEE.EDUCATION.DELETE_CONFIRM'))) {
      this.employeeService.deleteEducation(this.employeeId, item.id).subscribe({
        next: () => {
          this.educations = this.educations.filter(e => e.id !== item.id);
          this.educationEdited.emit(this.educations);
          this.data = this.educations;
          this.snackBar.open(
            this.translate.instant('EMPLOYEE.EDUCATION.DELETE_SUCCESS'),
            this.translate.instant('ACTIONS.CLOSE'),
            { duration: 3000 }
          );
        },
        error: (error: any) => {
          console.error('Error deleting education:', error);
          this.snackBar.open(
            this.translate.instant('ERROR.DELETE_EDUCATION'),
            this.translate.instant('ACTIONS.CLOSE'),
            { duration: 3000 }
          );
        }
      });
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