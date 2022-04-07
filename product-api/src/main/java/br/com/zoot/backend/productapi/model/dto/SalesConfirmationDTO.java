package br.com.zoot.backend.productapi.model.dto;

import br.com.zoot.backend.productapi.enums.SalesStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalesConfirmationDTO {

	private String salesId;
	private SalesStatus status;
}
