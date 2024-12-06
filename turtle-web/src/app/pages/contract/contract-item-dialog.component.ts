import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ContractItem } from '../../models/contract-item.model';
import { Product } from '@models/product.model';
import { ProductService } from '../../services/product.service';
import { Observable } from 'rxjs';
import { map, startWith } from 'rxjs/operators';
import { Currency } from '../../models/currency.model';
import { Page } from '../../models/page.model';
import { ApiResponse } from '../../models/api.model';

interface DialogData {
  contractId?: number;
  currency: Currency;
}

@Component({
  selector: 'app-contract-item-dialog',
  templateUrl: './contract-item-dialog.component.html',
  styleUrls: ['./contract-item-dialog.component.scss']
})
export class ContractItemDialogComponent {
  form: FormGroup;
  products: Product[] = [];
  filteredProducts!: Observable<Product[]>;
  loading = false;

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<ContractItemDialogComponent>,
    private productService: ProductService,
    @Inject(MAT_DIALOG_DATA) public data: DialogData
  ) {
    this.form = this.fb.group({
      contractId: [data.contractId],
      product: [null, [Validators.required]],
      productName: ['', [Validators.required]],
      quantity: [1, [Validators.required, Validators.min(1)]],
      unitPrice: [0, [Validators.required, Validators.min(0)]],
      totalAmount: [0],
      modelNumber: [''],
      remarks: ['']
    });

    // Load products
    this.loadProducts();

    // Setup product filtering
    this.filteredProducts = this.form.get('product')!.valueChanges.pipe(
      startWith(''),
      map(value => this._filterProducts(value))
    );

    // Calculate total amount when quantity or unit price changes
    this.form.get('quantity')?.valueChanges.subscribe(() => this.calculateTotalAmount());
    this.form.get('unitPrice')?.valueChanges.subscribe(() => this.calculateTotalAmount());
  }

  private calculateTotalAmount(): void {
    const quantity = this.form.get('quantity')?.value || 0;
    const unitPrice = this.form.get('unitPrice')?.value || 0;
    this.form.patchValue({ totalAmount: quantity * unitPrice }, { emitEvent: false });
  }

  private loadProducts(): void {
    this.productService.getProducts().subscribe({
      next: (response: ApiResponse<Page<Product>>) => {
        if (response.code === 200 && response.data) {
          this.products = response.data.content;
        }
      },
      error: (error) => {
        console.error('Error loading products:', error);
      }
    });
  }

  private _filterProducts(value: string | Product): Product[] {
    if (!value) {
      return this.products;
    }
    const searchValue = typeof value === 'string' ? value.toLowerCase() : value.name.toLowerCase();
    return this.products.filter(product => 
      product.name.toLowerCase().includes(searchValue) ||
      (product.modelNumber && product.modelNumber.toLowerCase().includes(searchValue))
    );
  }

  onProductSelected(product: Product): void {
    if (product) {
      this.form.patchValue({
        product: product,
        productName: product.name,
        modelNumber: product.modelNumber || '',
        unitPrice: 0
      });
      
      // Recalculate total amount
      this.calculateTotalAmount();
      
      // Mark the form controls as touched to trigger validation
      Object.keys(this.form.controls).forEach(key => {
        const control = this.form.get(key);
        if (control) {
          control.markAsTouched();
        }
      });
    }
  }

  displayProductFn(product: Product | null): string {
    if (!product) return '';
    return product.name + (product.modelNumber ? ` (${product.modelNumber})` : '');
  }

  submit(): void {
    if (this.form.valid) {
      const formValue = this.form.value;
      const contractItem: ContractItem = {
        contractId: formValue.contractId,
        product: formValue.product,
        productName: formValue.productName,
        quantity: formValue.quantity,
        unitPrice: formValue.unitPrice,
        totalAmount: formValue.totalAmount,
        modelNumber: formValue.modelNumber,
        remarks: formValue.remarks
      };
      this.dialogRef.close(contractItem);
    }
  }

  cancel(): void {
    this.dialogRef.close();
  }
}
