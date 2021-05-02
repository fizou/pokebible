package com.pokebible.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfigurerAdapterImpl extends WebSecurityConfigurerAdapter {
    
    /*
     * Manage Security with ROLE to authorise the page/ressource access of the application
     *  
     *  Docs
     *    http://docs.spring.io/spring-boot/docs/current/reference/html/howto-security.html
     *    Switch off the Spring Boot security configuration
     * 
     */

    private static final Logger logger = LoggerFactory.getLogger(WebSecurityConfigurerAdapterImpl.class);

    private static final String ADMIN_ROLE = "ADMIN";
    private static final String USER_ROLE = "USER";

    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    @Autowired
    AuthenticationProviderImpl authenticationProviderImpl;
    
    private static final String[] WHITELIST = {

            // -- For developpement only: Authorize everything
            //"/**/*",
        
            // -- For production: Special authorisation as anonymousUser (Authorize static ressource, help and test pages, REST API and the swagger interface)  
            //"/resources/**", "/static/**",
            "/css/**", "/js/**", "/images/**", "/page/**", "/webfonts/**", // Need only when running in TOMCAT. static ressource are authorized by default in jar mode
            "/help", "/test", "/monitoring",
            // -- REST API
            "/api/**",
            // -- Swagger ui
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/v2/api-docs",
            // -- Database H2 console:  No more access to (Push to admin)
            "/h2-console/**",
            // -- Actuator : Only healthcheck is accessible for every one
            //"/actuator/**",
            "/actuator/health",
    }; 

    private static final String[] WHITELIST_ADMIN = {

            // -- Database H2 Console : Reserved to admin
            //"/h2-console/**",
            // -- -- Actuator : All Data (except healthcheck) is reserved to admin
            "/actuator/**",
    }; 
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {

	logger.debug("Security - configure");

        http
                .headers().disable() // if enable, it adds lot of high security parameter in header of response included X-Frame-Options: DENY ! 
//                .headers().frameOptions().disable() // Deprecate :(
                .cors().disable()    // put at disabled here (Can be overide to authorize in SimpleCORSFilter Class)  
                .csrf().disable()    // put at disabled to let H2-Console working (Almost it...) 
                .authorizeRequests()
                .antMatchers(WHITELIST).permitAll()
                .antMatchers(WHITELIST_ADMIN).hasAnyRole(ADMIN_ROLE)
                //.antMatchers("/home").hasAnyRole(USER_ROLE, ADMIN_ROLE)
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login").permitAll()
                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET")).permitAll() // Need if csrf is enable to let logout button working
                .and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler); // In case of error go to this class 
    }

    //Spring Boot autmatically authorise this already... Only for need for war tomcat run !
    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

	// Define ROLE of each user
        
        logger.debug("Security - configureGlobal");
        
        // Authentification HardCoded
        //auth.inMemoryAuthentication()
        //        .withUser("user").password("{noop}password").roles(USER_ROLE)
        //        .and()
        //        .withUser("admin").password("{noop}password").roles(ADMIN_ROLE);
        
        // Authentification Custom (same as inMemoryAuthentication but manual and customizable)
        auth.authenticationProvider(authenticationProviderImpl);

    }
    
}
