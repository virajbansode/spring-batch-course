package com.example.batchwithh2.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.example.batchwithh2.service.ThirdTaskLet;

@Configuration
public class BatchConfiguration {

    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private ThirdTaskLet thirdTaskLet;


    @Bean
    public Job getJob(){
        
        return new JobBuilder("sampleJob", jobRepository)
                .start(getFirstTaskletStep())//intial step
                .next(getSecondTaskletStep())// sub sequent step of the job
                //we can add multiple using .next(....)
                .next(getThirdTaskletStep())
                .build();
    }

    @Bean
    public Step getFirstTaskletStep(){
        return new StepBuilder("getFirstTaskletStep",jobRepository).tasklet(getTasklet(),transactionManager).build();
    }

    @Bean
    public Tasklet getTasklet(){
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
                System.out.println("--------------------------");
                System.out.println("| First Tasklet Step     |");
                System.out.println("--------------------------");
                return RepeatStatus.FINISHED;
            }
        };
        
    }
    //---------------- 2nd tasklet step
    @Bean
    public Step getSecondTaskletStep(){
        return new StepBuilder("getSecondTaskletStep",jobRepository).tasklet(getSecondTasklet(),transactionManager).build();
    }

    @Bean
    public Tasklet getSecondTasklet(){
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
                System.out.println("--------------------------");
                System.out.println("|Second Tasklet Step      |");
                System.out.println("--------------------------");
                return RepeatStatus.FINISHED;
            }
        };
    }

    //---------------- 3rd tasklet step
    @Bean
    public Step getThirdTaskletStep(){
        return new StepBuilder("getThirdTaskletStep",jobRepository).tasklet(thirdTaskLet,transactionManager).build();
    }

}
