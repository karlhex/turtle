import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { ConfirmDialogComponent } from '../components/confirmdialog/confirm-dialog.component';
import { ActionComponent } from './components/action/action.component';
import { BaseListComponent } from './components/base-list/base-list.component';

// Material Modules
import { MatCardModule } from '@angular/material/card';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDialogModule } from '@angular/material/dialog';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatDividerModule } from '@angular/material/divider';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatTabsModule } from '@angular/material/tabs';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatListModule } from '@angular/material/list';
import { MatChipsModule } from '@angular/material/chips';
import { MatMenuModule } from '@angular/material/menu';

// AoT requires an exported function for factories
export function HttpLoaderFactory(http: HttpClient) {
  return new TranslateHttpLoader(http);
}

const MATERIAL_MODULES = [
  MatCardModule,
  MatInputModule,
  MatButtonModule,
  MatIconModule,
  MatDialogModule,
  MatTableModule,
  MatPaginatorModule,
  MatSortModule,
  MatSnackBarModule,
  MatProgressSpinnerModule,
  MatProgressBarModule,
  MatDatepickerModule,
  MatNativeDateModule,
  MatSlideToggleModule,
  MatTooltipModule,
  MatFormFieldModule,
  MatSelectModule,
  MatDividerModule,
  MatAutocompleteModule,
  MatTabsModule,
  MatToolbarModule,
  MatSidenavModule,
  MatListModule,
  MatChipsModule,
  MatMenuModule
];

const SHARED_MODULES = [
  CommonModule,
  FormsModule,
  ReactiveFormsModule,
  HttpClientModule,
  TranslateModule,
  ...MATERIAL_MODULES
];

@NgModule({
  declarations: [
    ConfirmDialogComponent,
    ActionComponent,
    BaseListComponent
  ],
  imports: [
    ...SHARED_MODULES
  ],
  exports: [
    ...SHARED_MODULES,
    ConfirmDialogComponent,
    ActionComponent,
    BaseListComponent
  ]
})
export class SharedModule { }
