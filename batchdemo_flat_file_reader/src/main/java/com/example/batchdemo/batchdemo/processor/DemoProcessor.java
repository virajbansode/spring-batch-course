package com.example.batchdemo.batchdemo.processor;

import org.springframework.batch.item.ItemProcessor;

import com.example.batchdemo.batchdemo.model.StudentCsv;

public class DemoProcessor implements ItemProcessor<StudentCsv,StudentCsv>{

    @Override
    public StudentCsv process(StudentCsv input) throws Exception {
       System.out.println("#### DemoProcessor Input: "+input);
       return input;
    }

}
