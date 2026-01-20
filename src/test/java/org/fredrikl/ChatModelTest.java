package org.fredrikl;


import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.chat.request.ResponseFormatType;
import dev.langchain4j.model.chat.request.json.JsonArraySchema;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import dev.langchain4j.model.chat.request.json.JsonSchema;
import dev.langchain4j.model.chat.request.json.JsonSchemaElement;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.ollama.OllamaContainer;

import java.io.IOException;
import java.util.List;

import static org.fredrikl.Models.LLAMA31;
import static org.fredrikl.OllamaUtil.createChatModel;
import static org.fredrikl.OllamaUtil.createOllamaContainer;
import static org.fredrikl.PDFDemoHelper.createDemoPDF;
import static org.fredrikl.parser.pdf.PDFParserService.extractText;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ChatModelTest {

    //    Models llmModel = TINYLLAMA; // Does not support structured outputs
    Models model = LLAMA31;
    OllamaContainer ollamaContainer;
    ChatModel modelApi;

    @BeforeAll
    void setUp() throws IOException, InterruptedException {
        ollamaContainer = createOllamaContainer(model.getName(), model.getImageName());
        modelApi = createChatModel(model, ollamaContainer);
        createDemoPDF();
    }

    @AfterAll
    void tearDown() {
        if (ollamaContainer != null) {
            ollamaContainer.stop();
        }
    }

    @Test
    void shouldGenerateMusicianTopThreeAlbums() {
        Musician musician = new PracticalChatModel().generateTopThreeAlbumsNoFormatting(modelApi, "Miles Davis");
        assertTrue(musician.albums()
                .contains("Kind of Blue"));
    }

    @Test
    void shouldRespondWithJson() {

        JsonObjectSchema musician = JsonObjectSchema.builder()
                .addStringProperty("name")
                .required("name")
                .build();

        JsonSchemaElement arraySchema = JsonArraySchema.builder()
                .description("All names of the people found")
                .items(musician)
                .build();

        JsonObjectSchema root = JsonObjectSchema.builder()
                .addProperty("musicians", arraySchema)
                .required("musicians")
                .build();

        ResponseFormat responseFormat = ResponseFormat.builder()
                .type(ResponseFormatType.JSON)
                .jsonSchema(JsonSchema.builder()
                        .rootElement(root)
                        .build())
                .build();

        SystemMessage systemMsg = SystemMessage.from("""
                You are an expert in Jazz music.  Reply with only the names of the artists, albums, etc. Be very concise.
                """);
        UserMessage userMessage = UserMessage.from("Top 10 Jazz musicians");
        ChatRequest chatRequest = ChatRequest.builder()
                .responseFormat(responseFormat)
                .messages(List.of(systemMsg, userMessage))
                .build();
        modelApi.chat(chatRequest);
    }

//    This method is not supported with the models I use (tinyllama and llama3.1) due to multi content i.e. text and PDF.
//    @Test
//    void shouldSummarizePDF() {
//        UserMessage userMessage = UserMessage.from(TextContent.from("Summarize the following PDF file"), PdfFileContent.from(Paths.get("stuff/demo.pdf")
//                .toUri()));
//
//        ChatResponse result = modelApi.chat(userMessage);
//    }

    @Test
    void interaction() {

    }

    @Test
    void summarizePDFText() throws IOException {
        String pdfText = extractText("stuff/demo.pdf");

        UserMessage userMessage = UserMessage.from(TextContent.from("Summarize the following text: " + pdfText));
        modelApi.chat(userMessage);
    }
}