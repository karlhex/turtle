package com.fwai.turtle.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import com.fwai.turtle.common.BaseDTO;
import lombok.EqualsAndHashCode;

/**
 * Contact DTO
 * 联系人信息传输对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ContactDTO extends BaseDTO {
    private Long id;

    // 基本信息 Basic Information
    private String firstName;
    private String lastName;
    private String address;
    private String email;

    // 联系方式 Contact Information
    private String mobilePhone;
    private String workPhone;
    private String homePhone;

    // 公司信息 Company Information
    private Long companyId;
    private String companyName;  // 所属公司名称
    private String department;   // 部门
    private String title;       // 职位

    // 个人信息 Personal Information
    private String gender;        // 性别
    private String nativePlace;   // 籍贯
    private String ethnicity;     // 民族
    private String maritalStatus; // 婚姻状况
    private String nationality;   // 国籍
    private String birthDate;     // 出生日期
    private String religion;      // 宗教

    // 教育背景 Educational Background
    private String university;     // 毕业院校
    private String major;         // 专业
    private String graduationYear; // 毕业年份
    private String degree;        // 学位

    // 其他信息 Other Information
    private String hobbies;       // 爱好
    private String remarks;       // 备注
    private Boolean active;       // 是否激活
}
