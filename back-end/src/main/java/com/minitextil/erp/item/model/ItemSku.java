package com.minitextil.erp.item.model;

import jakarta.persistence.*;

@Entity
@Table(name = "TBL_ITEM_SKU")
public class ItemSku {

    @EmbeddedId
    private ItemSkuId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("itemPai")
    @JoinColumn(name = "item_pai_id")
    private Item itemPai;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("varianteId")
    @JoinColumn(name = "variante_id")
    private VarianteItem variante;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(unique = true)
    private Item itemReduzido;

    public ItemSkuId getId() {
        return id;
    }

    public void setId(ItemSkuId id) {
        this.id = id;
    }

    public Item getItemPai() {
        return itemPai;
    }

    public void setItemPai(Item itemPai) {
        this.itemPai = itemPai;
    }

    public VarianteItem getVariante() {
        return variante;
    }

    public void setVariante(VarianteItem variante) {
        this.variante = variante;
    }

    public Item getItemReduzido() {
        return itemReduzido;
    }

    public void setItemReduzido(Item itemReduzido) {
        this.itemReduzido = itemReduzido;
    }
}