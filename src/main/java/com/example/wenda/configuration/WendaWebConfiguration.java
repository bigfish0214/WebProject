package com.example.wenda.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.example.wenda.interceptor.LoginRequiredInterceptor;
import com.example.wenda.interceptor.PassportInterceptor;


@Component
public class WendaWebConfiguration extends WebMvcConfigurationSupport{
	
	@Autowired
	PassportInterceptor passportInterceptor;
	@Autowired
	LoginRequiredInterceptor loginRequiredInterceptor;
	
	@Override
	protected void addInterceptors(InterceptorRegistry registry) {
		// TODO Auto-generated method stub
		registry.addInterceptor(passportInterceptor);
		registry.addInterceptor(loginRequiredInterceptor).addPathPatterns("/user/*");
		super.addInterceptors(registry);
	}

	@Override
	protected void addResourceHandlers(ResourceHandlerRegistry registry) {
		// TODO Auto-generated method stub
		registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
		super.addResourceHandlers(registry);

		
	}
	
	
	
}
