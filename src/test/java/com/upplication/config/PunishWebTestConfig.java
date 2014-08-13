package com.upplication.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;


@Configuration
@EnableJpaRepositories(basePackageClasses = com.upplication.thepunisher.Application.class)
public class PunishWebTestConfig extends WebMvcConfigurationSupport {

}
