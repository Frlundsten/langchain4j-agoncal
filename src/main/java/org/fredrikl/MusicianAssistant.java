package org.fredrikl;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.openai.OpenAiChatModel;

import java.util.List;

import static java.lang.System.exit;

public class MusicianAssistant {
    public static void main(String[] args) {
        String name = args[0];

        String BASE_URL = System.getProperty("BASE_URL");
        String MODEL = System.getProperty("MODEL");

        ChatModel model = OpenAiChatModel.builder()
                .baseUrl(BASE_URL)
                .modelName(MODEL)
                .temperature(0.3)
                .logRequests(true)
                .logResponses(true)
                .build();

        Musician musician = new MusicianAssistant().generateTopThreeAlbums(model, name);

        System.out.println(musician);
        exit(0);
    }

    Musician generateTopThreeAlbums(ChatModel model,
            String name) {
        SystemMessage systemMsg = SystemMessage.from("""
                You are an expert in Jazz music.  Reply with only the names of the artists, albums, etc.  Be very concise.  If a list is given, separate the items with commas.
                """);
        UserMessage userMsg = UserMessage.from(String.format("Only list the top 3 albums of %s", name));
        List<ChatMessage> messages = List.of(systemMsg, userMsg);
        ChatResponse albums = model.chat(messages);
        String topThreeAlbums = albums.aiMessage()
                .text();
        return new Musician(name, topThreeAlbums);
    }
}
