package br.com.zoot.backend.productapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.zoot.backend.productapi.model.Supplier;

public interface SupplierRepository extends JpaRepository<Supplier, Integer> {

	Supplier findByNameIgnoreCase(String name);
}
