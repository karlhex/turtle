import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ContractItem } from '../../models/contract-item.model';
import { Product } from '@models/product.model';
import { ProductService } from '../../services/product.service';
import { Observable, combineLatest } from 'rxjs';
import { map, startWith } from 'rxjs/operators';
import { Currency } from '../../models/currency.model';
import { Page } from '../../models/page.model';
import { ApiResponse } from '../../models/api.model';

interface DialogData {
  currency: Currency;
  id?: number;
  product?: Product;
  quantity?: number;
  unitPrice?: number;
  totalAmount?: number;
  modelNumber?: string;
  remarks?: string;
}

@Component({
  selector: 'app-contract-item-dialog',
  templateUrl: './contract-item-dialog.component.html',
  styleUrls: ['./contract-item-dialog.component.scss'],
})
export class ContractItemDialogComponent {
  form: FormGroup;
  products: Product[] = [];
  filteredProducts$: Observable<Product[]>;
  loading = false;

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<ContractItemDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData,
    private productService: ProductService
  ) {
    this.form = this.fb.group({
      id: [data.id],
      product: [data.product || null, [Validators.required]],
      quantity: [data.quantity || 1, [Validators.required, Validators.min(1)]],
      unitPrice: [data.unitPrice || 0, [Validators.required, Validators.min(0)]],
      totalAmount: [data.totalAmount || 0],
      modelNumber: [data.modelNumber || ''],
      remarks: [data.remarks || ''],
    });

    // Calculate total amount when quantity or unit price changes
    combineLatest([
      this.form.get('quantity')!.valueChanges.pipe(startWith(this.form.get('quantity')!.value)),
      this.form.get('unitPrice')!.valueChanges.pipe(startWith(this.form.get('unitPrice')!.value)),
    ]).subscribe(([quantity, unitPrice]) => {
      const total = quantity * unitPrice;
      this.form.patchValue({ totalAmount: total }, { emitEvent: false });
    });

    // Load products
    this.loadProducts();

    // Setup product filtering
    this.filteredProducts$ = this.form.get('product')!.valueChanges.pipe(
      startWith(''),
      map(value => this._filterProducts(value))
    );
  }

  private loadProducts(): void {
    this.loading = true;
    this.productService.getProducts().subscribe(
      (response: ApiResponse<Page<Product>>) => {
        if (response.code === 200 && response.data) {
          this.products = response.data.content;
        }
        this.loading = false;
      },
      error => {
        console.error('Error loading products:', error);
        this.loading = false;
      }
    );
  }

  private _filterProducts(value: string | Product): Product[] {
    if (!value) {
      return this.products;
    }

    if (typeof value === 'string') {
      const filterValue = value.toLowerCase();
      return this.products.filter(
        product =>
          product.name.toLowerCase().includes(filterValue) ||
          (product.modelNumber && product.modelNumber.toLowerCase().includes(filterValue))
      );
    }

    return this.products;
  }

  displayProductFn(product: Product): string {
    return product ? product.name : '';
  }

  onProductSelected(product: Product): void {
    if (product) {
      this.form.patchValue({
        product: product,
        modelNumber: product.modelNumber || '',
        unitPrice: 0,
      });

      // Recalculate total amount
      const quantity = this.form.get('quantity')?.value || 0;
      const unitPrice = 0;
      this.form.patchValue({ totalAmount: quantity * unitPrice }, { emitEvent: false });

      // Mark the form controls as touched to trigger validation
      Object.keys(this.form.controls).forEach(key => {
        const control = this.form.get(key);
        control?.markAsTouched();
      });
    }
  }

  submit(): void {
    if (this.form.valid) {
      const formValue = this.form.value;
      const contractItem: ContractItem = {
        id: formValue.id,
        product: formValue.product,
        productName: formValue.product?.name || '',
        quantity: formValue.quantity,
        unitPrice: formValue.unitPrice,
        totalAmount: formValue.quantity * formValue.unitPrice,
        modelNumber: formValue.modelNumber,
        remarks: formValue.remarks,
      };

      console.log('Submitting contract item:', contractItem);
      this.dialogRef.close(contractItem);
    } else {
      // 如果表单无效，标记所有字段为touched以显示验证错误
      Object.keys(this.form.controls).forEach(key => {
        const control = this.form.get(key);
        control?.markAsTouched();
      });
      console.log('Form is invalid:', this.form.errors);
    }
  }

  cancel(): void {
    this.dialogRef.close();
  }
}
