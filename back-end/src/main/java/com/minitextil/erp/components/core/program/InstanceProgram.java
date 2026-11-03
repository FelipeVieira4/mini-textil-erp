package com.minitextil.erp.components.core.program;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.Consumer;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;

public class InstanceProgram extends VerticalLayout {

    private static final long serialVersionUID = 1L;

    private final Tabs stackTabs = new Tabs();
    private final Div contentArea = new Div();
    private final Deque<StackEntry> stack = new ArrayDeque<>();
    private final Tab instanceTab;
    private final ProgramRegistry programRegistry;

    public InstanceProgram(ProgramRegistry programRegistry, String rootProgramId, ProgramParams rootParams, Tab instanceTab) {
        this.programRegistry = programRegistry;
        this.instanceTab = instanceTab;

        setSizeFull();
        setPadding(false);
        setSpacing(false);

        stackTabs.setWidthFull();
        contentArea.setSizeFull();

        add(stackTabs, contentArea);
        setFlexGrow(1, contentArea);

        // O primeiro programa empilhado será o programa raiz (root)
        pushProgram(rootProgramId, rootParams, null);
    }

    public void pushProgram(String programId, ProgramParams params, Consumer<Object> onResult) {
        if (!stack.isEmpty()) {
            stack.peek().tab().setEnabled(false); // Desabilita a Tab do programa anterior
        }

        ProgramaDef programaDef = programRegistry.get(programId);
        String tituloTexto = programaDef.titulo();

        Tab tab;
        if (!isRoot()) {
            Span titulo = new Span(tituloTexto);

            Button btnFechar = new Button(VaadinIcon.CLOSE_SMALL.create());
            btnFechar.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ICON);
            btnFechar.getStyle().set("font-size", "12px").set("margin-left", "8px");

            HorizontalLayout tabContent = new HorizontalLayout(titulo, btnFechar);
            tabContent.setAlignItems(FlexComponent.Alignment.CENTER);
            tabContent.setSpacing(false);

            tab = new Tab(tabContent);

            btnFechar.getElement().addEventListener("click", _ -> popProgram())
                    .addEventData("event.stopPropagation()");
        } else {
            tab = new Tab(tituloTexto);
        }

        // Instancia o programa usando o ProgramRegistry / Vaadin Instantiator
        Program program = programRegistry.createInstance(programId);
        StackEntry entry = new StackEntry(programId, tab, program, onResult);
        stack.push(entry);

        stackTabs.add(tab);
        stackTabs.setSelectedTab(tab);

        program.onOpen(params, new DefaultProgramContext(this));
        showContent(program.getView());
        syncInstanceTabLabel();
    }

    public void popProgram() {
        popProgram(null);
    }

    public void popProgram(Object result) {
        if (stack.size() <= 1) {
            return;
        }

        StackEntry closed = stack.pop();
        StackEntry previous = stack.peek();

        if (previous != null) {
            previous.tab().setEnabled(true);
            stackTabs.setSelectedTab(previous.tab());
            showContent(previous.program().getView());
        }

        stackTabs.remove(closed.tab());
        closed.program().onClose();

        syncInstanceTabLabel();

        if (closed.onResult() != null) {
            closed.onResult().accept(result);
        }
    }

    private void showContent(Component view) {
        contentArea.removeAll();
        if (view != null) {
            contentArea.add(view);
        }
    }

    private void syncInstanceTabLabel() {
        if (stack.isEmpty() || instanceTab == null) {
            return;
        }

        String tituloAtual = programRegistry.get(stack.peek().programId()).titulo();

        instanceTab.getChildren()
                .filter(HorizontalLayout.class::isInstance)
                .map(HorizontalLayout.class::cast)
                .flatMap(hl -> hl.getChildren().filter(Span.class::isInstance).map(Span.class::cast))
                .findFirst()
                .ifPresent(span -> span.setText(tituloAtual));
    }

    public Tab getInstanceTab() {
        return instanceTab;
    }

    public boolean isRoot() {
        return stack.isEmpty();
    }

    private record StackEntry(String programId, Tab tab, Program program, Consumer<Object> onResult) {}

    private static class DefaultProgramContext implements ProgramContext {
        private final InstanceProgram owner;

        DefaultProgramContext(InstanceProgram owner) {
            this.owner = owner;
        }

        @Override
        public void openProgram(String programId, ProgramParams params) {
            owner.pushProgram(programId, params, null);
        }

        @Override
        public void openProgram(String programId, ProgramParams params, Consumer<Object> onResult) {
            owner.pushProgram(programId, params, onResult);
        }

        @Override
        public void closeProgram() {
            owner.popProgram(null);
        }

        @Override
        public void closeProgram(Object result) {
            owner.popProgram(result);
        }
    }
}