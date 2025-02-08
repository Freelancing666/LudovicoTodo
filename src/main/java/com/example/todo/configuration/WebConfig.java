package com.example.todo.configuration;

import com.example.todo.configuration.AuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        System.out.println("WebConfig: Registrazione dell'Interceptor");
        registry.addInterceptor(authInterceptor).addPathPatterns("/api/**"); // Protegge gli endpoint
    }
}
