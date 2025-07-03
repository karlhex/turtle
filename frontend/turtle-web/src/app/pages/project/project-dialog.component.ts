import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { Project } from '../../models/project.model';
import { ProjectService } from '../../services/project.service';
import { ProjectStatus } from '../../types/project-status.enum';
import { EmployeeService } from '../../services/employee.service';
import { Employee } from '../../models/employee.model';
import { Observable } from 'rxjs';
import { map, startWith } from 'rxjs/operators';
import { Contract } from '../../models/contract.model';
import { PaymentTotalPipe } from '../../pipes/payment-total.pipe';
import { ContractType } from '../../types/contract-type.enum';

@Component({
  selector: 'app-project-dialog',
  templateUrl: './project-dialog.component.html',
  styleUrls: ['./project-dialog.component.scss'],
  providers: [PaymentTotalPipe],
})
export class ProjectDialogComponent implements OnInit {
  form: FormGroup;
  projectStatuses = Object.values(ProjectStatus);
  employees: Employee[] = [];
  filteredEmployees!: Observable<Employee[]>;
  isEdit: boolean;
  contracts: Contract[] = [];
  contractTypes = ContractType;
  groupedContracts: { [key in ContractType]: Contract[] } = {
    [ContractType.PURCHASE]: [],
    [ContractType.SALES]: [],
  };
  totalsByType: { [key in ContractType]: { total: number; paid: number } } = {
    [ContractType.PURCHASE]: { total: 0, paid: 0 },
    [ContractType.SALES]: { total: 0, paid: 0 },
  };
  displayedColumns: string[] = [
    'contractNo',
    'title',
    'type',
    'totalAmount',
    'paidAmount',
    'status',
    'actions',
  ];

  constructor(
    private fb: FormBuilder,
    private dialog: MatDialog,
    private projectService: ProjectService,
    private employeeService: EmployeeService,
    private dialogRef: MatDialogRef<ProjectDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { project?: Project }
  ) {
    this.isEdit = !!data.project;
    this.form = this.fb.group({
      projectName: ['', [Validators.required]],
      projectNo: ['', [Validators.required]],
      startDate: [null, [Validators.required]],
      endDate: [null, [Validators.required]],
      status: [ProjectStatus.NOT_STARTED, [Validators.required]],
      manager: [null, [Validators.required]],
      remarks: [''],
    });

    if (this.isEdit && data.project) {
      this.form.patchValue({
        ...data.project,
        startDate: data.project.startDate ? new Date(data.project.startDate) : null,
        endDate: data.project.endDate ? new Date(data.project.endDate) : null,
      });
      this.contracts = data.project.contracts || [];
      this.groupContracts();
    }
  }

  ngOnInit(): void {
    this.loadEmployees();
    this.setupEmployeeFilter();
  }

  loadEmployees(): void {
    this.employeeService.getActiveEmployees().subscribe({
      next: response => {
        if (response.code === 200 && response.data) {
          this.employees = response.data;
        }
      },
      error: error => {
        console.error('Error loading employees:', error);
      },
    });
  }

  groupContracts() {
    // Reset grouped contracts
    this.groupedContracts = {
      [ContractType.PURCHASE]: [],
      [ContractType.SALES]: [],
    };
    this.totalsByType = {
      [ContractType.PURCHASE]: { total: 0, paid: 0 },
      [ContractType.SALES]: { total: 0, paid: 0 },
    };

    // Group contracts by type
    this.contracts.forEach(contract => {
      this.groupedContracts[contract.type].push(contract);

      // Calculate totals for each type
      this.totalsByType[contract.type].total += contract.totalAmount;
      const paidAmount =
        contract.downPayments?.reduce((sum, payment) => sum + payment.amount, 0) || 0;
      this.totalsByType[contract.type].paid += paidAmount;
    });
  }

  getTotalAmount(): number {
    return Object.values(this.totalsByType).reduce((sum, type) => sum + type.total, 0);
  }

  getTotalPaid(): number {
    return Object.values(this.totalsByType).reduce((sum, type) => sum + type.paid, 0);
  }

  private setupEmployeeFilter(): void {
    this.filteredEmployees = this.form.get('manager')!.valueChanges.pipe(
      startWith(''),
      map(value => {
        const searchValue = typeof value === 'string' ? value : value?.name || '';
        return this._filterEmployees(searchValue);
      })
    );
  }

  private _filterEmployees(value: string): Employee[] {
    const filterValue = value.toLowerCase();
    return this.employees.filter(employee => employee.name.toLowerCase().includes(filterValue));
  }

  displayFn(employee: Employee): string {
    return employee?.name || '';
  }

  onSubmit(): void {
    if (this.form.valid) {
      const formValue = this.form.value;
      const projectId = this.data.project?.id;

      const project: Project = {
        ...formValue,
        id: projectId,
      };

      if (this.isEdit && !projectId) {
        console.error('Project ID is missing for update operation');
        return;
      }

      const request = this.isEdit
        ? this.projectService.updateProject(projectId!, project)
        : this.projectService.createProject(project);

      request.subscribe({
        next: () => {
          this.dialogRef.close(true);
        },
      });
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }

  openContractDialog(): void {
    // Implement contract dialog opening logic
    console.log('Opening contract dialog');
  }

  viewContract(contract: Contract): void {
    // Implement contract viewing logic
    console.log('Viewing contract:', contract);
  }

  editContract(contract: Contract): void {
    // Implement contract editing logic
    console.log('Editing contract:', contract);
  }
}
