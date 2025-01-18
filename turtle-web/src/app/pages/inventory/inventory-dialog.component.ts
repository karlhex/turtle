import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { TranslateService } from '@ngx-translate/core';
import { Inventory } from '../../models/inventory.model';
import { InventoryService } from '../../services/inventory.service';
import { InventoryStatus } from '../../types/inventory-status.enum';
import { ShippingMethod } from '../../types/shipping-method.enum';
import { ProductService } from '../../services/product.service';
import { ContractService } from '../../services/contract.service';
import { EmployeeService } from '../../services/employee.service';
import { AppRoutingModule } from '@app/app-routing.module';
import { ApiResponse } from '../../models/api.model';
import { Page } from '../../models/page.model';
import { Product } from '../../models/product.model';
import { Contract } from '../../models/contract.model';
import { Employee } from '../../models/employee.model';
import { Observable } from 'rxjs';
import { Company } from '../../models/company.model';
import { CompanyService } from '../../services/company.service';
// 导入 InventoryAction
import { InventoryAction } from './../../types/inventory-action.enum';

@Component({
  selector: 'app-inventory-dialog',
  templateUrl: './inventory-dialog.component.html',
  styleUrls: ['./inventory-dialog.component.scss']
})
export class InventoryDialogComponent implements OnInit {
  form: FormGroup;
  loading = false;
  
  // Dropdown options
  products: any[] = [];
  contracts: any[] = [];
  employees: any[] = [];
  companies: any[] = [];
  shippingMethods = Object.values(ShippingMethod);
  statuses = Object.values(InventoryStatus);
  
  // Action types
  actionTypes = Object.values(InventoryAction);
  selectedAction: InventoryAction = this.data.action;

  constructor(
    private fb: FormBuilder,
    private inventoryService: InventoryService,
    private productService: ProductService,
    private contractService: ContractService,
    private employeeService: EmployeeService,
    private companyService: CompanyService,
    private snackBar: MatSnackBar,
    private translate: TranslateService,
    private dialogRef: MatDialogRef<InventoryDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: {inventory: Inventory, action: InventoryAction}
  ) {
    this.form = this.createForm();
    this.form.get('action')?.disable();
  }

  ngOnInit(): void {
    this.loadDropdownOptions();
    this.setupFormBasedOnAction();
  }

  createForm(): FormGroup {
    const form = this.fb.group({
      // Common fields
      action: [this.data.action, Validators.required],
      remarks: [''],

      // Storage specific fields
      productId: [this.data.inventory?.productId ?? null],
      quantity: [this.data.inventory?.quantity ?? 0, [Validators.min(0)]],
      license: [this.data.inventory?.license ?? null],
      purchaseContractId: [this.data.inventory?.purchaseContractId ?? null],
      salesContractId: [this.data.inventory?.salesContractId ?? null],
      storageTime: [this.data.inventory?.storageTime ?? null],

      // Outbound/Borrow/Return specific fields
      outTime: [this.data.inventory?.outTime ?? null],
      shippingMethod: [this.data.inventory?.shippingMethod ?? null],
      shippingAddress: [this.data.inventory?.shippingAddress ?? null],
      receiverName: [this.data.inventory?.receiverName ?? null],
      receiverPhone: [this.data.inventory?.receiverPhone ?? null],
      expressTrackingNumber: [this.data.inventory?.expressTrackingNumber ?? null],
      handlingEmployeeId: [this.data.inventory?.handlingEmployeeId ?? null],

      // Borrow specific fields
      borrowedCompanyId: [this.data.inventory?.borrowedCompanyId ?? null],

      // Return specific fields
      status: [this.data.inventory?.status ?? null]
    });

    // Disable all fields if in VIEW mode
    if (this.data.action === InventoryAction.VIEW) {
      Object.keys(form.controls).forEach(key => {
        form.get(key)?.disable();
      });
    }

    return form;
  }

  loadDropdownOptions(): void {
    // Load products, contracts, and employees for dropdowns
    this.productService.getProducts().subscribe( (response:ApiResponse<Page<Product>>) => 
      this.products = response.data.content
    );
    this.contractService.getContracts({page: 0, size: 100}).subscribe(response => 
      this.contracts = response.data.content
    );
    this.employeeService.getActiveEmployees().subscribe( (response:ApiResponse<Employee[]>) => 
      this.employees = response.data
    );
    this.companyService.getCompanies({page: 0, size: 100}).subscribe( (response:ApiResponse<Page<Company>>) => 
      this.companies = response.data.content
    );
  }

  setupFormBasedOnAction(): void {
    // If in VIEW mode, disable form submission
    if (this.data.action === InventoryAction.VIEW) {
      this.form.disable();
      return;
    }

    // Listen to action changes and update form validation
    this.form.get('action')?.valueChanges.subscribe(action => {
      this.selectedAction = action;
      this.updateFormValidation(action);
    });
  }

  updateFormValidation(action: InventoryAction): void {
    const form = this.form;
    
    // Reset all validators
    form.get('productId')?.clearValidators();
    form.get('quantity')?.clearValidators();
    form.get('purchaseContractId')?.clearValidators();

    switch (action) {
      case InventoryAction.STORAGE:
        form.get('productId')?.setValidators([Validators.required]);
        form.get('quantity')?.setValidators([Validators.required, Validators.min(0)]);
        form.get('purchaseContractId')?.setValidators([Validators.required]);
        break;
      
      case InventoryAction.OUTBOUND:
      case InventoryAction.BORROW:
        form.get('handlingEmployeeId')?.setValidators([Validators.required]);
        form.get('outTime')?.setValidators([Validators.required]);
        break;
      
      case InventoryAction.RETURN:
        // No specific additional validators
        break;
    }

    // Rerun validation
    form.get('productId')?.updateValueAndValidity();
    form.get('quantity')?.updateValueAndValidity();
    form.get('purchaseContractId')?.updateValueAndValidity();
    form.get('handlingEmployeeId')?.updateValueAndValidity();
    form.get('outTime')?.updateValueAndValidity();
  }

  onSubmit(): void {
    // Prevent submission in VIEW mode
    if (this.data.action === InventoryAction.VIEW) {
      this.dialogRef.close();
      return;
    }

    if (this.form.valid) {
      this.loading = true;
      const formValue = this.form.value;
      
      let inventoryData: Inventory = {
        remarks: formValue.remarks
      };

      let request: Observable<ApiResponse<Inventory>>;

      switch (this.selectedAction) {
        case InventoryAction.OUTBOUND:
          inventoryData = {
            ...inventoryData,
            status: InventoryStatus.OUT_OF_STOCK,
            outTime: formValue.outTime,
            salesContractId: formValue.salesContractId,
            shippingMethod: formValue.shippingMethod,
            shippingAddress: formValue.shippingAddress,
            receiverName: formValue.receiverName,
            receiverPhone: formValue.receiverPhone,
            expressTrackingNumber: formValue.expressTrackingNumber,
            handlingEmployeeId: formValue.handlingEmployeeId
          };
          request = this.inventoryService.outbound(this.data?.inventory?.id ?? 0, inventoryData);
          break;

        case InventoryAction.BORROW:
          inventoryData = {
            ...inventoryData,
            status: InventoryStatus.BORROWED,
            outTime: formValue.outTime,
            shippingMethod: formValue.shippingMethod,
            shippingAddress: formValue.shippingAddress,
            receiverName: formValue.receiverName,
            receiverPhone: formValue.receiverPhone,
            expressTrackingNumber: formValue.expressTrackingNumber,
            borrowedCompanyId: formValue.borrowedCompanyId,
            handlingEmployeeId: formValue.handlingEmployeeId
          };
          request = this.inventoryService.borrow(this.data?.inventory?.id ?? 0, inventoryData);
          break;
        
        case InventoryAction.RETURN:
          inventoryData = {
            ...inventoryData,
            status: InventoryStatus.IN_STOCK
          };
          request = this.inventoryService.return(this.data?.inventory?.id ?? 0, inventoryData);
          break;

          default: // default to STORAGE
            inventoryData = {
              ...inventoryData,
              productId: formValue.productId,
              quantity: formValue.quantity,
              license: formValue.license,
              purchaseContractId: formValue.purchaseContractId,
              storageTime: formValue.storageTime,
              status: InventoryStatus.IN_STOCK
            };
            request = this.inventoryService.create(inventoryData);
            break;  
      }

      console.log('Submitting inventory data:', inventoryData);

      request.subscribe({
        next: (response) => {
          if (response.code === 200) {
            this.snackBar.open(
              this.translate.instant('COMMON.OPERATION_SUCCESS'),
              this.translate.instant('COMMON.CLOSE'),
              { duration: 3000 }
            );
          } else {
            this.snackBar.open(
              response.message || this.translate.instant('COMMON.ERROR_OCCURRED'),
              this.translate.instant('COMMON.CLOSE'),
              { duration: 3000 }
            );
          }
          this.dialogRef.close(true);
          this.loading = false;
        },
        error: (error) => {
          console.error('Error processing inventory:', error);
          this.snackBar.open(
            this.translate.instant('COMMON.ERROR_OCCURRED'),
            this.translate.instant('COMMON.CLOSE'),
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