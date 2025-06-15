package com.example.batchdemo.batchdemo.writter;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

import com.example.batchdemo.batchdemo.model.StudentCsv;

public class DemoWritter implements ItemWriter<StudentCsv> {

    @Override
    public void write(Chunk<? extends StudentCsv> chunk) throws Exception {
       chunk.getItems().stream().forEach(System.out::println);
    }

}
