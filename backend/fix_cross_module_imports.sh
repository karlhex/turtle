#!/bin/bash

echo "Fixing cross-module import issues..."

# Fix Inventory entity imports
echo "Fixing Inventory entity imports..."
sed -i '' 's/import com\.fwai\.turtle\.modules\.customer\.entity\.Product;/import com.fwai.turtle.modules.customer.entity.Product;/g' src/main/java/com/fwai/turtle/modules/project/entity/Inventory.java
sed -i '' 's/import com\.fwai\.turtle\.modules\.customer\.entity\.Company;/import com.fwai.turtle.modules.customer.entity.Company;/g' src/main/java/com/fwai/turtle/modules/project/entity/Inventory.java
sed -i '' 's/import com\.fwai\.turtle\.modules\.organization\.entity\.Employee;/import com.fwai.turtle.modules.organization.entity.Employee;/g' src/main/java/com/fwai/turtle/modules/project/entity/Inventory.java

# Fix Project entity imports
echo "Fixing Project entity imports..."
sed -i '' 's/import com\.fwai\.turtle\.modules\.organization\.entity\.Employee;/import com.fwai.turtle.modules.organization.entity.Employee;/g' src/main/java/com/fwai/turtle/modules/project/entity/Project.java

# Fix CompanyService imports
echo "Fixing CompanyService imports..."
sed -i '' 's/import com\.fwai\.turtle\.modules\.customer\.dto\.BankAccountDTO;/import com.fwai.turtle.modules.finance.dto.BankAccountDTO;/g' src/main/java/com/fwai/turtle/modules/customer/service/CompanyService.java

# Fix CompanyDTO imports
echo "Fixing CompanyDTO imports..."
sed -i '' 's/import com\.fwai\.turtle\.modules\.customer\.dto\.BankAccountDTO;/import com.fwai.turtle.modules.finance.dto.BankAccountDTO;/g' src/main/java/com/fwai/turtle/modules/customer/dto/CompanyDTO.java
sed -i '' 's/import com\.fwai\.turtle\.modules\.customer\.dto\.TaxInfoDTO;/import com.fwai.turtle.modules.finance.dto.TaxInfoDTO;/g' src/main/java/com/fwai/turtle/modules/customer/dto/CompanyDTO.java

# Fix ReimbursementServiceImpl imports
echo "Fixing ReimbursementServiceImpl imports..."
sed -i '' 's/import com\.fwai\.turtle\.modules\.finance\.repository\.EmployeeRepository;/import com.fwai.turtle.modules.organization.repository.EmployeeRepository;/g' src/main/java/com/fwai/turtle/modules/finance/service/impl/ReimbursementServiceImpl.java
sed -i '' 's/import com\.fwai\.turtle\.modules\.finance\.repository\.ProjectRepository;/import com.fwai.turtle.modules.project.repository.ProjectRepository;/g' src/main/java/com/fwai/turtle/modules/finance/service/impl/ReimbursementServiceImpl.java

# Fix ContractItemDTO imports
echo "Fixing ContractItemDTO imports..."
sed -i '' 's/import com\.fwai\.turtle\.modules\.project\.dto\.ProductDTO;/import com.fwai.turtle.modules.customer.dto.ProductDTO;/g' src/main/java/com/fwai/turtle/modules/project/dto/ContractItemDTO.java

# Fix security module imports
echo "Fixing security module imports..."
find src/main/java/com/fwai/turtle/security -name "*.java" -exec sed -i '' 's/import com\.fwai\.turtle\.security\.entity\.Role;/import com.fwai.turtle.modules.organization.entity.Role;/g' {} \;
find src/main/java/com/fwai/turtle/security -name "*.java" -exec sed -i '' 's/import com\.fwai\.turtle\.security\.entity\.User;/import com.fwai.turtle.modules.organization.entity.User;/g' {} \;
find src/main/java/com/fwai/turtle/security -name "*.java" -exec sed -i '' 's/import com\.fwai\.turtle\.security\.service\.UserService;/import com.fwai.turtle.modules.organization.service.UserService;/g' {} \;

# Fix UserEmployeeMappingService imports
echo "Fixing UserEmployeeMappingService imports..."
sed -i '' 's/import com\.fwai\.turtle\.security\.entity\.User;/import com.fwai.turtle.modules.organization.entity.User;/g' src/main/java/com/fwai/turtle/modules/organization/service/UserEmployeeMappingService.java
sed -i '' 's/import com\.fwai\.turtle\.modules\.organization\.repository\.UserRepository;/import com.fwai.turtle.modules.organization.repository.UserRepository;/g' src/main/java/com/fwai/turtle/modules/organization/service/UserEmployeeMappingService.java

# Fix DepartmentController imports
echo "Fixing DepartmentController imports..."
sed -i '' 's/import com\.fwai\.turtle\.base\.ApiResponse;/import com.fwai.turtle.common.ApiResponse;/g' src/main/java/com/fwai/turtle/modules/organization/controller/DepartmentController.java

# Fix ContractServiceImpl imports
echo "Fixing ContractServiceImpl imports..."
sed -i '' 's/import com\.fwai\.turtle\.modules\.project\.dto\.CurrencyDTO;/import com.fwai.turtle.modules.finance.dto.CurrencyDTO;/g' src/main/java/com/fwai/turtle/modules/project/service/impl/ContractServiceImpl.java
sed -i '' 's/import com\.fwai\.turtle\.modules\.project\.entity\.Invoice;/import com.fwai.turtle.modules.finance.entity.Invoice;/g' src/main/java/com/fwai/turtle/modules/project/service/impl/ContractServiceImpl.java
sed -i '' 's/import com\.fwai\.turtle\.modules\.project\.entity\.Contact;/import com.fwai.turtle.modules.customer.entity.Contact;/g' src/main/java/com/fwai/turtle/modules/project/service/impl/ContractServiceImpl.java
sed -i '' 's/import com\.fwai\.turtle\.modules\.project\.entity\.Product;/import com.fwai.turtle.modules.customer.entity.Product;/g' src/main/java/com/fwai/turtle/modules/project/service/impl/ContractServiceImpl.java
sed -i '' 's/import com\.fwai\.turtle\.modules\.project\.entity\.TaxInfo;/import com.fwai.turtle.modules.finance.entity.TaxInfo;/g' src/main/java/com/fwai/turtle/modules/project/service/impl/ContractServiceImpl.java
sed -i '' 's/import com\.fwai\.turtle\.modules\.project\.mapper\.ContactMapper;/import com.fwai.turtle.modules.customer.mapper.ContactMapper;/g' src/main/java/com/fwai/turtle/modules/project/service/impl/ContractServiceImpl.java
sed -i '' 's/import com\.fwai\.turtle\.modules\.project\.mapper\.InvoiceMapper;/import com.fwai.turtle.modules.finance.mapper.InvoiceMapper;/g' src/main/java/com/fwai/turtle/modules/project/service/impl/ContractServiceImpl.java
sed -i '' 's/import com\.fwai\.turtle\.modules\.project\.repository\.ContactRepository;/import com.fwai.turtle.modules.customer.repository.ContactRepository;/g' src/main/java/com/fwai/turtle/modules/project/service/impl/ContractServiceImpl.java
sed -i '' 's/import com\.fwai\.turtle\.modules\.project\.repository\.ProductRepository;/import com.fwai.turtle.modules.customer.repository.ProductRepository;/g' src/main/java/com/fwai/turtle/modules/project/service/impl/ContractServiceImpl.java
sed -i '' 's/import com\.fwai\.turtle\.modules\.project\.repository\.TaxInfoRepository;/import com.fwai.turtle.modules.finance.repository.TaxInfoRepository;/g' src/main/java/com/fwai/turtle/modules/project/service/impl/ContractServiceImpl.java
sed -i '' 's/import com\.fwai\.turtle\.modules\.project\.repository\.InvoiceRepository;/import com.fwai.turtle.modules.finance.repository.InvoiceRepository;/g' src/main/java/com/fwai/turtle/modules/project/service/impl/ContractServiceImpl.java
sed -i '' 's/import com\.fwai\.turtle\.modules\.project\.dto\.CompanyDTO;/import com.fwai.turtle.modules.customer.dto.CompanyDTO;/g' src/main/java/com/fwai/turtle/modules/project/service/impl/ContractServiceImpl.java
sed -i '' 's/import com\.fwai\.turtle\.modules\.project\.repository\.CurrencyRepository;/import com.fwai.turtle.modules.finance.repository.CurrencyRepository;/g' src/main/java/com/fwai/turtle/modules/project/service/impl/ContractServiceImpl.java
sed -i '' 's/import com\.fwai\.turtle\.modules\.project\.repository\.CompanyRepository;/import com.fwai.turtle.modules.customer.repository.CompanyRepository;/g' src/main/java/com/fwai/turtle/modules/project/service/impl/ContractServiceImpl.java

# Fix ContractDTO imports
echo "Fixing ContractDTO imports..."
sed -i '' 's/import com\.fwai\.turtle\.modules\.project\.dto\.CompanyDTO;/import com.fwai.turtle.modules.customer.dto.CompanyDTO;/g' src/main/java/com/fwai/turtle/modules/project/dto/ContractDTO.java
sed -i '' 's/import com\.fwai\.turtle\.modules\.project\.dto\.ContactDTO;/import com.fwai.turtle.modules.customer.dto.ContactDTO;/g' src/main/java/com/fwai/turtle/modules/project/dto/ContractDTO.java
sed -i '' 's/import com\.fwai\.turtle\.modules\.project\.dto\.CurrencyDTO;/import com.fwai.turtle.modules.finance.dto.CurrencyDTO;/g' src/main/java/com/fwai/turtle/modules/project/dto/ContractDTO.java
sed -i '' 's/import com\.fwai\.turtle\.modules\.project\.dto\.InvoiceDTO;/import com.fwai.turtle.modules.finance.dto.InvoiceDTO;/g' src/main/java/com/fwai/turtle/modules/project/dto/ContractDTO.java

# Fix JwtAuthenticationFilter imports
echo "Fixing JwtAuthenticationFilter imports..."
sed -i '' 's/import com\.fwai\.turtle\.common\./import com.fwai.turtle.common./g' src/main/java/com/fwai/turtle/security/config/JwtAuthenticationFilter.java

# Fix InventoryMapper imports
echo "Fixing InventoryMapper imports..."
sed -i '' 's/import com\.fwai\.turtle\.modules\.project\.mapper\.ProductIdMapper;/import com.fwai.turtle.modules.customer.mapper.ProductIdMapper;/g' src/main/java/com/fwai/turtle/modules/project/mapper/InventoryMapper.java
sed -i '' 's/import com\.fwai\.turtle\.modules\.project\.mapper\.CompanyIdMapper;/import com.fwai.turtle.modules.customer.mapper.CompanyIdMapper;/g' src/main/java/com/fwai/turtle/modules/project/mapper/InventoryMapper.java
sed -i '' 's/import com\.fwai\.turtle\.modules\.project\.mapper\.EmployeeIdMapper;/import com.fwai.turtle.modules.organization.mapper.EmployeeIdMapper;/g' src/main/java/com/fwai/turtle/modules/project/mapper/InventoryMapper.java

# Fix ContactController imports
echo "Fixing ContactController imports..."
sed -i '' 's/import com\.fwai\.turtle\.dto\.ContactDTO;/import com.fwai.turtle.modules.customer.dto.ContactDTO;/g' src/main/java/com/fwai/turtle/modules/customer/controller/ContactController.java

# Fix PermissionCheckServiceImpl imports
echo "Fixing PermissionCheckServiceImpl imports..."
sed -i '' 's/import com\.fwai\.turtle\.security\.service\.UserService;/import com.fwai.turtle.modules.organization.service.UserService;/g' src/main/java/com/fwai/turtle/base/service/impl/PermissionCheckServiceImpl.java

echo "Cross-module import fixes completed!" 