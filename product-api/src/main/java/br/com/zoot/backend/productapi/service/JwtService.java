package br.com.zoot.backend.productapi.service;

import static org.springframework.util.ObjectUtils.isEmpty;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.com.zoot.backend.productapi.exception.AuthenticationException;
import br.com.zoot.backend.productapi.model.dto.JwtResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    
    private static final String BEARER = "bearer";

    @Value("${app-config.secrets.api-secret}")
    private String secret;

    public void isAuthorized(String token){

    	var accessToken = getToken(token);
    	
        try {
            var claims = Jwts.parserBuilder()
                             .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
                             .build()
                             .parseClaimsJws(accessToken)
                             .getBody();
            
            var user = JwtResponse.getUser(claims);
            if (isEmpty(user) || isEmpty(user.getId()))
            	throw new AuthenticationException("Invalid user");
        }
        catch(Exception e){
            e.printStackTrace();
            throw new AuthenticationException("Error processing token");
        }
    }

    private String getToken(String token){

        if(isEmpty(token))
            throw new AuthenticationException("Access token is missing");

        if (token.toLowerCase().contains(BEARER)){
            return token.replace(BEARER, Strings.EMPTY);
        }
        else{
            return token;
        }
    }
}
