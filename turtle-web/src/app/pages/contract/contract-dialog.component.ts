import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { TranslateService } from '@ngx-translate/core';
import { Contract } from '../../models/contract.model';
import { ContractService } from '../../services/contract.service';
import { ContractType } from '../../types/contract-type.enum';
import { ContractStatus } from '../../types/contract-status.enum';
import { CurrencyService } from '../../services/currency.service';
import { ProjectService } from '../../services/project.service';
import { CompanyService } from '../../services/company.service';
import { Currency } from '../../models/currency.model';
import { Project } from '../../models/project.model';
import { Company } from '../../models/company.model';
import { ApiResponse } from '../../models/api.model';
import { Page } from '../../models/page.model';
import { Observable, map, startWith } from 'rxjs';
import { ContractItem } from '../../models/contract-item.model';
import { ContractItemDialogComponent } from './contract-item-dialog.component';
import { ConfirmDialogComponent } from '@components/confirmdialog/confirm-dialog.component';
import { MatDialog } from '@angular/material/dialog';
import { ContractDownPayment } from '../../models/contract-down-payment.model';
import { ContractDownPaymentDialogComponent } from './contract-down-payment-dialog.component';

@Component({
  selector: 'app-contract-dialog',
  templateUrl: './contract-dialog.component.html',
  styleUrls: ['./contract-dialog.component.scss']
})
export class ContractDialogComponent implements OnInit {
  form: FormGroup;
  isEdit: boolean;
  loading = false;
  contractTypes = Object.values(ContractType);
  contractStatuses = Object.values(ContractStatus);
  currencies: Currency[] = [];
  projects: Project[] = [];
  companies: Company[] = [];
  filteredBuyerCompanies!: Observable<Company[]>;
  filteredSellerCompanies!: Observable<Company[]>;
  contractItems: ContractItem[] = [];
  contractDownPayments: ContractDownPayment[] = [];
  displayedColumns: string[] = ['name', 'quantity', 'unitPrice', 'amount', 'actions'];
  displayedDownPaymentColumns: string[] = ['paymentInstruction', 'debitCreditType', 'amount', 'plannedDate', 'actualDate', 'paymentStatus', 'actions'];

  constructor(
    private fb: FormBuilder,
    private contractService: ContractService,
    private currencyService: CurrencyService,
    private projectService: ProjectService,
    private companyService: CompanyService,
    private dialogRef: MatDialogRef<ContractDialogComponent>,
    private snackBar: MatSnackBar,
    private translate: TranslateService,
    private dialog: MatDialog,
    @Inject(MAT_DIALOG_DATA) public data: Contract
  ) {
    this.isEdit = !!data.id;
    this.form = this.fb.group({
      id: [data.id],
      contractNo: [data.contractNo, [Validators.required]],
      title: [data.title, [Validators.required]],
      buyerCompany: [data.buyerCompany, [Validators.required]],
      sellerCompany: [data.sellerCompany, [Validators.required]],
      type: [data.type, [Validators.required]],
      signingDate: [data.signingDate, [Validators.required]],
      startDate: [data.startDate, [Validators.required]],
      endDate: [data.endDate, [Validators.required]],
      contactPerson: [data.contactPerson, [Validators.required]],
      contactPhone: [data.contactPhone],
      contactEmail: [data.contactEmail, [Validators.email]],
      projectId: [data.projectId],
      totalAmount: [data.totalAmount, [Validators.required, Validators.min(0)]],
      currency: [data.currency, [Validators.required]],
      status: [data.status || ContractStatus.PENDING_SIGNATURE, [Validators.required]],
      projectNo: [data.projectNo],
      description: [data.description],
      remarks: [data.remarks],
      paymentTerms: [data.paymentTerms],
      deliveryTerms: [data.deliveryTerms],
      warrantyTerms: [data.warrantyTerms]
    });
    
    // Initialize items if editing
    if (data.items) {
      this.contractItems = [...data.items];
    }
    if (data.downPayments) {
      this.contractDownPayments = [...data.downPayments];
    }
  }

  ngOnInit(): void {
    this.loadCurrencies();
    this.loadProjects();
    this.loadCompanies();
    
    // Initialize filtered companies
    this.initializeCompanyFilters();
  }

  private initializeCompanyFilters(): void {
    // Setup buyer company filter
    this.filteredBuyerCompanies = this.form.get('buyerCompany')!.valueChanges.pipe(
      startWith(''),
      map(value => {
        const name = typeof value === 'string' ? value : value?.fullName;
        return name ? this._filterCompanies(name) : this.companies.slice();
      })
    );

    // Setup seller company filter
    this.filteredSellerCompanies = this.form.get('sellerCompany')!.valueChanges.pipe(
      startWith(''),
      map(value => {
        const name = typeof value === 'string' ? value : value?.fullName;
        return name ? this._filterCompanies(name) : this.companies.slice();
      })
    );
  }

  private _filterCompanies(value: string): Company[] {
    const filterValue = value.toLowerCase();
    return this.companies.filter(company => 
      company.fullName.toLowerCase().includes(filterValue) ||
      (company.shortName && company.shortName.toLowerCase().includes(filterValue))
    );
  }

  loadCurrencies(): void {
    this.currencyService.getCurrencies({ page: 0, size: 100 }).subscribe({
      next: (response) => {
        this.currencies = response.data.content;
      },
      error: (error) => {
        this.showError('Error loading currencies');
        console.error('Failed to load currencies:', error);
      }
    });
  }

  loadProjects(): void {
    this.projectService.getProjects({ page: 0, size: 100 }).subscribe({
      next: (response: ApiResponse<Page<Project>>) => {
        this.projects = response.data.content;
      },
      error: (error) => {
        this.showError('Error loading projects');
        console.error('Failed to load projects:', error);
      }
    });
  }

  loadCompanies(): void {
    this.companyService.getCompanies({ page: 0, size: 100 }).subscribe({
      next: (response: ApiResponse<Page<Company>>) => {
        this.companies = response.data.content;
      },
      error: (error) => {
        this.showError('Error loading companies');
        console.error('Failed to load companies:', error);
      }
    });
  }

  public displayCompany(company: Company | null): string {
    return company ? company.fullName : '';
  }

  addContractItem(): void {
    const dialogRef = this.dialog.open(ContractItemDialogComponent, {
      width: '600px',
      data: {
        currency: this.form.get('currency')?.value
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log("Add contract item", result);
      if (result) {
        // Create a new array to trigger change detection
        this.contractItems = [...this.contractItems, result];
        console.log("Updated contract items", this.contractItems);
      }
    });
  }

  onContractItemDeleted(item: ContractItem): void {
    const index = this.contractItems.indexOf(item);
    if (index > -1) {
      this.contractItems.splice(index, 1);
    }
  }

  onContractItemEdited(item: ContractItem): void {
    const dialogRef = this.dialog.open(ContractItemDialogComponent, {
      width: '600px',
      data: {
        currency: this.form.get('currency')?.value,
        item: item
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        const index = this.contractItems.findIndex(i => i === item);
        if (index > -1) {
          this.contractItems[index] = result;
        }
      }
    });
  }

  openDownPaymentDialog(downPayment?: ContractDownPayment): void {
    const dialogRef = this.dialog.open(ContractDownPaymentDialogComponent, {
      data: {
        contractId: this.form.get('id')?.value,
        currency: this.form.get('currency')?.value,
        ...downPayment
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        const index = this.contractDownPayments.findIndex(dp => dp === downPayment);
        if (index !== -1) {
          this.contractDownPayments[index] = result;
        } else {
          this.contractDownPayments.push(result);
        }
        this.contractDownPayments = [...this.contractDownPayments];
      }
    });
  }

  deleteDownPayment(downPayment: ContractDownPayment): void {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: {
        title: this.translate.instant('contract.downPayment.delete.title'),
        message: this.translate.instant('contract.downPayment.delete.message')
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        const index = this.contractDownPayments.indexOf(downPayment);
        if (index !== -1) {
          this.contractDownPayments.splice(index, 1);
          this.contractDownPayments = [...this.contractDownPayments];
        }
      }
    });
  }

  onDownPaymentStatusChanged(event: {item: ContractDownPayment, status: boolean}): void {
    const index = this.contractDownPayments.findIndex(dp => dp === event.item);
    if (index !== -1) {
      this.contractDownPayments[index] = {
        ...this.contractDownPayments[index],
        paymentStatus: event.status,
        actualDate: event.status ? new Date() : undefined
      };
      this.contractDownPayments = [...this.contractDownPayments];
    }
  }

  submit(): void {
    if (this.form.valid) {
      this.loading = true;
      const formValue = this.form.value;
      formValue.items = this.contractItems;
      formValue.downPayments = this.contractDownPayments;
      const request = this.isEdit
        ? this.contractService.updateContract(formValue.id!, formValue)
        : this.contractService.createContract(formValue);

      console.log("Saving contract:", formValue);

      request.subscribe({
        next: (response: ApiResponse<Contract>) => {
          console.log(response);
          if (response.code === 200) {
            this.dialogRef.close(response.data);
            this.showSuccess(this.isEdit ? 'Contract updated successfully' : 'Contract created successfully');
          } else {
            this.showError(response.message || 'Failed to save contract');
            this.loading = false;
          }
        },
        error: (error) => {
          this.showError('Error saving contract');
          console.error('Failed to save contract:', error);
          this.loading = false;
        }
      });
    } else {
      Object.keys(this.form.controls).forEach(key => {
        const control = this.form.get(key);
        if (control?.invalid) {
          control.markAsTouched();
        }
      });
    }
  }

  cancel(): void {
    this.dialogRef.close();
  }

  private showError(message: string): void {
    this.snackBar.open(message, 'Close', {
      duration: 5000,
      panelClass: ['error-snackbar']
    });
  }

  private showSuccess(message: string): void {
    this.snackBar.open(message, 'Close', {
      duration: 3000,
      panelClass: ['success-snackbar']
    });
  }
}
