package com.example.customerservice.repository;

import com.example.customerservice.entity.UserService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserServiceRepository extends JpaRepository<UserService, Long> {
}
