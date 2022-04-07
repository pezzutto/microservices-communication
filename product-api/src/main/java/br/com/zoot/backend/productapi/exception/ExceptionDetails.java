package br.com.zoot.backend.productapi.exception;

import lombok.Data;

@Data
public class ExceptionDetails {
	
	private int status;
	private String message;
	
}
