import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatTableModule } from '@angular/material/table';
import { MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatChipsModule } from '@angular/material/chips';
import { TranslateModule } from '@ngx-translate/core';

import { UserManagementComponent } from './user-management.component';
import { UserEditDialogComponent } from './user-edit-dialog/user-edit-dialog.component';
import { UserEmployeeMappingComponent } from './user-employee-mapping/user-employee-mapping.component';
import { ChangePasswordDialogComponent } from './change-password-dialog/change-password-dialog.component';
import { BaseListModule } from '../../components/base-list/base-list.module';

@NgModule({
  declarations: [
    UserManagementComponent,
    UserEditDialogComponent,
    UserEmployeeMappingComponent,
    ChangePasswordDialogComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    MatTableModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatIconModule,
    MatMenuModule,
    MatProgressBarModule,
    MatTooltipModule,
    MatChipsModule,
    TranslateModule,
    BaseListModule
  ],
  exports: [
    UserManagementComponent,
    UserEditDialogComponent,
    UserEmployeeMappingComponent,
    ChangePasswordDialogComponent
  ]
})
export class UserManagementModule { }
