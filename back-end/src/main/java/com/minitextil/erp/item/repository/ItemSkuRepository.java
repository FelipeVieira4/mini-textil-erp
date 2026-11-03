package com.minitextil.erp.item.repository;

import com.minitextil.erp.item.model.ItemSku;
import com.minitextil.erp.item.model.ItemSkuId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemSkuRepository extends JpaRepository<ItemSku, ItemSkuId> {
    boolean existsByItemReduzido_Id(Long itemReduzidoId);
}
