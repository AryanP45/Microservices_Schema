package com.aryanp45.orderservice.controller;

import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.aryanp45.orderservice.dto.OrderRequest;
import com.aryanp45.orderservice.service.OrderService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {
	
	private final OrderService orderService;
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@CircuitBreaker(name = "inventory",fallbackMethod = "fallbackMethod")  // same name as configured in properties
	@TimeLimiter(name = "inventory")
	@Retry(name = "inventory")
	public CompletableFuture<String> placeOrder(@RequestBody OrderRequest orderRequest) {
		return CompletableFuture.supplyAsync(()->orderService.placeOrder(orderRequest));
	}
	
	public CompletableFuture<String> fallbackMethod(OrderRequest orderRequest,RuntimeException runtimeException) {
		return CompletableFuture.supplyAsync(()->"Opps ! something went wrong, please order in some time");
	}
	
	@GetMapping
	@CircuitBreaker(name = "inventory",fallbackMethod = "fallbackMethod")
	public String getdata() {
		return "working";
	}
	
	
}
