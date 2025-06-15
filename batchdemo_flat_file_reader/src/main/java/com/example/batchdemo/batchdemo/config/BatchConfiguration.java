package com.example.batchdemo.batchdemo.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import com.example.batchdemo.batchdemo.model.StudentCsv;
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
        .<StudentCsv,StudentCsv>chunk(2, platformTransactionManager)
        .reader(studentCsvReader())
        .processor(demoProcessor())
        .writer(demoWritter())
        .build();
    }

    @Bean
    public FlatFileItemReader<StudentCsv> studentCsvReader() {
        return new FlatFileItemReaderBuilder<StudentCsv>()
                .name("personItemReader")
                .resource(new ClassPathResource("studentCsv.csv")) // Path to your file
                .delimited() // Use delimited file format
                .names("ID", "First Name", "Last Name","Email") // Column names
                .linesToSkip(1)
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                    setTargetType(StudentCsv.class);
                }})
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
