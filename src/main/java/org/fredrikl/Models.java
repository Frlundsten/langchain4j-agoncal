package org.fredrikl;

public enum Models {
    TINYLLAMA("tinyllama"),
    QWEN257("qwen2.5-coder:7b"),
    QWEN2514("qwen2.5-coder:14b"),
    LLAMA31("llama3.1");

    private final String MODEL_NAME;

    Models(String modelName) {
        this.MODEL_NAME = modelName;
    }

    public String getName() {
        return MODEL_NAME;
    }
}
