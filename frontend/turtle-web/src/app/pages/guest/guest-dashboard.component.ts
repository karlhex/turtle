import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-guest-dashboard',
  templateUrl: './guest-dashboard.component.html',
  styleUrls: ['./guest-dashboard.component.scss'],
})
export class GuestDashboardComponent {
  constructor(private router: Router) {}

  openApplicationForm(): void {
    this.router.navigate(['/employee-application/new']);
  }

  viewMyApplications(): void {
    this.router.navigate(['/employee-application/my-applications']);
  }
}
