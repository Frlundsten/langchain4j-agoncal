package org.fredrikl;

import org.testcontainers.ollama.OllamaContainer;

import java.io.IOException;

/**
 * Creates an ollama container used for testing
 */
public class OllamaUtil {
    private OllamaUtil() {}
    public static OllamaContainer createOllamaContainer(String MODEL_NAME,
            String IMAGE_NAME) throws IOException, InterruptedException {
        OllamaContainer ollama = new OllamaContainer(IMAGE_NAME)
                .withFileSystemBind("/var/lib/docker/volumes/ollama/_data", "/root/.ollama");

        ollama.start();

        // Pull the model only if not already present
        var result = ollama.execInContainer("sh", "-c", "test -d /root/.ollama/models/manifests/registry.ollama.ai/library/" + MODEL_NAME);
        if (result.getExitCode() != 0) {
            System.out.println("Pulling Ollama model: " + MODEL_NAME);
            ollama.execInContainer("ollama", "pull", MODEL_NAME);
            System.out.println("Model pulled successfully!");
        } else {
            System.out.println("Model already exists in volume, skipping pull.");
        }

        return ollama.withEnv("OLLAMA_MODELS", "/root/.ollama/models");
    }
}
