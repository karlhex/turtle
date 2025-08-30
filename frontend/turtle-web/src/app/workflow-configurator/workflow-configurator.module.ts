import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { DragDropModule } from '@angular/cdk/drag-drop';

import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatCardModule } from '@angular/material/card';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { FormsModule } from '@angular/forms';

import { WorkflowCanvasComponent } from './workflow-canvas/workflow-canvas.component';
import { NodePaletteComponent } from './node-palette/node-palette.component';
import { PropertyPanelComponent } from './property-panel/property-panel.component';
import { WorkflowToolbarComponent } from './workflow-toolbar/workflow-toolbar.component';
import { WorkflowManagementComponent } from './workflow-management/workflow-management.component';
import { WorkflowConfiguratorRoutingModule } from './workflow-configurator-routing.module';

@NgModule({
  declarations: [
    WorkflowCanvasComponent,
    NodePaletteComponent,
    PropertyPanelComponent,
    WorkflowToolbarComponent,
    WorkflowManagementComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    DragDropModule,
    MatIconModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatSnackBarModule,
    MatCardModule,
    MatToolbarModule,
    MatTooltipModule,
    MatProgressSpinnerModule,
    FormsModule,
    WorkflowConfiguratorRoutingModule
  ],
  exports: [
    WorkflowManagementComponent
  ]
})
export class WorkflowConfiguratorModule { }