package org.fredrikl;

public enum Models {
    TINYLLAMA("tinyllama", "ollama/ollama:latest"),
    QWEN257("qwen2.5-coder:7b","ollama/ollama:latest"),
    QWEN2514("qwen2.5-coder:14b","ollama/ollama:latest"),
    LLAMA31("llama3.1", "ollama/ollama:latest");


    private final String modelName;
    private final String imageName;

    Models(String modelName,
            String imageName) {
        this.modelName = modelName;
        this.imageName = imageName;
    }

    public String getName() {
        return modelName;
    }

    public String getImageName() {
        return imageName;
    }
}
