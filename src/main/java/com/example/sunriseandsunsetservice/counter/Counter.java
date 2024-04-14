package com.example.sunriseandsunsetservice.counter;

import org.springframework.stereotype.Component;

@Component
public class Counter {

    private int count = 0;

    public synchronized void inc() {

        this.count++;
    }

    public synchronized int getCount() {

        return this.count;
    }
}
