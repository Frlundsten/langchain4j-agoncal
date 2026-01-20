package org.fredrikl;

import dev.langchain4j.community.store.memory.chat.redis.RedisChatMemoryStore;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;

import java.util.Scanner;

import static java.lang.System.exit;

public class MusicianAssistant {
    public static void main(String[] args) {

//        PracticalChatModel chatModel = new PracticalChatModel();
//        PracticalImageModel imageModel = new PracticalImageModel();
//        PracticalScoringModel scoringModel = new PracticalScoringModel();
//        PracticalModerationModel moderationModel = new PracticalModerationModel();
//
//        chatModel.call(name);
//        imageModel.call();
//        scoringModel.call();
//        moderationModel.call();

        ChatMemoryStore chatMemoryStore = RedisChatMemoryStore.builder()
                .host("localhost")
                .port(6379)
                .build();
        ChatMemory chatMemory = MessageWindowChatMemory.builder()
                .maxMessages(20)
                .chatMemoryStore(chatMemoryStore)
                .build();
        ChatModel model = PracticalChatModel.ollamaInstance();

        SystemMessage systemMsg = SystemMessage.from("""
                You are a chatbot. You speak like a pirate.
                """);

        Scanner scanner = new Scanner(System.in);
        String input = "";

        while (true) {
            System.out.println("Input question:");
            input = scanner.nextLine();
            if ("q".equals(input)) {
                exit(0);
            }
            UserMessage userMessage = UserMessage.from(input);
            chatMemory.add(userMessage);
            AiMessage aiMessage = model.chat(chatMemory.messages())
                    .aiMessage();
            chatMemory.add(aiMessage);
        }
    }
}
