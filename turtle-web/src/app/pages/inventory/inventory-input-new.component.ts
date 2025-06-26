import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { TranslateService } from '@ngx-translate/core';
import { InventoryService } from '../../services/inventory.service';
import { ProductService } from '../../services/product.service';
import { CompanyService } from '../../services/company.service';
import { EmployeeService } from '../../services/employee.service';
import { Inventory } from '../../models/inventory.model';
import { Product } from '../../models/product.model';
import { Company } from '../../models/company.model';
import { Employee } from '../../models/employee.model';
import { InventoryStatus } from '../../types/inventory-status.enum';
import { ShippingMethod } from '../../types/shipping-method.enum';
import { InputPageConfig } from '../../components/input-page/input-page.component';

@Component({
  selector: 'app-inventory-input-new',
  templateUrl: './inventory-input-new.component.html',
  styleUrls: ['./inventory-input-new.component.scss'],
})
export class InventoryInputNewComponent implements OnInit {
  config: InputPageConfig = {
    title: 'INVENTORY.TITLE',
    layout: 'auto-column',
    autoColumnThreshold: 6,
    showSaveButton: true,
    showCancelButton: true,
    showResetButton: true,
    autoSave: false,
    fields: [
      {
        key: 'productId',
        label: 'INVENTORY.PRODUCT',
        type: 'select',
        required: true,
        options: [],
        width: 2
      },
      {
        key: 'quantity',
        label: 'INVENTORY.QUANTITY',
        type: 'number',
        required: true,
        placeholder: 'INVENTORY.QUANTITY_PLACEHOLDER',
        width: 2
      },
      {
        key: 'license',
        label: 'INVENTORY.LICENSE',
        type: 'text',
        required: false,
        placeholder: 'INVENTORY.LICENSE_PLACEHOLDER',
        width: 2
      },
      {
        key: 'status',
        label: 'INVENTORY.STATUS',
        type: 'select',
        required: true,
        options: [
          { value: InventoryStatus.IN_STOCK, label: 'INVENTORY.STATUS.IN_STOCK' },
          { value: InventoryStatus.OUT_OF_STOCK, label: 'INVENTORY.STATUS.OUT_OF_STOCK' },
          { value: InventoryStatus.BORROWED, label: 'INVENTORY.STATUS.BORROWED' }
        ],
        width: 2
      },
      {
        key: 'storageTime',
        label: 'INVENTORY.STORAGE_TIME',
        type: 'date',
        required: false,
        width: 2
      },
      {
        key: 'outTime',
        label: 'INVENTORY.OUT_TIME',
        type: 'date',
        required: false,
        width: 2
      },
      {
        key: 'borrowedCompanyId',
        label: 'INVENTORY.BORROWED_COMPANY',
        type: 'select',
        required: false,
        options: [],
        width: 2
      },
      {
        key: 'handlingEmployeeId',
        label: 'INVENTORY.HANDLING_EMPLOYEE',
        type: 'select',
        required: false,
        options: [],
        width: 2
      },
      {
        key: 'shippingAddress',
        label: 'INVENTORY.SHIPPING_ADDRESS',
        type: 'textarea',
        required: false,
        placeholder: 'INVENTORY.SHIPPING_ADDRESS_PLACEHOLDER',
        rows: 2,
        width: 4
      },
      {
        key: 'shippingMethod',
        label: 'INVENTORY.SHIPPING_METHOD',
        type: 'select',
        required: false,
        options: [
          { value: ShippingMethod.EXPRESS, label: 'INVENTORY.SHIPPING_METHOD.EXPRESS' },
          { value: ShippingMethod.STANDARD, label: 'INVENTORY.SHIPPING_METHOD.STANDARD' },
          { value: ShippingMethod.OVERNIGHT, label: 'INVENTORY.SHIPPING_METHOD.OVERNIGHT' },
          { value: ShippingMethod.GROUND, label: 'INVENTORY.SHIPPING_METHOD.GROUND' }
        ],
        width: 2
      },
      {
        key: 'receiverName',
        label: 'INVENTORY.RECEIVER_NAME',
        type: 'text',
        required: false,
        placeholder: 'INVENTORY.RECEIVER_NAME_PLACEHOLDER',
        width: 2
      },
      {
        key: 'receiverPhone',
        label: 'INVENTORY.RECEIVER_PHONE',
        type: 'text',
        required: false,
        placeholder: 'INVENTORY.RECEIVER_PHONE_PLACEHOLDER',
        width: 2
      },
      {
        key: 'expressTrackingNumber',
        label: 'INVENTORY.EXPRESS_TRACKING_NUMBER',
        type: 'text',
        required: false,
        placeholder: 'INVENTORY.EXPRESS_TRACKING_NUMBER_PLACEHOLDER',
        width: 2
      },
      {
        key: 'remarks',
        label: 'INVENTORY.REMARKS',
        type: 'textarea',
        required: false,
        placeholder: 'INVENTORY.REMARKS_PLACEHOLDER',
        rows: 3,
        width: 4
      }
    ]
  };

  initialData: any = {};
  loading = false;
  products: Product[] = [];
  companies: Company[] = [];
  employees: Employee[] = [];

  constructor(
    private inventoryService: InventoryService,
    private productService: ProductService,
    private companyService: CompanyService,
    private employeeService: EmployeeService,
    private dialogRef: MatDialogRef<InventoryInputNewComponent>,
    private snackBar: MatSnackBar,
    private translate: TranslateService,
    @Inject(MAT_DIALOG_DATA) public data: { mode: 'create' | 'edit'; inventory?: Inventory }
  ) {
    if (data.mode === 'edit' && data.inventory) {
      this.initialData = { ...data.inventory };
    }
  }

  ngOnInit(): void {
    this.loadProducts();
    this.loadCompanies();
    this.loadEmployees();
  }

  private loadProducts(): void {
    this.productService.getAllActive(0, 1000).subscribe({
      next: (response: any) => {
        if (response.code === 200) {
          this.products = response.data;
          const productField = this.config.fields.find(f => f.key === 'productId');
          if (productField) {
            productField.options = this.products.map(product => ({
              value: product.id,
              label: `${product.name} (${product.modelNumber})`
            }));
          }
        }
      },
      error: (error: any) => console.error('Error loading products:', error)
    });
  }

  private loadCompanies(): void {
    this.companyService.getAllActive().subscribe({
      next: (response: any) => {
        if (response.code === 200) {
          this.companies = response.data;
          const companyField = this.config.fields.find(f => f.key === 'borrowedCompanyId');
          if (companyField) {
            companyField.options = this.companies.map(company => ({
              value: company.id,
              label: company.fullName
            }));
          }
        }
      },
      error: (error: any) => console.error('Error loading companies:', error)
    });
  }

  private loadEmployees(): void {
    this.employeeService.getActiveEmployees().subscribe({
      next: (response: any) => {
        if (response.code === 200) {
          this.employees = response.data;
          const employeeField = this.config.fields.find(f => f.key === 'handlingEmployeeId');
          if (employeeField) {
            employeeField.options = this.employees.map(employee => ({
              value: employee.id,
              label: employee.name
            }));
          }
        }
      },
      error: (error: any) => console.error('Error loading employees:', error)
    });
  }

  onSave(data: any): void {
    if (this.validateForm(data)) {
      this.loading = true;
      const inventory = data;

      const request = this.data.mode === 'create'
        ? this.inventoryService.create(inventory)
        : this.inventoryService.outbound(inventory.id!, inventory);

      request.subscribe({
        next: (response: any) => {
          if (response.code === 200) {
            this.snackBar.open(
              this.translate.instant(
                this.data.mode === 'create' ? 'INVENTORY.CREATE_SUCCESS' : 'INVENTORY.UPDATE_SUCCESS'
              ),
              this.translate.instant('ACTIONS.CLOSE'),
              { duration: 3000 }
            );
            this.dialogRef.close(response.data);
          } else {
            this.snackBar.open(
              response.message || this.translate.instant('ERROR.SAVE_INVENTORY'),
              this.translate.instant('ACTIONS.CLOSE'),
              { duration: 3000 }
            );
          }
          this.loading = false;
        },
        error: (error: any) => {
          console.error('Error saving inventory:', error);
          this.snackBar.open(
            this.translate.instant('ERROR.SAVE_INVENTORY'),
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
    if (!data.productId) {
      this.snackBar.open(
        this.translate.instant('INVENTORY.PRODUCT_REQUIRED'),
        this.translate.instant('ACTIONS.CLOSE'),
        { duration: 3000 }
      );
      return false;
    }

    if (data.quantity === null || data.quantity === undefined || data.quantity < 0) {
      this.snackBar.open(
        this.translate.instant('INVENTORY.QUANTITY_REQUIRED'),
        this.translate.instant('ACTIONS.CLOSE'),
        { duration: 3000 }
      );
      return false;
    }

    if (!data.status) {
      this.snackBar.open(
        this.translate.instant('INVENTORY.STATUS_REQUIRED'),
        this.translate.instant('ACTIONS.CLOSE'),
        { duration: 3000 }
      );
      return false;
    }

    return true;
  }
} 