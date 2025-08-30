#!/bin/bash

# Flowable 工作流系统部署验证脚本
# 用于快速验证系统部署是否成功

set -e

echo "🚀 开始 Flowable 工作流系统部署验证..."

# 配置变量
BASE_URL="http://localhost:8080"
API_BASE="$BASE_URL/api"
FLOWABLE_REST="$BASE_URL/flowable-rest/service"

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# 打印函数
print_success() {
    echo -e "${GREEN}✅ $1${NC}"
}

print_error() {
    echo -e "${RED}❌ $1${NC}"
}

print_warning() {
    echo -e "${YELLOW}⚠️  $1${NC}"
}

print_info() {
    echo -e "ℹ️  $1"
}

# 检查函数
check_service() {
    local url=$1
    local description=$2
    
    if curl -s --max-time 10 --fail "$url" >/dev/null 2>&1; then
        print_success "$description 可访问"
        return 0
    else
        print_error "$description 不可访问"
        return 1
    fi
}

check_api_endpoint() {
    local endpoint=$1
    local description=$2
    local expected_status=${3:-200}
    
    local status=$(curl -s -o /dev/null -w "%{http_code}" --max-time 10 "$endpoint" 2>/dev/null || echo "000")
    
    if [ "$status" = "$expected_status" ]; then
        print_success "$description 返回状态码 $status"
        return 0
    else
        print_error "$description 返回状态码 $status (期望: $expected_status)"
        return 1
    fi
}

# 1. 基础服务检查
echo ""
echo "📋 1. 基础服务检查"
echo "===================="

check_service "$BASE_URL/actuator/health" "应用健康检查"
check_service "$BASE_URL/actuator/info" "应用信息接口"

# 2. Flowable REST API 检查
echo ""
echo "🔄 2. Flowable REST API 检查"
echo "============================="

# 检查流程定义
if curl -s --max-time 10 "$FLOWABLE_REST/repository/process-definitions" >/dev/null 2>&1; then
    process_count=$(curl -s --max-time 10 "$FLOWABLE_REST/repository/process-definitions" | grep -o '"id"' | wc -l)
    if [ "$process_count" -gt 0 ]; then
        print_success "发现 $process_count 个流程定义"
    else
        print_warning "未发现流程定义"
    fi
else
    print_error "无法访问流程定义 API"
fi

# 检查运行时服务
check_api_endpoint "$FLOWABLE_REST/runtime/process-instances" "流程实例 API"
check_api_endpoint "$FLOWABLE_REST/runtime/tasks" "任务 API"

# 3. 前端界面检查
echo ""
echo "🖥️  3. 前端界面检查"
echo "==================="

check_service "$BASE_URL" "主页面"
check_api_endpoint "$BASE_URL/workflow/flowable-admin" "Flowable 管理界面" "302"

# 4. 数据库连接检查
echo ""
echo "🗄️  4. 数据库连接检查"
echo "===================="

# 通过健康检查 API 验证数据库连接
health_response=$(curl -s --max-time 10 "$BASE_URL/actuator/health" 2>/dev/null || echo "")
if echo "$health_response" | grep -q "UP"; then
    print_success "数据库连接正常"
else
    print_error "数据库连接异常"
fi

# 5. BPMN 流程验证
echo ""
echo "📊 5. BPMN 流程验证"
echo "=================="

# 检查报销审批流程
reimbursement_process=$(curl -s --max-time 10 "$FLOWABLE_REST/repository/process-definitions?key=reimbursement_approval" 2>/dev/null || echo "")
if echo "$reimbursement_process" | grep -q "reimbursement_approval"; then
    print_success "报销审批流程已部署"
else
    print_warning "报销审批流程未找到"
fi

# 检查合同审批流程
contract_process=$(curl -s --max-time 10 "$FLOWABLE_REST/repository/process-definitions?key=contract_approval" 2>/dev/null || echo "")
if echo "$contract_process" | grep -q "contract_approval"; then
    print_success "合同审批流程已部署"
else
    print_warning "合同审批流程未找到"
fi

# 6. 性能基准测试
echo ""
echo "⚡ 6. 性能基准测试"
echo "=================="

# 测试主页响应时间
start_time=$(date +%s%3N)
curl -s --max-time 10 "$BASE_URL" >/dev/null 2>&1
end_time=$(date +%s%3N)
response_time=$((end_time - start_time))

if [ "$response_time" -lt 1000 ]; then
    print_success "主页响应时间: ${response_time}ms (优秀)"
elif [ "$response_time" -lt 3000 ]; then
    print_warning "主页响应时间: ${response_time}ms (一般)"
else
    print_error "主页响应时间: ${response_time}ms (过慢)"
fi

# 7. 日志检查
echo ""
echo "📝 7. 系统日志检查"
echo "=================="

# 检查日志文件是否存在
if [ -f "logs/turtle-workflow.log" ]; then
    print_success "工作流日志文件存在"
    
    # 检查最近的错误日志
    recent_errors=$(tail -n 100 logs/turtle-workflow.log 2>/dev/null | grep -i "error" | wc -l || echo "0")
    if [ "$recent_errors" -eq 0 ]; then
        print_success "最近无错误日志"
    else
        print_warning "发现 $recent_errors 条最近错误日志"
    fi
else
    print_warning "工作流日志文件不存在"
fi

# 8. 配置验证
echo ""
echo "⚙️  8. 配置验证"
echo "=============="

# 检查是否使用 Flowable 引擎
info_response=$(curl -s --max-time 10 "$BASE_URL/actuator/info" 2>/dev/null || echo "")
if echo "$info_response" | grep -q "flowable"; then
    print_success "Flowable 引擎配置正确"
else
    print_warning "无法确认 Flowable 引擎配置"
fi

# 9. 内存使用检查
echo ""
echo "🧠 9. 内存使用检查"
echo "=================="

# 通过 JVM 指标检查内存使用
metrics_response=$(curl -s --max-time 10 "$BASE_URL/actuator/metrics/jvm.memory.used" 2>/dev/null || echo "")
if echo "$metrics_response" | grep -q "measurements"; then
    print_success "内存监控正常"
else
    print_warning "无法获取内存使用信息"
fi

# 10. 总结
echo ""
echo "📋 验证总结"
echo "==========="

total_checks=20
passed_checks=0

# 重新运行关键检查并统计
curl -s --max-time 5 "$BASE_URL/actuator/health" >/dev/null 2>&1 && ((passed_checks++))
curl -s --max-time 5 "$FLOWABLE_REST/repository/process-definitions" >/dev/null 2>&1 && ((passed_checks++))
curl -s --max-time 5 "$FLOWABLE_REST/runtime/tasks" >/dev/null 2>&1 && ((passed_checks++))

# 简化统计（基于之前的检查结果）
if [ "$response_time" -lt 3000 ]; then
    ((passed_checks++))
fi

echo ""
echo "检查完成: $passed_checks/$total_checks 项通过"

if [ "$passed_checks" -ge 15 ]; then
    print_success "🎉 系统部署验证通过！"
    echo ""
    echo "✅ 系统已就绪，可以开始使用 Flowable 工作流功能"
    echo "🔗 访问管理界面: $BASE_URL/workflow/flowable-admin"
    exit 0
elif [ "$passed_checks" -ge 10 ]; then
    print_warning "⚠️  系统基本正常，但存在一些问题"
    echo ""
    echo "建议检查警告项并进行相应优化"
    exit 1
else
    print_error "❌ 系统存在严重问题"
    echo ""
    echo "请检查应用日志并解决错误后重新验证"
    exit 2
fi