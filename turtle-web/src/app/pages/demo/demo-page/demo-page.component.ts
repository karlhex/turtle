import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ListPageConfig, ColumnConfig, ActionConfig, BulkActionConfig } from '../../../components/list-page/list-page.component';
import { InputPageConfig, FormFieldConfig, SelectOption } from '../../../components/input-page/input-page.component';

@Component({
  selector: 'app-demo-page',
  templateUrl: './demo-page.component.html',
  styleUrls: ['./demo-page.component.scss']
})
export class DemoPageComponent implements OnInit {
  // List Page Demo Data
  listConfig: ListPageConfig = {
    title: '员工列表演示',
    columns: [
      {
        key: 'id',
        label: 'ID',
        type: 'number',
        sortable: true,
        width: '80px'
      },
      {
        key: 'name',
        label: '姓名',
        type: 'text',
        sortable: true,
        width: '120px'
      },
      {
        key: 'email',
        label: '邮箱',
        type: 'text',
        sortable: true,
        width: '200px'
      },
      {
        key: 'department',
        label: '部门',
        type: 'text',
        sortable: true,
        width: '120px'
      },
      {
        key: 'position',
        label: '职位',
        type: 'text',
        sortable: true,
        width: '120px'
      },
      {
        key: 'salary',
        label: '薪资',
        type: 'number',
        sortable: true,
        width: '100px',
        formatter: (value: number) => `¥${value.toLocaleString()}`
      },
      {
        key: 'hireDate',
        label: '入职日期',
        type: 'date',
        sortable: true,
        width: '120px'
      },
      {
        key: 'isActive',
        label: '状态',
        type: 'boolean',
        sortable: true,
        width: '80px'
      },
      {
        key: 'actions',
        label: '操作',
        type: 'action',
        sortable: false,
        width: '120px',
        actions: [
          {
            label: '编辑',
            icon: 'edit',
            color: 'primary',
            action: (item: any) => this.editEmployee(item)
          },
          {
            label: '删除',
            icon: 'delete',
            color: 'warn',
            action: (item: any) => this.deleteEmployee(item),
            disabled: (item: any) => !item.isActive
          }
        ]
      }
    ],
    pageSizeOptions: [5, 10, 20],
    defaultPageSize: 10,
    showSearch: true,
    showExport: true,
    showBulkActions: true,
    searchPlaceholder: '搜索员工姓名、邮箱或部门...'
  };

  bulkActions: BulkActionConfig[] = [
    {
      label: '批量删除',
      icon: 'delete',
      color: 'warn',
      action: (items: any[]) => this.bulkDelete(items),
      disabled: (items: any[]) => items.length === 0
    },
    {
      label: '批量激活',
      icon: 'check_circle',
      color: 'primary',
      action: (items: any[]) => this.bulkActivate(items),
      disabled: (items: any[]) => items.every(item => item.isActive)
    }
  ];

  // Sample data
  employees = [
    {
      id: 1,
      name: '张三',
      email: 'zhangsan@company.com',
      department: '技术部',
      position: '高级工程师',
      salary: 15000,
      hireDate: new Date('2022-01-15'),
      isActive: true
    },
    {
      id: 2,
      name: '李四',
      email: 'lisi@company.com',
      department: '人力资源部',
      position: 'HR专员',
      salary: 8000,
      hireDate: new Date('2022-03-20'),
      isActive: true
    },
    {
      id: 3,
      name: '王五',
      email: 'wangwu@company.com',
      department: '财务部',
      position: '会计',
      salary: 10000,
      hireDate: new Date('2021-11-10'),
      isActive: false
    },
    {
      id: 4,
      name: '赵六',
      email: 'zhaoliu@company.com',
      department: '市场部',
      position: '市场专员',
      salary: 9000,
      hireDate: new Date('2022-06-01'),
      isActive: true
    },
    {
      id: 5,
      name: '钱七',
      email: 'qianqi@company.com',
      department: '技术部',
      position: '前端工程师',
      salary: 12000,
      hireDate: new Date('2022-08-15'),
      isActive: true
    }
  ];

  loading = false;
  totalEmployees = 5;

  // Input Page Demo Data
  inputConfig: InputPageConfig = {
    title: '员工信息表单',
    subtitle: '这是一个演示表单，展示了各种输入字段类型',
    layout: 'two-column',
    fields: [
      {
        key: 'name',
        label: '姓名',
        type: 'text',
        required: true,
        placeholder: '请输入员工姓名',
        minLength: 2,
        maxLength: 50,
        helpText: '请输入真实姓名，长度2-50个字符'
      },
      {
        key: 'email',
        label: '邮箱',
        type: 'email',
        required: true,
        placeholder: 'example@company.com',
        helpText: '请输入有效的邮箱地址'
      },
      {
        key: 'phone',
        label: '电话',
        type: 'text',
        required: false,
        placeholder: '13800138000',
        pattern: '^1[3-9]\\d{9}$',
        helpText: '请输入11位手机号码'
      },
      {
        key: 'department',
        label: '部门',
        type: 'select',
        required: true,
        options: [
          { value: 'IT', label: '技术部' },
          { value: 'HR', label: '人力资源部' },
          { value: 'FINANCE', label: '财务部' },
          { value: 'MARKETING', label: '市场部' },
          { value: 'SALES', label: '销售部' }
        ]
      },
      {
        key: 'position',
        label: '职位',
        type: 'text',
        required: true,
        placeholder: '请输入职位名称'
      },
      {
        key: 'salary',
        label: '薪资',
        type: 'number',
        required: true,
        min: 0,
        step: 100,
        helpText: '请输入月薪金额'
      },
      {
        key: 'hireDate',
        label: '入职日期',
        type: 'date',
        required: true
      },
      {
        key: 'isActive',
        label: '在职状态',
        type: 'checkbox',
        required: false,
        defaultValue: true
      },
      {
        key: 'gender',
        label: '性别',
        type: 'radio',
        required: true,
        options: [
          { value: 'MALE', label: '男' },
          { value: 'FEMALE', label: '女' },
          { value: 'OTHER', label: '其他' }
        ]
      },
      {
        key: 'avatar',
        label: '头像',
        type: 'file',
        required: false,
        accept: 'image/*',
        helpText: '支持 JPG、PNG 格式，最大 2MB'
      },
      {
        key: 'description',
        label: '个人简介',
        type: 'textarea',
        required: false,
        rows: 4,
        maxLength: 500,
        placeholder: '请简要介绍个人经历和技能...'
      }
    ],
    showSaveButton: true,
    showCancelButton: true,
    showResetButton: true,
    saveButtonText: '保存员工信息',
    cancelButtonText: '取消',
    resetButtonText: '重置表单',
    autoSave: true,
    autoSaveInterval: 30000
  };

  employeeData = {
    name: '张三',
    email: 'zhangsan@company.com',
    phone: '13800138000',
    department: 'IT',
    position: '高级工程师',
    salary: 15000,
    hireDate: new Date('2022-01-15'),
    isActive: true,
    gender: 'MALE',
    description: '具有5年前端开发经验，熟悉Angular、React、Vue等主流框架。'
  };

  saving = false;
  showInputForm = false;

  constructor(private router: Router) {}

  ngOnInit(): void {}

  // List Page Event Handlers
  onPageChange(event: { page: number; size: number }) {
    console.log('分页变化:', event);
    // 这里应该调用API获取数据
  }

  onSearch(value: string) {
    console.log('搜索:', value);
    // 这里应该调用API搜索数据
  }

  onSortChange(event: { column: string; direction: string }) {
    console.log('排序变化:', event);
    // 这里应该调用API排序数据
  }

  onRefresh() {
    console.log('刷新数据');
    this.loading = true;
    setTimeout(() => {
      this.loading = false;
    }, 1000);
  }

  onExport() {
    console.log('导出数据');
    // 这里应该实现导出功能
  }

  editEmployee(employee: any) {
    console.log('编辑员工:', employee);
    this.employeeData = { ...employee };
    this.showInputForm = true;
  }

  deleteEmployee(employee: any) {
    console.log('删除员工:', employee);
    if (confirm(`确定要删除员工 ${employee.name} 吗？`)) {
      this.employees = this.employees.filter(emp => emp.id !== employee.id);
      this.totalEmployees = this.employees.length;
    }
  }

  bulkDelete(items: any[]) {
    console.log('批量删除:', items);
    if (confirm(`确定要删除选中的 ${items.length} 个员工吗？`)) {
      const ids = items.map(item => item.id);
      this.employees = this.employees.filter(emp => !ids.includes(emp.id));
      this.totalEmployees = this.employees.length;
    }
  }

  bulkActivate(items: any[]) {
    console.log('批量激活:', items);
    items.forEach(item => {
      const employee = this.employees.find(emp => emp.id === item.id);
      if (employee) {
        employee.isActive = true;
      }
    });
  }

  // Input Page Event Handlers
  onSave(formData: any) {
    console.log('保存表单数据:', formData);
    this.saving = true;
    setTimeout(() => {
      this.saving = false;
      alert('保存成功！');
      this.showInputForm = false;
    }, 1000);
  }

  onCancel() {
    console.log('取消操作');
    this.showInputForm = false;
  }

  onReset() {
    console.log('重置表单');
  }

  onFieldChange(event: { key: string; value: any }) {
    console.log(`字段 ${event.key} 值变为:`, event.value);
  }

  onValidationChange(event: { valid: boolean; errors: any }) {
    console.log('表单验证状态:', event);
  }

  // Demo Navigation
  showListDemo() {
    this.showInputForm = false;
  }

  showInputDemo() {
    this.showInputForm = true;
  }
}
