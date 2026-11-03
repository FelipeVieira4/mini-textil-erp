package com.minitextil.erp.components.core.program;

import com.vaadin.flow.component.Component;

public interface Program {

    Component getView();

    default void onOpen(ProgramContext context) {}
    default void onOpen(ProgramParams params, ProgramContext context) {}
    default void onClose() {}
}