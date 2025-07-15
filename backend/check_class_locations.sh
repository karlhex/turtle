#!/bin/bash

# 1. 定义需要检查的类及其期望目录
# 格式：类名:期望目录
classes=(
  "Product:src/main/java/com/fwai/turtle/modules/customer/entity"
  "Company:src/main/java/com/fwai/turtle/modules/customer/entity"
  "Employee:src/main/java/com/fwai/turtle/modules/organization/entity"
  "Role:src/main/java/com/fwai/turtle/modules/organization/entity"
  "User:src/main/java/com/fwai/turtle/modules/organization/entity"
  "BankAccountDTO:src/main/java/com/fwai/turtle/modules/finance/dto"
  "TaxInfoDTO:src/main/java/com/fwai/turtle/modules/finance/dto"
  "ProductDTO:src/main/java/com/fwai/turtle/modules/customer/dto"
  "CompanyDTO:src/main/java/com/fwai/turtle/modules/customer/dto"
  "ContactDTO:src/main/java/com/fwai/turtle/modules/customer/dto"
  "EmployeeDTO:src/main/java/com/fwai/turtle/modules/organization/dto"
  "InvoiceDTO:src/main/java/com/fwai/turtle/modules/finance/dto"
  "CurrencyDTO:src/main/java/com/fwai/turtle/modules/finance/dto"
  "UserService:src/main/java/com/fwai/turtle/modules/organization/service"
  "UserRepository:src/main/java/com/fwai/turtle/modules/organization/repository"
  "RoleRepository:src/main/java/com/fwai/turtle/modules/organization/repository"
  "ProductIdMapper:src/main/java/com/fwai/turtle/modules/customer/mapper"
  "CompanyIdMapper:src/main/java/com/fwai/turtle/modules/customer/mapper"
  "EmployeeIdMapper:src/main/java/com/fwai/turtle/modules/organization/mapper"
  "UserMapper:src/main/java/com/fwai/turtle/modules/organization/mapper"
  "PasswordValidationService:src/main/java/com/fwai/turtle/base/service"
)

missing=()
mismatch=()

for entry in "${classes[@]}"; do
  class="${entry%%:*}"
  dir="${entry##*:}"
  found_file=$(find "$dir" -name "$class.java" 2>/dev/null | head -n 1)
  if [[ -z "$found_file" ]]; then
    missing+=("$class (expected in $dir)")
  else
    # 检查包声明
    expected_package=$(echo "$dir" | sed 's#src/main/java/##;s#/#.#g')
    actual_package=$(grep -m1 '^package ' "$found_file" | sed 's/package //;s/;//')
    if [[ "$expected_package" != "$actual_package" ]]; then
      mismatch+=("$class: $found_file (package: $actual_package, expected: $expected_package)")
    fi
  fi
done

echo "==== 缺失的类文件 ===="
for m in "${missing[@]}"; do
  echo "$m"
done

echo "\n==== 包声明不一致的类文件 ===="
for mm in "${mismatch[@]}"; do
  echo "$mm"
done

echo "\n检查完成。" 