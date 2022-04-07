package br.com.zoot.backend.productapi.service;

import static org.springframework.util.ObjectUtils.isEmpty;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.zoot.backend.productapi.controller.client.SalesClient;
import br.com.zoot.backend.productapi.enums.SalesStatus;
import br.com.zoot.backend.productapi.exception.ValidationException;
import br.com.zoot.backend.productapi.model.Category;
import br.com.zoot.backend.productapi.model.Product;
import br.com.zoot.backend.productapi.model.Supplier;
import br.com.zoot.backend.productapi.model.dto.ProductCheckStockRequest;
import br.com.zoot.backend.productapi.model.dto.ProductQuantityDTO;
import br.com.zoot.backend.productapi.model.dto.ProductRequest;
import br.com.zoot.backend.productapi.model.dto.ProductResponse;
import br.com.zoot.backend.productapi.model.dto.ProductSalesResponse;
import br.com.zoot.backend.productapi.model.dto.ProductStockDTO;
import br.com.zoot.backend.productapi.model.dto.SalesConfirmationDTO;
import br.com.zoot.backend.productapi.model.dto.SuccessResponse;
import br.com.zoot.backend.productapi.rabbitmq.SalesConfirmationSender;
import br.com.zoot.backend.productapi.repository.CategoryRepository;
import br.com.zoot.backend.productapi.repository.ProductRepository;
import br.com.zoot.backend.productapi.repository.SupplierRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProductService {
	
	@Autowired
	private ProductRepository repo;
	
	@Autowired
	private CategoryRepository categoryRepo;
	
	@Autowired
	private SupplierRepository supplierRepo;
	
	@Autowired
	private SalesConfirmationSender salesConfirmationSender;
	
	@Autowired
	private SalesClient salesClient;
	
	public List<ProductResponse> findAll() {
	
		return repo.findAll()
				.stream()
				.map(p -> ProductResponse.of(p))
				.collect(Collectors.toList());

	}
	
	public Product findById(Integer id) {
		
		if(isEmpty(id))
			throw new ValidationException("Product Id cannot be null");
		
		return repo.findById(id).orElseThrow(() -> new ValidationException("Product not found"));
	}

	public List<ProductResponse> findByContainingName(String name) {
		
		return repo.findByNameIgnoreCaseContaining(name)
				.stream()
				.map(p -> ProductResponse.of(p))
				.collect(Collectors.toList());
		
	}
	
	public List<ProductResponse> findByCategory(Integer categoryId) {
		
		if(isEmpty(categoryId))
			throw new ValidationException("CategoryId cannot be null");

		return repo.findByCategoryId(categoryId)
				.stream()
				.map(p -> ProductResponse.of(p))
				.collect(Collectors.toList());
	}

	public List<ProductResponse> findBySupplier(Integer supplierId) {
		
		if(isEmpty(supplierId))
			throw new ValidationException("SupplierId cannot be null");

		return repo.findBySupplierId(supplierId)
				.stream()
				.map(p -> ProductResponse.of(p))
				.collect(Collectors.toList());

	}
	
	public ProductResponse save(ProductRequest request) {
		
		validateProduct(request);
		
		Category category = categoryRepo.findById(request.getCategoryId()).orElseThrow(() -> new ValidationException("Category not found"));
		Supplier supplier = supplierRepo.findById(request.getSupplierId()).orElseThrow(() -> new ValidationException("Supplier not found"));
		
		var product = repo.save(Product.of(request, category, supplier));
		
		return ProductResponse.of(product);
	}
	
	public ProductResponse update(Integer id, ProductRequest request) {
		
		if(isEmpty(id))
			throw new ValidationException("SupplierId cannot be null");
		
		validateProduct(request);
		
		Product product = repo.findById(id).orElseThrow(() -> new ValidationException("Product not found"));
		
		Category category = categoryRepo.findById(request.getCategoryId()).orElseThrow(() -> new ValidationException("Category not found"));
		Supplier supplier = supplierRepo.findById(request.getSupplierId()).orElseThrow(() -> new ValidationException("Supplier not found"));
		
		
		product.setName(request.getName());
		product.setCategory(category);
		product.setSupplier(supplier);
		product.setAvailableQty(request.getAvailableQty());
		
		return ProductResponse.of(repo.save(product));
		
	}
	
	public SuccessResponse delete(Integer id) {
	
		if(isEmpty(id))
			throw new ValidationException("Product Id cannot be null");
		
		Product product = repo.findById(id).orElseThrow(() -> new ValidationException("Product not found"));
		
		repo.delete(product);
		return SuccessResponse.create("Product was deleted");
	}
	
	public Boolean existsByCategoryId(Integer id) {
		return repo.existsByCategoryId(id);
	}
	
	public Boolean existsBySupplierId(Integer id) {
		return repo.existsBySupplierId(id);
	}
	
	@Transactional
	public void updateProductStock(ProductStockDTO productStockDto) {
		
		try {
		
			List<Product> prodElected = new ArrayList<Product>();
			
			// Valida objeto recebido
			String message = validateStockUpdate(productStockDto);
			if (message != null)
				throw new ValidationException(message);
			
			// Tenta atualizar
			productStockDto.getProducts().forEach(prod -> {
				
				var product = repo.findById(prod.getProductId()).orElse(null);
				
				if(product.getAvailableQty() == 0)
					throw new ValidationException(String.format("Product %s is out of stock", product.getName()));
				
				if(product.getAvailableQty() < prod.getQuantity())
					throw new ValidationException(String.format("Insufficient stock for product %s", product.getName()));
				
				product.updateStock(prod.getQuantity());
				
				prodElected.add(product);
				
			});
			
			if(prodElected.size() > 0) {

				repo.saveAll(prodElected);
				
				var approvedMessage = new SalesConfirmationDTO(productStockDto.getSalesId(), SalesStatus.APPROVED);
				salesConfirmationSender.sendSalesConfirmationMessage(approvedMessage);
			}
		}
		catch(Exception e) {
			log.error("Error updating stock: {}", e.getMessage(), e);
			var rejected = new SalesConfirmationDTO(productStockDto.getSalesId(), SalesStatus.REJECTED);
			salesConfirmationSender.sendSalesConfirmationMessage(rejected);
		}
	}
	
	
	public ProductSalesResponse findProductSales(Integer id) {
	
		// Mais pra validar se o product existe
		var product = findById(id);
		
		try {
			
			var sales = salesClient
					.findSalesByProductId(id)
					.orElseThrow(() -> new ValidationException("Sales not found by this product"));
			
			return ProductSalesResponse.of(product, sales.getSalesId());
		}
		catch(Exception e) {
			throw new ValidationException("Error getting product's sales");
		}
		
	}
		
	public SuccessResponse checkProductStock(ProductCheckStockRequest req) {
		
		if(isEmpty(req) || isEmpty(req.getProducts()))
			throw new ValidationException("Products must be informed.");
		
		for(ProductQuantityDTO prod : req.getProducts()) {
			
			Product stProd = findById(prod.getProductId());
			
			if(prod.getQuantity() > stProd.getAvailableQty())
				throw new ValidationException(String.format("Insufficient stock for product %d", stProd.getId()));
			
		}
		
		return SuccessResponse.create("All products has stock");
	}
	
	private void validateProduct(ProductRequest request) {
		
		if (isEmpty(request.getName()))
			throw new ValidationException("Product name cannot be null");
		
		if (isEmpty(request.getAvailableQty()) || request.getAvailableQty() <= 0)
			throw new ValidationException("Available quantity must greather than zero");
				
		if (isEmpty(request.getCategoryId()))
			throw new ValidationException("Category id cannot be null");
		
		if (isEmpty(request.getSupplierId()))
			throw new ValidationException("Supplier id cannot be null");

	}

	private String validateStockUpdate(ProductStockDTO dto) {
		
		if(isEmpty(dto))
			return "Product data cannot be null";

		if(isEmpty(dto.getSalesId()))
			return "SalesId cannot be null";
		
		if(isEmpty(dto.getProducts()))
			return "Sales products cannot be null";
		
		for(ProductQuantityDTO prod : dto.getProducts()) {
			
			if(isEmpty(prod.getProductId())) {
				return "ProductId cannot be null";
			}
			if(isEmpty(prod.getQuantity()) || prod.getQuantity() < 0) {
				return "Product quantity cannot be null or less than zero";
			}
		}
		
		return null;
	
	}
}
