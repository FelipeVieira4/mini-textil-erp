package com.minitextil.erp.components.core.program;

public record ProgramaDef(String id, String titulo, String viewClassName, Boolean show) {

    public ProgramaDef{
        if (show==null){
            show=true;
        }
    }

    public Boolean isVisible(){
        return show;
    }

    @SuppressWarnings("unchecked")
    public Class<? extends Program> resolveViewClass() {

        try {
            Class<?> clazz = Class.forName(viewClassName);
            if (!Program.class.isAssignableFrom(clazz)) {
                throw new IllegalStateException(
                        "Classe " + viewClassName + " não implementa Program");
            }
            return (Class<? extends Program>) clazz;
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(
                    "Classe de view não encontrada: " + viewClassName, e);
        }
    }
}