package com.samratalam.spring_batch_practice.customer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerManagementServiceImpl implements
        CustomerRegistrationService {

    private final JobLauncher jobLauncher;

    @Autowired
    @Qualifier("customerImportBatchJob")
    private Job customerImportBatchJob;


    @Override
    public void importCustomers(String filePath) {
        try {

            JobParametersBuilder parametersBuilder = new JobParametersBuilder();
            parametersBuilder.addString("filePath", filePath); // file path as params
            parametersBuilder.addDate("launchDate", new Date());
            parametersBuilder.addString("createdBy", "samrat"); //will update by loggedIn username

            JobExecution jobExecution = jobLauncher.run(customerImportBatchJob, parametersBuilder.toJobParameters());

//            if (jobExecution.getExitStatus().getExitCode().equals(ExitStatus.COMPLETED.getExitCode())) {
//                log.info("Customer imported successfully");
//                Files.deleteIfExists(Paths.get(filePath));
//            }

        } catch (JobExecutionAlreadyRunningException e) {
            throw new RuntimeException(e);
        } catch (JobRestartException e) {
            throw new RuntimeException(e);
        } catch (JobInstanceAlreadyCompleteException e) {
            throw new RuntimeException(e);
        } catch (JobParametersInvalidException e) {
            throw new RuntimeException(e);
        }

    }

}
