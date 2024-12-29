import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatTable } from '@angular/material/table';
import { PageEvent } from '@angular/material/paginator';
import { RolePermission } from '../../models/role-permission.model';
import { RolePermissionService } from '../../services/role-permission.service';
import { RolePermissionDialogComponent } from './role-permission-dialog.component';
import { BaseListComponent } from '../../components/base-list/base-list.component';
import { ApiResponse } from '../../models/api.model';
import { Page } from '../../models/page.model';

@Component({
  selector: 'app-role-permission-list',
  templateUrl: './role-permission-list.component.html',
  styleUrls: ['./role-permission-list.component.scss']
})
export class RolePermissionListComponent implements OnInit {
  @ViewChild(MatTable) table!: MatTable<RolePermission>;
  @ViewChild(BaseListComponent) baseList!: BaseListComponent;

  displayedColumns: string[] = [
    'roleName',
    'permission',
    'actions'
  ];
  
  dataSource: RolePermission[] = [];
  loading = false;
  totalElements = 0;
  pageSize = 10;
  pageIndex = 0;

  constructor(
    private rolePermissionService: RolePermissionService,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.loadData();
  }
  onPageChange(event: PageEvent): void {
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;
    this.loadData();
  }
  
  loadData(event?: PageEvent): void {
    this.loading = true;
    if (event) {
      this.pageIndex = event.pageIndex;
      this.pageSize = event.pageSize;
    }

    this.rolePermissionService.findAll(this.pageIndex, this.pageSize)
      .subscribe({
        next: (response) => {
          this.dataSource = response.data.content;
          this.totalElements = response.data.totalElements;
          this.loading = false;
        },
        error: (error) => {
          console.error('Error loading role permissions:', error);
          this.loading = false;
        }
      });
  }

  openDialog(rolePermission?: RolePermission): void {
    const dialogRef = this.dialog.open(RolePermissionDialogComponent, {
      width: '600px',
      data: rolePermission || {}
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadData();
      }
    });
  }

  delete(id: number): void {
    if (confirm('Are you sure you want to delete this role permission?')) {
      this.rolePermissionService.delete(id).subscribe({
        next: () => {
          this.loadData();
        },
        error: (error) => {
          console.error('Error deleting role permission:', error);
        }
      });
    }
  }
}
