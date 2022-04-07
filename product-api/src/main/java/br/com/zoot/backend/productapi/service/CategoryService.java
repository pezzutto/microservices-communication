package br.com.zoot.backend.productapi.service;

import static org.springframework.util.ObjectUtils.isEmpty;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.zoot.backend.productapi.exception.ValidationException;
import br.com.zoot.backend.productapi.model.Category;
import br.com.zoot.backend.productapi.model.dto.CategoryRequest;
import br.com.zoot.backend.productapi.model.dto.CategoryResponse;
import br.com.zoot.backend.productapi.model.dto.SuccessResponse;
import br.com.zoot.backend.productapi.repository.CategoryRepository;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository repo;
	
	@Autowired
	private ProductService prodService;
	
	public List<CategoryResponse> findAll(){
		
		return repo.findAll()
				.stream()
				.map(c -> CategoryResponse.of(c))
				.collect(Collectors.toList());
		
	}
	
	public CategoryResponse findById(Integer id) {
	
		if (isEmpty(id))
			throw new ValidationException("Id cannot be null");
		
		var category = repo
				.findById(id)
				.orElseThrow(() -> new ValidationException("Category not found"));
		
		return CategoryResponse.of(category);
	}

	public CategoryResponse findByName(String name) {

		if (isEmpty(name))
			throw new ValidationException("Name cannot be null");

		var category = repo.findByNameIgnoreCase(name);
		if (isEmpty(category))
			throw new ValidationException("Category not found");
		
		return CategoryResponse.of(category);
	}
	
	public CategoryResponse save(CategoryRequest req) {
		
		validateCategoryNameInformed(req);
		
		var category = repo.save(Category.of(req));
		return  CategoryResponse.of(category);

	}
	
	public CategoryResponse update(Integer id, CategoryRequest req) {
		
		if (isEmpty(id))
			throw new ValidationException("Category Id cannot be null");
		
		validateCategoryNameInformed(req);
		
		var category = repo
				.findById(id)
				.orElseThrow(() -> new ValidationException("Category not found"));
		
		category.setName(req.getName());
		
		return  CategoryResponse.of(repo.save(category));
	}
	
	public SuccessResponse delete(Integer id) {
	
		if (isEmpty(id))
			throw new ValidationException("Id cannot be null");
		
		if (prodService.existsByCategoryId(id))
			throw new ValidationException("There are products with this category");
		
		repo.deleteById(id);

		return SuccessResponse.create("Category was deleted");
	}
	
	private void validateCategoryNameInformed(CategoryRequest req) {
		if (isEmpty(req.getName())) {
			throw new ValidationException("Category name cannot be null");
		}
	}
	
	
	
}


