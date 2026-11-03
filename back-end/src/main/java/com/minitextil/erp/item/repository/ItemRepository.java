package com.minitextil.erp.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.minitextil.erp.item.model.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
}