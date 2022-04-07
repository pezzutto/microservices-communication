package br.com.zoot.backend.productapi.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.zoot.backend.productapi.model.dto.ProductStockDTO;
import br.com.zoot.backend.productapi.service.ProductService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ProductStockListener {

	@Autowired
	private ProductService prodService;
	
	@RabbitListener(queues = "${app-config.rabbit.queue.product-stock}")
	public void receiveProductStockMessage(ProductStockDTO productStockDto) throws JsonProcessingException {
		log.info("Mensagem recebida: {}", new ObjectMapper().writeValueAsString(productStockDto));
		prodService.updateProductStock(productStockDto);
	}
}
