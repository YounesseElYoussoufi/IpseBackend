package com.example.ipsebackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;


@SpringBootApplication
public class IpseBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(IpseBackendApplication.class, args);
    }



}

