package com.samratalam.spring_batch_practice.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerCSVDto {
    private String msisdn;
    private String fullName;
    private String email;
}
