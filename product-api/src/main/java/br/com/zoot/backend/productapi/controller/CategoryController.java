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

import br.com.zoot.backend.productapi.model.dto.CategoryRequest;
import br.com.zoot.backend.productapi.model.dto.CategoryResponse;
import br.com.zoot.backend.productapi.model.dto.SuccessResponse;
import br.com.zoot.backend.productapi.service.CategoryService;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

	@Autowired
	private CategoryService service;
	
	@GetMapping
	public List<CategoryResponse> findAll() {
		return service.findAll();
	}
	
	@GetMapping("/{id}")
	public CategoryResponse findById(@PathVariable Integer id){
		return service.findById(id);
	}
	
	@GetMapping("/name/{name}")
	public CategoryResponse findByName(@PathVariable String name) {
		return service.findByName(name);
	}
	
	@PostMapping
	public CategoryResponse save(@RequestBody CategoryRequest request) {
		return service.save(request);
	}
	
	@PutMapping("/{id}")
	public CategoryResponse update(@PathVariable Integer id, @RequestBody CategoryRequest request) {
		return service.update(id, request);
	}
	
	@DeleteMapping("/{id}")
	public SuccessResponse delete(@PathVariable Integer id) {
		return service.delete(id);
	}
}
