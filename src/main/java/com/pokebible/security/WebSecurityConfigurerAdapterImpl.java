package com.pokebible.security;

import com.pokebible.restapi.FilterRestApiAuthentication;
import com.pokebible.restapi.FilterGenerateToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/*
 * Manage Security with ROLE to authorise the page/ressource access of the application
 *  
 *  Docs
 *    http://docs.spring.io/spring-boot/docs/current/reference/html/howto-security.html
 *    Switch off the Spring Boot security configuration
 * 
 */

@Configuration
@EnableWebSecurity
public class WebSecurityConfigurerAdapterImpl extends WebSecurityConfigurerAdapter {
    
    private static final Logger logger = LoggerFactory.getLogger(WebSecurityConfigurerAdapterImpl.class);

    private static final String ROLE_ADMIN = "ROLE_ADMIN";
    private static final String ROLE_USER = "ROLE_USER";

    @Autowired
    private AccessDeniedHandlerImpl accessDeniedHandlerImpl;
    @Autowired
    private BasicAuthenticationEntryPointImpl basicAuthenticationEntryPointImpl;

    @Autowired
    AuthenticationProviderImpl authenticationProviderImpl;
    
    private static final String[] WHITELIST_ANONYMOUS = {

            // -- For developpement only: Authorize everything
            //"/**/*",
        
            // -- For production: Special authorisation as anonymousUser (Authorize static ressource, help and test pages, REST API and the swagger interface)  
            //"/resources/**", "/static/**",
            "/css/**", "/js/**", "/images/**", "/page/**", "/webfonts/**", // Need only when running in TOMCAT. static ressource are authorized by default in jar mode
            "/help", "/test", "/monitoring",
            // -- Rest API - Target NOT accessible by anonymous. Only token generation is accessible for every one
            //"/api/**",
            "/api/auth/generateToken",
            // -- Swagger ui - Target accessible by anonymous
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/v2/api-docs",
            // -- Database H2 console - Target NOT accessible by anonymous. Only Admin have access.
            "/h2-console/**",
            // -- Actuator : health / metric - Target only HealthCheck is accessible by anonymous. Only Admin have access to the metric.
            "/actuator/health",
            //"/actuator/**",
    }; 

    private static final String[] WHITELIST_USER = {

            // -- Rest API - Target Read API are accessible by user role  
            //"/api/**",
    }; 

    private static final String[] WHITELIST_ADMIN = {

            // -- Rest API - Target Read AND Write API are accessible by admin role  
            //"/api/**",
            // -- Database H2 Console : Reserved to admin
            "/h2-console/**",
            // -- -- Actuator : All Data (except healthcheck) is reserved to admin
            "/actuator/**",
    }; 
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {

	logger.info("Starting Security (HttpSecurity Configuration) ...");

        http
                .headers().disable() // if enable, it adds lot of high security parameter in header of response included X-Frame-Options: DENY ! 
//                .headers().frameOptions().disable() // Deprecate :(
//                .cors().disable()    // put at disabled here (Can be overide in SimpleCORSFilter Class) 
                .cors().and()
                .csrf().disable()    // put at disabled to let H2-Console working (Almost it...) 
                .authorizeRequests()
                .antMatchers(WHITELIST_ANONYMOUS).permitAll() // these ressources are accessible by everyone (logged or not) 
                .antMatchers(WHITELIST_USER).hasAuthority(ROLE_USER) // these ressources are accessible only by people logged as User Role
                .antMatchers(WHITELIST_ADMIN).hasAuthority(ROLE_ADMIN) // these ressources are accessible only by people logged as Admin Role
                .antMatchers(HttpMethod.GET, "/api/**").hasAnyRole("USER", "ADMIN")  // Secure Default API with Role
                //.antMatchers(HttpMethod.GET, "/api/**").hasAuthority(ROLE_ADMIN)  
                .antMatchers(HttpMethod.PATCH, "/api/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/api/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/**").hasRole("ADMIN")
                
                //.antMatchers("/home").hasAnyRole(USER_ROLE, ADMIN_ROLE)
                //.antMatchers("/api/generateToken").permitAll() // this ressource is accessible by everyone To let them log themseft via rest API ;)

                .anyRequest().authenticated() // other resources need to be logged as USER or ADMIN
                .and()
                .formLogin().loginPage("/login").permitAll() // this ressource is accessible by everyone to let them log themself via the login form
                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET")).permitAll() // this ressource is accessible by everyone. AntPathRequestMatcher needed if csrf is enable to let logout button working
                .and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandlerImpl) // In case of an authenticated user try to call protected ressource go to this class 
                .and()
                .exceptionHandling().authenticationEntryPoint(basicAuthenticationEntryPointImpl) // In case of an unauthenticated user try to call protected ressource go to this class 
                .and()
                // Rest API Authentification
                .addFilterBefore(new FilterGenerateToken("/api/auth/generateToken", authenticationManager()), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new FilterRestApiAuthentication(), UsernamePasswordAuthenticationFilter.class)
                ;        
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
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
        
	logger.info("Starting Security (AuthenticationManager Configuration) ...");
        
        // Authentification HardCoded
        //auth.inMemoryAuthentication()
        //        .withUser("user").password("{noop}password").roles(USER_ROLE)
        //        .and()
        //        .withUser("admin").password("{noop}password").roles(ADMIN_ROLE);
        
        // Authentification Custom (same as inMemoryAuthentication but manual and customizable)
        auth.authenticationProvider(authenticationProviderImpl);

    }
    
}
