package org.fredrikl;


import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.ollama.OllamaContainer;

import java.io.IOException;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MusicianAssistantTest {

    Models llmModel = Models.TINYLLAMA;
    OllamaContainer ollamaContainer;
    ChatModel model;

    @BeforeAll
    void setUp() throws IOException, InterruptedException {
        ollamaContainer = OllamaUtil.createOllamaContainer(llmModel.getName(), llmModel.getContainerName());

        model = OllamaChatModel.builder()
                .baseUrl(String.format("http://%s:%d", ollamaContainer.getHost(),
                        ollamaContainer.getFirstMappedPort()))
                .modelName(llmModel.getName())
                .temperature(0.0)
                .timeout(Duration.ofMinutes(5))
                .build();
    }

    @AfterAll
    void tearDown() {
        if (ollamaContainer != null) {
            ollamaContainer.stop();
        }
    }

    @Test
    public void shouldGenerateMusicianTopThreeAlbums() {
        Musician musician = new MusicianAssistant().generateTopThreeAlbums(model, "Miles Davis");
        assertTrue(musician.albums()
                .contains("Kind of Blue"));
    }
}