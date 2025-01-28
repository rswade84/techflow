package org.taskntech.tech_flow.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.taskntech.tech_flow.interceptor.LoggingInterceptor;

@Configuration
public class WebLogConfig implements WebMvcConfigurer {
//^^ editing some default settings in webMVConfigurer to make an interceptor

    //allows the use of loggingInterceptor injunction with Spring
    @Autowired
    private LoggingInterceptor loggingInterceptor;

    //adds loggingInterceptor to the interceptor chain
    //tells spring to run every http request/response through this interceptor
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loggingInterceptor);
    }
}

