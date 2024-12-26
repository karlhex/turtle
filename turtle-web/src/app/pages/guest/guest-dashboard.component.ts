import { Component } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { EmployeeDialogComponent } from '../employee/employee-dialog.component';

@Component({
  selector: 'app-guest-dashboard',
  templateUrl: './guest-dashboard.component.html',
  styleUrls: ['./guest-dashboard.component.scss']
})
export class GuestDashboardComponent {
  constructor(private dialog: MatDialog) {}

  openApplicationForm(): void {
    this.dialog.open(EmployeeDialogComponent, {
      width: '80%',
      data: {
        mode: 'application',
        employee: {}
      }
    });
  }
}
