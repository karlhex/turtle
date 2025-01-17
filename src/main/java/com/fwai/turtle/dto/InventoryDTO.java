package com.fwai.turtle.dto;

import com.fwai.turtle.types.InventoryStatus;
import com.fwai.turtle.types.ShippingMethod;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.fwai.turtle.common.BaseDTO;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class InventoryDTO extends BaseDTO {

    private Long productId;  // 产品ID
    private String productName;  // 产品名称

    private Integer quantity;  // 数量
    private String license;  // 许可证

    private Long purchaseContractId;  // 采购合同ID
    private String purchaseContractNo;  // 采购合同编号

    private Long salesContractId;  // 销售合同ID
    private String salesContractNo;  // 销售合同编号

    private LocalDateTime storageTime;  // 入库时间
    private LocalDateTime outTime;  // 出库时间

    private Long borrowedCompanyId;  // 借出公司ID
    private String borrowedCompanyName;  // 借出公司名称

    private InventoryStatus status;  // 库存状态
    private String remarks;  // 备注

    private String shippingAddress;  // 发送地址
    private ShippingMethod shippingMethod;  // 运送方式
    private String receiverName;  // 接收人
    private String expressTrackingNumber;  // 快递单号
    private String receiverPhone;  // 接收人电话

    private Long handlingEmployeeId;  // 经办员工ID
    private String handlingEmployeeName;  // 经办员工姓名
}
