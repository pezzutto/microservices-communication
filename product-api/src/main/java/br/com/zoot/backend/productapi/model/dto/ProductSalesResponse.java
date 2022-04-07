package br.com.zoot.backend.productapi.model.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.zoot.backend.productapi.model.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductSalesResponse {

	private Integer id;
	private String name;
	private CategoryResponse category;
	private SupplierResponse supplier;
	private Integer availableQty;
	private List<String> sales;
	
	@JsonProperty("createdAt")
	@JsonFormat(pattern="dd/MM/yyyy HH:mm:ss")
	private LocalDateTime createdAt;
	
	public static ProductSalesResponse of(Product product, List<String> sales) {
	
		return ProductSalesResponse
				.builder()
				.id(product.getId())
				.name(product.getName())
				.availableQty(product.getAvailableQty())
				.createdAt(product.getCreatedAt())
				.supplier(SupplierResponse.of(product.getSupplier()))
				.category(CategoryResponse.of(product.getCategory()))
				.sales(sales)
				.build();
	}
	
}
