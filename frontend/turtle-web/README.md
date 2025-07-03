# Turtle Web Application

This project was generated with [Angular CLI](https://github.com/angular/angular-cli) version 15.2.0.

## Development Setup

### Prerequisites
- Node.js >= 18.0.0
- npm >= 8.0.0

### Installation
```bash
npm install
```

## Development Scripts

### Code Quality Tools

#### ESLint
- **Lint code**: `npm run lint`
- **Fix linting issues**: `npm run lint:fix`

#### Prettier
- **Format code**: `npm run format`
- **Check formatting**: `npm run format:check`

#### Testing
- **Run tests**: `npm run test`
- **Run tests with coverage**: `npm run test:coverage`
- **Run tests in CI mode**: `npm run test:ci`

### Development Server
```bash
npm start
```
Navigate to `http://localhost:4200/`. The application will automatically reload if you change any of the source files.

### Build
```bash
npm run build
```
The build artifacts will be stored in the `dist/` directory.

### Code Coverage Requirements
The project enforces minimum code coverage requirements:
- **Statements**: 80%
- **Branches**: 80%
- **Functions**: 80%
- **Lines**: 80%

## Git Hooks
The project uses Husky to enforce code quality on commit:
- **Pre-commit**: Automatically runs linting and formatting on staged files

## Code Style Guidelines

### TypeScript
- Use explicit return types for functions
- Prefer arrow functions
- Use readonly properties when possible
- Avoid `any` type (warning level)
- Use proper JSDoc documentation

### Angular
- Component selectors should use kebab-case with 'app' prefix
- Directive selectors should use camelCase with 'app' prefix
- Use OnPush change detection strategy when possible
- Implement lifecycle interfaces properly

### Import Order
Imports are automatically sorted in the following order:
1. Built-in modules
2. External modules
3. Internal modules
4. Parent modules
5. Sibling modules
6. Index modules

## Testing
The project uses Jasmine and Karma for unit testing. Tests are located alongside the source files with `.spec.ts` extension.

## Further Help
To get more help on the Angular CLI use `ng help` or go check out the [Angular CLI Overview and Command Reference](https://angular.io/cli) page.
