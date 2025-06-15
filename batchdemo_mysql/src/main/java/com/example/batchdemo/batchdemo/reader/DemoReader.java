package com.example.batchdemo.batchdemo.reader;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

public class DemoReader implements ItemReader<String> {

    private String[] messages = {"Hello", "World", "Spring", "Batch"};
    private int index = 0;

    @Override
    public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (index < messages.length) {
            String readWorld =  messages[index++];
            System.out.println("readWorld: "+readWorld);
            return readWorld;
        } else {
            return null;
        }
    }
}
