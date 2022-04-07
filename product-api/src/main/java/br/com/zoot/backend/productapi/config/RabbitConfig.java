package br.com.zoot.backend.productapi.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

	@Value("${app-config.rabbit.exchange.product}")
	private String productTopicExchange;
	
	@Value("${app-config.rabbit.routingKey.product-stock}")
	private String productStockRoutingKey;
	
	@Value("${app-config.rabbit.routingKey.sales-confirmation}")
	private String salesConfirmationRoutingKey;
	
	@Value("${app-config.rabbit.queue.product-stock}")
	private String productStockMQ;
	
	@Value("${app-config.rabbit.queue.sales-confirmation}")
	private String salesConfirmationMQ;
	
	
	@Bean
	public TopicExchange productTopicExchange() {
		return new TopicExchange(productTopicExchange);
	}
	
	@Bean
	public Queue productStockQueue() {
		return new Queue(productStockMQ, true);
	}
	
	@Bean
	public Queue salesConfirmationQueue() {
		return new Queue(salesConfirmationMQ);
	}
	
	@Bean
	public Binding productStockQueueBinding(TopicExchange topicExchange) {
		return BindingBuilder
				.bind(productStockQueue())
				.to(topicExchange)
				.with(productStockRoutingKey);
	}
	
	@Bean
	public Binding salesConfirmationQueueBinding(TopicExchange topicExchange) {
		return BindingBuilder
				.bind(salesConfirmationQueue())
				.to(topicExchange)
				.with(salesConfirmationRoutingKey);
	}
	
	@Bean
	public MessageConverter jsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}
	
}
