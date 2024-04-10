package com.aryanp45.orderservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.aryanp45.orderservice.dto.OrderRequest;
import com.aryanp45.orderservice.service.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {
	
	private final OrderService orderService;
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public String placeOrder(@RequestBody OrderRequest orderRequest) {
		System.out.println(orderRequest.toString());
		orderService.placeOrder(orderRequest);
		return "order placed successfully";
	}
	
	@GetMapping
	public String getdata() {
		return "working";
	}
	
	
}
