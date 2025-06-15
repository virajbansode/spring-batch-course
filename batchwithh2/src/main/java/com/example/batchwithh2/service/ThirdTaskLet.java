package com.example.batchwithh2.service;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Service;

@Service
public class ThirdTaskLet implements Tasklet{

    @Override
    public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
        System.out.println("--------------------------");
        System.out.println("| Third Tasklet Step     |");
        System.out.println("--------------------------");
        return RepeatStatus.FINISHED;
    }

}
