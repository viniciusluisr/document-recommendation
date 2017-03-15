package com.document.recommendation.infra.exception.config;

import cz.jirutka.spring.exhandler.RestHandlerExceptionResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import javax.annotation.PostConstruct;
import java.util.List;

@Configuration
@DependsOn(value = {"restExceptionResolver", "exceptionHandlerExceptionResolver"})
public class ResolversConfig {

    private List<HandlerExceptionResolver> resolvers;
    private ApplicationContext applicationContext;

    @Autowired
    public ResolversConfig(List<HandlerExceptionResolver> resolvers, ApplicationContext applicationContext) {
        this.resolvers = resolvers;
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    public void addExceptionHandlers() {
        resolvers.add(applicationContext.getBean(RestHandlerExceptionResolver.class));
        resolvers.add(applicationContext.getBean(ExceptionHandlerExceptionResolver.class));
    }
}
