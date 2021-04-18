package com.pokebible;

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

@Configuration
// http://docs.spring.io/spring-boot/docs/current/reference/html/howto-security.html
// Switch off the Spring Boot security configuration
@EnableWebSecurity
public class Security extends WebSecurityConfigurerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(Security.class);

    private static final String ADMIN_ROLE = "ADMIN";
    private static final String USER_ROLE = "USER";

    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    @Autowired
    SecurityAuthenticationProvider customAuthProvider;
    
    private static final String[] AUTH_WHITELIST = {

            // -- For developpement only : Remove when put in production (authorize everything)
            //"/**/*",
            // -- special authorisation
            "/page/**", "/help", "/api/**",
            // -- swagger ui
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/v2/api-docs",
            "/webjars/**"
    };
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {

	logger.debug("Security - configure");

        http
                .headers().disable()
                .cors().disable()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(AUTH_WHITELIST).permitAll()
                .antMatchers("/home").hasAnyRole(USER_ROLE,ADMIN_ROLE)
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                .logout()
                .permitAll()
                .and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler);
    }


    // roles admin allow to access /admin/**
    // roles user allow to access /user/**
    // custom 403 access denied handler

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

	logger.debug("Security - configureGlobal");
        
/*
        // Authentification HardCoded
        auth.inMemoryAuthentication()
                .withUser("user").password("password").roles(USER_ROLE)
                .and()
                .withUser("admin").password("password").roles(ADMIN_ROLE)
		.and()
		.withUser("sadmin").password("6bda7eq").roles(ADMIN_ROLE);
*/
        // Authentification Custom
        auth.authenticationProvider(customAuthProvider);

    }

    
    //Spring Boot configured this already... Only for tomcat run !
    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
    }

}
