package br.com.zoot.backend.productapi.rabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.zoot.backend.productapi.model.dto.SalesConfirmationDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SalesConfirmationSender {

	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@Value("${app-config.rabbit.exchange.product}")
	private String productTopicExchange;
	
	@Value("${app-config.rabbit.routingKey.sales-confirmation}")
	private String salesConfirmationRoutingKey;
	
	public void sendSalesConfirmationMessage(SalesConfirmationDTO dto) {
		
		try {
			
			log.info("Sending message: {}", new ObjectMapper().writeValueAsString(dto));
			
			rabbitTemplate.convertAndSend(productTopicExchange, salesConfirmationRoutingKey, dto);
			
			log.info("Message sent.");
			
		}
		catch(Exception e) {
			log.error("Error sending message: {}", e.getMessage());
		}
		
	}
	
}
