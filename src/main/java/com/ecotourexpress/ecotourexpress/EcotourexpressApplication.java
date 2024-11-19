package com.ecotourexpress.ecotourexpress;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class EcotourexpressApplication {
    public static void main(String[] args) {
        SpringApplication.run(EcotourexpressApplication.class, args);
    }
}
