package com.minitextil.erp.components.core.program;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class ProgramErp extends VerticalLayout implements Program {

    private static final long serialVersionUID = 1L;

    private Div cabecalho;

    public ProgramErp() {}

    /**
     * Define (ou substitui) o cabeçalho padrão da tela.
     * Chame uma vez no construtor da tela filha.
     */
    protected void setCabecalho(String titulo, String subtitulo) {
        if (cabecalho != null) {
            remove(cabecalho);
        }

        H2 h2 = new H2(titulo);
        h2.addClassName("page-title");

        cabecalho = new Div(h2);
        cabecalho.addClassName("page-header");

        if (subtitulo != null && !subtitulo.isBlank()) {
            Paragraph sub = new Paragraph(subtitulo);
            sub.addClassName("page-subtitle");
            cabecalho.add(sub);
        }

        addComponentAsFirst(cabecalho);
    }

    /** Atalho: só título. */
    protected void setCabecalho(String titulo) {
        setCabecalho(titulo, null);
    }

    /** Envolve conteúdo em um cartão padrão. */
    protected Div criarCard(Component... componentes) {
        Div card = new Div(componentes);
        card.addClassName("card");
        return card;
    }

    @Override
    public Component getView() {
        return this;
    }
}