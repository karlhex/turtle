import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;
  error = '';
  loading = false;

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.loginForm = this.formBuilder.group({
      username: ['', [Validators.required]],
      password: ['', [Validators.required]],
    });
  }

  ngOnInit() {
    // Redirect to dashboard if already logged in
    if (this.authService.isLoggedIn()) {
      this.router.navigate(['/dashboard']);
    }
  }

  onSubmit(): void {
    if (this.loginForm.valid) {
      this.loading = true;
      this.error = '';

      console.log('Attempting login with:', {
        username: this.loginForm.value.username,
        password: '***',
      });

      this.authService.login(this.loginForm.value).subscribe({
        next: response => {
          this.loading = false;
          console.log('Login response:', response);
          if (!response || !response.data || !response.data.tokenPair) {
            this.error = 'Invalid response from server';
            console.error('Invalid response:', response);
          }
        },
        error: error => {
          this.loading = false;
          console.error('Login error:', error);
          this.error = error.error?.message || 'Login failed. Please try again.';
        },
      });
    } else {
      console.log('Form is invalid:', this.loginForm.errors);
    }
  }
}
