package com.example.batchdemo.batchdemo;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BatchdemoApplication implements CommandLineRunner{

	@Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job demoJob;

	@Autowired
	private JobRepository jobRepository;

	public static void main(String[] args) {
		SpringApplication.run(BatchdemoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		JobParameters jobParameters = new JobParametersBuilder()
                .addLong("timestamp", System.currentTimeMillis())
				//.addString("executionDate", LocalDate.now().toString())
                .toJobParameters();
				jobLauncher.run(demoJob, jobParameters);

		//read batch_job_execution_params for LastJobExecution.does it has same day entry. 
		//do not start again for today
		//incrementer(new RunIdIncrementer()) in jobBuilder checks for Jobparameters and if found entry for same param and vlue then throws error

		/*  if (jobRepository.getLastJobExecution(demoJob.getName(), jobParameters) == null) {
            jobLauncher.run(demoJob, jobParameters);
        } else {
              System.out.println("Job already ran for this date");
        }  */
	}

}
