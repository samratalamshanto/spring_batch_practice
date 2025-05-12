package com.samratalam.spring_batch_practice.config.batch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.samratalam.spring_batch_practice.customer.entity.Customer;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

@Slf4j
public class CustomerBatchSkipListener implements SkipListener<Customer, Number>, StepExecutionListener {
    private StepExecution stepExecution;  //can store the error in DB

    @Override
    public void beforeStep(StepExecution stepExecution) {
        this.stepExecution = stepExecution;  //can listen execution
    }

    @Override
    public void onSkipInRead(Throwable throwable) {
        log.error("On Read Customer Batch details: [JobExecId={}, StepName={}, fileName={}, createTime={}] " +
                        "errorMsg={}",
                stepExecution.getJobExecutionId(), stepExecution.getStepName(),
                stepExecution.getJobParameters().getString("filePath"),
                stepExecution.getCreateTime(),
                throwable.getMessage());
    }

    @Override
    public void onSkipInWrite(Number item, Throwable throwable) {
        log.error("On Write Customer Batch details: [JobExecId={}, StepName={}, fileName={}, createTime={}] " +
                        "itemNumber={}, errorMsg={}",
                stepExecution.getJobExecutionId(), stepExecution.getStepName(),
                stepExecution.getJobParameters().getString("filePath"),
                stepExecution.getCreateTime(),
                item, throwable.getMessage());
    }

    @SneakyThrows
    @Override
    public void onSkipInProcess(Customer customer, Throwable throwable) {
        log.error("On Process Customer Batch details: [JobExecId={}, StepName={}, fileName={}, createTime={}]" +
                        " Item={} due to this errorMsg={}",
                stepExecution.getJobExecutionId(), stepExecution.getStepName(),
                stepExecution.getJobParameters().getString("filePath"),
                stepExecution.getCreateTime(),
                new ObjectMapper().writeValueAsString(customer),
                throwable.getMessage());
    }
}


