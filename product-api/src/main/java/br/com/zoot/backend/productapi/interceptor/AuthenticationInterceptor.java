package br.com.zoot.backend.productapi.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import br.com.zoot.backend.productapi.service.JwtService;
import feign.Request.HttpMethod;

public class AuthenticationInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtService jwtService;

    @Override
    public boolean preHandle(HttpServletRequest request, 
                             HttpServletResponse response, 
                             Object handler) throws Exception {
        
        if (HttpMethod.OPTIONS.name().equals(request.getMethod()))
            return true;

        var auth = request.getHeader("Authorization");

        // Se não tiver autorizado, manda AuthenticationExeption
        jwtService.isAuthorized(auth);
        return true;
    }

}
