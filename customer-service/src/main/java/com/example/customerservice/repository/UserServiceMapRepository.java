package com.example.customerservice.repository;

import com.example.customerservice.entity.UserServicesMap;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserServiceMapRepository extends JpaRepository<UserServicesMap, Long> {
}
