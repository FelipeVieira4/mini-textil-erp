package com.minitextil.erp.estoque.service;

import com.minitextil.erp.estoque.models.ItemEstoque;
import com.minitextil.erp.estoque.models.ItemEstoqueId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemEstoqueRepository extends JpaRepository<ItemEstoque, ItemEstoqueId> {
}
