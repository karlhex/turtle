package com.fwai.turtle.persistence.mapper;

import org.springframework.stereotype.Component;

import com.fwai.turtle.dto.InventoryDTO;
import com.fwai.turtle.persistence.entity.Inventory;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class InventoryMapper {

    private final ProductIdMapper ProductIdMapper;
    private final ContractIdMapper contractIdMapper;
    private final CompanyIdMapper companyIdMapper;
    private final EmployeeIdMapper employeeIdMapper;

    public InventoryDTO toDTO(Inventory inventory)
    {
        InventoryDTO inventoryDTO = InventoryDTO.builder()
                .productName(inventory.getProduct() != null ? inventory.getProduct().getName() : null)
                .quantity(inventory.getQuantity())
                .license(inventory.getLicense())
                .purchaseContractNo(inventory.getPurchaseContract() != null ? inventory.getPurchaseContract().getContractNo() : null)
                .salesContractNo(inventory.getSalesContract() != null ? inventory.getSalesContract().getContractNo() : null)
                .storageTime(inventory.getStorageTime())
                .outTime(inventory.getOutTime())
                .borrowedCompanyName(inventory.getBorrowedCompany() != null ? inventory.getBorrowedCompany().getFullName() : null)
                .status(inventory.getStatus())
                .remarks(inventory.getRemarks())
                .shippingAddress(inventory.getShippingAddress())
                .shippingMethod(inventory.getShippingMethod())
                .receiverName(inventory.getReceiverName())
                .expressTrackingNumber(inventory.getExpressTrackingNumber())
                .receiverPhone(inventory.getReceiverPhone())
                .handlingEmployeeName(inventory.getHandlingEmployee() != null ? inventory.getHandlingEmployee().getName() : null)
                .build();

        inventoryDTO.setId(inventory.getId());
        inventoryDTO.setProductId(inventory.getProduct() != null ? inventory.getProduct().getId() : null);
        inventoryDTO.setPurchaseContractId(inventory.getPurchaseContract() != null ? inventory.getPurchaseContract().getId() : null);
        inventoryDTO.setSalesContractId(inventory.getSalesContract() != null ? inventory.getSalesContract().getId() : null);
        inventoryDTO.setBorrowedCompanyId(inventory.getBorrowedCompany() != null ? inventory.getBorrowedCompany().getId() : null);
        return inventoryDTO;
    }

    public Inventory toEntity(InventoryDTO inventoryDTO) {
        Inventory inventory = new Inventory();

        inventory.setProduct(ProductIdMapper.fromId(inventoryDTO.getProductId()));
        inventory.setQuantity(inventoryDTO.getQuantity() != null ? inventoryDTO.getQuantity() : null);
        inventory.setLicense(inventoryDTO.getLicense() != null ? inventoryDTO.getLicense() : null);

        inventory.setPurchaseContract(contractIdMapper.fromId(inventoryDTO.getPurchaseContractId()));
        
        inventory.setStorageTime(inventoryDTO.getStorageTime() != null ? inventoryDTO.getStorageTime() : null);

        inventory.setStatus(inventoryDTO.getStatus() != null ? inventoryDTO.getStatus() : null);
        inventory.setRemarks(inventoryDTO.getRemarks() != null ? inventoryDTO.getRemarks() : null);

        return inventory;
    }

    public void updateEntityWhenOut(Inventory inventory, InventoryDTO inventoryDTO) {
        inventory.setOutTime(inventoryDTO.getOutTime() != null ? inventoryDTO.getOutTime() : null);
        inventory.setShippingAddress(inventoryDTO.getShippingAddress() != null ? inventoryDTO.getShippingAddress() : null);
        inventory.setShippingMethod(inventoryDTO.getShippingMethod() != null ? inventoryDTO.getShippingMethod() : null);
        inventory.setReceiverName(inventoryDTO.getReceiverName() != null ? inventoryDTO.getReceiverName() : null);
        inventory.setExpressTrackingNumber(inventoryDTO.getExpressTrackingNumber() != null ? inventoryDTO.getExpressTrackingNumber() : null);
        inventory.setReceiverPhone(inventoryDTO.getReceiverPhone() != null ? inventoryDTO.getReceiverPhone() : null);

        inventory.setHandlingEmployee(employeeIdMapper.fromId(inventoryDTO.getHandlingEmployeeId()));

        inventory.setStatus(inventoryDTO.getStatus() != null ? inventoryDTO.getStatus() : null);
        inventory.setRemarks(inventoryDTO.getRemarks() != null ? inventoryDTO.getRemarks() : null);

        inventory.setBorrowedCompany(null);

        inventory.setSalesContract(contractIdMapper.fromId(inventoryDTO.getSalesContractId()));

    }

    public void updateEntityWhenBorrow(Inventory inventory, InventoryDTO inventoryDTO) {
        inventory.setOutTime(inventoryDTO.getOutTime() != null ? inventoryDTO.getOutTime() : null);
        inventory.setShippingAddress(inventoryDTO.getShippingAddress() != null ? inventoryDTO.getShippingAddress() : null);
        inventory.setShippingMethod(inventoryDTO.getShippingMethod() != null ? inventoryDTO.getShippingMethod() : null);
        inventory.setReceiverName(inventoryDTO.getReceiverName() != null ? inventoryDTO.getReceiverName() : null);
        inventory.setExpressTrackingNumber(inventoryDTO.getExpressTrackingNumber() != null ? inventoryDTO.getExpressTrackingNumber() : null);
        inventory.setReceiverPhone(inventoryDTO.getReceiverPhone() != null ? inventoryDTO.getReceiverPhone() : null);

        inventory.setHandlingEmployee(employeeIdMapper.fromId(inventoryDTO.getHandlingEmployeeId()));   

        inventory.setBorrowedCompany(companyIdMapper.fromId(inventoryDTO.getBorrowedCompanyId()));

        inventory.setStatus(inventoryDTO.getStatus() != null ? inventoryDTO.getStatus() : null);
        inventory.setRemarks(inventoryDTO.getRemarks() != null ? inventoryDTO.getRemarks() : null);

    }

    public void updateEntityWhenReturn(Inventory inventory, InventoryDTO inventoryDTO) {
        inventory.setOutTime(null);
        inventory.setStatus(inventoryDTO.getStatus());
        inventory.setRemarks(inventoryDTO.getRemarks());
    }
}
