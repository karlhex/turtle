import { Component, Input, Output, EventEmitter } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ApplicationCertification } from '../../models/employee-application.model';
import { ApplicationCertificationDialogComponent } from './application-certification-dialog.component';

@Component({
  selector: 'app-application-certification-list',
  templateUrl: './application-certification-list.component.html',
  styleUrls: ['./application-certification-list.component.scss'],
})
export class ApplicationCertificationListComponent {
  @Input() certifications: ApplicationCertification[] = [];
  @Input() editable = true;
  @Output() certificationsChange = new EventEmitter<ApplicationCertification[]>();

  displayedColumns: string[] = [
    'name',
    'issuer',
    'issueDate',
    'expiryDate',
    'certificateNumber',
    'actions',
  ];

  constructor(private dialog: MatDialog) {}

  onAddCertification(): void {
    const dialogRef = this.dialog.open(ApplicationCertificationDialogComponent, {
      width: '600px',
      data: { mode: 'add' },
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.certifications = [...this.certifications, result];
        this.certificationsChange.emit(this.certifications);
      }
    });
  }

  onEditCertification(certification: ApplicationCertification, index: number): void {
    const dialogRef = this.dialog.open(ApplicationCertificationDialogComponent, {
      width: '600px',
      data: { mode: 'edit', certification: { ...certification } },
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.certifications = this.certifications.map((c, i) => (i === index ? result : c));
        this.certificationsChange.emit(this.certifications);
      }
    });
  }

  onDeleteCertification(index: number): void {
    if (confirm('确定要删除这个证书吗？')) {
      this.certifications = this.certifications.filter((_, i) => i !== index);
      this.certificationsChange.emit(this.certifications);
    }
  }

  formatDate(dateString?: string): string {
    if (!dateString) return '-';
    return new Date(dateString).toLocaleDateString('zh-CN');
  }

  isExpired(expiryDate?: string): boolean {
    if (!expiryDate) return false;
    return new Date(expiryDate) < new Date();
  }

  isExpiringSoon(expiryDate?: string): boolean {
    if (!expiryDate) return false;
    const expiry = new Date(expiryDate);
    const now = new Date();
    const diffDays = Math.ceil((expiry.getTime() - now.getTime()) / (1000 * 60 * 60 * 24));
    return diffDays > 0 && diffDays <= 30; // 30天内过期
  }
}