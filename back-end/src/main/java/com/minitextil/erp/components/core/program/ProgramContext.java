package com.minitextil.erp.components.core.program;

import java.util.function.Consumer;

public interface ProgramContext {
    void openProgram(String programId, ProgramParams params);
    void openProgram(String programId, ProgramParams params, Consumer<Object> onResult);
    void closeProgram();
    void closeProgram(Object result);
}