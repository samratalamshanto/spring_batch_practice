package com.samratalam.spring_batch_practice.config.batch;

import com.samratalam.spring_batch_practice.customer.entity.Customer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class CustomerBatchConfig {

    private final String[] columnNames = {
            "msisdn", "fullName", "email"
    };

    @Bean
    @StepScope
    public FlatFileItemReader<Customer> reader(@Value("#{jobParameters['filePath']}") String filePath) {
        FlatFileItemReader<Customer> reader = new FlatFileItemReader<>();
        reader.setResource(new FileSystemResource(filePath));
        reader.setName("csvReader");
        reader.setLinesToSkip(1);
        reader.setLineMapper(lineMapper());
        return reader;
    }


    private LineMapper<Customer> lineMapper() {
        DefaultLineMapper<Customer> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setDelimiter(",");
        tokenizer.setStrict(false);
        tokenizer.setNames(columnNames);

        BeanWrapperFieldSetMapper<Customer> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Customer.class);

        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);

        return lineMapper;
    }

    @Bean
    public CustomerBatchProcessor processor() {
        return new CustomerBatchProcessor();
    }

    @Bean
    public CustomerBatchWriter writer() {
        return new CustomerBatchWriter();
    }

    @Bean
    public Step customerImportStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager) {
        return new StepBuilder("customerImportStep", jobRepository)
                .<Customer, Customer>chunk(100, transactionManager)
                .reader(reader(null))
                .processor(processor())
                .writer(writer())
                .taskExecutor(taskExecutor()) // Parallel
                .faultTolerant()
                .skipPolicy(customerSkipPolicy())
                .skip(FlatFileParseException.class)
//                .skipLimit(5)
                .listener(customerBatchSkipListener())
                .build();
    }

    @Bean(name = "customerImportBatchJob")
    public Job job(JobRepository jobRepository,
                   Step customerImportStep) {
        return new JobBuilder("customerImportJob", jobRepository)
                .preventRestart()
                .start(customerImportStep)
                .build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        SimpleAsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor();
        taskExecutor.setConcurrencyLimit(10);
        return taskExecutor;
    }

    @Bean
    public CustomerBatchSkipPolicy customerSkipPolicy() {
        return new CustomerBatchSkipPolicy();
    }

    @Bean
    public CustomerBatchSkipListener customerBatchSkipListener() {
        return new CustomerBatchSkipListener();
    }
}
