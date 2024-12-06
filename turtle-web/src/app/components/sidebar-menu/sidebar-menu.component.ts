import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Router } from '@angular/router';

interface MenuItem {
  title: string;
  icon: string;
  route?: string;
  children?: MenuItem[];
  divider?: boolean;
}

@Component({
  selector: 'app-sidebar-menu',
  templateUrl: './sidebar-menu.component.html',
  styleUrls: ['./sidebar-menu.component.scss']
})
export class SidebarMenuComponent {
  @Input() isCollapsed = false;
  @Output() menuToggled = new EventEmitter<void>();

  menuItems: MenuItem[] = [
    {
      title: 'app.menu.dashboard',
      icon: 'dashboard',
      route: '/dashboard'
    },
    {
      title: 'app.menu.hr',
      icon: 'people',
      children: [
        {
          title: 'app.menu.employees',
          icon: 'badge',
          route: '/employees'
        },
        {
          title: 'app.menu.departments',
          icon: 'business',
          route: '/departments'
        },
        {
          title: 'app.menu.positions',
          icon: 'work',
          route: '/positions'
        }

      ],
      divider: true
    },
    {
      title: 'app.menu.attendance',
      icon: 'event_note',
      route: '/attendance',
      divider: true
    },
    {
      title: 'app.menu.contract_management',
      icon: 'description',
      children: [
        {
          title: 'app.menu.contracts',
          icon: 'description',
          route: '/contracts'
        },
        {
          title: 'app.menu.currencies',
          icon: 'attach_money',
          route: '/currencies'
        },
        {
          title: 'app.menu.company',
          icon: 'business_center',
          route: '/companies'
        },
        {
          title: 'app.menu.tax_info',
          icon: 'receipt',
          route: '/tax-infos'
        }
      ],
      divider: true
    },
    {
      title: 'app.menu.system',
      icon: 'settings',
      children: [
        {
          title: 'app.menu.users',
          icon: 'manage_accounts',
          route: '/users'
        }
      ]
    }
  ];

  expandedItems: Set<string> = new Set();

  constructor(private router: Router) {}

  toggleMenu() {
    this.menuToggled.emit();
  }

  toggleSubmenu(title: string, event: Event) {
    event.preventDefault();
    if (this.isCollapsed) {
      return;
    }
    
    if (this.expandedItems.has(title)) {
      this.expandedItems.delete(title);
    } else {
      this.expandedItems.add(title);
    }
  }

  isExpanded(title: string): boolean {
    return this.expandedItems.has(title);
  }

  hasActiveChild(item: MenuItem): boolean {
    if (item.route) {
      return this.router.isActive(item.route, false);
    }
    return item.children?.some(child => 
      child.route && this.router.isActive(child.route, false)
    ) || false;
  }
}