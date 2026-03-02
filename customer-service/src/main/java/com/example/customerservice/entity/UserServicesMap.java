package com.example.customerservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "user_services_map")
public class UserServicesMap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @ColumnDefault("now()")
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @NotNull
    @Column(name = "object_id", nullable = false)
    private Long objectId;

    @NotNull
    @Column(name = "object_type", nullable = false, length = Integer.MAX_VALUE)
    private String objectType;

    @NotNull
    @Column(name = "user_services_type", nullable = false, length = Integer.MAX_VALUE)
    private String userServicesType;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_services_id", nullable = false)
    private UserService userServices;


}