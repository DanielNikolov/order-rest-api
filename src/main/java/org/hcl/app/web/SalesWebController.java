package org.hcl.app.web;

import java.util.List;

import org.hcl.app.business.domain.APIResponse;
import org.hcl.app.business.service.SalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SalesWebController {

	private final SalesService salesService;
	
	@Autowired
	public SalesWebController(SalesService salesService) {
		super();
		this.salesService = salesService;
	}

	@GetMapping("/api/sales")
	public APIResponse getAllSales(@RequestParam(name = "date", required = false) String dateString) {
		return this.salesService.getSales(dateString);
	}
}
