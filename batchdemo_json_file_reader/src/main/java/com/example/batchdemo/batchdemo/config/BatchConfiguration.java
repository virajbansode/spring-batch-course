package com.example.batchdemo.batchdemo.config;

import org.hibernate.cache.spi.support.AbstractReadWriteAccess.Item;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import com.example.batchdemo.batchdemo.model.Student;
import com.example.batchdemo.batchdemo.processor.DemoProcessor;
import com.example.batchdemo.batchdemo.writter.DemoWritter;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;


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
                .build();
    }

    

    @Bean
    public Step demoStep(){
        return new StepBuilder("demoStep", jobRepository)
        .<Student,Student>chunk(2, platformTransactionManager)
        .reader(demoReader())
        .processor(demoProcessor())
        .writer(demoWritter())
        .build();
    }

    @Bean
    @StepScope
    public JsonItemReader<Student> demoReader(){

                return new JsonItemReaderBuilder<Student>()
        .jsonObjectReader(new JacksonJsonObjectReader<>(Student.class))
        .resource(new ClassPathResource("studentInformation.json"))
        .name("jsonItemReader")
        .build();
    }


     @Bean
    public DemoProcessor demoProcessor(){
        return new DemoProcessor();
    } 

    @Bean
    public DemoWritter demoWritter(){
        return new DemoWritter();
    }
}
