package com.example.batchdemo.batchdemo.processor;

import org.springframework.batch.item.ItemProcessor;

import com.example.batchdemo.batchdemo.model.Student;

public class DemoProcessor implements ItemProcessor<Student,Student>{

    @Override
    public Student process(Student input) throws Exception {
       System.out.println("#### DemoProcessor Input: "+input);
       return input;
    }

}
