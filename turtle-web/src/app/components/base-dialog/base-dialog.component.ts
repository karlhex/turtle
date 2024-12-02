import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

export interface BaseDialogData {
  title: string;
  content: any;
}

@Component({
  selector: 'app-base-dialog',
  templateUrl: './base-dialog.component.html',
  styleUrls: ['./base-dialog.component.scss']
})
export class BaseDialogComponent {
  constructor(
    public dialogRef: MatDialogRef<BaseDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: BaseDialogData
  ) {}

  onClose(): void {
    this.dialogRef.close();
  }
}
 