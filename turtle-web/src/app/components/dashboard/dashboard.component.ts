import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  username: string = '';
  
  // Sample dashboard data
  stats = [
    { title: 'Total Users', value: '1,234', trend: '+12%', icon: 'people' },
    { title: 'Active Projects', value: '42', trend: '+5%', icon: 'folder' },
    { title: 'Tasks Completed', value: '789', trend: '+25%', icon: 'task_alt' },
    { title: 'System Status', value: 'Healthy', trend: '99.9%', icon: 'monitoring' }
  ];

  constructor(
    private router: Router,
    private authService: AuthService
  ) {}

  ngOnInit() {
    const currentUser = this.authService.getCurrentUser();
    if (currentUser) {
      // You might want to decode the JWT token to get the username
      // For now, we'll use a placeholder
      this.username = 'User';
    } else {
      this.router.navigate(['/login']);
    }
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
