package com.samratalam.spring_batch_practice.config.batch;

import com.samratalam.spring_batch_practice.customer.entity.Customer;
import com.samratalam.spring_batch_practice.customer.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomerBatchWriter implements ItemWriter<Customer> {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public void write(Chunk<? extends Customer> chunk) throws Exception {
        log.info("Chunk Size={}", chunk.getItems().size());
        customerRepository.saveAll(chunk.getItems());
    }
}
