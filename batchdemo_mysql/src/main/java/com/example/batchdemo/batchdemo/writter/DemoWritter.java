package com.example.batchdemo.batchdemo.writter;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

public class DemoWritter implements ItemWriter<String> {

    @Override
    public void write(Chunk<? extends String> chunk) throws Exception {
       chunk.getItems().stream().forEach(System.out::println);
    }

}
