package com.example.portfolio.api;

import com.example.portfolio.service.ServiceConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;


@SpringBootApplication
@Import(ServiceConfiguration.class)
public class PortfolioApiApplication {
    public static void main(String[] args){
        SpringApplication.run(PortfolioApiApplication.class,args);
    }
}
