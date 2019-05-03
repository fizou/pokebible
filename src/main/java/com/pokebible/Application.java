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
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Predicates;
import java.util.Locale;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import springfox.documentation.builders.ApiInfoBuilder;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
public class Application extends SpringBootServletInitializer {
    
    private static final Logger loggerApplication = LoggerFactory.getLogger(SpringBootServletInitializer.class);

    //For Springboot run
    public static void main(String[] args) {
        loggerApplication.warn("Application - main - Starting Application...");
        SpringApplication.run(Application.class, args);
    }

    //Display Application Version at startup
    @Autowired
    public BuildProperties buildProperties;

    @PostConstruct
    private void postConstructBuildProperties() {
        loggerApplication.debug("Application - postConstructBuildProperties - Name : {}",buildProperties.getName());
        loggerApplication.debug("Application - postConstructBuildProperties - Version : {}",buildProperties.getVersion());
        loggerApplication.debug("Application - postConstructBuildProperties - Build Time : {}",buildProperties.getTime());
        loggerApplication.warn("Application - Starting Application '{}' v{} ({})",buildProperties.getName(),buildProperties.getVersion(),buildProperties.getTime());
    }
    
    //For Tomcat run
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        loggerApplication.warn("Application - configure - Configure Application ");
        return application.sources(Application.class);
    }
    
    //For Swagger run
    @Configuration
    @EnableSwagger2
    //@ComponentScan("com.pokebible")
    @Import({springfox.documentation.spring.data.rest.configuration.SpringDataRestConfiguration.class})
    public class SwaggerConfig {                                    

        ApiInfo apiInfo() {
            loggerApplication.warn("Application - SwaggerConfig - Starting Api Swagger UI...");
            return new ApiInfoBuilder()
                .title("PokeBible API")
                .description("Access to PokeBible database with REST API")
                .license("Apache 2.0")
                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
                .termsOfServiceUrl("")
                .version("1.0.0")
                .contact(new Contact("", "", "fizou@yahoo.com"))
                .build();
        }

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
              .apiInfo(apiInfo())
            ;
        }
        
    }

/*
@Bean
public LocaleResolver localeResolver() {
    loggerApplication.warn("Application - localeResolver - localeResolver...");
    SessionLocaleResolver slr = new SessionLocaleResolver();
    slr.setDefaultLocale(Locale.FRENCH);
    return slr;
}

@Bean
    public MessageSource messageSource() {
        final ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:messages");
        messageSource.setUseCodeAsDefaultMessage(true);
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheSeconds(5);
        return messageSource;
    }    
    
   @Bean
   public LocaleResolver localeResolver(){
        SessionLocaleResolver localeResolver = new SessionLocaleResolver();
        localeResolver.setDefaultLocale(Locale.US);
        return  localeResolver;
    }
*/
    
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
