package com.fwai.turtle.persistence.mapper;

import com.fwai.turtle.dto.InventoryDTO;
import com.fwai.turtle.persistence.entity.Inventory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface InventoryMapper {

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "purchaseContract.id", target = "purchaseContractId")
    @Mapping(source = "purchaseContract.contractNo", target = "purchaseContractNo")
    @Mapping(source = "salesContract.id", target = "salesContractId")
    @Mapping(source = "salesContract.contractNo", target = "salesContractNo")
    @Mapping(source = "borrowedCompany.id", target = "borrowedCompanyId")
    @Mapping(source = "borrowedCompany.fullName", target = "borrowedCompanyName")
    @Mapping(source = "handlingEmployee.id", target = "handlingEmployeeId")
    @Mapping(source = "handlingEmployee.name", target = "handlingEmployeeName")
    InventoryDTO toDTO(Inventory inventory);

    @Mapping(source = "productId", target = "product.id")
    @Mapping(source = "purchaseContractId", target = "purchaseContract.id")
    @Mapping(source = "salesContractId", target = "salesContract.id")
    @Mapping(source = "borrowedCompanyId", target = "borrowedCompany.id")
    @Mapping(source = "handlingEmployeeId", target = "handlingEmployee.id")
    Inventory toEntity(InventoryDTO inventoryDTO);
}
