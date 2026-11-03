package com.minitextil.erp.operador.view;

import java.util.List;
import java.util.function.Consumer;

import com.minitextil.erp.components.grid.GridComponent;
import com.minitextil.erp.components.grid.TipoColunas;
import com.minitextil.erp.operador.repository.OperadorRepository;
import com.vaadin.flow.component.dialog.Dialog;

import com.minitextil.erp.operador.model.Operador;

public class LookupOperador {
	
    public void VerTodosOperador(OperadorRepository operadorRepository, Consumer<Operador> selecionarListener) {
        GridComponent<Operador> grid = new GridComponent<>(Operador.class);
		
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Operadores");
        dialog.setWidth("280px");
		
        List<Operador> listaOperadores = operadorRepository.findAll();
		
        grid.setColumnReorderingAllowed(true);
        grid.addColunaComFiltro(Operador::getId, "ID", TipoColunas.Numerico);
        grid.addColunaComFiltro(Operador::getNome, "Nome", TipoColunas.Alfanumerico);
        grid.addColunaComFiltro(Operador::getEmail, "Email", TipoColunas.Alfanumerico);
		
        
        grid.addItemDoubleClickListener(event -> {
            Operador selecionado = event.getItem();
            if (selecionarListener != null) {
            	selecionarListener.accept(selecionado);
            }
            dialog.close();
        });
		
        dialog.setWidth("60vw");
        dialog.setHeight("40vh");
        
        grid.setItensComFiltro(listaOperadores);
        dialog.add(grid);
        
        dialog.open();
    }
}