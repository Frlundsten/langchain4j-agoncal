package org.fredrikl;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.chat.request.ResponseFormatType;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import dev.langchain4j.model.chat.request.json.JsonSchema;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import org.tinylog.Logger;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static dev.langchain4j.model.LambdaStreamingResponseHandler.onPartialResponseAndError;

public class PracticalChatModel {
    private static volatile ChatModel openApiInstance;
    private static volatile ChatModel ollamaInstance;
    private static volatile StreamingChatModel streamingChatModel;

    public static ChatModel openApiInstance() {
        if (openApiInstance == null) {
            synchronized (PracticalChatModel.class) {
                if (openApiInstance == null) {
                    String BASE_URL = System.getenv("CHAT_BASE_URL");
                    String MODEL = System.getenv("CHAT_MODEL");

                    if (BASE_URL == null || MODEL == null) {
                        throw new IllegalStateException("Both CHAT_BASE_URL and CHAT_MODEL system properties must be set.");
                    }

                    openApiInstance = OpenAiChatModel.builder()
                            .baseUrl(BASE_URL)
                            .modelName(MODEL)
                            .temperature(0.3)
                            .timeout(Duration.ofSeconds(10))
                            .logRequests(true)
                            .logResponses(true)
                            .build();

                    Logger.info("Initialized ChatModel singleton: {} at {}", MODEL, BASE_URL);
                }
            }
        }
        return openApiInstance;
    }


    public static ChatModel ollamaInstance() {
        if (ollamaInstance == null) {
            synchronized (PracticalChatModel.class) {
                if (ollamaInstance == null) {
                    String BASE_URL = System.getenv("CHAT_BASE_URL");
                    String MODEL = System.getenv("CHAT_MODEL");

                    if (BASE_URL == null || MODEL == null) {
                        throw new IllegalStateException("Both CHAT_BASE_URL and CHAT_MODEL system properties must be set.");
                    }

                    ollamaInstance = OllamaChatModel.builder()
                            .baseUrl(String.format("%s", BASE_URL))
                            .modelName(MODEL)
                            .temperature(0.0)
                            .timeout(Duration.ofMinutes(5))
                            .logRequests(true)
                            .logResponses(true)
                            .build();

                    Logger.info("Initialized ChatModel singleton: {} at {}", MODEL, BASE_URL);
                }
            }
        }
        return ollamaInstance;
    }


    private static StreamingChatModel getStreamingInstance() {
        if (streamingChatModel == null) {
            synchronized (PracticalChatModel.class) {
                if (streamingChatModel == null) {
                    String BASE_URL = System.getProperty("CHAT_BASE_URL");
                    String MODEL = System.getProperty("CHAT_MODEL");

                    if (BASE_URL == null || MODEL == null) {
                        throw new IllegalStateException("Both CHAT_BASE_URL and CHAT_MODEL system properties must be set.");
                    }

                    streamingChatModel = OpenAiStreamingChatModel.builder()
                            .baseUrl(BASE_URL)
                            .modelName(MODEL)
                            .temperature(0.3)
                            .timeout(Duration.ofSeconds(10))
                            .logRequests(true)
                            .logResponses(true)
                            .build();

                    Logger.info("Initialized ChatModel singleton: {} at {}", MODEL, BASE_URL);
                }
            }
        }
        return streamingChatModel;
    }

    public void call(String name) {

        ChatModel model = openApiInstance();
        StreamingChatModel streamingChatModel = getStreamingInstance();

        generateTopThreeAlbumsNoFormatting(model, name);
        generateTopThreeAlbumsInJsonFormat(model);
        generateStreamResponse(streamingChatModel);
        generateStreamResponseLambda(streamingChatModel);
    }

    protected Musician generateTopThreeAlbumsNoFormatting(ChatModel model,
            String name) {
        Logger.debug("Generating top 3 albums in standard format...");
        SystemMessage systemMsg = SystemMessage.from("""
                You are an expert in Jazz music.  Reply with only the names of the artists, albums, etc.  Be very concise.  If a list is given, separate the items with commas.
                """);
        UserMessage userMsg = UserMessage.from(String.format("Only list the top 3 albums of %s", name));
        List<ChatMessage> messages = List.of(systemMsg, userMsg);
        ChatResponse albums = model.chat(messages);
        System.out.println(albums);
        String topThreeAlbums = albums.aiMessage()
                .text();
        return new Musician(name, topThreeAlbums);
    }

    /**
     * Some models do not support the Responseformatting!
     *
     * @param model
     */
    protected void generateTopThreeAlbumsInJsonFormat(ChatModel model) {
        Logger.debug("Generating top 10 Jazz musicians in json format...");
        ResponseFormat responseFormat = ResponseFormat.builder()
                .type(ResponseFormatType.JSON)
                .jsonSchema(JsonSchema.builder()
                        .name("Musician")
                        .rootElement(JsonObjectSchema.builder()
                                .addStringProperty("name")
                                .required("name")
                                .build())
                        .build())
                .build();

        SystemMessage systemMsg = SystemMessage.from("""
                You are an expert in Jazz music.  Reply with only the names of the artists, albums, etc.  Be very concise.  If a list is given, separate the items with commas.
                """);
        UserMessage userMessage = UserMessage.from("Top 10 Jazz musicians");
        ChatRequest chatRequest = ChatRequest.builder()
                .responseFormat(responseFormat)
                .messages(List.of(systemMsg, userMessage))
                .build();
        model.chat(chatRequest);
    }

    protected void generateStreamResponse(StreamingChatModel model) {
        Logger.debug("Streaming chat model...");
        CompletableFuture<ChatResponse> futureResponse = new CompletableFuture<>();

        model.chat("Who are some influential female musicians?", new StreamingChatResponseHandler() {
            @Override
            public void onPartialResponse(String partialResponse) {
                Logger.debug(partialResponse); // The model will stream every token
            }

            @Override
            public void onCompleteResponse(ChatResponse completeResponse) {
                futureResponse.complete(completeResponse);
            }

            @Override
            public void onError(Throwable error) {
                futureResponse.completeExceptionally(error);
            }
        });

        futureResponse.join();
    }

    // Doesn't generate..
    protected void generateStreamResponseLambda(StreamingChatModel model) {
        Logger.debug("Streaming chat model with lambda call...");
//        model.chat("Who are some influential female musicians?",onPartialResponse(System.out::println));
        model.chat("Who are some influential female musicians?", onPartialResponseAndError(System.out::println, Throwable::printStackTrace));
    }


    /**
     * Github has models that can be used. It's free but rate limited.
     */
//    protected void githubModelGeneration() {
//        GitHubModelsChatModel model = GitHubModelsChatModel.builder()
//                .gitHubToken(GITHUB_TOKEN)
//                .modelName(GPT_4_O_MINI)
//                .logRequestsAndResponses(true)
//                .build();
//        System.out.println(model.chat("List some influential Jazz musicians"));
//    }
}
