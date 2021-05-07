package com.pokebible.restapi;

import com.google.common.base.Predicates;
import com.pokebible.Pokemon;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.core.mapping.ExposureConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.data.rest.configuration.SpringDataRestConfiguration;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

//
// Application: Configuration of REST API and Swagger
//
    
@Configuration
public class RestConfiguration implements RepositoryRestConfigurer {

    private static final Logger log = LoggerFactory.getLogger(RestConfiguration.class);

    // Activate Id in REST API response 
    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {

        log.info("Starting REST API...");

        config.exposeIdsFor(Pokemon.class);
    }

    // Activate Swagger Interface on http://server/swagger-ui/
    @Configuration
    @EnableSwagger2WebMvc
    @Import(SpringDataRestConfiguration.class)
    public class SwaggerConfig {                                    

        ApiInfo apiInfo() {
            return new ApiInfoBuilder()
                .title("PokeBible API")
                .description("Access to PokeBible referential with REST API")
                .license("Apache 2.0")
                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
                .termsOfServiceUrl("")
                .version("1.0.0")
                .contact(new Contact("", "", "fizou@yahoo.com"))
                .build();
        }

        @Bean
        public Docket api() { 
            
            log.info("Starting Swagger UI...");

            return new Docket(DocumentationType.SWAGGER_2)  
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.regex("/api.*"))
                .build() 
                .apiInfo(apiInfo())
            ;
        }

    }
}


