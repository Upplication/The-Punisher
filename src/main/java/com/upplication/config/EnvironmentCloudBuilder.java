package com.upplication.config;

import com.upplication.config.util.EnvironmentVars;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;


@Configuration
@Profile("elasticbeanstalk")
public class EnvironmentCloudBuilder {

    @Value("${dataSource.elasticbeanstalk.driverClassName}")
    private String driver;
    @Value("#{'jdbc:mysql://' + systemProperties['RDS_HOSTNAME'] + ':' + systemProperties['RDS_PORT'] + '/' + systemProperties['RDS_DB_NAME']}")
    private String url;
    @Value("#{systemProperties['RDS_USERNAME']}")
    private String username;
    @Value("#{systemProperties['RDS_PASSWORD']}")
    private String password;
    @Value("${hibernate.elasticbeanstalk.dialect}")
    private String dialect;
    @Value("${hibernate.hbm2ddl.auto}")
    private String hbm2ddlAuto;


    @Bean
    public EnvironmentVars build() {

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
