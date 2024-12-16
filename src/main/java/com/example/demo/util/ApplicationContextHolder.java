package com.example.demo.util;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ApplicationContextHolder {
    private final Map<String, Long> context = new ConcurrentHashMap<>();

    public void set(String key, Long value) {
        context.put(key, value);
    }

    public Long get(String key) {
        return context.get(key);
    }

    public void remove(String key) {
        context.remove(key);
    }
}
