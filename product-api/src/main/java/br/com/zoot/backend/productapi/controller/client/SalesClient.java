package br.com.zoot.backend.productapi.controller.client;

import java.util.Optional;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import br.com.zoot.backend.productapi.model.dto.SalesProductResponse;

@FeignClient(
	name = "salesClient", 
	contextId = "salesClient",
	url = "${app-config.services.sales}"
)
public interface SalesClient {

	@GetMapping("products/{id}")
	Optional<SalesProductResponse> findSalesByProductId(@PathVariable("id") Integer id);
	
}
