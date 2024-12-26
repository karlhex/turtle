package com.fwai.turtle.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "employee_attendance")
@Data
public class EmployeeAttendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(name = "attendance_date")
    private LocalDate attendanceDate;

    @Column(name = "check_in_time")
    private LocalTime checkInTime;

    @Column(name = "check_out_time")
    private LocalTime checkOutTime;

    private String status;
    private String remarks;
} 