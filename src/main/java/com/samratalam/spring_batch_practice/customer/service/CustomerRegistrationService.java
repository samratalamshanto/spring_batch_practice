package com.samratalam.spring_batch_practice.customer.service;

import org.springframework.stereotype.Service;

@Service
public interface CustomerRegistrationService {
    void importCustomers(String filePath);
}
