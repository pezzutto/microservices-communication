package br.com.zoot.backend.productapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.zoot.backend.productapi.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

	Category findByNameIgnoreCase(String name); 
}
