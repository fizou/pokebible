package com.pokebible.restapi;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
//import io.swagger.annotations.ApiModelProperty;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * This is the master Entity of REST API Authentification
 * 
 * - Provide a Credentials object to permit the transmission of username/password in JSON in the body of generateToken controller/filter method
 * - Provide CreateToken / VerifyToken methods access by Filters to put or retreive 'Authorization: Bearer xxxx...' token in Header of HttpRequest/HtttResponse
 * 
 */
@JsonPropertyOrder({"username", "password"})
public class ApiCredentials {
    
    private static final Logger logger = LoggerFactory.getLogger(ApiCredentials.class);

//    @ApiModelProperty(position = 0)
    private String username;
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

//    @ApiModelProperty(position = 1)
    private String password;
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public ApiCredentials(HttpServletRequest request) {
        
        // Try to read param of the request to find username=admin&password=password in GET url or POST body
        this.username = request.getParameter("username");
        this.password = request.getParameter("password");
        
        if (this.username==null&&this.password==null) {
            try {
                // Try to read body of the request to find json credentials with this format{"username": "admin", "password": "password"}
                String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
                if (body==null) body="";

                logger.debug("Body of the request: "+body.replaceAll(System.lineSeparator(),""));

                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNodeRoot = objectMapper.readTree(body);
                this.username=jsonNodeRoot.at("").get("username").asText();                
                this.password=jsonNodeRoot.at("").get("password").asText();

            } catch (Exception e) {
                logger.error("Error on json body parse: "+e);
                //e.printStackTrace();
            }
            
        }

    }    

    static final long EXPIRATIONTIME = 864_000_000; // 10 days
    static final String SECRET = "ThisIsASecret";
     
    static final String HEADER_STRING = "Authorization";
    static final String TOKEN_PREFIX = "Bearer";
    static final String AUTHORITIES_KEY = "scopes";
 
    public static void CreateToken(HttpServletResponse response, Authentication authentication) {

        String username = authentication.getName();
        String authorities = authentication 
            .getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));

        logger.debug("Creating JWT token for user: "+username+" with "+authorities+" Role");

        String token=null;
        try {
            token = Jwts.builder()
                .setSubject(username)
                .claim(AUTHORITIES_KEY, authorities)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
        } catch (Exception e) {
            logger.error("Error while creating token: "+e);
        }
        
        logger.debug("Add JWT token to Response Header: "+token);

        response.addHeader(HEADER_STRING, TOKEN_PREFIX + " " + token);
    }
 
    public static Authentication verifyToken(HttpServletRequest request) {

        String token = request.getHeader(HEADER_STRING);

        logger.debug("Retreive JWT token from request header: "+token);
        
        if (token != null) {

            // Eliminate 'Bearer ' string in case of API user put it in Authorisation Field
            token=token.replaceAll("Bearer ","");

            // Clear Actual Session
            SecurityContextHolder.clearContext();
            
            // Parse the token.
            String user=null;
            Collection authorities=null;
            try {
                
                Claims claims=Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                    .getBody();

                user = claims
                    .getSubject();
                
                authorities=Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
                
            } catch (Exception e) {
                logger.error("Error while parsing token: "+e);
            }
            
            logger.debug("User retreive from token: "+user);
            logger.debug("Role retreive  from token: "+authorities);
 
            return user != null ? new UsernamePasswordAuthenticationToken(user, null, authorities) : null;
        }

        return null;

    }
    
}

