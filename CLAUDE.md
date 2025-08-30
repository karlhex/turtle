# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Turtle is an enterprise management system built with a Spring Boot backend and Angular frontend. The system manages employees, contracts, projects, finance, and inventory with role-based access control and workflow management.

## Architecture

### Backend (Spring Boot 3.3.1, Java 21)
- **Package Structure**: Modular design with `com.fwai.turtle.modules` containing domain-specific modules
- **Core Technologies**: Spring Boot 3.3.1, Java 21, Maven, PostgreSQL, Redis
- **Key Dependencies**: MapStruct 1.5.5, Lombok 1.18.30, SpringDoc OpenAPI 2.3.0, JWT 0.12.6
- **Modules**:
  - `customer`: Company, Contact, Person, Product management
  - `finance`: Bank accounts, Currency, Invoices, Reimbursements, Tax info
  - `organization`: Departments, Employees, Positions, User-Employee mapping, HR data
  - `project`: Contracts, Projects, Inventory management
  - `workflow`: Approval processes, Workflow configurations, Multi-step approvals
- **Base Package**: Contains shared entities, DTOs, controllers, utilities, security config
- **Security**: JWT-based authentication with role-based permissions (15min access, 7-day refresh tokens)
- **Database**: PostgreSQL with JPA/Hibernate, Flyway for migrations, H2 for testing
- **Cache**: Redis for session management and performance optimization

### Frontend (Angular 16)
- **Core Technologies**: Angular 16, TypeScript 4.9.4, Angular Material 16, NgRx Store 20
- **Architecture**: Feature-based routing with lazy-loaded modules
- **UI Framework**: Angular Material with custom theming
- **State Management**: NgRx Store/Effects with RxJS BehaviorSubjects for auth state
- **Internationalization**: ngx-translate with Chinese and English support
- **Structure**: Pages organized by domain (employee, contract, project, approval, etc.)
- **Development Tools**: ESLint, Prettier, Karma/Jasmine for testing

## Development Commands

### Quick Start
```bash
# Check environment
make check

# Setup development environment
make setup

# Start all services
make start

# Individual services
make start-backend   # Backend on :8080
make start-frontend  # Frontend on :4200
```

### Manual Commands
```bash
# Backend
./backend/mvnw spring-boot:run

# Frontend
cd frontend/turtle-web && npm install && npm start
```

### Build & Clean
```bash
make build   # Build both frontend and backend
make clean   # Clean all build artifacts
```

## Testing Commands

### Backend Tests
```bash
# Run all tests
./backend/mvnw test

# Run specific test class
./backend/mvnw test -Dtest=UserServiceTests

# Run with coverage
./backend/mvnw test jacoco:report
```

### Frontend Tests
```bash
# Run tests
cd frontend/turtle-web && npm test

# Run specific test
ng test --include="**/user.service.spec.ts"

# Linting
npm run lint
npm run lint:fix

# Formatting
npm run format
```

## Key Configuration Files

### Backend
- `application.yml` - Main configuration (PostgreSQL, Redis, JWT)
- `pom.xml` - Maven dependencies and build configuration
- Flyway migrations in `db/migration/`

### Frontend
- `environment.ts` - Development API endpoint (localhost:8080)
- `environment.prod.ts` - Production configuration
- `angular.json` - Build and serve configurations

## Code Patterns and Architecture Principles

### Backend Patterns (Spring Boot)
- **Layered Architecture**: Strict separation between Controllers, Services, and Repositories
- **Controllers**: REST endpoints with pagination support using `Pageable`. Handle only request/response logic
- **Services**: Interface-based design with implementation classes in `impl/` packages. All business logic resides here
- **Repositories**: Spring Data JPA repositories for database operations only
- **Entities**: JPA entities with Lombok annotations (@Data, @Builder, etc.)
- **DTOs**: MapStruct for entity-DTO mapping. DTOs used for all data transfer between layers
- **Exception Handling**: GlobalExceptionHandler with custom exceptions
- **Security**: JWT tokens with 15min access, 7-day refresh tokens
- **Validation**: Jakarta Bean Validation with @Valid annotations
- **Data Flow**: RestController ↔ DTO ↔ Service ↔ Repository ↔ Entity

### Architecture Rules (From Cursor Configuration)
- **SOLID, DRY, KISS, YAGNI principles** must be followed
- **OWASP security best practices** are mandatory
- **Controllers cannot autowire Repositories directly** - must use Services
- **Services cannot query database directly** - must use Repository methods
- **Data transfer between layers must use DTOs only**
- **Entity classes only for database query results**

### Frontend Patterns (Angular)
- **Services**: HTTP services with RxJS observables, centralized API communication
- **Components**: Dialog-based CRUD operations with shared components
- **Guards**: AuthGuard for route protection based on JWT tokens
- **Interceptors**: AuthInterceptor for automatic JWT token injection
- **Directives**: has-permission directive for conditional UI element visibility
- **State Management**: NgRx for complex state, BehaviorSubjects for simple auth state
- **Code Quality**: Max 4 parameters per function, max 50 executable lines, 80 char line limit

## Database Setup

```bash
# PostgreSQL setup (default)
database: emsdb
user: ems
password: example
host: localhost:5432

# Redis (optional)
host: localhost:6379
database: 0
```

## Module Structure

Each backend module follows this strict pattern:
```
module-name/
├── config/          # Module-specific configuration beans
├── controller/      # REST endpoints (@RestController)
├── dto/            # Data transfer objects (request/response)
├── entity/         # JPA entities (database mapping)
├── mapper/         # MapStruct mappers (entity ↔ DTO conversion)
├── repository/     # Spring Data JPA repositories (data access)
└── service/        # Business logic
    ├── [Service].java          # Service interfaces
    └── impl/                   # Service implementations
        └── [Service]Impl.java
```

### Current Modules
- **customer**: Company, Contact, Person, Product entities and operations
- **finance**: BankAccount, Currency, Invoice, Reimbursement, TaxInfo management
- **organization**: Department, Employee (with HR submodules), Position management
- **project**: Contract, Project, Inventory management
- **workflow**: Approval processes, multi-step workflows, approval history tracking

## Common Development Tasks

### Adding a New Entity
1. Create entity in appropriate module's `entity/` package
2. Create DTO in `dto/` package
3. Create repository interface extending JpaRepository
4. Create service interface and implementation
5. Create controller with CRUD endpoints
6. Add MapStruct mapper for entity-DTO conversion

### Adding a New Frontend Page
1. Create module in `src/app/pages/[domain]/`
2. Add routing in module's routing configuration
3. Create components (list, input, dialog)
4. Add service in `src/app/services/`
5. Update navigation in sidebar-menu

### Authentication Flow
1. User logs in via `/auth/signin` endpoint
2. JWT tokens returned (access + refresh)
3. Tokens stored in localStorage via TokenStorageService
4. AuthInterceptor adds Authorization header to requests
5. Permissions loaded and stored for UI control

## Workflow and Approval System

The system includes a comprehensive workflow management module:

### Workflow Components
- **Workflow**: Main workflow definition with steps and configurations
- **ApprovalRequest**: Individual approval requests submitted to workflows
- **ApprovalStep**: Individual steps within an approval process
- **ApprovalHistory**: Audit trail of all approval actions
- **UnifiedWorkflowService**: Central service for workflow operations across modules

### Approval Integration
- **Reimbursement approval**: Multi-step approval for expense reimbursements
- **Contract approval**: Approval workflows for contract management
- **Configurable workflows**: Admin-configurable approval steps and rules

## Development Environment

### Required Software
- **Java 21** - Backend runtime
- **Node.js ≥18.0.0** - Frontend development
- **npm ≥8.0.0** - Package management
- **PostgreSQL** - Primary database
- **Redis** - Caching and session storage (optional for development)

### Environment Setup Script
The project includes development setup scripts:
- `./scripts/dev-setup.sh check` - Verify environment requirements
- `./scripts/dev-setup.sh setup` - Install and configure development environment
- `./scripts/dev-setup.sh start` - Start all services for development