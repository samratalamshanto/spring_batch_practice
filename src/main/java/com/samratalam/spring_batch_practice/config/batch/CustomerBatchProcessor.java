package com.samratalam.spring_batch_practice.config.batch;

import com.samratalam.spring_batch_practice.customer.entity.Customer;
import com.samratalam.spring_batch_practice.customer.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

@Slf4j
public class CustomerBatchProcessor implements ItemProcessor<Customer, Customer> {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public Customer process(Customer customer) throws Exception {
        if (ObjectUtils.isEmpty(customer.getMsisdn())) {
            return null;
        }

        if (customerRepository.findByMsisdn(customer.getMsisdn()).isPresent()) {
            log.warn("CustomerBatchProcessor:: Customer with Msisdn {} already exists", customer.getMsisdn());
            return null;
        }

        return customer;
    }
}
