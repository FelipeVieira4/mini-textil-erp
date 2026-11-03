package com.minitextil.erp.item.model;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public record ItemSkuId(
        Long itemPai,
        Long varianteId
) implements Serializable {}