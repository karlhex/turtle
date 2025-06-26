import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { TranslateService } from '@ngx-translate/core';
import { InputPageConfig } from '../../components/input-page/input-page.component';
import { Position } from '../../models/position.model';
import { PositionService } from '../../services/position.service';

@Component({
  selector: 'app-position-input-new',
  templateUrl: './position-input-new.component.html',
  styleUrls: ['./position-input-new.component.scss']
})
export class PositionInputNewComponent implements OnInit {
  config: InputPageConfig = {
    title: 'POSITION.TITLE',
    fields: [
      {
        key: 'name',
        label: 'POSITION.NAME',
        type: 'text',
        required: true,
        placeholder: 'POSITION.NAME_PLACEHOLDER',
        width: 2
      },
      {
        key: 'code',
        label: 'POSITION.CODE',
        type: 'text',
        required: true,
        placeholder: 'POSITION.CODE_PLACEHOLDER',
        width: 2
      },
      {
        key: 'level',
        label: 'POSITION.LEVEL',
        type: 'number',
        required: false,
        placeholder: 'POSITION.LEVEL_PLACEHOLDER',
        width: 2
      },
      {
        key: 'isActive',
        label: 'POSITION.STATUS',
        type: 'checkbox',
        required: false,
        width: 2
      },
      {
        key: 'description',
        label: 'POSITION.DESCRIPTION',
        type: 'textarea',
        required: false,
        placeholder: 'POSITION.DESCRIPTION_PLACEHOLDER',
        rows: 3,
        width: 4
      },
      {
        key: 'responsibilities',
        label: 'POSITION.RESPONSIBILITIES',
        type: 'textarea',
        required: false,
        placeholder: 'POSITION.RESPONSIBILITIES_PLACEHOLDER',
        rows: 4,
        width: 4
      }
    ],
    layout: 'two-column',
    showSaveButton: true,
    showCancelButton: true,
    showResetButton: true,
    saveButtonText: 'COMMON.SAVE',
    cancelButtonText: 'COMMON.CANCEL',
    resetButtonText: 'COMMON.RESET'
  };

  initialData: any = {};
  loading = false;

  constructor(
    private positionService: PositionService,
    private dialogRef: MatDialogRef<PositionInputNewComponent>,
    private snackBar: MatSnackBar,
    private translate: TranslateService,
    @Inject(MAT_DIALOG_DATA) public data: { mode: 'create' | 'edit'; position?: Position }
  ) {
    if (data.mode === 'edit' && data.position) {
      this.initialData = { ...data.position };
    }
  }

  ngOnInit(): void {
    // No additional initialization needed
  }

  onSave(data: any): void {
    if (this.validateForm(data)) {
      this.loading = true;
      const position = data;

      const request = this.data.mode === 'create'
        ? this.positionService.createPosition(position)
        : this.positionService.updatePosition(this.data.position!.id!, position);

      request.subscribe({
        next: (response: any) => {
          if (response.code === 200) {
            this.snackBar.open(
              this.translate.instant(
                this.data.mode === 'create' ? 'POSITION.CREATE_SUCCESS' : 'POSITION.UPDATE_SUCCESS'
              ),
              this.translate.instant('ACTIONS.CLOSE'),
              { duration: 3000 }
            );
            this.dialogRef.close(response.data);
          } else {
            this.snackBar.open(
              response.message || this.translate.instant('ERROR.SAVE_POSITION'),
              this.translate.instant('ACTIONS.CLOSE'),
              { duration: 3000 }
            );
          }
          this.loading = false;
        },
        error: (error: any) => {
          console.error('Error saving position:', error);
          this.snackBar.open(
            this.translate.instant('ERROR.SAVE_POSITION'),
            this.translate.instant('ACTIONS.CLOSE'),
            { duration: 3000 }
          );
          this.loading = false;
        }
      });
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }

  onReset(): void {
    console.log('Form reset');
  }

  onFieldChange(key: string, value: any): void {
    console.log('Field change:', key, value);
  }

  onValidationChange(valid: boolean, errors: any): void {
    console.log('Validation change:', valid, errors);
  }

  private validateForm(data: any): boolean {
    if (!data.name || data.name.trim() === '') {
      this.snackBar.open(
        this.translate.instant('POSITION.NAME_REQUIRED'),
        this.translate.instant('ACTIONS.CLOSE'),
        { duration: 3000 }
      );
      return false;
    }

    if (!data.code || data.code.trim() === '') {
      this.snackBar.open(
        this.translate.instant('POSITION.CODE_REQUIRED'),
        this.translate.instant('ACTIONS.CLOSE'),
        { duration: 3000 }
      );
      return false;
    }

    return true;
  }
} 