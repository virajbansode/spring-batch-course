package com.example.batchdemo.batchdemo.config;

import java.io.IOException;

import javax.sql.DataSource;
import javax.xml.transform.Result;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.json.JacksonJsonObjectMarshaller;
import org.springframework.batch.item.json.JsonFileItemWriter;
import org.springframework.batch.item.json.builder.JsonFileItemWriterBuilder;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.batch.item.xml.builder.StaxEventItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.XmlMappingException;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.transaction.PlatformTransactionManager;

import com.example.batchdemo.batchdemo.model.Student;
import com.example.batchdemo.batchdemo.model.StudentRowMapper;
import com.example.batchdemo.batchdemo.processor.DemoProcessor;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final DataSource dataSource;

    public BatchConfiguration(JobRepository jobRepository,
     PlatformTransactionManager platformTransactionManager,
     DataSource dataSource) {
        this.jobRepository = jobRepository;
        this.platformTransactionManager = platformTransactionManager;
        this.dataSource = dataSource;
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
        .writer(demoWritter()).build();
    }

    @Bean
    public  JdbcCursorItemReader<Student> demoReader(){
        JdbcCursorItemReader<Student> jdbcCursorItemReader = new JdbcCursorItemReader<Student>();
        jdbcCursorItemReader.setDataSource(dataSource);
        jdbcCursorItemReader.setSql("select * from student");
        jdbcCursorItemReader.setName("jdbcReader");
        jdbcCursorItemReader.setRowMapper(new StudentRowMapper());
        return jdbcCursorItemReader;
    }

    @Bean
    public DemoProcessor demoProcessor(){
        return new DemoProcessor();
    }

    @Bean
    public StaxEventItemWriter demoWritter(){

        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        // Set the classes that should be bound by JAXB
        marshaller.setClassesToBeBound(Student.class);

        return new StaxEventItemWriterBuilder<Student>()
            .marshaller(marshaller)
            .rootTagName("students")
            .resource(new FileSystemResource("target/students.xml"))
            .name("sutdentXmlFileItemWriter")
            .build();
    }

}
