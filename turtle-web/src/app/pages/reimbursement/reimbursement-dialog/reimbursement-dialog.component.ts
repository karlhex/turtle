import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, FormArray } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { TranslateService } from '@ngx-translate/core';
import { ReimbursementService } from '../../../services/reimbursement.service';
import { EmployeeService } from '../../../services/employee.service';
import { ProjectService } from '../../../services/project.service';
import { 
  Reimbursement, 
  CreateReimbursementRequest,
  UpdateReimbursementRequest 
} from '../../../models/reimbursement.model';
import { CreateReimbursementItemRequest } from '../../../models/reimbursement-item.model';
import { Employee } from '../../../models/employee.model';
import { Project } from '../../../models/project.model';
import { ProjectStatus } from '../../../types/project-status.enum';
import { ReimbursementStatus } from '../../../types/reimbursement-status.enum';

interface DialogData {
  mode: 'create' | 'edit';
  reimbursement?: Reimbursement;
}

@Component({
  selector: 'app-reimbursement-dialog',
  templateUrl: './reimbursement-dialog.component.html',
  styleUrls: ['./reimbursement-dialog.component.scss']
})
export class ReimbursementDialogComponent implements OnInit {
  form: FormGroup;
  loading = false;
  mode: 'create' | 'edit';
  employees: Employee[] = [];
  projects: Project[] = [];
  totalAmount = 0;
  status: ReimbursementStatus = ReimbursementStatus.DRAFT;

  get isReadOnly(): boolean {
    return this.status === ReimbursementStatus.PENDING || 
           this.status === ReimbursementStatus.APPROVED;
  }

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<ReimbursementDialogComponent>,
    @Inject(MAT_DIALOG_DATA) private data: DialogData,
    private reimbursementService: ReimbursementService,
    private employeeService: EmployeeService,
    private projectService: ProjectService,
    private snackBar: MatSnackBar,
    private translate: TranslateService
  ) {
    this.mode = data.mode;
    this.form = this.createForm();
  }

  ngOnInit(): void {
    if (this.mode === 'edit' && this.data.reimbursement) {
      this.populateForm(this.data.reimbursement);
    }
    this.loadEmployees();
    this.loadProjects();
  }

  private loadEmployees(): void {
    this.employeeService.getActiveEmployees().subscribe({
      next: (response) => {
        this.employees = response.data;
      },
      error: (error) => {
        console.error('Error loading employees:', error);
        this.snackBar.open(
          this.translate.instant('common.error.loadFailed'),
          this.translate.instant('common.action.close'),
          { duration: 3000 }
        );
      }
    });
  }

  private loadProjects(): void {
    const params = {
      page: 0,
      size: 1000,
      status: ProjectStatus.IN_PROGRESS
    };
    
    this.projectService.getProjects(params).subscribe({
      next: (response) => {
        this.projects = response.data.content;
      },
      error: (error) => {
        console.error('Error loading projects:', error);
        this.snackBar.open(
          this.translate.instant('common.error.loadFailed'),
          this.translate.instant('common.action.close'),
          { duration: 3000 }
        );
      }
    });
  }

  private createForm(): FormGroup {
    return this.fb.group({
      reimbursementNo: ['', Validators.required],
      applicationDate: ['', Validators.required],
      applicantId: ['', Validators.required],
      projectId: [''],
      remarks: [''],
      items: this.fb.array([]),
      status: [ReimbursementStatus.DRAFT]
    });
  }

  private updateTotalAmount(): void {
    const items = this.form.get('items')?.value || [];
    this.totalAmount = items.reduce((sum: number, item: any) => sum + (Number(item.amount) || 0), 0);
  }

  private populateForm(reimbursement: Reimbursement): void {
    this.form.patchValue({
      reimbursementNo: reimbursement.reimbursementNo,
      applicationDate: reimbursement.applicationDate,
      applicantId: reimbursement.applicantId,
      projectId: reimbursement.projectId,
      remarks: reimbursement.remarks,
      status: reimbursement.status
    });

    this.status = reimbursement.status as ReimbursementStatus;

    // Clear existing items
    const itemsArray = this.form.get('items') as FormArray;
    while (itemsArray.length !== 0) {
      itemsArray.removeAt(0);
    }

    console.log(reimbursement);
    
    // Add items if they exist
    if (reimbursement.items && reimbursement.items.length > 0) {
      reimbursement.items.forEach(item => {
        console.log(item);

        itemsArray.push(this.createItemFormGroup(item));
      });
    }

    // Update total amount
    this.updateTotalAmount();
  }

  private createItemFormGroup(item?: any): FormGroup {
    const group = this.fb.group({
      amount: [item?.amount || '', [Validators.required, Validators.min(0)]],
      reason: [item?.reason || '', Validators.required],
      occurrenceDate: [item?.occurrenceDate || '', Validators.required],
      remarks: [item?.remarks || '']
    });

    group.get('amount')?.valueChanges.subscribe(() => {
      this.updateTotalAmount();
    });

    return group;
  }

  get items(): FormArray {
    return this.form.get('items') as FormArray;
  }

  addItem(): void {
    this.items.push(this.createItemFormGroup());
  }

  removeItem(index: number): void {
    this.items.removeAt(index);
  }

  onSave(): void {
    if (this.form.invalid) return;

    this.loading = true;
    const formValue = this.form.value;
    formValue.status = ReimbursementStatus.DRAFT;

    if (this.mode === 'create') {
      const request: CreateReimbursementRequest = {
        ...formValue,
        totalAmount: this.totalAmount
      };

      this.reimbursementService.create(request).subscribe({
        next: () => {
          this.snackBar.open(
            this.translate.instant('reimbursement.message.createSuccess'),
            this.translate.instant('common.action.close'),
            { duration: 3000 }
          );
          this.dialogRef.close(true);
        },
        error: (error) => {
          console.error('Error creating reimbursement:', error);
          this.snackBar.open(
            this.translate.instant('reimbursement.message.createError'),
            this.translate.instant('common.action.close'),
            { duration: 3000 }
          );
          this.loading = false;
        }
      });
    } else {
      const request: UpdateReimbursementRequest = {
        ...formValue,
        id: this.data.reimbursement!.id,
        totalAmount: this.totalAmount
      };

      this.reimbursementService.update(this.data.reimbursement!.id, request).subscribe({
        next: () => {
          this.snackBar.open(
            this.translate.instant('reimbursement.message.updateSuccess'),
            this.translate.instant('common.action.close'),
            { duration: 3000 }
          );
          this.dialogRef.close(true);
        },
        error: (error) => {
          console.error('Error updating reimbursement:', error);
          this.snackBar.open(
            this.translate.instant('reimbursement.message.updateError'),
            this.translate.instant('common.action.close'),
            { duration: 3000 }
          );
          this.loading = false;
        }
      });
    }
  }

  onSubmit(): void {
    if (this.form.invalid) return;

    this.loading = true;
    const formValue = this.form.value;
    formValue.status = ReimbursementStatus.PENDING;

    if (this.mode === 'create') {
      const request: CreateReimbursementRequest = {
        ...formValue,
        totalAmount: this.totalAmount
      };

      this.reimbursementService.create(request).subscribe({
        next: () => {
          this.snackBar.open(
            this.translate.instant('reimbursement.message.submitSuccess'),
            this.translate.instant('common.action.close'),
            { duration: 3000 }
          );
          this.dialogRef.close(true);
        },
        error: (error) => {
          console.error('Error submitting reimbursement:', error);
          this.snackBar.open(
            this.translate.instant('reimbursement.message.submitError'),
            this.translate.instant('common.action.close'),
            { duration: 3000 }
          );
          this.loading = false;
        }
      });
    } else {
      const request: UpdateReimbursementRequest = {
        ...formValue,
        id: this.data.reimbursement!.id,
        totalAmount: this.totalAmount
      };

      this.reimbursementService.update(this.data.reimbursement!.id, request).subscribe({
        next: () => {
          this.snackBar.open(
            this.translate.instant('reimbursement.message.submitSuccess'),
            this.translate.instant('common.action.close'),
            { duration: 3000 }
          );
          this.dialogRef.close(true);
        },
        error: (error) => {
          console.error('Error submitting reimbursement:', error);
          this.snackBar.open(
            this.translate.instant('reimbursement.message.submitError'),
            this.translate.instant('common.action.close'),
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
