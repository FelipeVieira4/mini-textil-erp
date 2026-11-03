package com.minitextil.erp.item.view;

import com.minitextil.erp.components.core.program.ProgramContext;
import com.minitextil.erp.components.core.program.ProgramErp;
import com.minitextil.erp.components.core.program.ProgramParams;
import com.minitextil.erp.item.model.ItemSku;
import com.minitextil.erp.item.model.ItemSkuId;
import com.minitextil.erp.item.repository.ItemSkuRepository;
import com.minitextil.erp.item.service.ItemSkuService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;

import java.util.Optional;


public class CadastroItemSku extends ProgramErp {

    private final ItemSkuService service;
    private final ItemSkuRepository repository;

    // Estado da tela: se != null, estamos em modo edição de um registro já existente.
    // Se null, "Salvar" vai criar um ItemSku (e Item reduzido) novo.
    private ItemSku itemSkuAtual;

    private final IntegerField idItemPai = new IntegerField("Item Pai");
    private final Button btObterItemPai = new Button(new Icon(VaadinIcon.SEARCH));

    private final IntegerField idVariante = new IntegerField("Variante");
    private final Button btObterVariante = new Button(new Icon(VaadinIcon.SEARCH));

    private final TextField descricaoItemVariante = new TextField("Descrição do Item Variante");

    public CadastroItemSku(ItemSkuRepository skuRepository, ItemSkuService skuService) {
        this.repository = skuRepository;
        this.service = skuService;

        this.setWidthFull();
        this.setAlignItems(Alignment.CENTER);

        idItemPai.setValueChangeMode(ValueChangeMode.ON_BLUR);
        idVariante.setValueChangeMode(ValueChangeMode.ON_BLUR);

        // dispara em ambos os campos, não só na variante — cobre o caso do usuário
        // trocar o item pai depois de já ter preenchido a variante
        idItemPai.addValueChangeListener(_ -> tentarCarregar());
        idVariante.addValueChangeListener(_ -> tentarCarregar());

        Button btSalvar = new Button("Salvar", _ -> salvar());
        btSalvar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);


        setCabecalho("Cadastro de Item SKU", "Dados gerais de item SKU");

        FormLayout formLayout = new FormLayout();
        formLayout.setWidth("50vw");
        formLayout.setRowSpacing("25px");

        formLayout.add(idItemPai, idVariante, descricaoItemVariante, new Div(), btSalvar);

        add(criarCard(formLayout));
    }

    private void tentarCarregar() {
        Integer pai = idItemPai.getValue();
        Integer variante = idVariante.getValue();

        if (pai == null || variante == null) {
            itemSkuAtual = null;
            return;
        }

        mostrarDadosItemSku(pai.longValue(), variante.longValue());
    }

    private void mostrarDadosItemSku(Long idItemPai, Long idVariante) {
        ItemSkuId itemSkuId = new ItemSkuId(idItemPai, idVariante);
        Optional<ItemSku> itemSkuOpt = repository.findById(itemSkuId);

        if (itemSkuOpt.isPresent()) {
            itemSkuAtual = itemSkuOpt.get(); // guarda a instância real pra reaproveitar no salvar()
            descricaoItemVariante.setValue(itemSkuAtual.getItemReduzido().getDescricao());

            Notification.show("Variante (" + itemSkuAtual.getItemReduzido().getId() + ") encontrada! Modo de edição ativo.");
        } else {
            itemSkuAtual = null; // modo cadastro
            descricaoItemVariante.clear();
            Notification.show("ID não encontrado. Um novo registro será criado ao salvar.", 4000, Notification.Position.MIDDLE);
        }
    }

    private void salvar() {
        Integer pai = idItemPai.getValue();
        Integer variante = idVariante.getValue();

        if (pai == null || variante == null) {
            Notification.show("Preencha Item Pai e Variante.").addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }

        // Usa a instância carregada (edição) ou cria uma nova (cadastro) — nunca os dois ao mesmo tempo
        ItemSku itemSku = (itemSkuAtual != null) ? itemSkuAtual : new ItemSku();

        Long codItemPai = pai.longValue();
        Long codVariante = variante.longValue();

        try {
            ItemSku itemSkuSalvo = service.salvarItemSku(
                    codItemPai,
                    codVariante,
                    itemSku,
                    descricaoItemVariante.getValue()
            );

            Notification.show("Variante salva com sucesso! ID: " + itemSkuSalvo.getId().itemPai())
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);

            itemSkuAtual = null;
            limparFormulario();
            idItemPai.clear();
            idVariante.clear();

        } catch (Exception e) {
            Notification.show("Erro ao salvar: " + e.getMessage()).addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    private void limparFormulario() {
        descricaoItemVariante.clear();
    }

    @Override
    public Component getView() {
        return this;
    }

    @Override
    public void onOpen(ProgramParams params, ProgramContext context) {
    }
}