package com.aryanp45.orderservice.service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;

import com.aryanp45.orderservice.dto.InventoryResponse;
import com.aryanp45.orderservice.dto.OrderLineItemsDto;
import com.aryanp45.orderservice.dto.OrderRequest;
import com.aryanp45.orderservice.model.Order;
import com.aryanp45.orderservice.model.OrderLineItems;
import com.aryanp45.orderservice.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

	private final OrderRepository orderRepository;
	private final WebClient.Builder webClientBuilder;

	public void placeOrder(OrderRequest orderRequest) {
		Order order = new Order();
		order.setOrderNumber(UUID.randomUUID().toString());
		List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtos().stream()
				.map(orderLineItemsDto -> mapToDto(orderLineItemsDto))
//			.map(this::mapToDto)
				.toList();

		order.setOrderLineItems(orderLineItems);

		List<String> skuCodes = order.getOrderLineItems().stream().map(OrderLineItems::getSkuCode).toList();

		// check inventory
		InventoryResponse[] inventoryResponses = webClientBuilder.build().get()
				.uri("http://inventory-service/api/inventory",
						uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
				.retrieve().bodyToMono(InventoryResponse[].class).block(); // for synchronous communication

		boolean allProductsInStock = Arrays.stream(inventoryResponses)
				.allMatch(inventoryResponse -> inventoryResponse.isInStock());

		if (allProductsInStock)
			orderRepository.save(order);
		else
			throw new IllegalArgumentException("product is not in stock");

	}

	private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
		OrderLineItems orderLineItems = new OrderLineItems();
		orderLineItems.setPrice(orderLineItemsDto.getPrice());
		orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
		orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
		return orderLineItems;
	}
}
