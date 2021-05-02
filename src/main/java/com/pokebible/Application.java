package com.pokebible;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
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
// Application: Main Class of SpringBoot - Everything Start Here ;)
//
// Notes for Tomcat run instead spring boot jar launch :
// 1) put <packaging>war</packaging> in pom.xml 
// 2) Uncomment following dependency in pom.xml
// <dependency>
//  <groupId>org.springframework.boot</groupId>
//  <artifactId>spring-boot-starter-tomcat</artifactId>
//  <scope>provided</scope>
// </dependency>
// 3) Uncomment this code
//  @SpringBootApplication
//  public class Application extends SpringBootServletInitializer {
// 4) Comment this code
//  @SpringBootApplication
//  public class Application {
// 5) To Run in war mode on Tomcat in NETBEANS 
// 5.1 Create a Tomcat Server
// 5.2 Select Tomcat Server in Run Menu
// 5.3 Indicate /pokebible in Context Path
// 5.4 Restart Netbeans interface
// 6) To reRun in jar without in NETBEANS 
// 6.1 Undo the Change (You can let the POM dependency)
// 6.2 Restart Netbeans interface
    
@SpringBootApplication
public class Application extends SpringBootServletInitializer {

     private static final Logger loggerApplication = LoggerFactory.getLogger(Application.class);

    //For Springboot run
    public static void main(String[] args) {
        loggerApplication.info("main - Starting Application...");
        SpringApplication.run(Application.class, args);
    }

    //Display message and buildProperties version at startup
    @Autowired
    public BuildProperties buildProperties;

    @PostConstruct
    private void postConstructBuildProperties() {
        loggerApplication.debug("Name : {}",buildProperties.getName());
        loggerApplication.debug("Version : {}",buildProperties.getVersion());
        loggerApplication.debug("Build Time : {}",buildProperties.getTime());
        
        loggerApplication.info("Starting Application '{}' v{} ({})",buildProperties.getName(), buildProperties.getVersion(), buildProperties.getTime());
    }
    
    // Activate Swagger Interface on http://server/swagger-ui/
    @Configuration
    @EnableSwagger2WebMvc
    @Import(SpringDataRestConfiguration.class)
    public class SwaggerConfig {                                    

        ApiInfo apiInfo() {
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
            loggerApplication.info("Starting Swagger UI");
            
            //return new Docket(DocumentationType.SWAGGER_2)
            //  .select()
            //  .apis(RequestHandlerSelectors.any())
            //  .paths(PathSelectors.any())
            //  .build();

            return new Docket(DocumentationType.SWAGGER_2)  
              .select()                                  
              .apis(RequestHandlerSelectors.any())              
            //  .apis(RequestHandlerSelectors.basePackage("com.pokebible"))
            //  .paths(PathSelectors.regex("/pokebible/api.*"))
              .paths(PathSelectors.regex("/api.*"))
            //  .paths(PathSelectors.any())
              .build() 
            //  .pathMapping("/pokebible")
              .apiInfo(apiInfo())
            ;
        }
        
    }

}
