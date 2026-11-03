package com.minitextil.erp.item.repository;

import com.minitextil.erp.item.model.ItemSku;
import com.minitextil.erp.item.model.ItemSkuId;
import com.minitextil.erp.item.model.VarianteItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VarianteRepository extends JpaRepository<VarianteItem, Long> { }
