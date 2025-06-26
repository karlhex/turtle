# 通用页面组件使用指南

本项目提供了两个可复用的页面组件，用于快速构建列表页面和输入页面。

## 📋 List Page Component (列表页面组件)

### 功能特性
- ✅ 数据表格展示
- ✅ 分页功能
- ✅ 搜索/过滤
- ✅ 排序功能
- ✅ 批量操作
- ✅ 导出功能
- ✅ 响应式设计
- ✅ 加载状态
- ✅ 空数据状态

### 使用示例

```typescript
// 在组件中配置
export class EmployeeListComponent {
  listConfig: ListPageConfig = {
    title: '员工管理',
    columns: [
      {
        key: 'name',
        label: '姓名',
        type: 'text',
        sortable: true,
        width: '150px'
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
        width: '150px'
      },
      {
        key: 'status',
        label: '状态',
        type: 'boolean',
        sortable: true,
        width: '100px'
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
            action: (item) => this.editEmployee(item)
          },
          {
            label: '删除',
            icon: 'delete',
            color: 'warn',
            action: (item) => this.deleteEmployee(item),
            disabled: (item) => item.status === 'INACTIVE'
          }
        ]
      }
    ],
    pageSizeOptions: [10, 20, 50, 100],
    defaultPageSize: 20,
    showSearch: true,
    showExport: true,
    showBulkActions: true,
    searchPlaceholder: '搜索员工姓名、邮箱或部门...'
  };

  bulkActions: BulkActionConfig[] = [
    {
      label: '批量删除',
      icon: 'delete',
      action: (items) => this.bulkDelete(items),
      disabled: (items) => items.length === 0
    },
    {
      label: '批量激活',
      icon: 'check_circle',
      action: (items) => this.bulkActivate(items)
    }
  ];

  onPageChange(event: { page: number; size: number }) {
    // 处理分页
    this.loadEmployees(event.page, event.size);
  }

  onSearch(value: string) {
    // 处理搜索
    this.searchEmployees(value);
  }

  onSortChange(event: { column: string; direction: string }) {
    // 处理排序
    this.sortEmployees(event.column, event.direction);
  }
}
```

```html
<!-- 在模板中使用 -->
<app-list-page
  [config]="listConfig"
  [data]="employees"
  [loading]="loading"
  [totalItems]="totalEmployees"
  [bulkActions]="bulkActions"
  (pageChange)="onPageChange($event)"
  (searchChange)="onSearch($event)"
  (sortChange)="onSortChange($event)"
  (refresh)="loadEmployees()"
  (export)="exportEmployees()">
</app-list-page>
```

## 📝 Input Page Component (输入页面组件)

### 功能特性
- ✅ 动态表单字段
- ✅ 多种布局模式 (单列/双列/步骤)
- ✅ 表单验证
- ✅ 自动保存
- ✅ 文件上传
- ✅ 响应式设计
- ✅ 加载状态
- ✅ 自定义组件支持

### 使用示例

```typescript
// 在组件中配置
export class EmployeeFormComponent {
  inputConfig: InputPageConfig = {
    title: '员工信息',
    subtitle: '请填写员工的基本信息',
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
          { value: 'MARKETING', label: '市场部' }
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
    autoSaveInterval: 30000 // 30秒自动保存
  };

  onSave(formData: FormData) {
    // 处理保存
    this.saveEmployee(formData);
  }

  onCancel() {
    // 处理取消
    this.router.navigate(['/employees']);
  }

  onFieldChange(event: { key: string; value: any }) {
    // 处理字段变化
    console.log(`字段 ${event.key} 值变为:`, event.value);
  }
}
```

```html
<!-- 在模板中使用 -->
<app-input-page
  [config]="inputConfig"
  [data]="employeeData"
  [loading]="loading"
  [saving]="saving"
  (save)="onSave($event)"
  (cancel)="onCancel()"
  (reset)="onReset()"
  (fieldChange)="onFieldChange($event)"
  (validationChange)="onValidationChange($event)">
</app-input-page>
```

## 🎨 布局模式

### List Page 布局
- 自适应表格布局
- 响应式设计，支持移动端
- 支持自定义列宽和排序

### Input Page 布局
1. **Single Column (单列布局)**
   - 适合字段较少的表单
   - 最大宽度 800px，居中显示

2. **Two Column (双列布局)**
   - 适合字段较多的表单
   - 最大宽度 1200px，两列并排

3. **Stepper (步骤布局)**
   - 适合复杂的多步骤表单
   - 线性验证，必须按顺序完成

## 🔧 自定义配置

### 字段类型支持
- `text`: 文本输入
- `number`: 数字输入
- `email`: 邮箱输入
- `password`: 密码输入
- `textarea`: 多行文本
- `select`: 下拉选择
- `checkbox`: 复选框
- `radio`: 单选框
- `date`: 日期选择
- `file`: 文件上传
- `custom`: 自定义组件

### 验证规则
- `required`: 必填验证
- `minLength/maxLength`: 长度验证
- `min/max`: 数值范围验证
- `pattern`: 正则表达式验证
- `email`: 邮箱格式验证

## 📱 响应式设计

两个组件都支持响应式设计：
- 桌面端：完整功能展示
- 平板端：适配中等屏幕
- 移动端：优化触摸操作

## 🌙 深色主题

组件自动支持系统深色主题：
- 自动检测系统主题设置
- 适配深色模式配色
- 保持良好的对比度

## 🚀 最佳实践

1. **配置管理**: 将组件配置提取到单独的配置文件
2. **类型安全**: 使用 TypeScript 接口定义数据结构
3. **错误处理**: 实现完善的错误处理和用户反馈
4. **性能优化**: 使用 OnPush 变更检测策略
5. **可访问性**: 确保组件符合 WCAG 标准

## 📦 依赖要求

确保项目中已安装以下 Angular Material 模块：
- `MatTableModule`
- `MatSortModule`
- `MatPaginatorModule`
- `MatFormFieldModule`
- `MatInputModule`
- `MatSelectModule`
- `MatCheckboxModule`
- `MatRadioModule`
- `MatDatepickerModule`
- `MatButtonModule`
- `MatIconModule`
- `MatStepperModule`
- `MatProgressSpinnerModule`
- `MatTooltipModule` 