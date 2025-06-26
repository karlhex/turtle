import { NgModule } from '@angular/core';
import { CompanyListComponent } from './company-list.component';
import { CompanyDialogComponent } from './company-dialog.component';
import { CompanyListNewComponent } from './company-list-new.component';
import { CompanyInputNewComponent } from './company-input-new.component';
import { BaseListModule } from '../../components/base-list/base-list.module';
import { ActionModule } from '@app/components/action/action.module';
import { PersonInputModule } from '@app/components/person-input/person-input.module';
import { SharedModule } from '../../shared/shared.module';

@NgModule({
  declarations: [
    CompanyListComponent, 
    CompanyDialogComponent,
    CompanyListNewComponent,
    CompanyInputNewComponent
  ],
  imports: [
    SharedModule,
    ActionModule,
    PersonInputModule,
    BaseListModule,
  ],
  exports: [
    CompanyListComponent, 
    CompanyDialogComponent,
    CompanyListNewComponent,
    CompanyInputNewComponent
  ],
})
export class CompanyModule {}
