#!/bin/bash

# Turtle 项目开发环境设置脚本
# 用于在新的 backend/frontend 目录结构下快速启动项目

echo "🐢 Turtle 项目开发环境设置"
echo "================================"

# 检查必要的工具
check_tools() {
    echo "检查开发工具..."
    
    if ! command -v java &> /dev/null; then
        echo "❌ Java 未安装，请先安装 Java 17+"
        exit 1
    fi
    
    if ! command -v node &> /dev/null; then
        echo "❌ Node.js 未安装，请先安装 Node.js"
        exit 1
    fi
    
    if ! command -v npm &> /dev/null; then
        echo "❌ npm 未安装，请先安装 npm"
        exit 1
    fi
    
    echo "✅ 开发工具检查完成"
}

# 设置后端环境
setup_backend() {
    echo ""
    echo "设置后端环境..."
    cd backend
    
    if [ ! -f "pom.xml" ]; then
        echo "❌ 后端 pom.xml 文件不存在"
        exit 1
    fi
    
    echo "✅ 后端环境设置完成"
    cd ..
}

# 设置前端环境
setup_frontend() {
    echo ""
    echo "设置前端环境..."
    cd frontend/turtle-web
    
    if [ ! -f "package.json" ]; then
        echo "❌ 前端 package.json 文件不存在"
        exit 1
    fi
    
    echo "安装前端依赖..."
    npm install
    
    echo "✅ 前端环境设置完成"
    cd ../..
}

# 启动后端服务
start_backend() {
    echo ""
    echo "启动后端服务..."
    cd backend
    ./mvnw spring-boot:run &
    BACKEND_PID=$!
    echo "后端服务已启动 (PID: $BACKEND_PID)"
    cd ..
}

# 启动前端服务
start_frontend() {
    echo ""
    echo "启动前端服务..."
    cd frontend/turtle-web
    npm start &
    FRONTEND_PID=$!
    echo "前端服务已启动 (PID: $FRONTEND_PID)"
    cd ../..
}

# 主函数
main() {
    case "$1" in
        "check")
            check_tools
            ;;
        "setup")
            check_tools
            setup_backend
            setup_frontend
            ;;
        "start-backend")
            start_backend
            ;;
        "start-frontend")
            start_frontend
            ;;
        "start")
            check_tools
            setup_backend
            setup_frontend
            start_backend
            sleep 5
            start_frontend
            echo ""
            echo "🎉 所有服务已启动！"
            echo "后端: http://localhost:8080"
            echo "前端: http://localhost:4200"
            echo ""
            echo "按 Ctrl+C 停止所有服务"
            wait
            ;;
        *)
            echo "用法: $0 {check|setup|start-backend|start-frontend|start}"
            echo ""
            echo "命令说明:"
            echo "  check        - 检查开发工具"
            echo "  setup        - 设置开发环境"
            echo "  start-backend - 启动后端服务"
            echo "  start-frontend - 启动前端服务"
            echo "  start        - 设置环境并启动所有服务"
            exit 1
            ;;
    esac
}

main "$@" 