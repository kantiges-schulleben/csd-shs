package com.klnsdr.axon;

import org.springframework.stereotype.Component;

@Component
public class ExitHandler {
    public void exit(int status) {
        System.exit(status);
    }
}
