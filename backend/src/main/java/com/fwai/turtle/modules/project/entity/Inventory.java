package com.fwai.turtle.modules.project.entity;

import com.fwai.turtle.base.entity.BaseEntity;
import com.fwai.turtle.base.types.InventoryStatus;
import com.fwai.turtle.base.types.ShippingMethod;
import com.fwai.turtle.modules.organization.entity.Employee;
import com.fwai.turtle.modules.customer.entity.Company;
import com.fwai.turtle.modules.project.entity.Contract;
import com.fwai.turtle.modules.customer.entity.Product;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "inventories")
@EqualsAndHashCode(callSuper = true)
public class Inventory extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;  // 产品

    @Column(nullable = false)
    private Integer quantity;  // 数量

    @Column(length = 255)
    private String license;  // 许可证

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_contract_id")
    private Contract purchaseContract;  // 采购合同

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sales_contract_id")
    private Contract salesContract;  // 销售合同

    @Column(name = "storage_time")
    private LocalDateTime storageTime;  // 入库时间

    @Column(name = "out_time")
    private LocalDateTime outTime;  // 出库时间

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "borrowed_company_id")
    private Company borrowedCompany;  // 借出公司

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InventoryStatus status;  // 库存状态

    @Column(length = 1000)
    private String remarks;  // 备注

    @Column(name = "shipping_address", length = 500)
    private String shippingAddress;  // 发送地址

    @Enumerated(EnumType.STRING)
    @Column(name = "shipping_method")
    private ShippingMethod shippingMethod;  // 运送方式

    @Column(name = "receiver_name", length = 100)
    private String receiverName;  // 接收人

    @Column(name = "express_tracking_number", length = 100)
    private String expressTrackingNumber;  // 快递单号

    @Column(name = "receiver_phone", length = 20)
    private String receiverPhone;  // 接收人电话

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "handling_employee_id")
    private Employee handlingEmployee;  // 经办员工
}
