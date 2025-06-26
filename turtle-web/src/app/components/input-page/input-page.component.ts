import { Component, Input, Output, EventEmitter, OnInit, OnDestroy, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators, AbstractControl } from '@angular/forms';
import { Subject, takeUntil } from 'rxjs';
import { MatDatepicker } from '@angular/material/datepicker';

export interface InputPageConfig {
  title: string;
  subtitle?: string;
  fields: FormFieldConfig[];
  layout: 'single' | 'two-column' | 'stepper' | 'auto-column';
  showSaveButton: boolean;
  showCancelButton: boolean;
  showResetButton: boolean;
  saveButtonText?: string;
  cancelButtonText?: string;
  resetButtonText?: string;
  autoSave?: boolean;
  autoSaveInterval?: number;
  autoColumnThreshold?: number; // 自动两列的字段数量阈值
}

export interface FormFieldConfig {
  key: string;
  label: string;
  type: 'text' | 'number' | 'email' | 'password' | 'textarea' | 'select' | 'checkbox' | 'radio' | 'date' | 'file' | 'custom';
  required: boolean;
  validators?: any[];
  placeholder?: string;
  helpText?: string;
  disabled?: boolean;
  hidden?: boolean;
  defaultValue?: any;
  options?: SelectOption[];
  rows?: number;
  maxLength?: number;
  minLength?: number;
  pattern?: string;
  min?: number;
  max?: number;
  step?: number;
  accept?: string;
  multiple?: boolean;
  customComponent?: string;
  width?: 1 | 2 | 3 | 4; // 1=100%, 2=1/2, 3=1/3, 4=1/4
}

export interface SelectOption {
  value: any;
  label: string;
  disabled?: boolean;
}

export interface FormData {
  [key: string]: any;
}

@Component({
  selector: 'app-input-page',
  templateUrl: './input-page.component.html',
  styleUrls: ['./input-page.component.scss']
})
export class InputPageComponent implements OnInit, OnDestroy {
  @Input() config!: InputPageConfig;
  @Input() data: FormData = {};
  @Input() loading = false;
  @Input() saving = false;

  @Output() save = new EventEmitter<FormData>();
  @Output() cancel = new EventEmitter<void>();
  @Output() reset = new EventEmitter<void>();
  @Output() fieldChange = new EventEmitter<{ key: string; value: any }>();
  @Output() validationChange = new EventEmitter<{ valid: boolean; errors: any }>();

  @ViewChild('datePicker') datePicker!: MatDatepicker<Date>;

  form!: FormGroup;
  private destroy$ = new Subject<void>();
  private autoSaveTimer?: any;

  // 计算实际使用的布局
  get actualLayout(): string {
    if (this.config.layout === 'auto-column') {
      const threshold = this.config.autoColumnThreshold || 6;
      return this.config.fields.length > threshold ? 'two-column' : 'single';
    }
    return this.config.layout;
  }

  // 获取可见字段
  get visibleFields(): FormFieldConfig[] {
    return this.config.fields.filter(field => this.isFieldVisible(field));
  }

  // 两列布局：左列字段
  get leftColumnFields(): FormFieldConfig[] {
    const fields = this.visibleFields;
    const mid = Math.ceil(fields.length / 2);
    return fields.slice(0, mid);
  }
  // 两列布局：右列字段
  get rightColumnFields(): FormFieldConfig[] {
    const fields = this.visibleFields;
    const mid = Math.ceil(fields.length / 2);
    return fields.slice(mid);
  }

  constructor(private fb: FormBuilder) {}

  ngOnInit(): void {
    this.initializeForm();
    this.setupAutoSave();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
    if (this.autoSaveTimer) {
      clearInterval(this.autoSaveTimer);
    }
  }

  private initializeForm(): void {
    const group: { [key: string]: AbstractControl } = {};

    this.config.fields.forEach(field => {
      const validators = this.buildValidators(field);
      group[field.key] = this.fb.control(
        this.data[field.key] || field.defaultValue || this.getDefaultValue(field),
        validators
      );
    });

    this.form = this.fb.group(group);

    // Listen to form changes
    this.form.valueChanges.pipe(takeUntil(this.destroy$)).subscribe(values => {
      this.validationChange.emit({
        valid: this.form.valid,
        errors: this.form.errors
      });
    });
  }

  private buildValidators(field: FormFieldConfig): any[] {
    const validators: any[] = [];

    if (field.required) {
      validators.push(Validators.required);
    }

    if (field.validators) {
      validators.push(...field.validators);
    }

    if (field.minLength) {
      validators.push(Validators.minLength(field.minLength));
    }

    if (field.maxLength) {
      validators.push(Validators.maxLength(field.maxLength));
    }

    if (field.pattern) {
      validators.push(Validators.pattern(field.pattern));
    }

    if (field.min !== undefined) {
      validators.push(Validators.min(field.min));
    }

    if (field.max !== undefined) {
      validators.push(Validators.max(field.max));
    }

    if (field.type === 'email') {
      validators.push(Validators.email);
    }

    return validators;
  }

  private getDefaultValue(field: FormFieldConfig): any {
    switch (field.type) {
      case 'checkbox':
        return false;
      case 'select':
        return field.options?.[0]?.value || '';
      case 'number':
        return 0;
      default:
        return '';
    }
  }

  private setupAutoSave(): void {
    if (this.config.autoSave && this.config.autoSaveInterval) {
      this.autoSaveTimer = setInterval(() => {
        if (this.form.valid && this.form.dirty) {
          this.onSave();
        }
      }, this.config.autoSaveInterval);
    }
  }

  onSave(): void {
    if (this.form.valid) {
      this.save.emit(this.form.value);
    } else {
      this.markFormGroupTouched();
    }
  }

  onCancel(): void {
    this.cancel.emit();
  }

  onReset(): void {
    this.form.reset();
    this.reset.emit();
  }

  onFieldChange(key: string, value: any): void {
    this.fieldChange.emit({ key, value });
  }

  onFileSelect(event: any, field: FormFieldConfig): void {
    const files = event.target.files;
    if (files && files.length > 0) {
      if (field.multiple) {
        this.form.get(field.key)?.setValue(Array.from(files));
      } else {
        this.form.get(field.key)?.setValue(files[0]);
      }
    }
  }

  getFieldError(field: FormFieldConfig): string {
    const control = this.form.get(field.key);
    if (control && control.errors && control.touched) {
      if (control.errors['required']) {
        return `${field.label} 是必填项`;
      }
      if (control.errors['email']) {
        return '请输入有效的邮箱地址';
      }
      if (control.errors['minlength']) {
        return `${field.label} 最少需要 ${control.errors['minlength'].requiredLength} 个字符`;
      }
      if (control.errors['maxlength']) {
        return `${field.label} 最多只能有 ${control.errors['maxlength'].requiredLength} 个字符`;
      }
      if (control.errors['pattern']) {
        return `${field.label} 格式不正确`;
      }
      if (control.errors['min']) {
        return `${field.label} 不能小于 ${control.errors['min'].min}`;
      }
      if (control.errors['max']) {
        return `${field.label} 不能大于 ${control.errors['max'].max}`;
      }
    }
    return '';
  }

  isFieldVisible(field: FormFieldConfig): boolean {
    if (field.hidden) {
      return false;
    }
    // Add conditional visibility logic here if needed
    return true;
  }

  isFieldDisabled(field: FormFieldConfig): boolean {
    if (field.disabled) {
      return true;
    }
    if (this.loading || this.saving) {
      return true;
    }
    return false;
  }

  private markFormGroupTouched(): void {
    Object.keys(this.form.controls).forEach(key => {
      const control = this.form.get(key);
      control?.markAsTouched();
    });
  }

  getFieldClass(field: FormFieldConfig): string {
    let base = 'form-field';
    if (field.required) base += ' required';
    if (field.disabled) base += ' disabled';
    base += ` field-width-${field.width || 2}`;
    return base;
  }

  // Helper methods for safe event handling
  onInputChange(event: Event, fieldKey: string): void {
    const target = event.target as HTMLInputElement;
    if (target) {
      this.onFieldChange(fieldKey, target.value);
    }
  }

  onTextareaChange(event: Event, fieldKey: string): void {
    const target = event.target as HTMLTextAreaElement;
    if (target) {
      this.onFieldChange(fieldKey, target.value);
    }
  }

  onSelectChange(event: any, fieldKey: string): void {
    this.onFieldChange(fieldKey, event.value);
  }

  onDateChange(event: any, fieldKey: string): void {
    this.onFieldChange(fieldKey, event.value);
  }

  onCheckboxChange(event: any, fieldKey: string): void {
    this.onFieldChange(fieldKey, event.checked);
  }

  onRadioChange(event: any, fieldKey: string): void {
    this.onFieldChange(fieldKey, event.value);
  }
}
