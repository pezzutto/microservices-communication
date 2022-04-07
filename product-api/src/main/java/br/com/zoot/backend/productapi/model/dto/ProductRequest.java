package br.com.zoot.backend.productapi.model.dto;

import lombok.Data;

@Data
public class ProductRequest {
	
	private String name;
	private Integer categoryId;
	private Integer supplierId;
	private Integer availableQty;
	
	
	
}
