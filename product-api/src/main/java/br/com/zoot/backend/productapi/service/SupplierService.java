package br.com.zoot.backend.productapi.service;

import static org.springframework.util.ObjectUtils.isEmpty;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.zoot.backend.productapi.exception.ValidationException;
import br.com.zoot.backend.productapi.model.Supplier;
import br.com.zoot.backend.productapi.model.dto.SuccessResponse;
import br.com.zoot.backend.productapi.model.dto.SupplierRequest;
import br.com.zoot.backend.productapi.model.dto.SupplierResponse;
import br.com.zoot.backend.productapi.repository.SupplierRepository;

@Service
public class SupplierService {

	@Autowired
	private SupplierRepository repo;
	
	@Autowired
	private ProductService prodService;
	

	public List<SupplierResponse> findAll() {
		return repo.findAll().stream().map(SupplierResponse::of).collect(Collectors.toList());
	}

	public Supplier findById(Integer id) {
		
		if (isEmpty(id))
			throw new ValidationException("Id cannot be null");
		
		return repo
				.findById(id)
				.orElseThrow(() -> new ValidationException("Supplier not found"));
	}
	
	public SupplierResponse findByName(String name) {

		if (isEmpty(name))
			throw new ValidationException("Name cannot be null");
		
		var supplier = repo.findByNameIgnoreCase(name);
		if (isEmpty(supplier))
			throw new ValidationException("Supplier not found");
		
		return SupplierResponse.of(supplier);
	}
	
	public SupplierResponse save(SupplierRequest request) {

		if (isEmpty(request.getName()))
			throw new ValidationException("Supplier name cannot be null");
	
		var supplier = repo.save(Supplier.of(request));
		return SupplierResponse.of(supplier);
		
	}
	
	public SupplierResponse update(Integer id, SupplierRequest request) {
		
		if (isEmpty(id))
			throw new ValidationException("Id cannot be null");
		
		if (isEmpty(request.getName()))
			throw new ValidationException("Supplier Name cannot be null");
		
		Supplier supplier = repo
				.findById(id)
				.orElseThrow(() -> new ValidationException("Supplier not found"));
		
		supplier.setName(request.getName());
		
		return SupplierResponse.of(repo.save(supplier));
	}
	
	public SuccessResponse delete(Integer id) {
		
		if (isEmpty(id))
			throw new ValidationException("Supplier Id cannot be null");
		
		if(prodService.existsBySupplierId(id))
			throw new ValidationException("There are products with this supplier");
			
		repo.deleteById(id);
		
		return SuccessResponse.create("Supplier was deleted");
		
	}
	
}
