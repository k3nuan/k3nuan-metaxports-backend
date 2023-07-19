package com.meta.sports.common;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Order(99)
public class ProcessGenTracker {

    public Map<Long, Integer> process;

    public ProcessGenTracker() { process = new HashMap<>(); }

    public boolean evalStatus(Long id) {
        Integer status = process.getOrDefault(id, 1);

        if (status == 1) process.remove(id);

        return status == 1;
    }

}
