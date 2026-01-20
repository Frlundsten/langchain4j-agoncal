package org.fredrikl;

import dev.langchain4j.model.moderation.Moderation;
import dev.langchain4j.model.moderation.ModerationModel;
import dev.langchain4j.model.openai.OpenAiModerationModel;
import dev.langchain4j.model.output.Response;
import org.tinylog.Logger;

public class PracticalModerationModel {
    public void call() {
        String BASE_URL = System.getProperty("MODERATION_BASE_URL");
        String MODEL = System.getProperty("MODERATION_MODEL");

        if (BASE_URL == null) {
            Logger.debug("No moderation base url provided..");
            return;
        }

        ModerationModel model = OpenAiModerationModel.builder()
                .baseUrl(BASE_URL)
                .modelName(MODEL)
                .logRequests(true)
                .logResponses(true)
                .build();

        Response<Moderation> response = model.moderate("I want to kill all bass players.");

        System.out.println(response.content());
    }
}
