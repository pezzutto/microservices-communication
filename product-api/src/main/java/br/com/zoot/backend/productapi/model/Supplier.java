package br.com.zoot.backend.productapi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.beans.BeanUtils;

import br.com.zoot.backend.productapi.model.dto.SupplierRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="SUPPLIER")
public class Supplier {
	
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "supplier_generator" )
    @SequenceGenerator(
    	name="supplier_generator", 
    	sequenceName = "supplier_seq",  
    	initialValue = 1, 
    	allocationSize = 1
    )
	private Integer id;
	
	@Column(name="name", nullable=false)
	private String name;
	
	public static Supplier of(SupplierRequest request) {
		var supplier = new Supplier();
		BeanUtils.copyProperties(request, supplier);
		return supplier;
	}
}
