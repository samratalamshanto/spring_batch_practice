package com.samratalam.spring_batch_practice.customer.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    @Column(unique = true, nullable = false)
    private String msisdn;
    @Column
    private String customerType;
    @Column
    private String brand;
    @Column
    private LocalDate activationDate;
    @Column
    private String fullName;
    private String category;
    @Column
    private String email;
}
