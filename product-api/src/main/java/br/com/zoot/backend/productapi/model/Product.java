package br.com.zoot.backend.productapi.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.zoot.backend.productapi.model.dto.ProductRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="PRODUCT")
public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_generator")
	@SequenceGenerator(
		name="product_generator", 
		sequenceName = "product_seq", 
		initialValue = 1, 
	    allocationSize = 1
	)
	private Integer id;
	
	@Column(name="name", nullable=false)
	private String name;
	
	@ManyToOne
	@JoinColumn(name="category_id")
	private Category category;
	
	@ManyToOne
	@JoinColumn(name="supplier_id")
	private Supplier supplier;
	
	@Column(name="available_qty", nullable=false)
	private Integer availableQty;

	@Column(name="created_at", nullable=false, updatable=false)
	private LocalDateTime createdAt;
	
	@PrePersist
	public void setCreatedAt() {
		createdAt = LocalDateTime.now();
	}
	
	public void updateStock(Integer quantity) {
		availableQty -= quantity;
	}
	
	public static Product of(ProductRequest request, Category category, Supplier supplier) {
		
		return Product
				.builder()
				.name(request.getName())
				.category(category)
				.supplier(supplier)
				.availableQty(request.getAvailableQty())
				.build();

	}
}
