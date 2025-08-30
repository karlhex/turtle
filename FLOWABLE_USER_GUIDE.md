# Flowable 工作流用户指南

## 概述

本系统已集成 Flowable 工作流引擎，提供可视化的流程管理和审批功能。Flowable 是一个轻量级的业务流程管理（BPM）和工作流引擎，支持 BPMN 2.0 标准。

## 功能特性

### 🔄 **工作流管理**
- 可视化流程设计和管理
- 支持复杂的业务流程路由
- 实时流程状态监控
- 历史数据跟踪和审计

### ✅ **任务管理** 
- 个人和组任务分配
- 任务认领和完成
- 任务优先级管理
- 超时提醒和升级

### 📊 **监控和报表**
- 流程实例状态监控
- 性能指标统计
- 流程执行路径可视化
- 审批历史记录查询

## 系统架构

### 技术栈
- **后端**: Spring Boot 3.3.1 + Flowable 7.0.1
- **前端**: Angular 16 + Angular Material
- **数据库**: PostgreSQL + Redis (缓存)

### 集成方式
- **双引擎支持**: 可在传统工作流和 Flowable 之间切换
- **无缝集成**: 保持现有 API 接口兼容性
- **数据同步**: ApprovalRequest 实体与 Flowable 流程实例关联

## 用户界面指南

### 访问 Flowable 管理界面

1. **登录系统**后，在左侧导航菜单中找到"工作流管理"
2. 点击"Flowable管理"进入管理界面
3. 需要 `workflow.admin` 权限才能访问

### 管理界面功能

#### 1. 流程定义管理
- **查看流程定义**: 显示所有可用的工作流模板
- **启动流程实例**: 手动启动新的工作流程
- **流程版本管理**: 查看不同版本的流程定义

#### 2. 流程实例监控
- **运行中实例**: 查看所有正在执行的流程
- **实例状态**: 监控流程执行状态和进度
- **流程操作**: 暂停、激活、删除流程实例
- **流程图查看**: 可视化显示流程执行路径

#### 3. 任务管理
- **待办任务**: 显示分配给用户或组的任务
- **任务认领**: 从候选任务池中认领任务
- **任务完成**: 提交任务处理结果
- **任务筛选**: 按用户、流程等条件过滤

## 业务流程说明

### 报销审批流程 (reimbursement_approval)

**流程步骤**:
1. **提交申请** → 员工提交报销申请
2. **部门审批** → 部门领导审批
3. **金额路由** → 根据金额自动路由到相应审批人
   - ≤ ¥1,000: 财务审批
   - ¥1,000 - ¥5,000: 总经理审批  
   - > ¥5,000: CEO 审批
4. **流程完成** → 审批通过或拒绝

**使用场景**:
- 日常费用报销
- 差旅费用报销
- 办公用品采购报销

### 合同审批流程 (contract_approval)

**流程步骤**:
1. **项目经理初审** → 项目经理审核合同内容
2. **金额判断** → 根据合同金额确定审批路径
3. **并行审核** (>10万合同):
   - 法务审核 (Legal Review)
   - 财务审核 (Finance Review)
4. **高级审批**:
   - 50万-100万: 总经理审批
   - >100万: CEO 审批
5. **合同生效** → 审批完成后合同生效

**使用场景**:
- 销售合同审批
- 采购合同审批
- 服务协议审批

## API 使用指南

### 提交审批申请

```java
// 创建工作流提交请求
WorkflowSubmissionRequest request = new WorkflowSubmissionRequest();
request.setBusinessType("REIMBURSEMENT");
request.setBusinessId(1001L);
request.setBusinessNo("REIMB-2024-001");
request.setTitle("差旅费报销");
request.setAmount(BigDecimal.valueOf(1200.0));
request.setSubmitterId(currentUserId);

// 提交到工作流引擎
WorkflowSubmissionResponse response = unifiedWorkflowService.submitForApproval(request);
```

### 处理审批任务

```java
// 审批操作
WorkflowActionRequest actionRequest = new WorkflowActionRequest();
actionRequest.setWorkflowId(workflowId);
actionRequest.setUserId(currentUserId);
actionRequest.setComments("审批通过");

// 执行审批
WorkflowActionResponse response = unifiedWorkflowService.approve(actionRequest);
```

### 查询流程状态

```java
// 查询工作流状态
WorkflowStatusResponse status = unifiedWorkflowService.getWorkflowStatus("REIMBURSEMENT", businessId);
```

## 配置管理

### 引擎切换

在 `application.yml` 中配置工作流引擎:

```yaml
workflow:
  engine: flowable  # 使用 Flowable 引擎
  # engine: legacy  # 使用传统引擎
```

### 性能调优

```yaml
flowable:
  async-executor-activate: true
  async-executor:
    core-pool-size: 4
    max-pool-size: 20
    queue-capacity: 500
  history-level: audit
```

## 故障排查

### 常见问题

#### 1. 流程启动失败
- **检查流程定义**: 确认 BPMN 文件格式正确
- **验证变量**: 检查必需的流程变量是否设置
- **权限验证**: 确认用户有启动流程的权限

#### 2. 任务无法完成
- **变量检查**: 确认任务完成时的变量格式正确
- **条件表达式**: 检查 BPMN 中的条件表达式语法
- **数据库连接**: 验证数据库连接是否正常

#### 3. 性能问题
- **数据库索引**: 检查 Flowable 相关表的索引
- **缓存配置**: 启用适当的缓存机制
- **异步执行**: 启用异步任务执行器

### 日志分析

重要日志路径:
- **应用日志**: `logs/turtle-workflow.log`
- **Flowable 日志**: 设置 `org.flowable` 日志级别为 DEBUG

### 监控指标

通过 Spring Boot Actuator 监控:
- `/actuator/health` - 系统健康状态
- `/actuator/metrics` - 性能指标
- `/actuator/flowable` - Flowable 特定指标

## 最佳实践

### 流程设计原则

1. **简单性**: 保持流程路径清晰简单
2. **可扩展性**: 设计时考虑未来的业务变更
3. **用户体验**: 减少不必要的审批步骤
4. **异常处理**: 为每个流程节点设计异常处理

### 性能优化

1. **批处理**: 使用批量操作处理大量任务
2. **缓存**: 启用流程定义和任务查询缓存
3. **索引**: 为查询频繁的字段创建数据库索引
4. **清理**: 定期清理历史数据

### 安全考虑

1. **权限控制**: 严格控制流程启动和任务执行权限
2. **数据加密**: 敏感流程变量使用加密存储
3. **审计日志**: 记录所有关键操作的审计日志
4. **访问限制**: 限制管理界面的访问权限

## 支持和帮助

### 技术支持
- 查看系统日志获取错误详情
- 使用监控界面检查系统状态
- 联系系统管理员获取技术支持

### 文档资源
- [Flowable 官方文档](https://www.flowable.com/open-source/docs/)
- [BPMN 2.0 规范](https://www.omg.org/spec/BPMN/2.0/)
- 项目 CLAUDE.md - 开发者指南

### 培训材料
- 系统操作录屏教程
- 流程设计最佳实践文档
- 常见问题解答 (FAQ)

---

*本文档会根据系统更新和用户反馈持续完善。如有疑问或建议，请联系系统管理员。*