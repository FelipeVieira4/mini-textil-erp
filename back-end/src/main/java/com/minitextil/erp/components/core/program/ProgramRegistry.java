package com.minitextil.erp.components.core.program;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.di.Instantiator;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ProgramRegistry {

    private final List<ModuloDef> modulos;
    private final Map<String, ProgramaDef> porId;

    public ProgramRegistry(ObjectMapper objectMapper) {
        try (InputStream is = getClass().getResourceAsStream("/programas.json")) {
            JavaType listType = objectMapper.getTypeFactory()
                    .constructCollectionType(List.class, ModuloDef.class);

            List<ModuloDef> loaded = objectMapper.readValue(is, listType);
            this.modulos = loaded;
        } catch (IOException e) {
            throw new IllegalStateException("Falha ao carregar programas.json", e);
        }

        this.porId = modulos.stream()
                .flatMap(m -> m.programas().stream())
                .collect(Collectors.toMap(ProgramaDef::id, Function.identity()));
    }

    public List<ModuloDef> getModulos() {
        return modulos;
    }

    public ProgramaDef get(String programId) {
        ProgramaDef p = porId.get(programId);
        if (p == null) {
            throw new IllegalArgumentException("Programa não cadastrado: " + programId);
        }
        return p;
    }

    public Program createInstance(String programId) {
        Class<? extends Program> viewClass = get(programId).resolveViewClass();
        return Instantiator.get(UI.getCurrent()).getOrCreate(viewClass);
    }
}