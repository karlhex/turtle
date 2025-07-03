import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { TranslateService } from '@ngx-translate/core';
import { ProductService } from '../../services/product.service';
import { CompanyService } from '../../services/company.service';
import { Product } from '../../models/product.model';
import { Company } from '../../models/company.model';
import { ProductType } from '../../types/product-type.enum';
import { InputPageConfig, FormFieldConfig } from '../../components/input-page/input-page.component';
import { Observable } from 'rxjs';
import { map, startWith } from 'rxjs/operators';

@Component({
  selector: 'app-product-input-new',
  templateUrl: './product-input-new.component.html',
  styleUrls: ['./product-input-new.component.scss'],
})
export class ProductInputNewComponent implements OnInit {
  config: InputPageConfig = {
    title: 'PRODUCT.TITLE',
    layout: 'auto-column',
    autoColumnThreshold: 6,
    showSaveButton: true,
    showCancelButton: true,
    showResetButton: true,
    autoSave: false,
    fields: [
      {
        key: 'name',
        label: 'PRODUCT.NAME',
        type: 'text',
        required: true,
        placeholder: 'PRODUCT.NAME_PLACEHOLDER',
        width: 2
      },
      {
        key: 'modelNumber',
        label: 'PRODUCT.MODEL_NUMBER',
        type: 'text',
        required: true,
        placeholder: 'PRODUCT.MODEL_NUMBER_PLACEHOLDER',
        width: 2
      },
      {
        key: 'type',
        label: 'PRODUCT.TYPE',
        type: 'select',
        required: true,
        options: [
          { value: ProductType.SOFTWARE, label: 'PRODUCT.TYPE.SOFTWARE' },
          { value: ProductType.HARDWARE, label: 'PRODUCT.TYPE.HARDWARE' },
          { value: ProductType.SERVICE, label: 'PRODUCT.TYPE.SERVICE' }
        ],
        width: 2
      },
      {
        key: 'unit',
        label: 'PRODUCT.UNIT',
        type: 'text',
        required: true,
        placeholder: 'PRODUCT.UNIT_PLACEHOLDER',
        width: 2
      },
      {
        key: 'manufacturerId',
        label: 'PRODUCT.MANUFACTURER',
        type: 'select',
        required: true,
        options: [],
        width: 2
      },
      {
        key: 'warrantyPeriod',
        label: 'PRODUCT.WARRANTY_PERIOD',
        type: 'number',
        required: false,
        placeholder: 'PRODUCT.WARRANTY_PERIOD_PLACEHOLDER',
        width: 2
      },
      {
        key: 'description',
        label: 'PRODUCT.DESCRIPTION',
        type: 'textarea',
        required: false,
        placeholder: 'PRODUCT.DESCRIPTION_PLACEHOLDER',
        rows: 3,
        width: 4
      },
      {
        key: 'specifications',
        label: 'PRODUCT.SPECIFICATIONS',
        type: 'textarea',
        required: false,
        placeholder: 'PRODUCT.SPECIFICATIONS_PLACEHOLDER',
        rows: 3,
        width: 4
      },
      {
        key: 'remarks',
        label: 'PRODUCT.REMARKS',
        type: 'textarea',
        required: false,
        placeholder: 'PRODUCT.REMARKS_PLACEHOLDER',
        rows: 3,
        width: 4
      },
      {
        key: 'active',
        label: 'PRODUCT.STATUS',
        type: 'checkbox',
        required: false,
        width: 4
      }
    ]
  };

  initialData: any = {};
  loading = false;
  companies: Company[] = [];

  constructor(
    private productService: ProductService,
    private companyService: CompanyService,
    private dialogRef: MatDialogRef<ProductInputNewComponent>,
    private snackBar: MatSnackBar,
    private translate: TranslateService,
    @Inject(MAT_DIALOG_DATA) public data: { mode: 'create' | 'edit'; product?: Product }
  ) {
    if (data.mode === 'edit' && data.product) {
      this.initialData = { 
        ...data.product,
        manufacturerId: data.product.manufacturer?.id
      };
    }
  }

  ngOnInit(): void {
    this.loadCompanies();
  }

  private loadCompanies(): void {
    this.companyService.getAllActive().subscribe({
      next: (response: any) => {
        if (response.code === 200) {
          this.companies = response.data;
          // 更新manufacturerId字段的选项
          const manufacturerField = this.config.fields.find(f => f.key === 'manufacturerId');
          if (manufacturerField) {
            manufacturerField.options = this.companies.map(company => ({
              value: company.id,
              label: company.fullName
            }));
          }
        }
      },
      error: (error: any) => console.error('Error loading companies:', error)
    });
  }

  onSave(data: any): void {
    if (this.validateForm(data)) {
      this.loading = true;
      const product = {
        ...data,
        manufacturer: this.companies.find(c => c.id === data.manufacturerId)
      };

      const request = this.data.mode === 'create'
        ? this.productService.create(product)
        : this.productService.update(this.data.product!.id!, product);

      request.subscribe({
        next: (response: any) => {
          if (response.code === 200) {
            this.snackBar.open(
              this.translate.instant(
                this.data.mode === 'create' ? 'PRODUCT.CREATE_SUCCESS' : 'PRODUCT.UPDATE_SUCCESS'
              ),
              this.translate.instant('ACTIONS.CLOSE'),
              { duration: 3000 }
            );
            this.dialogRef.close(response.data);
          } else {
            this.snackBar.open(
              response.message || this.translate.instant('ERROR.SAVE_PRODUCT'),
              this.translate.instant('ACTIONS.CLOSE'),
              { duration: 3000 }
            );
          }
          this.loading = false;
        },
        error: (error: any) => {
          console.error('Error saving product:', error);
          this.snackBar.open(
            this.translate.instant('ERROR.SAVE_PRODUCT'),
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

  onReset(): void {
    console.log('Form reset');
  }

  onFieldChange(key: string, value: any): void {
    console.log('Field change:', key, value);
  }

  onValidationChange(valid: boolean, errors: any): void {
    console.log('Validation change:', valid, errors);
  }

  private validateForm(data: any): boolean {
    if (!data.name || data.name.trim() === '') {
      this.snackBar.open(
        this.translate.instant('PRODUCT.NAME_REQUIRED'),
        this.translate.instant('ACTIONS.CLOSE'),
        { duration: 3000 }
      );
      return false;
    }

    if (!data.modelNumber || data.modelNumber.trim() === '') {
      this.snackBar.open(
        this.translate.instant('PRODUCT.MODEL_NUMBER_REQUIRED'),
        this.translate.instant('ACTIONS.CLOSE'),
        { duration: 3000 }
      );
      return false;
    }

    if (!data.type) {
      this.snackBar.open(
        this.translate.instant('PRODUCT.TYPE_REQUIRED'),
        this.translate.instant('ACTIONS.CLOSE'),
        { duration: 3000 }
      );
      return false;
    }

    if (!data.unit || data.unit.trim() === '') {
      this.snackBar.open(
        this.translate.instant('PRODUCT.UNIT_REQUIRED'),
        this.translate.instant('ACTIONS.CLOSE'),
        { duration: 3000 }
      );
      return false;
    }

    if (!data.manufacturerId) {
      this.snackBar.open(
        this.translate.instant('PRODUCT.MANUFACTURER_REQUIRED'),
        this.translate.instant('ACTIONS.CLOSE'),
        { duration: 3000 }
      );
      return false;
    }

    return true;
  }
} 