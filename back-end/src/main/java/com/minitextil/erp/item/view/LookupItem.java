package com.minitextil.erp.item.view;

import com.minitextil.erp.components.grid.GridComponent;
import com.minitextil.erp.components.grid.TipoColunas;
import com.minitextil.erp.item.model.Item;
import com.minitextil.erp.item.model.VarianteItem;
import com.minitextil.erp.item.repository.ItemRepository;
import com.minitextil.erp.item.repository.VarianteRepository;
import com.vaadin.flow.component.dialog.Dialog;

import java.util.List;
import java.util.function.Consumer;

public class LookupItem {

    public void VerTodosItens(ItemRepository itemRepository, Consumer<Item> selecionarListener) {
        GridComponent<Item> grid = new GridComponent<>(Item.class);

        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Itens");
        dialog.setWidth("280px");

        List<Item> listaOperadores = itemRepository.findAll();

        grid.setColumnReorderingAllowed(true);
        grid.addColunaComFiltro(Item::getId, "ID", TipoColunas.Numerico);
        grid.addColunaComFiltro(Item::getDescricao, "Descrição", TipoColunas.Alfanumerico);
        grid.addColunaComFiltro(Item::getUnidade, "Unidade", TipoColunas.Alfanumerico);


        grid.addItemDoubleClickListener(event -> {
            Item itemSelecionado = event.getItem();
            if (selecionarListener != null) {
                selecionarListener.accept(itemSelecionado);
            }
            dialog.close();
        });

        dialog.setWidth("60vw");
        dialog.setHeight("40vh");

        grid.setItensComFiltro(listaOperadores);
        dialog.add(grid);

        dialog.open();
    }


    public void VerTodasVariantes(VarianteRepository varianteRepositoryRepository, Consumer<VarianteItem> selecionarListener) {
        GridComponent<VarianteItem> grid = new GridComponent<>(VarianteItem.class);

        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Itens");
        dialog.setWidth("280px");

        List<VarianteItem> listaVariantes = varianteRepositoryRepository.findAll();

        grid.setColumnReorderingAllowed(true);
        grid.addColunaComFiltro(VarianteItem::getId, "ID", TipoColunas.Numerico);
        grid.addColunaComFiltro(VarianteItem::getDescricao, "Descrição", TipoColunas.Alfanumerico);
        grid.addColunaComFiltro(VarianteItem::isAtivo, "Situação", TipoColunas.Boolean);
        grid.addColunaComFiltro(VarianteItem::getHexaCor, "Cor Variante", TipoColunas.Alfanumerico);

        grid.addItemDoubleClickListener(event -> {
            VarianteItem varianteSelecionada = event.getItem();
            if (selecionarListener != null) {
                selecionarListener.accept(varianteSelecionada);
            }
            dialog.close();
        });

        dialog.setWidth("60vw");
        dialog.setHeight("40vh");

        grid.setItensComFiltro(listaVariantes);
        dialog.add(grid);

        dialog.open();
    }
}