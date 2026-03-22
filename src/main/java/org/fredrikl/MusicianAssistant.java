package org.fredrikl;

import dev.langchain4j.community.store.memory.chat.redis.RedisChatMemoryStore;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import org.fredrikl.parser.pdf.ParseByLangchain4j;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.Scanner;

import static java.lang.System.exit;

/*
docker run -d --gpus all -v ollama:/root/.ollama -p 11434:11434 --name ollama ollama/ollama:latest -------- runs faster with gpu!
 */
public class MusicianAssistant {
    public static void main(String[] args) {
        ParseByLangchain4j parser = new ParseByLangchain4j();
        Document loadedDocument = parser.loadFile();
        parser.split(loadedDocument);


//        PracticalChatModel chatModel = new PracticalChatModel();
//        PracticalImageModel imageModel = new PracticalImageModel();
//        PracticalScoringModel scoringModel = new PracticalScoringModel();
//        PracticalModerationModel moderationModel = new PracticalModerationModel();
//
//        chatModel.call(name);
//        imageModel.call();
//        scoringModel.call();
//        moderationModel.call();
//-------------------------------------------------------------
//        ChatMemoryStore chatMemoryStore = RedisChatMemoryStore.builder()
//                .host("localhost")
//                .port(6379)
//                .build();
//        ChatMemory chatMemory = MessageWindowChatMemory.builder()
//                .maxMessages(20)
//                .chatMemoryStore(chatMemoryStore)
//                .build();
//        ChatModel model = PracticalChatModel.ollamaInstance();
//
//        SystemMessage systemMsg = SystemMessage.from("""
//                You are a chatbot. You speak like a pirate.
//                """);
//
//        Scanner scanner = new Scanner(System.in);
//        String input;
//
//        while (true) {
//            System.out.println("Input question:");
//            input = scanner.nextLine();
//            if ("q".equals(input)) {
//                exit(0);
//            }
//                UserMessage userMessage = UserMessage.from(input);
//                chatMemory.add(userMessage);
//                AiMessage aiMessage = model.chat(chatMemory.messages())
//                        .aiMessage();
//                chatMemory.add(aiMessage);


//                UserMessage userMessage = UserMessage.from(input);
//                AiMessage aiMessage = model.chat(userMessage).aiMessage();
//                System.out.println(aiMessage.text());
//        }


//        while (true) {
//            System.out.println("Paste your question or code (type END on a new line when done, or q to quit):");
//
//            StringBuilder inputBuilder = new StringBuilder();
//            while (scanner.hasNextLine()) {
//                String line = scanner.nextLine();
//                if ("END".equals(line)) {
//                    break; // stop reading multi-line input
//                }
//                if ("q".equals(line)) {
//                    System.exit(0); // exit if user types q
//                }
//                inputBuilder.append(line).append("\n");
//            }
//
//            String input = inputBuilder.toString().trim(); // full pasted input
//
//            if (input.isEmpty()) {
//                System.out.println("No input detected. Try again.");
//                continue;
//            }
//
//            try {
//                UserMessage userMessage = UserMessage.from(input);
//                AiMessage aiMessage = model.chat(userMessage).aiMessage();
//                System.out.println("=== Model Response ===");
//                System.out.println(aiMessage.text()); // properly formatted multi-line output
//                System.out.println("======================");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
    }
}
