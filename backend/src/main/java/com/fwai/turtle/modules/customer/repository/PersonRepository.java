package com.fwai.turtle.modules.customer.repository;

import com.fwai.turtle.modules.customer.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface PersonRepository extends JpaRepository<Person, Long> {
    @Query("SELECT p FROM Person p WHERE " +
           "LOWER(p.firstName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(p.lastName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(p.mobilePhone) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(p.workPhone) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(p.homePhone) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(p.companyName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(p.department) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(p.position) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Person> searchByNameOrPhone(@Param("query") String query);
}
