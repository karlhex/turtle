import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatTable } from '@angular/material/table';
import { PageEvent } from '@angular/material/paginator';
import { Project, ProjectQuery } from '../../models/project.model';
import { ProjectService } from '../../services/project.service';
import { ProjectDialogComponent } from './project-dialog.component';
import { BaseListComponent } from '../../components/base-list/base-list.component';
import { ProjectStatus } from '../../types/project-status.enum';
import { ApiResponse } from '../../models/api.model';
import { Page } from '../../models/page.model';

@Component({
  selector: 'app-project-list',
  templateUrl: './project-list.component.html',
  styleUrls: ['./project-list.component.scss']
})
export class ProjectListComponent implements OnInit {
  @ViewChild(MatTable) table!: MatTable<Project>;
  @ViewChild(BaseListComponent) baseList!: BaseListComponent;

  displayedColumns: string[] = [
    'projectNo',
    'projectName',
    'status',
    'manager',
    'startDate',
    'endDate',
    'actions'
  ];
  
  dataSource: Project[] = [];
  projectStatuses = Object.values(ProjectStatus);
  loading = false;
  totalElements = 0;
  pageSize = 10;
  pageIndex = 0;

  constructor(
    private projectService: ProjectService,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.loadProjects();
  }

  loadProjects(search?: string): void {
    this.loading = true;
    const query: ProjectQuery = {
      page: this.pageIndex,
      size: this.pageSize,
      search: search
    };

    this.projectService.getProjects(query).subscribe({
      next: (response: ApiResponse<Page<Project>>) => {
        this.dataSource = response.data.content;
        this.totalElements = response.data.totalElements;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });
  }

  onSearch(searchText: string): void {
    this.pageIndex = 0;
    this.loadProjects(searchText);
  }

  onAdd(): void {
    const dialogRef = this.dialog.open(ProjectDialogComponent, {
      width: '800px',
      data: {}
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadProjects();
      }
    });
  }

  onEdit(project: Project): void {
    const dialogRef = this.dialog.open(ProjectDialogComponent, {
      width: '800px',
      data: { project }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadProjects();
      }
    });
  }

  onPageChange(event: PageEvent): void {
    this.pageSize = event.pageSize;
    this.pageIndex = event.pageIndex;
    this.loadProjects();
  }
}
