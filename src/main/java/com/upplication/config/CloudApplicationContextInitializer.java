package com.upplication.config;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

public class CloudApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext>{
    // FIXME: why not works?
    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {

        if (System.getProperty("RDS_DB_NAME") != null) {
            configurableApplicationContext.getEnvironment().addActiveProfile("elasticbeanstalk");
            //
        }
        else {
            configurableApplicationContext.getEnvironment().addActiveProfile("default");
        }
        configurableApplicationContext.refresh();
    }
}
