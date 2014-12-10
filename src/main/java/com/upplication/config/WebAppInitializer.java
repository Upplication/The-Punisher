package com.upplication.config;

import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.*;

public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    public void onStartup(ServletContext context) throws ServletException {
        super.onStartup(context);

        String activeProfile = "default";

        if (System.getProperty("RDS_DB_NAME") != null) {
            activeProfile = "elasticbeanstalk";
        }

        context.setInitParameter("spring.profiles.active", activeProfile);
    }


    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[] {ApplicationConfig.class, EnvironmentCloudBuilder.class, EnvironmentLocalBuilder.class, DataSourceConfig.class, JpaConfig.class,  SecurityConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[] {WebMvcConfig.class};
    }

    @Override
    protected Filter[] getServletFilters() {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);

        DelegatingFilterProxy securityFilterChain = new DelegatingFilterProxy("springSecurityFilterChain");

        return new Filter[] {characterEncodingFilter, securityFilterChain};
    }

    @Override
    protected void customizeRegistration(ServletRegistration.Dynamic registration) {
        registration.setInitParameter("defaultHtmlEscape", "true");
        // TODO: FIXME: what is not work?
        //registration.setInitParameter("spring.profiles.active", "elasticbeanstalk");
        //registration.setInitParameter(ContextLoader.CONTEXT_INITIALIZER_CLASSES_PARAM, CloudApplicationContextInitializer.class.getCanonicalName());
    }
}