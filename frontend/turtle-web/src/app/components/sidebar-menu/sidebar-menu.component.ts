import { Component, Input, Output, EventEmitter, OnInit, OnDestroy } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { PermissionService } from '../../services/permission.service';
import { AuthService } from '../../services/auth.service';
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
  roles?: string[]; // Allow specific roles to see this menu item
  hideForRoles?: string[]; // Hide for specific roles
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

  constructor(private router: Router, private permissionService: PermissionService, private authService: AuthService) {
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
    return combineLatest([
      this.checkRolePermission(item),
      this.checkItemPermission(item)
    ]).pipe(
      map(([hasRolePermission, hasItemPermission]) => hasRolePermission && hasItemPermission)
    );
  }

  // 检查角色权限
  private checkRolePermission(item: MenuItem): Observable<boolean> {
    return this.authService.getUserRole().pipe(
      map(userRole => {
        // 如果指定了允许的角色，检查用户角色是否在允许列表中
        if (item.roles && item.roles.length > 0) {
          return item.roles.includes(userRole);
        }
        
        // 如果指定了禁止的角色，检查用户角色是否在禁止列表中
        if (item.hideForRoles && item.hideForRoles.length > 0) {
          return !item.hideForRoles.includes(userRole);
        }
        
        // 默认允许显示
        return true;
      })
    );
  }

  // 检查具体权限
  private checkItemPermission(item: MenuItem): Observable<boolean> {
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
      roles: ['SYSTEM', 'EMPLOYEE'], // Only system users and employees can see HR module
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
        {
          title: 'menu.employee_applications',
          icon: 'person_add',
          route: '/employee-application/hr/list',
          permission: 'hr.application.read',
        },
        {
          title: 'menu.application_to_employee',
          icon: 'how_to_reg',
          route: '/employee-application/hr/approved',
          permission: 'hr.application.approve',
        },
      ],
      divider: true,
    },
    {
      title: 'menu.approval_management',
      icon: 'approval',
      hideForRoles: ['GUEST'], // Hide from guest users
      children: [
        {
          title: 'menu.unified_approval',
          icon: 'assignment_turned_in',
          route: '/approval/list',
          // 移除权限要求，所有非GUEST员工都能查看
        },
        {
          title: 'menu.my_pending_approvals',
          icon: 'pending_actions',
          route: '/approval/pending',
          // 移除权限要求，所有非GUEST员工都能查看自己的任务
        },
        {
          title: 'menu.approval_history',
          icon: 'history',
          route: '/approval/history',
          // 移除权限要求，所有非GUEST员工都能查看历史
        },
      ],
      divider: true,
    },
    {
      title: 'menu.guest_applications',
      icon: 'assignment',
      roles: ['GUEST'], // Only guest users see this menu
      children: [
        {
          title: 'menu.new_application',
          icon: 'add_box',
          route: '/employee-application/new',
          permission: 'guest.application.create',
        },
        {
          title: 'menu.my_applications',
          icon: 'list_alt',
          route: '/employee-application/my-applications',
          permission: 'guest.application.view',
        },
      ],
      divider: true,
    },
    {
      title: 'menu.crm',
      icon: 'contacts',
      hideForRoles: ['GUEST'], // Hide from guest users
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
      hideForRoles: ['GUEST'], // Hide from guest users
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
      hideForRoles: ['GUEST'], // Hide from guest users
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
      roles: ['SYSTEM'], // Only system users see this menu
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
    {
      title: 'menu.workflow',
      icon: 'account_tree',
      roles: ['SYSTEM', 'EMPLOYEE'], // System and employees can see workflow
      children: [
        // Workflow configurator removed (migrated to Flowable BPMN)
        {
          title: 'menu.flowable_admin',
          icon: 'admin_panel_settings',
          route: '/workflow/flowable-admin',
          permission: 'workflow.admin',
        },
      ],
    },
  ];
}
