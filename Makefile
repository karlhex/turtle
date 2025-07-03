# Turtle 项目 Makefile
# 提供便捷的开发命令

.PHONY: help check setup start-backend start-frontend start clean

# 默认目标
help:
	@echo "🐢 Turtle 项目开发命令"
	@echo "========================"
	@echo "make check        - 检查开发环境"
	@echo "make setup        - 设置开发环境"
	@echo "make start-backend - 启动后端服务"
	@echo "make start-frontend - 启动前端服务"
	@echo "make start        - 启动所有服务"
	@echo "make clean        - 清理构建文件"
	@echo "make build        - 构建项目"

# 检查开发环境
check:
	@./scripts/dev-setup.sh check

# 设置开发环境
setup:
	@./scripts/dev-setup.sh setup

# 启动后端服务
start-backend:
	@./scripts/dev-setup.sh start-backend

# 启动前端服务
start-frontend:
	@./scripts/dev-setup.sh start-frontend

# 启动所有服务
start:
	@./scripts/dev-setup.sh start

# 清理构建文件
clean:
	@echo "清理后端构建文件..."
	@cd backend && ./mvnw clean
	@echo "清理前端构建文件..."
	@cd frontend/turtle-web && rm -rf dist node_modules
	@echo "✅ 清理完成"

# 构建项目
build:
	@echo "构建后端..."
	@cd backend && ./mvnw clean package -DskipTests
	@echo "构建前端..."
	@cd frontend/turtle-web && npm run build
	@echo "✅ 构建完成" 