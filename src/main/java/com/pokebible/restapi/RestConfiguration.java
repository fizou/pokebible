package com.pokebible.restapi;

import com.pokebible.Pokemon;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.service.ApiKey;
//import springfox.documentation.service.AuthorizationScope;
//import springfox.documentation.service.Contact;
//import springfox.documentation.service.SecurityReference;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spi.service.contexts.SecurityContext;
//import springfox.documentation.spring.data.rest.configuration.SpringDataRestConfiguration;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 *
 * Configuration of REST API and Swagger-ui
 *
 */
@Configuration
//@OpenAPIDefinition(info = @Info(title = "PokeBible API", version = "1.0.0", description = "Access to PokeBible referential with REST API"))
//@SecurityScheme(name = "JWTToken", scheme = "bearer", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
public class RestConfiguration implements RepositoryRestConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(RestConfiguration.class);

    @Autowired
    private Environment env;


    // Activate Id in REST API response 
    @Override
    //public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {

        logger.info("Starting REST API on "+env.getProperty("spring.data.rest.base-path")+"/api ...");

        config.exposeIdsFor(Pokemon.class);
    }

/*    
    // Activate Swagger Interface on http://server/swagger-ui/#/Pokemon%20Entity
    // Activate json Api doc on http://server/v2/api-docs
    @Configuration
    @EnableSwagger2WebMvc
    @Import(SpringDataRestConfiguration.class)
    public class SwaggerConfig {                                    

        @Bean
        public Docket api() { 
            
            logger.info("Starting Swagger UI...");

            return new Docket(DocumentationType.SWAGGER_2)  
                .securityContexts(Arrays.asList(securityContext()))
                .securitySchemes(Arrays.asList(apiKey()))
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.regex(env.getProperty("spring.data.rest.base-path")+"/api.*"))
                //.paths(PathSelectors.regex("(?!"+env.getProperty("spring.data.rest.base-path")+"/api/profile).+")) // Eliminate /api/profile api
                .build() 
                .apiInfo(apiInfo())
            ;
        }

        // Global description of API
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

        // Activate JWT Protection for Swagger
        private ApiKey apiKey() { 
            return new ApiKey("JWT Token", "Authorization", "header"); 
        }
        
        private SecurityContext securityContext() { 
            return SecurityContext.builder().securityReferences(defaultAuth()).build(); 
        } 

        private List<SecurityReference> defaultAuth() { 
            
            return Arrays.asList(new SecurityReference("JWT Token", 
                new AuthorizationScope[]{
                   //new AuthorizationScope("global", "accessEverything")
                }
            )); 
        }

    }
*/

    @Bean
    // Activate Swagger Interface on http://server/swagger-ui/index.html
    // Activate json Api doc on http://server/v3/api-docs
    public OpenAPI customOpenAPI() {
      final String securitySchemeName = "JWT Authentification";
      return new OpenAPI()
          .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
          .components(
              new Components()
                  .addSecuritySchemes(securitySchemeName,
                      new SecurityScheme()
                          .name(securitySchemeName)
                          .type(SecurityScheme.Type.HTTP)
                          .scheme("bearer")
                          .bearerFormat("JWT")
                  )
          )
          .info(
              new Info().title("PokeBible API")
              .description("Access to PokeBible referential with REST API")
              .version("v1.0.0")
          );
        }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public")
                .pathsToMatch(env.getProperty("spring.data.rest.base-path")+"/api/**")
                .build();
    }        

}


