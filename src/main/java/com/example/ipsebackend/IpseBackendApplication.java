package com.example.ipsebackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;


@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.ipsebackend.repositories")
@EntityScan(basePackages = "com.example.ipsebackend.entities")
public class IpseBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(IpseBackendApplication.class, args);
    }



}

