package com.minitextil.erp.item.view;

import java.util.List;

import com.minitextil.erp.components.core.program.ProgramErp;
import com.minitextil.erp.components.grid.GridComponent;
import com.minitextil.erp.components.grid.TipoColunas;
import com.minitextil.erp.item.model.Item;
import com.minitextil.erp.item.repository.ItemRepository;
import com.vaadin.flow.component.Component;

public class ConsultaItem extends ProgramErp {
	    private static final long serialVersionUID = 1L;

	    private final GridComponent<Item> grid = new GridComponent<>(Item.class);
	    private final ItemRepository repository;

	    public ConsultaItem(ItemRepository repository) {
	        this.repository = repository;

	        setSizeFull();
	        setPadding(true);
	        setSpacing(true);

			setCabecalho("Consulta de Itens", "Consulta geral de itens");

	        configurarGrid();
	        add(grid);

	        carregarItens();
	    }

	    private void configurarGrid() {
	        grid.setColumnReorderingAllowed(true);

	        grid.addColunaComFiltro(Item::getId, "ID", TipoColunas.Numerico);
	        grid.addColunaComFiltro(Item::getDescricao, "Descrição", TipoColunas.Alfanumerico);
	        grid.addColunaComFiltro(Item::getUnidade, "Uni", TipoColunas.Alfanumerico);
	        grid.addColunaComFiltro(Item::isAtivo, "Ativo", TipoColunas.Boolean);
	    }

	    private void carregarItens() {
	        List<Item> listaItens = repository.findAll();
	        grid.setItensComFiltro(listaItens);
	    }

	    @Override
	    public Component getView() {
	        return this;
	    }
	}