import { Injectable } from '@angular/core';
import { MatSnackBar, MatSnackBarConfig } from '@angular/material/snack-bar';

@Injectable({
  providedIn: 'root'
})
export class SnackBarService {

  constructor(private snackBar: MatSnackBar) { }

  showSuccess(message: string, duration = 3000): void {
    const config: MatSnackBarConfig = {
      duration,
      panelClass: ['success-snackbar'],
      verticalPosition: 'top',
      horizontalPosition: 'center'
    };
    this.snackBar.open(message, '关闭', config);
  }

  showError(message: string, duration = 5000): void {
    const config: MatSnackBarConfig = {
      duration,
      panelClass: ['error-snackbar'],
      verticalPosition: 'top',
      horizontalPosition: 'center'
    };
    this.snackBar.open(message, '关闭', config);
  }

  showInfo(message: string, duration = 3000): void {
    const config: MatSnackBarConfig = {
      duration,
      panelClass: ['info-snackbar'],
      verticalPosition: 'top',
      horizontalPosition: 'center'
    };
    this.snackBar.open(message, '关闭', config);
  }

  showWarning(message: string, duration = 4000): void {
    const config: MatSnackBarConfig = {
      duration,
      panelClass: ['warning-snackbar'],
      verticalPosition: 'top',
      horizontalPosition: 'center'
    };
    this.snackBar.open(message, '关闭', config);
  }
}