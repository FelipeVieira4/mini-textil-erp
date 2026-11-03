package com.minitextil.erp.item.view;

import java.util.Optional;

import com.minitextil.erp.components.core.program.*;
import com.minitextil.erp.item.repository.VarianteRepository;
import com.minitextil.erp.item.model.VarianteItem;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;

public class CadastroVariante extends ProgramErp {
    private final Binder<VarianteItem> binder = new BeanValidationBinder<>(VarianteItem.class);
    private final VarianteRepository repository;

    private final IntegerField id = new IntegerField("Código");

    private final TextField descricao = new TextField("Descrição");
    private final Checkbox ativo = new Checkbox("Ativo");

    private final TextField codHexa = new TextField("Cor");
    private final TextField campoCor = new TextField("");

    public CadastroVariante(VarianteRepository repository) {
        this.repository = repository;

        this.setWidthFull();
        this.setAlignItems(Alignment.CENTER);

        setCabecalho("Cadastro de Variantes", "Dados gerais de variantes");

        binder.forField(descricao)
                .asRequired("Informe a descrição da Variante")
                .bind(VarianteItem::getDescricao, VarianteItem::setDescricao);

        binder.forField(ativo)
                .bind(VarianteItem::isAtivo, VarianteItem::setAtivo);

        binder.forField(codHexa)
                .bind(VarianteItem::getHexaCor, VarianteItem::setHexaCor);

        Button btObter = new Button(new Icon(VaadinIcon.SEARCH));
        HorizontalLayout idCampo = new HorizontalLayout(id, btObter);
        idCampo.setDefaultVerticalComponentAlignment(HorizontalLayout.Alignment.BASELINE);
        idCampo.setWidthFull();


        id.setValueChangeMode(ValueChangeMode.ON_BLUR);
        id.addValueChangeListener(event -> {
            Integer idDigitado = event.getValue();

            if (idDigitado != null) MostrarDadosVariante(idDigitado.longValue());
            else limparFormulario();
        });

        codHexa.addValueChangeListener(_ -> {
            campoCor.getStyle().set("background", codHexa.getValue());
        });

        btObter.addClickListener(_->{
            LookupItem lookup = new LookupItem();

            lookup.VerTodasVariantes(this.repository, VarianteSelecionada -> {
                id.setValue(VarianteSelecionada.getId().intValue());
            });
        });

        campoCor.setReadOnly(true);

        VerticalLayout espacoCampoCor = new VerticalLayout();
        espacoCampoCor.setAlignItems(Alignment.CENTER);


        espacoCampoCor.add(codHexa,campoCor);

        Button btSalvar = new Button("Salvar", _-> salvar());
        btSalvar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);


        FormLayout formLayout = new FormLayout();
        formLayout.setWidth("50vw");
        formLayout.setRowSpacing("25px");

        formLayout.add(idCampo, new Div(), descricao, ativo, espacoCampoCor, new Div(), btSalvar);
        add(criarCard(formLayout));
    }

    private void MostrarDadosVariante(Long idBusca) {
        Optional<VarianteItem> varianteOpt = repository.findById(idBusca);

        if (varianteOpt.isPresent()) {
            VarianteItem varianteItem = varianteOpt.get();
            binder.readBean(varianteItem);

            Notification.show("Variante ("+idBusca+") encontrado! Modo de edição ativo.");
        } else {
            limparFormulario();
            Notification.show("ID não encontrado. Um novo registro será criado ao salvar.", 4000, Notification.Position.MIDDLE);
        }
    }

    private void salvar() {
        boolean isEdicao = id.getValue() != null;

        VarianteItem varianteItem= new VarianteItem();
        if (binder.writeBeanIfValid(varianteItem)) {

            if (isEdicao) {
                varianteItem.setId(id.getValue().longValue());
            }

            try {
                VarianteItem varianteSalvo = repository.save(varianteItem);

                Notification.show("Variante salvo com sucesso! ID: " + varianteSalvo.getId())
                        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);

                limparFormulario();
                id.clear();

            } catch (Exception e) {
                Notification.show("Erro ao salvar: " + e.getMessage()).addThemeVariants(NotificationVariant.LUMO_ERROR);
            }

        } else {
            Notification.show("Preencha os campos obrigatórios corretamente.").addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }
	
	/*
	private void excluir(Long idExcluir) {

        try {
            repository.deleteById(idExcluir);
            
            Notification.show("Variante Excluir com sucesso! ID: " + idExcluir)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            
            limparFormulario();
            id.clear();
            
        } catch (Exception e) {
            Notification.show("Erro ao salvar: " + e.getMessage()).addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
	}
	*/


    private void limparFormulario() {
        binder.readBean(null);
        descricao.clear();
        ativo.setValue(true);
        codHexa.clear();
        //campoCor.getStyle().set("background", "");
    }
}