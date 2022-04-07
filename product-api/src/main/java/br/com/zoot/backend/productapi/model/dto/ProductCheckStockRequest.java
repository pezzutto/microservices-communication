package br.com.zoot.backend.productapi.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductCheckStockRequest {

	private List<ProductQuantityDTO> products;
	
}
