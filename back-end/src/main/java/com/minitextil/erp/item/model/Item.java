package com.minitextil.erp.item.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "TBL_ITEM")
public class Item {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Descrição do produto obrigátorio!")
	private String descricao;

	@Column(nullable = false)
	private boolean ativo = true;

	@Enumerated(EnumType.STRING)
	@NotNull(message = "Unidade do produto obrigátorio!")
	private UnidadeMedida unidade;

	//@OneToOne(fetch = FetchType.LAZY)
	//private ItemSku origemSku;   // nome mais claro: só é preenchido em itens reduzidos

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public UnidadeMedida getUnidade() {
		return unidade;
	}

	public void setUnidade(UnidadeMedida unidade) {
		this.unidade = unidade;
	}

	/*
	public ItemSku getOrigemSku() {
		return origemSku;
	}

	public void setOrigemSku(ItemSku origemSku) {
		this.origemSku = origemSku;
	}
	*/
}