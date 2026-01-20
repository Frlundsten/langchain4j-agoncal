package org.fredrikl;

import dev.langchain4j.data.image.Image;
import dev.langchain4j.model.image.ImageModel;
import dev.langchain4j.model.openai.OpenAiImageModel;
import dev.langchain4j.model.output.Response;
import org.tinylog.Logger;

/**
 * Represents an image as a URL or as a Base64-encoded string.
 */
public class PracticalImageModel {
    public void call() {
        String BASE_URL = System.getProperty("IMAGE_BASE_URL");
        String MODEL = System.getProperty("IMAGE_MODEL");

        if (BASE_URL == null) {
            Logger.debug("No image base url provided..");
            return;
        }

        ImageModel model = OpenAiImageModel.builder()
                .baseUrl(BASE_URL)
                .modelName(MODEL)
                .logRequests(true)
                .logResponses(true)
                .build();

        Response<Image> response = model.generate("Christmas tree");

        Image image = response.content();

        System.out.println(image.url());
        System.out.println(image.mimeType());

    }
}
