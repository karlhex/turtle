import { Component, Inject, TemplateRef } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

export type DialogSize = 'small' | 'medium' | 'large' | 'full';

export interface DialogButton {
  label: string;
  type?: 'primary' | 'secondary' | 'warn';
  onClick: () => void;
  disabled?: boolean;
  loading?: boolean;
  icon?: string;
}

export interface BaseDialogData {
  title: string;
  content: TemplateRef<any>;
  size?: DialogSize;
  buttons?: DialogButton[];
  showCloseButton?: boolean;
  loading?: boolean;
  ariaLabel?: string;
  preventClose?: boolean;
}

@Component({
  selector: 'app-base-dialog',
  templateUrl: './base-dialog.component.html',
  styleUrls: ['./base-dialog.component.scss'],
})
export class BaseDialogComponent {
  constructor(
    public dialogRef: MatDialogRef<BaseDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: BaseDialogData
  ) {
    // Set default values
    this.data = {
      ...{
        size: 'medium',
        showCloseButton: true,
        loading: false,
        preventClose: false,
        buttons: [],
      },
      ...this.data,
    };

    // Handle escape key and backdrop click
    if (this.data.preventClose) {
      dialogRef.disableClose = true;
    }

    // Set dialog size
    dialogRef.addPanelClass(`dialog-${this.data.size}`);

    // Set aria label for accessibility
    if (this.data.ariaLabel) {
      dialogRef.addPanelClass('custom-dialog-container');
    }
  }

  onClose(): void {
    if (!this.data.loading && !this.data.preventClose) {
      this.dialogRef.close();
    }
  }

  getButtonClass(button: DialogButton): string[] {
    const classes = ['mat-button'];
    if (button.type) {
      classes.push(`mat-${button.type}`);
    }
    return classes;
  }

  onKeydown(event: KeyboardEvent): void {
    // Handle keyboard navigation
    if (event.key === 'Escape' && !this.data.preventClose) {
      this.onClose();
    }
  }
}
