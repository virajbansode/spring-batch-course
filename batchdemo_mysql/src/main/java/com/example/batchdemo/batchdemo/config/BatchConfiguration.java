package com.example.batchdemo.batchdemo.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.example.batchdemo.batchdemo.listner.DemoJobExecutionListener;
import com.example.batchdemo.batchdemo.listner.StepExecutionListner;
import com.example.batchdemo.batchdemo.processor.DemoProcessor;
import com.example.batchdemo.batchdemo.reader.DemoReader;
import com.example.batchdemo.batchdemo.writter.DemoWritter;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    @Autowired
    private DemoJobExecutionListener demoJobExecutionListener;

    @Autowired
    private StepExecutionListner  stepExecutionListener;


    public BatchConfiguration(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        this.jobRepository = jobRepository;
        this.platformTransactionManager = platformTransactionManager;
    }

    @Bean
    public Job demoJob(){
        return new JobBuilder("demoJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                /*  if we comment the incrementer()
                    Caused by: org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException: 
                    A job instance already exists and is complete for identifying parameters={'executionDate':'{value=2025-06-10,
                    type=class java.lang.String, identifying=true}'}.  If you want to run this job again, change the parameters.
                  */
                .start(demoStep())
                .next(taskLetStep())
                .listener(demoJobExecutionListener)
                .build();
    }

    

    @Bean
    public Step demoStep(){
        return new StepBuilder("demoStep", jobRepository)
        .<String,String>chunk(2, platformTransactionManager)
        .reader(demoReader())
        .processor(demoProcessor())
        .writer(demoWritter()).listener(stepExecutionListener).build();
    }

    @Bean
    public DemoReader demoReader(){
        return new DemoReader();
    }

    @Bean
    public DemoProcessor demoProcessor(){
        return new DemoProcessor();
    }

    @Bean
    public DemoWritter demoWritter(){
        return new DemoWritter();
    }

    @Bean
    public Step taskLetStep(){
        return new StepBuilder("demoStep", jobRepository)
        .tasklet(tasklet(), platformTransactionManager)
     .build();
    }
    

    @Bean
    public Tasklet tasklet() {
        return new Tasklet() {

            @Override
            public RepeatStatus execute(StepContribution arg0, ChunkContext chunkContext) throws Exception {
                System.out.println("chunkContext.getStepContext().getJobExecutionContext()##### : "+chunkContext.getStepContext().getJobExecutionContext());
                return RepeatStatus.FINISHED;
            }
            
        };
    }
}
