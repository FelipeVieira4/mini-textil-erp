package com.minitextil.erp.components.core.program;

import java.util.HashMap;
import java.util.Map;

public class ProgramParams {

    private final Map<String, Object> values = new HashMap<>();

    public static ProgramParams empty() {
        return new ProgramParams();
    }

    public ProgramParams set(String key, Object value) {
        values.put(key, value);
        return this;
    }

    public boolean has(String key) {
        return values.containsKey(key);
    }

    public <T> T get(String key, Class<T> type) {
        Object value = values.get(key);
        return value == null ? null : type.cast(value);
    }

    public String getString(String key) {
        return get(key, String.class);
    }

    public Long getLong(String key) {
        return get(key, Long.class);
    }
}
