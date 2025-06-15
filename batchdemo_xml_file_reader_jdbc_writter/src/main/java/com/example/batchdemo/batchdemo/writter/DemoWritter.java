package com.example.batchdemo.batchdemo.writter;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

import com.example.batchdemo.batchdemo.model.Student;

public class DemoWritter implements ItemWriter<Student> {

    @Override
    public void write(Chunk<? extends Student> chunk) throws Exception {
       chunk.getItems().stream().forEach(System.out::println);
    }

}
