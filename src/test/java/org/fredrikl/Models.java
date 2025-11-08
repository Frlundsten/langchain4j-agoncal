package org.fredrikl;

public enum Models {
    TINYLLAMA("tinyllama", "ollama/ollama:latest");

    private final String modelName;
    private final String imageName;

    Models(String modelName, String imageName) {
        this.modelName = modelName;
        this.imageName = imageName;
    }

    public String getName() {
        return modelName;
    }

    public String getContainerName() {
        return imageName;
    }
}
