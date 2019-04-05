package com.pokebible;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Predicates;
import com.pokebible.*;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.data.rest.configuration.SpringDataRestConfiguration;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
public class Application extends SpringBootServletInitializer {

    private final static Logger logger = LoggerFactory.getLogger(SpringBootServletInitializer.class);

    //For Springboot run
    public static void main(String[] args) {
        logger.warn("Application - main - Starting Server V1.0 ...");
        SpringApplication.run(Application.class, args);
    }
	
    //For Tomcat run
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }
    
    //For Swagger run
    @Configuration
    @EnableSwagger2
    //@ComponentScan("com.pokebible")
    @Import({springfox.documentation.spring.data.rest.configuration.SpringDataRestConfiguration.class})
    public class SwaggerConfig {                                    

        @Bean
        public Docket api() { 
            return new Docket(DocumentationType.SWAGGER_2)  
              .select()                                  
              .apis(RequestHandlerSelectors.any())              
              .apis(Predicates.not(RequestHandlerSelectors.basePackage("org.springframework.data.rest.webmvc")))
//              .apis(Predicates.not(RequestHandlerSelectors.basePackage("org.springframework.boot")))
//              .apis(Predicates.not(RequestHandlerSelectors.basePackage("org.springframework.cloud")))
//              .paths(PathSelectors.any())                          
              .paths(PathSelectors.regex("/api.*"))
              .build() 
//            .pathMapping("/")
//            .genericModelSubstitutes(ResponseEntity.class)
//            .tags(new Tag("Pokemon Service", "All apis relating to pokemons"))
            ;
        }
        
    }
    
/*
    // See controllers.java 
    @RestController
    public class CustomController {
     
        @RequestMapping(value = "/custom", method = RequestMethod.POST)
        public String custom() {
            return "custom";
        }
    }
*/

}
