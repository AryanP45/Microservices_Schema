package com.aryanp45.inventoryservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aryanp45.inventoryservice.model.Inventory;

import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long>{
	
	List<Inventory> findBySkuCodeIn(List<String> skuCode);
	
}
