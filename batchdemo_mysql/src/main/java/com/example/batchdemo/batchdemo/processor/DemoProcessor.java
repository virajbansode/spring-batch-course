package com.example.batchdemo.batchdemo.processor;

import org.springframework.batch.item.ItemProcessor;

public class DemoProcessor implements ItemProcessor<String,String>{

    @Override
    public String process(String input) throws Exception {
       System.out.println("Input: "+input);
       return input.toUpperCase();
    }

}
