package com.upplication.config;

import com.upplication.config.util.EnvironmentVars;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;


@Configuration
@Profile("!elasticbeanstalk")
public class EnvironmentLocalBuilder {

    @Value("${dataSource.driverClassName}")
    private String driver;
    @Value("${dataSource.url}")
    private String url;
    @Value("${dataSource.username}")
    private String username;
    @Value("${dataSource.password}")
    private String password;
    @Value("${hibernate.dialect}")
    private String dialect;
    @Value("${hibernate.hbm2ddl.auto}")
    private String hbm2ddlAuto;


    @Bean
    public EnvironmentVars vars() {

        EnvironmentVars vars = new EnvironmentVars();
        vars.setDialect(dialect);
        vars.setDriver(driver);
        vars.setHbm2ddlAuto(hbm2ddlAuto);
        vars.setPassword(password);
        vars.setUrl(url);
        vars.setUsername(username);

        return vars;
    }
}
