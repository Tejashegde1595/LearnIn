package com.example.portfolio.service;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.example.portfolio.service")
@EntityScan("com.example.portfolio.service.Entity")
public class ServiceConfiguration {
}
