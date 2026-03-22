package org.fredrikl;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import org.testcontainers.ollama.OllamaContainer;

import java.io.IOException;
import java.time.Duration;

/**
 * Creates an ollama container used for testing
 */
public class OllamaUtil {
    private OllamaUtil() {
    }

    public static OllamaContainer createOllamaContainer(String MODEL_NAME) throws IOException, InterruptedException {

        // Do not use try-with-resource since it will shut down container...
        final String OLLAMA_IMAGE = "ollama/ollama:latest";
        OllamaContainer ollama = new OllamaContainer(OLLAMA_IMAGE)
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

    public static ChatModel createChatModel(Models model,
            OllamaContainer ollamaContainer) {
        return OllamaChatModel.builder()
                .baseUrl(String.format("http://%s:%d", ollamaContainer.getHost(),
                        ollamaContainer.getFirstMappedPort()))
                .modelName(model.getName())
                .temperature(0.0)
                .timeout(Duration.ofMinutes(5))
                .logRequests(true)
                .logResponses(true)
                .build();
    }
}
