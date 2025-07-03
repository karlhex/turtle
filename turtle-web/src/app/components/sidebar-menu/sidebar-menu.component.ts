import { Component, Input, Output, EventEmitter, OnInit, OnDestroy } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { PermissionService } from '../../services/permission.service';
import { map, shareReplay, take, filter, takeUntil } from 'rxjs/operators';
import { Observable, of, combineLatest, Subject } from 'rxjs';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { MatDividerModule } from '@angular/material/divider';

interface MenuItem {
  title: string;
  route?: string;
  icon?: string;
  permission?: string;
  children?: MenuItem[];
  divider?: boolean;
}

@Component({
  selector: 'app-sidebar-menu',
  templateUrl: './sidebar-menu.component.html',
  styleUrls: ['./sidebar-menu.component.scss'],
  standalone: false,
})
export class SidebarMenuComponent implements OnInit, OnDestroy {
  @Input() isCollapsed = false;
  @Output() menuToggled = new EventEmitter<void>();
  expandedMenus: Set<string> = new Set();

  private permissionCache = new Map<string, Observable<boolean>>();
  private destroy$ = new Subject<void>();
  private activeItemCache = new Map<string, boolean>();

  constructor(private router: Router, private permissionService: PermissionService) {
    // 监听路由事件，更新活动状态缓存
    this.router.events
      .pipe(
        filter((event): event is NavigationEnd => event instanceof NavigationEnd),
        takeUntil(this.destroy$)
      )
      .subscribe(() => {
        // 只在路由变化时清除缓存
        this.activeItemCache.clear();
      });
  }

  ngOnInit() {
    // 初始化时预加载所有权限检查
    this.preloadPermissions(this.menuItems);
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
    this.permissionCache.clear();
    this.activeItemCache.clear();
  }

  private preloadPermissions(items: MenuItem[]) {
    items.forEach(item => {
      if (item.permission) {
        this.getCachedPermission(item.permission);
      }
      if (item.children) {
        this.preloadPermissions(item.children);
      }
    });
  }

  private getCachedPermission(permission: string): Observable<boolean> {
    if (!this.permissionCache.has(permission)) {
      const permission$ = this.permissionService
        .hasPermission(permission)
        .pipe(take(1), shareReplay(1));
      this.permissionCache.set(permission, permission$);
    }
    return this.permissionCache.get(permission)!;
  }

  // 检查菜单项是否应该显示
  checkPermission(item: MenuItem): Observable<boolean> {
    if (!item.permission) {
      return of(true);
    }
    return this.getCachedPermission(item.permission);
  }

  // 检查父菜单项是否应该显示
  checkParentPermission(item: MenuItem): Observable<boolean> {
    if (!item.children || item.children.length === 0) {
      return this.checkPermission(item);
    }

    const cacheKey = `parent_${item.title}`;
    if (this.permissionCache.has(cacheKey)) {
      return this.permissionCache.get(cacheKey)!;
    }

    const parentPermission$ = item.permission
      ? this.getCachedPermission(item.permission)
      : of(true);

    const childrenPermissions$ = item.children.map(child => this.checkPermission(child));

    const result$ = combineLatest([parentPermission$, ...childrenPermissions$]).pipe(
      map(([parentHasPermission, ...childrenHavePermission]) => {
        if (item.permission && !parentHasPermission) {
          return false;
        }
        return childrenHavePermission.some(hasPermission => hasPermission);
      }),
      take(1),
      shareReplay(1)
    );

    this.permissionCache.set(cacheKey, result$);
    return result$;
  }

  // 检查是否有任何子菜单项处于激活状态
  hasActiveChild(item: MenuItem): boolean {
    const cacheKey = `active_${item.title}`;
    if (this.activeItemCache.has(cacheKey)) {
      return this.activeItemCache.get(cacheKey)!;
    }

    let isActive = false;
    if (item.route) {
      isActive = this.router.url.startsWith(item.route);
    } else if (item.children) {
      isActive = item.children.some(
        child => child.route && this.router.url.startsWith(child.route)
      );
    }

    this.activeItemCache.set(cacheKey, isActive);
    return isActive;
  }

  // 切换子菜单的展开/折叠状态
  toggleSubmenu(title: string, event: Event): void {
    event.stopPropagation();
    if (this.expandedMenus.has(title)) {
      this.expandedMenus.delete(title);
    } else {
      this.expandedMenus.add(title);
    }
  }

  // 检查子菜单是否展开
  isExpanded(title: string): boolean {
    return this.expandedMenus.has(title);
  }

  menuItems: MenuItem[] = [
    {
      title: 'menu.dashboard',
      icon: 'dashboard',
      route: '/dashboard',
    },
    {
      title: 'menu.hr',
      icon: 'people',
      children: [
        {
          title: 'menu.employees',
          icon: 'badge',
          route: '/employees',
          permission: 'hr.employee.view',
        },
        {
          title: 'menu.departments',
          icon: 'business',
          route: '/departments',
          permission: 'hr.department.view',
        },
        {
          title: 'menu.positions',
          icon: 'work',
          route: '/positions',
        },
      ],
      divider: true,
    },
    {
      title: 'menu.crm',
      icon: 'contacts',
      children: [
        {
          title: 'menu.companies',
          icon: 'business',
          route: '/companies',
          permission: 'crm.company.view',
        },
        {
          title: 'menu.contacts',
          icon: 'person',
          route: '/contacts',
          permission: 'crm.contact.view',
        },
      ],
      divider: true,
    },
    {
      title: 'menu.contract_management',
      icon: 'description',
      children: [
        {
          title: 'menu.contracts',
          icon: 'description',
          route: '/contracts',
        },
        {
          title: 'menu.projects',
          icon: 'calendar_today',
          route: '/projects',
        },
        {
          title: 'menu.products',
          icon: 'inventory_2',
          route: '/products',
        },
        {
        title: 'menu.tax_info',
          icon: 'receipt',
          route: '/tax-infos',
        },
        {
          title: 'menu.inventory',
          icon: 'inventory_2',
          route: '/inventories',
        },
      ],
      divider: true,
    },
    {
      title: 'menu.financial',
      icon: 'account_balance',
      children: [
        {
          title: 'menu.bank_accounts',
          icon: 'account_balance_wallet',
          route: '/bank-accounts',
        },
        {
          title: 'menu.reimbursement',
          icon: 'receipt_long',
          route: '/reimbursements',
        },
        {
          title: 'menu.currencies',
          icon: 'currency_exchange',
          route: '/currencies',
        },
      ],
      divider: true,
    },
    {
      title: 'menu.system',
      icon: 'settings',
      children: [
        {
          title: 'menu.users',
          icon: 'manage_accounts',
          route: '/users',
        },
        {
          title: 'menu.role_permissions',
          icon: 'security',
          route: '/role-permissions',
        },
      ],
    },
  ];
}
