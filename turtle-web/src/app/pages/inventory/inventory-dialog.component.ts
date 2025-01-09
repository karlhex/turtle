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

enum InventoryAction {
  STORAGE = 'STORAGE',
  OUTBOUND = 'OUTBOUND',
  BORROW = 'BORROW',
  RETURN = 'RETURN'
}

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
  shippingMethods = Object.values(ShippingMethod);
  
  // Action types
  actionTypes = Object.values(InventoryAction);
  selectedAction: InventoryAction = InventoryAction.STORAGE;

  constructor(
    private fb: FormBuilder,
    private inventoryService: InventoryService,
    private productService: ProductService,
    private contractService: ContractService,
    private employeeService: EmployeeService,
    private snackBar: MatSnackBar,
    private translate: TranslateService,
    private dialogRef: MatDialogRef<InventoryDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: Inventory
  ) {
    this.form = this.createForm();
  }

  ngOnInit(): void {
    this.loadDropdownOptions();
    this.setupFormBasedOnAction();
  }

  createForm(): FormGroup {
    return this.fb.group({
      // Common fields
      action: [InventoryAction.STORAGE, Validators.required],
      remarks: [''],

      // Storage specific fields
      productId: [''],
      quantity: ['', [Validators.min(0)]],
      license: [''],
      purchaseContractId: [''],
      storageTime: [new Date()],

      // Outbound/Borrow/Return specific fields
      outTime: [''],
      shippingMethod: [''],
      shippingAddress: [''],
      receiverName: [''],
      receiverPhone: [''],
      expressTrackingNumber: [''],
      handlingEmployeeId: ['']
    });
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
  }

  setupFormBasedOnAction(): void {
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
    if (this.form.valid) {
      this.loading = true;
      const formValue = this.form.value;
      
      let inventoryData: Inventory = {
        remarks: formValue.remarks
      };

      switch (formValue.action) {
        case InventoryAction.STORAGE:
          inventoryData = {
            ...inventoryData,
            productId: formValue.productId,
            quantity: formValue.quantity,
            license: formValue.license,
            purchaseContractId: formValue.purchaseContractId,
            storageTime: formValue.storageTime,
            status: InventoryStatus.IN_STOCK
          };
          break;
        
        case InventoryAction.OUTBOUND:
        case InventoryAction.BORROW:
          inventoryData = {
            ...inventoryData,
            outTime: formValue.outTime,
            status: InventoryStatus.OUT_OF_STOCK,
            shippingMethod: formValue.shippingMethod,
            shippingAddress: formValue.shippingAddress,
            receiverName: formValue.receiverName,
            receiverPhone: formValue.receiverPhone,
            expressTrackingNumber: formValue.expressTrackingNumber,
            handlingEmployeeId: formValue.handlingEmployeeId
          };
          break;
        
        case InventoryAction.RETURN:
          inventoryData = {
            ...inventoryData,
            status: InventoryStatus.IN_STOCK
          };
          break;
      }

      // Decide between create and update based on existing data
      const request = this.data?.id 
        ? this.inventoryService.update(this.data.id, inventoryData)
        : this.inventoryService.create(inventoryData);

      request.subscribe({
        next: (response) => {
          this.snackBar.open(
            this.translate.instant('COMMON.OPERATION_SUCCESS'),
            this.translate.instant('COMMON.CLOSE'),
            { duration: 3000 }
          );
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
