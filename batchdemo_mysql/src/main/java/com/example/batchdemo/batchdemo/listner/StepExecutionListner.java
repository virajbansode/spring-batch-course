package com.example.batchdemo.batchdemo.listner;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class StepExecutionListner implements StepExecutionListener {

    @Override
    public void beforeStep(StepExecution stepExecution) {
        System.out.println("Before step "+stepExecution.getStepName());
        System.out.println("stepExecution.getJobExecution().getExecutionContext() : "+stepExecution.getJobExecution().getExecutionContext());
        System.out.println("stepExecution.getExecutionContext() : "+stepExecution.getExecutionContext());

        stepExecution.getExecutionContext().put("tester","Sachin");
   }

   @Override
   public ExitStatus afterStep(StepExecution stepExecution) {
    System.out.println("After step "+stepExecution.getStepName());
        System.out.println("stepExecution.getJobExecution().getExecutionContext() : "+stepExecution.getJobExecution().getExecutionContext());
        System.out.println("stepExecution.getExecutionContext() : "+stepExecution.getExecutionContext());
      return null;
   }

}
