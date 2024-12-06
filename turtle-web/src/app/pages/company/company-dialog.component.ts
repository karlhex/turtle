import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { TranslateService } from '@ngx-translate/core';
import { CompanyService } from '../../services/company.service';
import { Company } from '../../models/company.model';

@Component({
  selector: 'app-company-dialog',
  templateUrl: './company-dialog.component.html',
  styleUrls: ['./company-dialog.component.scss']
})
export class CompanyDialogComponent {
  form: FormGroup;
  loading = false;

  constructor(
    private fb: FormBuilder,
    private companyService: CompanyService,
    private dialogRef: MatDialogRef<CompanyDialogComponent>,
    private snackBar: MatSnackBar,
    private translate: TranslateService,
    @Inject(MAT_DIALOG_DATA) public data: { mode: 'create' | 'edit', company?: Company }
  ) {
    this.form = this.fb.group({
      fullName: ['', [Validators.required]],
      shortName: [''],
      address: ['', [Validators.required]],
      phone: ['', [Validators.required]],
      email: ['', [Validators.email]],
      website: [''],
      remarks: ['']
    });

    if (data.mode === 'edit' && data.company) {
      this.form.patchValue(data.company);
    }
  }

  onSubmit(): void {
    if (this.form.valid) {
      this.loading = true;
      const company = this.form.value;

      const request = this.data.mode === 'create'
        ? this.companyService.createCompany(company)
        : this.companyService.updateCompany(this.data.company!.id!, company);

      request.subscribe({
        next: (response) => {
          this.snackBar.open(
            this.translate.instant(
              this.data.mode === 'create'
                ? 'COMPANY.CREATE_SUCCESS'
                : 'COMPANY.UPDATE_SUCCESS'
            ),
            this.translate.instant('ACTIONS.CLOSE'),
            { duration: 3000 }
          );
          this.dialogRef.close(response.data);
        },
        error: (error) => {
          console.error('Error saving company:', error);
          this.snackBar.open(
            this.translate.instant('ERROR.SAVE_COMPANY'),
            this.translate.instant('ACTIONS.CLOSE'),
            { duration: 3000 }
          );
          this.loading = false;
        }
      });
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}
