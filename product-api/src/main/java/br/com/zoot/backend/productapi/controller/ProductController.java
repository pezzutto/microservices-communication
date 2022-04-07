package br.com.zoot.backend.productapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.zoot.backend.productapi.model.dto.ProductCheckStockRequest;
import br.com.zoot.backend.productapi.model.dto.ProductRequest;
import br.com.zoot.backend.productapi.model.dto.ProductResponse;
import br.com.zoot.backend.productapi.model.dto.ProductSalesResponse;
import br.com.zoot.backend.productapi.model.dto.SuccessResponse;
import br.com.zoot.backend.productapi.service.ProductService;

@RestController
@RequestMapping("/api/product")
public class ProductController {

	@Autowired
	private ProductService service;
	
	@PostMapping
	public ProductResponse save(@RequestBody ProductRequest request) {
		return service.save(request);
	}
	
	@GetMapping
	public List<ProductResponse> findAll(){
		return service.findAll();
	}
	
	@GetMapping("/{id}")
	public ProductResponse findById(@PathVariable Integer id) {
		return ProductResponse.of(service.findById(id));
	}
	
	@GetMapping("/name/{name}")
	public List<ProductResponse> findByContainingName(@PathVariable String name){
		return service.findByContainingName(name);
	}
	
	@GetMapping("/category/{categoryId}")
	public List<ProductResponse> findByCategory(@PathVariable Integer categoryId){
		return service.findByCategory(categoryId);
	}
	
	@GetMapping("/supplier/{supplierId}")
	public List<ProductResponse> findBySupplier(@PathVariable Integer supplierId){
		return service.findBySupplier(supplierId);
	}

	@PutMapping("/{id}")
	public ProductResponse update(@PathVariable Integer id, @RequestBody ProductRequest request) {
		return service.update(id, request);
	}
		
	@DeleteMapping("/{id}")
	public SuccessResponse delete(@PathVariable Integer id) {
		return service.delete(id);
	}
	
	@GetMapping("{id}/sales")
	public ProductSalesResponse findProductSales(@PathVariable Integer id) {
		return service.findProductSales(id);
	}

	@PostMapping("check-stock")
	public SuccessResponse checkProductStock(@RequestBody ProductCheckStockRequest req) {
		return service.checkProductStock(req);
	}

}
