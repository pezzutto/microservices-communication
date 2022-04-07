package br.com.zoot.backend.productapi.interceptor;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import br.com.zoot.backend.productapi.exception.AuthenticationException;
import feign.RequestInterceptor;
import feign.RequestTemplate;


// Este interceptor obterá o token da requisição e embutirá nas chamadas do clients do FEIGN
public class FeignClientAuthInterceptor implements RequestInterceptor {

	@Override
	public void apply(RequestTemplate template) {
		
		var currentReq = getCurrentRequest();
		
		template.header("Authorization", currentReq.getHeader("Authorization"));
	}
		
	private HttpServletRequest getCurrentRequest() {
		try {
			
			ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			return attrs.getRequest();
			
		}
		catch(Exception e) {
			e.printStackTrace();
			throw new AuthenticationException("Error getting current request");
		}
	}
}
