package com.aryanp45.inventoryservice.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aryanp45.inventoryservice.dto.InventoryResponse;
import com.aryanp45.inventoryservice.repository.InventoryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

	private final InventoryRepository inventoryRepository;

	@Transactional(readOnly = true)
	public List<InventoryResponse> isInStock(List<String> skuCode) {

//		Implemented sleep for timeout testing in circuitbreaker - inventory(order-service)		
		log.info("wait started !!");
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		log.info("Wait ended !!");
		return inventoryRepository.findBySkuCodeIn(skuCode)
				.stream()
				.map(inventory -> 
					InventoryResponse.builder()
									.skuCode(inventory.getSkuCode())
									.isInStock(inventory.getQuantity() >0).build()
				).toList();
	}

}
