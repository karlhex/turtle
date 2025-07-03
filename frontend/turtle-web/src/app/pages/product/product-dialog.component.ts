import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { TranslateService } from '@ngx-translate/core';
import { Product } from '../../models/product.model';
import { Company } from '../../models/company.model';
import { ProductService } from '../../services/product.service';
import { CompanyService } from '../../services/company.service';
import { ApiResponse } from '../../models/api.model';
import { Observable, map, startWith } from 'rxjs';

@Component({
  selector: 'app-product-dialog',
  templateUrl: './product-dialog.component.html',
  styleUrls: ['./product-dialog.component.scss'],
})
export class ProductDialogComponent implements OnInit {
  form: FormGroup;
  loading = false;
  isEdit = false;
  companies: Company[] = [];
  filteredCompanies: Observable<Company[]>;

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<ProductDialogComponent>,
    private productService: ProductService,
    private companyService: CompanyService,
    private snackBar: MatSnackBar,
    private translate: TranslateService,
    @Inject(MAT_DIALOG_DATA) public data?: Product
  ) {
    this.form = this.fb.group({
      name: ['', Validators.required],
      modelNumber: [''],
      manufacturer: [null, Validators.required],
      type: [null, Validators.required],
      unit: ['', Validators.required],
      description: [''],
      specifications: [''],
      remarks: [''],
      warrantyPeriod: [null],
      active: [true],
    });

    if (data) {
      this.isEdit = true;
      this.form.patchValue(data);
    }

    this.filteredCompanies = this.form.get('manufacturer')!.valueChanges.pipe(
      startWith(''),
      map(value => {
        const name = typeof value === 'string' ? value : value?.fullName;
        return name ? this._filterCompanies(name) : this.companies.slice();
      })
    );
  }

  ngOnInit(): void {
    this.loadCompanies();
  }

  private loadCompanies(): void {
    this.companyService.getAllActive().subscribe({
      next: (response: ApiResponse<Company[]>) => {
        console.log('got companies', response);
        if (response.code === 200) {
          this.companies = response.data;
          console.log('loaded companies', this.companies);
        } else {
          console.error('Error loading companies:', response.message);
        }
      },
      error: error => {
        console.error('Error loading companies:', error);
      },
    });
  }

  private _filterCompanies(value: string): Company[] {
    const filterValue = value.toLowerCase();
    return this.companies.filter(company => company.fullName.toLowerCase().includes(filterValue));
  }

  displayCompany(company: Company): string {
    return company?.fullName || '';
  }

  submit(): void {
    if (this.form.valid) {
      this.loading = true;
      const product = this.form.value;

      const request = this.isEdit
        ? this.productService.update(this.data!.id!, product)
        : this.productService.create(product);

      request.subscribe({
        next: (response: ApiResponse<Product>) => {
          if (response.code === 200) {
            this.snackBar.open(
              this.translate.instant(
                this.isEdit ? 'SUCCESS.UPDATE_PRODUCT' : 'SUCCESS.CREATE_PRODUCT'
              ),
              this.translate.instant('COMMON.CLOSE'),
              { duration: 3000 }
            );
            this.dialogRef.close(true);
          }
          this.loading = false;
        },
        error: error => {
          console.error('Error saving product:', error);
          this.snackBar.open(
            this.translate.instant(this.isEdit ? 'ERROR.UPDATE_PRODUCT' : 'ERROR.CREATE_PRODUCT'),
            this.translate.instant('COMMON.CLOSE'),
            { duration: 3000 }
          );
          this.loading = false;
        },
      });
    }
  }

  cancel(): void {
    this.dialogRef.close();
  }
}
