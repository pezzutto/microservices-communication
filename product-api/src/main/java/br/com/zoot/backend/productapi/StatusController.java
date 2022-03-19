package br.com.zoot.backend.productapi;

import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class StatusController {

	@GetMapping("/status")
	public ResponseEntity<HashMap<String, Object>> getApiStatus() {
		var res = new HashMap<String, Object>();
	
		res.put("status", "up");
		res.put("service", "ProductApi");
		res.put("httpStatus", HttpStatus.OK.value());
		return ResponseEntity.ok(res);
		
	}
	
}
