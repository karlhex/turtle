#!/bin/bash

echo "Fixing remaining import issues..."

# Fix ApiResponse imports - should be in base.dto, not common
echo "Fixing ApiResponse imports..."
find src/main/java -name "*.java" -exec sed -i '' 's/import com\.fwai\.turtle\.common\.ApiResponse;/import com.fwai.turtle.base.dto.ApiResponse;/g' {} \;

# Fix Inventory entity - add missing imports
echo "Fixing Inventory entity imports..."
sed -i '' 's/import com\.fwai\.turtle\.modules\.customer\.entity\.Product;/import com.fwai.turtle.modules.customer.entity.Product;/g' src/main/java/com/fwai/turtle/modules/project/entity/Inventory.java
sed -i '' 's/import com\.fwai\.turtle\.modules\.customer\.entity\.Company;/import com.fwai.turtle.modules.customer.entity.Company;/g' src/main/java/com/fwai/turtle/modules/project/entity/Inventory.java
sed -i '' 's/import com\.fwai\.turtle\.modules\.organization\.entity\.Employee;/import com.fwai.turtle.modules.organization.entity.Employee;/g' src/main/java/com/fwai/turtle/modules/project/entity/Inventory.java

# Fix Project entity - add missing imports
echo "Fixing Project entity imports..."
sed -i '' 's/import com\.fwai\.turtle\.modules\.organization\.entity\.Employee;/import com.fwai.turtle.modules.organization.entity.Employee;/g' src/main/java/com/fwai/turtle/modules/project/entity/Project.java

# Fix CompanyDTO imports
echo "Fixing CompanyDTO imports..."
sed -i '' 's/import com\.fwai\.turtle\.modules\.customer\.dto\.BankAccountDTO;/import com.fwai.turtle.modules.finance.dto.BankAccountDTO;/g' src/main/java/com/fwai/turtle/modules/customer/dto/CompanyDTO.java
sed -i '' 's/import com\.fwai\.turtle\.modules\.customer\.dto\.TaxInfoDTO;/import com.fwai.turtle.modules.finance.dto.TaxInfoDTO;/g' src/main/java/com/fwai/turtle/modules/customer/dto/CompanyDTO.java

# Fix ContractItemDTO imports
echo "Fixing ContractItemDTO imports..."
sed -i '' 's/import com\.fwai\.turtle\.modules\.project\.dto\.ProductDTO;/import com.fwai.turtle.modules.customer.dto.ProductDTO;/g' src/main/java/com/fwai/turtle/modules/project/dto/ContractItemDTO.java

# Fix ProjectDTO imports
echo "Fixing ProjectDTO imports..."
sed -i '' 's/import com\.fwai\.turtle\.modules\.project\.dto\.EmployeeDTO;/import com.fwai.turtle.modules.organization.dto.EmployeeDTO;/g' src/main/java/com/fwai/turtle/modules/project/dto/ProjectDTO.java

# Fix ContractDTO imports
echo "Fixing ContractDTO imports..."
sed -i '' 's/import com\.fwai\.turtle\.modules\.project\.dto\.CompanyDTO;/import com.fwai.turtle.modules.customer.dto.CompanyDTO;/g' src/main/java/com/fwai/turtle/modules/project/dto/ContractDTO.java
sed -i '' 's/import com\.fwai\.turtle\.modules\.project\.dto\.ContactDTO;/import com.fwai.turtle.modules.customer.dto.ContactDTO;/g' src/main/java/com/fwai/turtle/modules/project/dto/ContractDTO.java
sed -i '' 's/import com\.fwai\.turtle\.modules\.project\.dto\.CurrencyDTO;/import com.fwai.turtle.modules.finance.dto.CurrencyDTO;/g' src/main/java/com/fwai/turtle/modules/project/dto/ContractDTO.java
sed -i '' 's/import com\.fwai\.turtle\.modules\.project\.dto\.InvoiceDTO;/import com.fwai.turtle.modules.finance.dto.InvoiceDTO;/g' src/main/java/com/fwai/turtle/modules/project/dto/ContractDTO.java

# Fix ContractServiceImpl imports
echo "Fixing ContractServiceImpl imports..."
sed -i '' 's/import com\.fwai\.turtle\.modules\.project\.repository\.CurrencyRepository;/import com.fwai.turtle.modules.finance.repository.CurrencyRepository;/g' src/main/java/com/fwai/turtle/modules/project/service/impl/ContractServiceImpl.java
sed -i '' 's/import com\.fwai\.turtle\.modules\.project\.repository\.CompanyRepository;/import com.fwai.turtle.modules.customer.repository.CompanyRepository;/g' src/main/java/com/fwai/turtle/modules/project/service/impl/ContractServiceImpl.java

# Fix ContractItemServiceImpl imports
echo "Fixing ContractItemServiceImpl imports..."
sed -i '' 's/import com\.fwai\.turtle\.modules\.project\.entity\.Product;/import com.fwai.turtle.modules.customer.entity.Product;/g' src/main/java/com/fwai/turtle/modules/project/service/impl/ContractItemServiceImpl.java
sed -i '' 's/import com\.fwai\.turtle\.modules\.project\.repository\.ProductRepository;/import com.fwai.turtle.modules.customer.repository.ProductRepository;/g' src/main/java/com/fwai/turtle/modules/project/service/impl/ContractItemServiceImpl.java

# Fix ProjectServiceImpl imports
echo "Fixing ProjectServiceImpl imports..."
sed -i '' 's/import com\.fwai\.turtle\.modules\.project\.entity\.Employee;/import com.fwai.turtle.modules.organization.entity.Employee;/g' src/main/java/com/fwai/turtle/modules/project/service/impl/ProjectServiceImpl.java
sed -i '' 's/import com\.fwai\.turtle\.modules\.project\.repository\.EmployeeRepository;/import com.fwai.turtle.modules.organization.repository.EmployeeRepository;/g' src/main/java/com/fwai/turtle/modules/project/service/impl/ProjectServiceImpl.java

# Fix InventoryMapper imports
echo "Fixing InventoryMapper imports..."
sed -i '' 's/import com\.fwai\.turtle\.modules\.project\.mapper\.ProductIdMapper;/import com.fwai.turtle.modules.customer.mapper.ProductIdMapper;/g' src/main/java/com/fwai/turtle/modules/project/mapper/InventoryMapper.java
sed -i '' 's/import com\.fwai\.turtle\.modules\.project\.mapper\.CompanyIdMapper;/import com.fwai.turtle.modules.customer.mapper.CompanyIdMapper;/g' src/main/java/com/fwai/turtle/modules/project/mapper/InventoryMapper.java
sed -i '' 's/import com\.fwai\.turtle\.modules\.project\.mapper\.EmployeeIdMapper;/import com.fwai.turtle.modules.organization.mapper.EmployeeIdMapper;/g' src/main/java/com/fwai/turtle/modules/project/mapper/InventoryMapper.java

# Fix AuthServiceImpl imports
echo "Fixing AuthServiceImpl imports..."
sed -i '' 's/import com\.fwai\.turtle\.modules\.project\.entity\.Employee;/import com.fwai.turtle.modules.organization.entity.Employee;/g' src/main/java/com/fwai/turtle/security/service/impl/AuthServiceImpl.java
sed -i '' 's/import com\.fwai\.turtle\.modules\.project\.entity\.Department;/import com.fwai.turtle.modules.organization.entity.Department;/g' src/main/java/com/fwai/turtle/security/service/impl/AuthServiceImpl.java
sed -i '' 's/import com\.fwai\.turtle\.security\.repository\.RoleRepository;/import com.fwai.turtle.modules.organization.repository.RoleRepository;/g' src/main/java/com/fwai/turtle/security/service/impl/AuthServiceImpl.java
sed -i '' 's/import com\.fwai\.turtle\.security\.service\.RolePermissionService;/import com.fwai.turtle.base.service.RolePermissionService;/g' src/main/java/com/fwai/turtle/security/service/impl/AuthServiceImpl.java

# Fix AttendanceTimeValidator imports
echo "Fixing AttendanceTimeValidator imports..."
sed -i '' 's/import com\.fwai\.turtle\.organization\.dto\.EmployeeAttendanceDTO;/import com.fwai.turtle.modules.organization.dto.EmployeeAttendanceDTO;/g' src/main/java/com/fwai/turtle/base/validation/AttendanceTimeValidator.java

# Fix UserServiceImpl imports
echo "Fixing UserServiceImpl imports..."
sed -i '' 's/import com\.fwai\.turtle\.base\.mapper\.UserMapper;/import com.fwai.turtle.modules.organization.mapper.UserMapper;/g' src/main/java/com/fwai/turtle/base/service/impl/UserServiceImpl.java
sed -i '' 's/import com\.fwai\.turtle\.base\.service\.PasswordValidationService;/import com.fwai.turtle.base.service.PasswordValidationService;/g' src/main/java/com/fwai/turtle/base/service/impl/UserServiceImpl.java

# Fix PermissionCheckAspect imports
echo "Fixing PermissionCheckAspect imports..."
sed -i '' 's/import com\.fwai\.turtle\.common\.ApiResponse;/import com.fwai.turtle.base.dto.ApiResponse;/g' src/main/java/com/fwai/turtle/base/aspect/PermissionCheckAspect.java
sed -i '' 's/import com\.fwai\.turtle\.base\.entity\.Role;/import com.fwai.turtle.modules.organization.entity.Role;/g' src/main/java/com/fwai/turtle/base/aspect/PermissionCheckAspect.java

echo "Remaining import fixes completed!" 