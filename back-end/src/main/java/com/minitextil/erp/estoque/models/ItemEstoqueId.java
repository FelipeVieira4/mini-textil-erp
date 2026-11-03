package com.minitextil.erp.estoque.models;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public record ItemEstoqueId(
        Long empresaId,
        Long itemId
) implements Serializable {};