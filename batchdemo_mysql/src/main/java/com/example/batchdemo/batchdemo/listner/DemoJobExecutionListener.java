package com.example.batchdemo.batchdemo.listner;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class DemoJobExecutionListener implements JobExecutionListener{

    @Override
    public void beforeJob(JobExecution jobExecution) {
     System.out.println("****************** Before JOB : "+jobExecution.getJobId()+") "+jobExecution.getJobInstance().getJobName());   
     jobExecution.getExecutionContext().put("developer", "viraj");
    }
    @Override
    public void afterJob(JobExecution jobExecution) {
        System.out.println("****************** After JOB : "+jobExecution.getJobId()+") "+jobExecution.getJobInstance().getJobName());   
    }

}
