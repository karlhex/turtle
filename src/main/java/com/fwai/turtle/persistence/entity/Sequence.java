package com.fwai.turtle.persistence.entity;

import com.fwai.turtle.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Comment;

/**
 * 流水号实体
 */
@Data
@Entity
@Table(name = "sequence")
@EqualsAndHashCode(callSuper = true)
public class Sequence extends BaseEntity {

    @Comment("流水号类型")
    @Column(nullable = false, length = 50, unique = true)
    private String type;

    @Comment("前缀")
    @Column(nullable = false, length = 10)
    private String prefix;

    @Comment("是否包含年")
    private Boolean includeYear = false;

    @Comment("是否包含月")
    private Boolean includeMonth = false;

    @Comment("是否包含日")
    private Boolean includeDay = false;

    @Comment("当前序号")
    @Column(nullable = false)
    private Long currentValue = 0L;
}
