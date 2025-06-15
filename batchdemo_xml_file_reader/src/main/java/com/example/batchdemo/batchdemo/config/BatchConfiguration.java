package com.example.batchdemo.batchdemo.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
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
    public StaxEventItemReader <Student> demoReader(){

        return new StaxEventItemReaderBuilder<Student>()
			.name("itemReader")
			.resource(new ClassPathResource("studentInformation.xml"))
			.addFragmentRootElements("student")
			.unmarshaller(jaxb2Marshaller())
			.build();
    }

    @Bean
    public Jaxb2Marshaller jaxb2Marshaller() {
        Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
        jaxb2Marshaller.setClassesToBeBound(Student.class);
        return jaxb2Marshaller;
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
