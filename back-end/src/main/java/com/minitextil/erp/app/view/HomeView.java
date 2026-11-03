package com.minitextil.erp.app.view;

import com.minitextil.erp.app.utils.ApplicationInfo;
import com.minitextil.erp.components.core.program.Program;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;


public class HomeView extends VerticalLayout implements Program {

    private static final long serialVersionUID = 1L;
    
    public HomeView(ApplicationInfo appInfo) {
    	
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        
        add(new H1("Bem Vindo"));
        add(new H2("MiniTêxtil ERP versão:"+appInfo.getVersion()));
        
        /*
        Button botaoAbrirCadOperador = new Button("Cadastro Operador");
        botaoAbrirCadOperador.addClickListener(_->{
        	this.context.openProgram(ProgramId.CADASTRO_OPERADOR, ProgramParams.empty());
        });
        
        add(botaoAbrirCadOperador);
        */
    }
    
	@Override
	public Component getView() {
		return this;
	}
}