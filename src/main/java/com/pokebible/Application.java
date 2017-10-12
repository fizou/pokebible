package com.pokebible;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pokebible.*;

@SpringBootApplication
public class Application extends SpringBootServletInitializer {
	 
	public static void main(String[] args) {
		Tracer.warn("Application - main - Starting Server...");
		SpringApplication.run(Application.class, args);
	}
	
    //For tomcat run
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

}
