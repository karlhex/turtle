import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { TranslateService } from '@ngx-translate/core';
import { Contact } from '../../models/contact.model';
import { Company } from '../../models/company.model';
import { ContactService } from '../../services/contact.service';
import { CompanyService } from '../../services/company.service';
import { Observable } from 'rxjs';
import { map, startWith } from 'rxjs/operators';

@Component({
  selector: 'app-contact-dialog',
  templateUrl: './contact-dialog.component.html',
  styleUrls: ['./contact-dialog.component.scss']
})
export class ContactDialogComponent implements OnInit {
  form: FormGroup;
  loading = false;
  showMore = false;
  companies: Company[] = [];
  filteredCompanies!: Observable<Company[]>;

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<ContactDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { mode: 'create' | 'edit', contact?: Contact },
    private contactService: ContactService,
    private companyService: CompanyService,
    private snackBar: MatSnackBar,
    private translate: TranslateService
  ) {
    this.form = this.fb.group({
      // 基本信息
      firstName: ['', [Validators.required]],
      lastName: ['', [Validators.required]],
      gender: [''],
      
      // 联系方式
      email: ['', [Validators.email]],
      mobilePhone: ['', [Validators.required]],
      workPhone: [''],
      homePhone: [''],
      address: [''],
      
      // 公司信息
      companyId: [null],
      companyName: [''],
      department: [''],
      title: [''],
      
      // 个人信息
      nativePlace: [''],
      ethnicity: [''],
      maritalStatus: [''],
      nationality: [''],
      birthDate: [''],
      religion: [''],
      
      // 教育信息
      university: [''],
      major: [''],
      graduationYear: [''],
      degree: [''],
      
      // 其他信息
      hobbies: [''],
      remarks: [''],
      active: [true]
    });
  }

  ngOnInit(): void {
    this.loadCompanies();
    if (this.data.mode === 'edit' && this.data.contact) {
      this.form.patchValue(this.data.contact);
    }

    // 设置公司自动完成
    this.filteredCompanies = this.form.get('companyName')!.valueChanges.pipe(
      startWith(''),
      map(value => this._filterCompanies(value))
    );
  }

  private _filterCompanies(value: string): Company[] {
    const filterValue = value.toLowerCase();
    return this.companies.filter(company => 
      company.fullName.toLowerCase().includes(filterValue) ||
      (company.shortName && company.shortName.toLowerCase().includes(filterValue))
    );
  }

  loadCompanies(): void {
    this.companyService.getAllActive().subscribe({
      next: (response) => {
        if (response.code === 200) {
          this.companies = response.data;
        }
      },
      error: (error) => console.error('Error loading companies:', error)
    });
  }

  onCompanySelected(company: Company): void {
    this.form.patchValue({
      companyId: company.id,
      companyName: company.fullName
    });
  }

  onSubmit(): void {
    if (this.form.valid) {
      this.loading = true;
      const contact: Contact = this.form.value;
      
      const request = this.data.mode === 'create' 
        ? this.contactService.createContact(contact)
        : this.contactService.updateContact(this.data.contact!.id!, contact);

      request.subscribe({
        next: (response) => {
          if (response.code === 200) {
            this.snackBar.open(
              this.translate.instant(
                this.data.mode === 'create'
                  ? 'CONTACT.CREATE_SUCCESS'
                  : 'CONTACT.UPDATE_SUCCESS'
              ),
              this.translate.instant('ACTIONS.CLOSE'),
              { duration: 3000 }
            );
            this.dialogRef.close(response.data);
          }
          this.loading = false;
        },
        error: (error) => {
          console.error('Error saving contact:', error);
          this.snackBar.open(
            error.error?.message || this.translate.instant('ERROR.SAVE_CONTACT'),
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

  toggleMore(): void {
    this.showMore = !this.showMore;
  }
}
