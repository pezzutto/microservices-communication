package br.com.zoot.backend.productapi.model.dto;

import org.springframework.beans.BeanUtils;

import br.com.zoot.backend.productapi.model.Category;
import lombok.Data;

@Data
public class CategoryResponse {
	
	private Integer id;
	private String name;
	
	public static CategoryResponse of(Category category) {
		var response = new CategoryResponse();
		BeanUtils.copyProperties(category, response);
		return response;
	}
}
