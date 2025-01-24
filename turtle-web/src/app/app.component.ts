import { Component, ViewChild } from '@angular/core';
import { MatSidenav } from '@angular/material/sidenav';
import { MatDialog } from '@angular/material/dialog';
import { Router, NavigationEnd } from '@angular/router';
import { AuthService } from './services/auth.service';
import { filter } from 'rxjs/operators';
import { TranslateService } from '@ngx-translate/core';
import { ChangePasswordDialogComponent } from './pages/user-management/change-password-dialog/change-password-dialog.component';
import { SidebarMenuComponent } from './components/sidebar-menu/sidebar-menu.component';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  @ViewChild('sidenav') sidenav!: MatSidenav;
  isLoginPage = false;
  isMenuCollapsed = false;

  constructor(
    private router: Router,
    private authService: AuthService,
    private translate: TranslateService,
    private dialog: MatDialog
  ) {
    // Initialize translations
    translate.setDefaultLang('zh');
    translate.use('zh');

    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe((event: any) => {
      this.isLoginPage = event.url === '/login';
    });
  }

  toggleMenu() {
    this.isMenuCollapsed = !this.isMenuCollapsed;
    this.sidenav.toggle();
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  switchLanguage(lang: string) {
    this.translate.use(lang);
  }

  openChangePasswordDialog() {
    this.dialog.open(ChangePasswordDialogComponent, {
      width: '400px'
    });
  }
}
