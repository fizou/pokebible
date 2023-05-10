package com.pokebible;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;


/**
 *
 * Application: Main Class of Pokebible SpringBoot Project - Everything Start Here ;) 
 *
 * This project is build by default as a jar with embeded Tomcat
 * 
 * Notes to run it as war inside a real Tomcat :
 *      1) put <packaging>war</packaging> in pom.xml 
 *      2) Uncomment following dependency in pom.xml
 *         <dependency>
 *           <groupId>org.springframework.boot</groupId>
 *           <artifactId>spring-boot-starter-tomcat</artifactId>
 *           <scope>provided</scope>
 *          </dependency>
 *      3) Uncomment this code
 *          @SpringBootApplication
 *          public class Application extends SpringBootServletInitializer {
 *      4) Comment this code
 *          @SpringBootApplication
 *          public class Application {
 *      5) To Run in war mode on Tomcat in NETBEANS 
 *          5.1 Create a Tomcat Server
 *          5.2 Select Tomcat Server in Run Menu
 *          5.3 Indicate /pokebible in Context Path
 *          5.4 Restart Netbeans interface
 *     6) To goback on jar format in NETBEANS 
 *          6.1 Undo the code changes (You can let the POM dependency)
 *          6.2 Restart Netbeans interface
 * 
 */
    
@SpringBootApplication
public class Application extends SpringBootServletInitializer {

     private static final Logger loggerApplication = LoggerFactory.getLogger(Application.class);

    //For Springboot run
    public static void main(String[] args) {
        loggerApplication.info("Starting Application...");
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
    
}
