import { Component, TemplateRef, ViewChild, Input } from '@angular/core';
import { BaseFilterSelectInputComponent } from '../base-filter-select-input/base-filter-select-input.component';
import { Project } from '@app/models/project.model';
import { NG_VALUE_ACCESSOR } from '@angular/forms';

@Component({
  selector: 'app-project-filter-select-input',
  templateUrl: '../base-filter-select-input/base-filter-select-input.component.html',
  styleUrls: ['../base-filter-select-input/base-filter-select-input.component.scss'],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: ProjectFilterSelectInputComponent,
      multi: true,
    },
  ],
})
export class ProjectFilterSelectInputComponent extends BaseFilterSelectInputComponent<Project> {
  @Input() override label: string = 'common.select_project';
  @ViewChild('optionTemplate', { static: true }) override optionTemplate!: TemplateRef<any>;

  override displayFn(project: Project): string {
    return project ? project.projectName : '';
  }

  override getSearchFields(project: Project): (string | undefined)[] {
    return [project.projectName, project.projectNo];
  }
}
