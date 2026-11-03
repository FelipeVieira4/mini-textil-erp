package com.minitextil.erp.operador.model;

import java.io.Serializable;

import org.springframework.stereotype.Component;

import com.minitextil.erp.components.core.security.SecurityUtils;
import com.minitextil.erp.empresa.model.Empresa;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;

@Component
@VaadinSessionScope
public class UsuarioSessao implements Serializable {

    private static final long serialVersionUID = 1L;

    private Empresa empresaAtiva;

    public Operador getOperadorLogado() {
        return SecurityUtils.getOperadorLogado();
    }

    public Empresa getEmpresaAtiva() {
        if (empresaAtiva == null && getOperadorLogado() != null) {
            return getOperadorLogado().getEmpresa();
        }
        return empresaAtiva;
    }

    public void setEmpresaAtiva(Empresa empresaAtiva) {
        this.empresaAtiva = empresaAtiva;
    }
}