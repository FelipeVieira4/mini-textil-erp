package com.minitextil.erp.operador.view;

import com.minitextil.erp.components.core.program.ProgramErp;
import com.minitextil.erp.components.grid.GridComponent;
import com.minitextil.erp.components.grid.TipoColunas;
import com.minitextil.erp.operador.model.Operador;
import com.minitextil.erp.operador.repository.OperadorRepository;
import com.vaadin.flow.component.Component;

import java.util.List;

public class ConsultaOperador extends ProgramErp {

    private static final long serialVersionUID = 1L;

    private final GridComponent<Operador> grid = new GridComponent<>(Operador.class);
    private final OperadorRepository repository;

    public ConsultaOperador(OperadorRepository operadorRepository) {
        this.repository = operadorRepository;

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        setCabecalho("Consulta de Operadores", "Consulta de operadores");

        configurarGrid();
        add(grid);

        carregarOperadores();
    }

    private void configurarGrid() {
        grid.setColumnReorderingAllowed(true);

        grid.addColunaComFiltro(Operador::getId, "ID", TipoColunas.Numerico);
        grid.addColunaComFiltro(Operador::getNome, "Nome", TipoColunas.Alfanumerico);
        grid.addColunaComFiltro(Operador::getEmail, "Email", TipoColunas.Alfanumerico);
    }

    private void carregarOperadores() {
        List<Operador> listaOperadores = repository.findAll();
        grid.setItensComFiltro(listaOperadores);
    }

    @Override
    public Component getView() {
        return this;
    }
}