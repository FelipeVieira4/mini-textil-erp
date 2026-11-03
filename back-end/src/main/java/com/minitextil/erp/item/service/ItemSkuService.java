package com.minitextil.erp.item.service;

import com.minitextil.erp.item.model.Item;
import com.minitextil.erp.item.model.ItemSku;
import com.minitextil.erp.item.model.ItemSkuId;
import com.minitextil.erp.item.model.VarianteItem;
import com.minitextil.erp.item.repository.ItemRepository;
import com.minitextil.erp.item.repository.ItemSkuRepository;
import com.minitextil.erp.item.repository.VarianteRepository;
import com.vaadin.flow.router.NotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ItemSkuService {

    private final ItemSkuRepository itemSkuRepository;
    private final ItemRepository itemRepository;
    private final VarianteRepository varianteItemRepository;

    public ItemSkuService(ItemSkuRepository itemSkuRepository, ItemRepository itemRepository, VarianteRepository varianteRepository) {
        this.itemSkuRepository = itemSkuRepository;
        this.itemRepository = itemRepository;
        this.varianteItemRepository = varianteRepository;
    }

    @Transactional
    public ItemSku salvarItemSku(Long idItemPai, Long idVariante, ItemSku itemSku, String descItemSku) {

        Item itemPai = itemRepository.findById(idItemPai)
                .orElseThrow(() -> new NotFoundException("Item pai não encontrado: " + idItemPai));

        if (itemSkuRepository.existsByItemReduzido_Id(idItemPai)) {
            throw new IllegalArgumentException(
                    "Item " + idItemPai + " já é um item reduzido (gerado por outro SKU) e não pode ser usado como item pai."
            );
        }

        VarianteItem variante = varianteItemRepository.findById(idVariante)
                .orElseThrow(() -> new NotFoundException("Variante não encontrada: " + idVariante));

        itemSku.setItemPai(itemPai);
        itemSku.setVariante(variante);
        itemSku.setId(new ItemSkuId(itemPai.getId(), variante.getId()));

        if (itemSku.getItemReduzido()==null) {
            Item itemReduzido = new Item();
            itemReduzido.setDescricao(descItemSku);
            itemReduzido.setUnidade(itemPai.getUnidade());
            itemReduzido.setAtivo(true);
            itemReduzido = itemRepository.save(itemReduzido);

            itemSku.setItemReduzido(itemReduzido);
        }else {
            Item itemReduzido = itemRepository.findById(itemSku.getItemReduzido().getId())
                    .orElseThrow(() -> new NotFoundException("Item reduzido do SKU não encontrado: " + itemSku.getItemReduzido().getId()));

            itemReduzido.setDescricao(descItemSku);
            itemRepository.save(itemReduzido);
        }
        return itemSkuRepository.save(itemSku);
    }
}