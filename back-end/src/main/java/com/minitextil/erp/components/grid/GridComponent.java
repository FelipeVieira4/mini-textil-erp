package com.minitextil.erp.components.grid;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.function.ValueProvider;

import com.vaadin.flow.function.SerializablePredicate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

/**
 * Grid genérico (T = tipo do item da linha) com sistema de filtro por coluna
 * embutido no próprio componente.
 *
 * Diferente da versão anterior, aqui NÃO é preciso passar um Consumer para
 * "receber" o filtro e aplicá-lo em algum lugar externo: o GridComponent
 * guarda os filtros ativos em uma lista interna (filtrosAtivos) e aplica
 * o resultado diretamente sobre um ListDataProvider<T>, usando o mesmo
 * ValueProvider que já é usado para exibir a coluna.
 */
public class GridComponent<T> extends Grid<T> {

    private static final long serialVersionUID = 1L;

    // Estado interno dos filtros: um FiltroColuna por coluna que tiver
    // um valor de filtro ativo no momento.
    private final List<FiltroColuna<T>> filtrosAtivos = new ArrayList<>();

    // Listeners "extras", só para quem quiser reagir a uma mudança de filtro
    // (ex: atualizar um contador de linhas). Não são mais necessários para
    // o filtro em si funcionar.
    private final List<Consumer<Void>> listenersDeFiltro = new ArrayList<>();

    private ListDataProvider<T> dataProvider;

    public GridComponent(Class<T> beanType) {
        super(beanType, false);
        setSizeFull();
        addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
    }

    /**
     * Use este método (em vez de setItems diretamente) para alimentar a grid.
     * Ele guarda os itens em um ListDataProvider interno, que é o que
     * permite aplicar os filtros das colunas sem depender de código externo.
     */
    public void setItensComFiltro(Collection<T> itens) {
        this.dataProvider = new ListDataProvider<>(itens);
        setItems(dataProvider);
        atualizarFiltros();
    }

    /**
     * Adiciona uma coluna com filtro embutido.
     *
     * @param valueProvider como extrair o valor de T para exibir E para filtrar
     * @param nomeColuna    título da coluna (também usado como chave interna do filtro)
     * @param tipoColuna    define o tipo de campo/lógica de comparação do filtro
     */
    public void addColunaComFiltro(ValueProvider<T, ?> valueProvider, String nomeColuna, TipoColunas tipoColuna) {
        Grid.Column<T> coluna = this.addColumn(valueProvider)
                .setSortable(true)
                .setResizable(true)
                .setKey(nomeColuna);

        if (tipoColuna == TipoColunas.Numerico || tipoColuna == TipoColunas.Decimal) {
            coluna.setFlexGrow(0).setWidth("140px");
        } else {
            coluna.setFlexGrow(1);
        }

        FiltroColuna<T> filtro = new FiltroColuna<>(nomeColuna, valueProvider, tipoColuna);
        Dialog dialogFiltro = criarDialogFiltroDinamico(filtro);

        coluna.setHeader(criarHeaderComFiltro(nomeColuna, dialogFiltro));
    }

    private HorizontalLayout criarHeaderComFiltro(String textoColuna, Dialog dialogFiltro) {
        HorizontalLayout layout = new HorizontalLayout(new Span(textoColuna));
        layout.setWidthFull();
        layout.setJustifyContentMode(JustifyContentMode.BETWEEN);
        layout.setAlignItems(Alignment.CENTER);

        Button btnFiltro = new Button(VaadinIcon.FILTER.create());
        btnFiltro.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        btnFiltro.addClickListener(_ -> dialogFiltro.open());

        layout.add(btnFiltro);
        return layout;
    }

    private Dialog criarDialogFiltroDinamico(FiltroColuna<T> filtro) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Filtrar " + filtro.nomeColuna);
        dialog.setWidth("280px");

        VerticalLayout conteudo = new VerticalLayout();
        conteudo.setPadding(false);

        ComboBox<String> comboCondicao = new ComboBox<>("Lógica");
        Button btnAplicar = new Button("Aplicar");
        Button btnLimpar = new Button("Limpar");

        Runnable acaoLimpar;

        switch (filtro.tipo) {
            case Numerico -> {
                comboCondicao.setItems("=", ">", "<");
                comboCondicao.setValue("=");
                comboCondicao.setWidthFull();

                IntegerField inputValor = new IntegerField("Valor");
                inputValor.setWidthFull();

                conteudo.add(comboCondicao, inputValor);

                btnAplicar.addClickListener(_ -> {
                    filtro.condicao = comboCondicao.getValue();
                    filtro.valor = inputValor.getValue();
                    aplicarFiltro(filtro);
                    dialog.close();
                });

                acaoLimpar = inputValor::clear;
            }
            case Decimal -> {
                comboCondicao.setItems("=", ">", "<");
                comboCondicao.setValue("=");
                comboCondicao.setWidthFull();

                NumberField inputValor = new NumberField("Valor");
                inputValor.setWidthFull();

                conteudo.add(comboCondicao, inputValor);

                btnAplicar.addClickListener(_ -> {
                    filtro.condicao = comboCondicao.getValue();
                    filtro.valor = inputValor.getValue();
                    aplicarFiltro(filtro);
                    dialog.close();
                });

                acaoLimpar = inputValor::clear;
            }
            case Boolean -> {
                comboCondicao.setVisible(false);

                ComboBox<Boolean> inputValor = new ComboBox<>("Valor");
                inputValor.setItems(Boolean.TRUE, Boolean.FALSE);
                inputValor.setItemLabelGenerator(v -> v ? "Sim" : "Não");
                inputValor.setWidthFull();

                conteudo.add(inputValor);

                btnAplicar.addClickListener(_ -> {
                    filtro.condicao = "=";
                    filtro.valor = inputValor.getValue();
                    aplicarFiltro(filtro);
                    dialog.close();
                });

                acaoLimpar = inputValor::clear;
            }
            default -> { // Alfanumerico
                comboCondicao.setItems("Contém", "Começa com", "Igual a");
                comboCondicao.setValue("Contém");
                comboCondicao.setWidthFull();

                TextField inputValor = new TextField("Texto");
                inputValor.setWidthFull();

                conteudo.add(comboCondicao, inputValor);

                btnAplicar.addClickListener(_ -> {
                    filtro.condicao = comboCondicao.getValue();
                    filtro.valor = inputValor.getValue();
                    aplicarFiltro(filtro);
                    dialog.close();
                });

                acaoLimpar = inputValor::clear;
            }
        }

        dialog.add(conteudo);

        btnLimpar.addClickListener(_ -> {
            acaoLimpar.run();
            filtro.valor = null;
            aplicarFiltro(filtro);
            dialog.close();
        });

        dialog.getFooter().add(btnLimpar, btnAplicar);
        return dialog;
    }

    /**
     * Atualiza (ou remove, se vazio) o filtro de uma coluna na lista interna
     * e reaplica o predicado combinado no ListDataProvider.
     */
    private void aplicarFiltro(FiltroColuna<T> filtro) {
        filtrosAtivos.removeIf(f -> f.nomeColuna.equals(filtro.nomeColuna));
        if (filtro.isAtivo()) {
            filtrosAtivos.add(filtro);
        }
        atualizarFiltros();
    }

    private void atualizarFiltros() {
        if (dataProvider == null) {
            return; // ainda não foi chamado setItensComFiltro
        }
        SerializablePredicate<T> predicadoCombinado = item ->
                filtrosAtivos.stream().allMatch(f -> f.testa(item));
        dataProvider.setFilter(predicadoCombinado);
        notificarMudancaFiltro();
    }

    /** Opcional: para telas que queiram reagir a uma mudança de filtro (ex: atualizar totais). */
    public void addFiltroChangeListener(Consumer<Void> listener) {
        this.listenersDeFiltro.add(listener);
    }

    private void notificarMudancaFiltro() {
        for (Consumer<Void> listener : listenersDeFiltro) {
            listener.accept(null);
        }
    }

    /**
     * Guarda o estado de filtro de UMA coluna: qual condição e valor foram
     * escolhidos, e sabe testar (via ValueProvider) se um item T passa nesse filtro.
     */
    private static class FiltroColuna<T> {
        final String nomeColuna;
        final ValueProvider<T, ?> valueProvider;
        final TipoColunas tipo;
        String condicao;
        Object valor;

        FiltroColuna(String nomeColuna, ValueProvider<T, ?> valueProvider, TipoColunas tipo) {
            this.nomeColuna = nomeColuna;
            this.valueProvider = valueProvider;
            this.tipo = tipo;
        }

        boolean isAtivo() {
            if (valor == null) return false;
            if (valor instanceof String s) return !s.isBlank();
            return true;
        }

        boolean testa(T item) {
            Object valorItem = valueProvider.apply(item);
            if (valorItem == null) return false;

            if (tipo == TipoColunas.Numerico || tipo == TipoColunas.Decimal) {
                double v1 = ((Number) valorItem).doubleValue();
                double v2 = ((Number) valor).doubleValue();
                return switch (condicao) {
                    case ">" -> v1 > v2;
                    case "<" -> v1 < v2;
                    default -> v1 == v2;
                };
            }

            if (tipo == TipoColunas.Boolean) {
                return valorItem.equals(valor);
            }

            String texto = valorItem.toString().toLowerCase();
            String textoFiltro = valor.toString().toLowerCase();
            return switch (condicao) {
                case "Começa com" -> texto.startsWith(textoFiltro);
                case "Igual a" -> texto.equals(textoFiltro);
                default -> texto.contains(textoFiltro);
            };
        }
    }
}