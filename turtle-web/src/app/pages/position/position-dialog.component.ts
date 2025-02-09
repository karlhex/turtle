import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Position } from '../../models/position.model';
import { PositionService } from '../../services/position.service';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-position-dialog',
  templateUrl: './position-dialog.component.html',
  styleUrls: ['./position-dialog.component.scss']
})
export class PositionDialogComponent implements OnInit {
  form: FormGroup;
  loading = false;
  isEditMode: boolean;

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<PositionDialogComponent>,
    private positionService: PositionService,
    private snackBar: MatSnackBar,
    private translate: TranslateService,
    @Inject(MAT_DIALOG_DATA) private data: { mode: 'create' | 'edit', position?: Position }
  ) {
    this.isEditMode = data.mode === 'edit';
    this.form = this.fb.group({
      name: ['', [Validators.required]],
      code: ['', [Validators.required]],
      level: [null],
      isActive: [true],
      description:[''],
      responsibilities: ['']
    });

    if (this.isEditMode && data.position) {
      this.form.patchValue(data.position);
    }
  }

  ngOnInit(): void {

  }

  onSubmit(): void {
    if (this.form.valid) {
      this.loading = true;
      const position: Position = this.form.value;

      const request = this.isEditMode
        ? this.positionService.updatePosition(this.data.position!.id, position)
        : this.positionService.createPosition(position);

      request.subscribe({
        next: (response) => {
          if (response.code === 200) {
            this.showSuccess(this.isEditMode ? 'position.update_success' : 'position.create_success');
          } else {
            this.showError(this.isEditMode ? 'position.update_error' : 'position.create_error');            
          }
          this.loading = false;
          this.dialogRef.close(true);
        },
        error: (error) => {
          console.error('Error saving position:', error);
          this.showError(this.isEditMode ? 'position.update_error' : 'position.create_error');
          this.loading = false;
        }
      });
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }

  private showSuccess(message: string): void {
    this.snackBar.open(this.translate.instant(message), 'OK', {
      duration: 3000,
      horizontalPosition: 'end',
      verticalPosition: 'top'
    });
  }

  private showError(message: string): void {
    this.snackBar.open(this.translate.instant(message), 'OK', {
      duration: 5000,
      horizontalPosition: 'end',
      verticalPosition: 'top',
      panelClass: ['error-snackbar']
    });
  }
}
