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

import br.com.zoot.backend.productapi.model.Supplier;
import br.com.zoot.backend.productapi.model.dto.SuccessResponse;
import br.com.zoot.backend.productapi.model.dto.SupplierRequest;
import br.com.zoot.backend.productapi.model.dto.SupplierResponse;
import br.com.zoot.backend.productapi.service.SupplierService;

@RestController
@RequestMapping("/api/supplier")
public class SupplierController {

	@Autowired
	private SupplierService service;
	
	@PostMapping
	public SupplierResponse save(@RequestBody SupplierRequest request) {
		return service.save(request);
	}

	@GetMapping
	public List<SupplierResponse> findAll() {
		return service.findAll();
	}

	@GetMapping("/{id}")
	public Supplier findById(@PathVariable Integer id) {
		return service.findById(id);
	}
	
	@GetMapping("/name/{name}")
	public SupplierResponse findByName(@PathVariable String name) {
		return service.findByName(name);
	}

	@PutMapping("/{id}")
	public SupplierResponse update(@PathVariable Integer id, @RequestBody SupplierRequest request) {
		return service.update(id, request);
	}

	@DeleteMapping("/{id}")
	public SuccessResponse delete(@PathVariable Integer id) {
		return service.delete(id);
	}
}
