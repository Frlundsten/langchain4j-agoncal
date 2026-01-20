package org.fredrikl;


import dev.langchain4j.model.cohere.CohereScoringModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.scoring.ScoringModel;
import org.tinylog.Logger;

/**
 * Rerank models sort text inputs by semantic relevance to a specified query.
 * They are often used to sort search results returned from an existing search solution
 */
public class PracticalScoringModel {
    String bio = """  
            Brooklyn native, she is a groundbreaking figure in speculative fiction.
            Her debut novel, "The Hundred Thousand Kingdoms launched her into literary stardom with its intricate world-building and exploration of power dynamics.
            Her Broken Earth trilogy, starting with "The Fifth Season further  solidified her reputation, winning consecutive awards in the genre.
            """;

    public void call() {

        String BASE_URL = System.getProperty("IMAGE_BASE_URL");
        String MODEL = System.getProperty("IMAGE_MODEL");


        if (BASE_URL == null) {
            Logger.debug("No scoring model base url provided..");
            return;
        }
        ScoringModel model = CohereScoringModel.builder()
                .baseUrl(BASE_URL)
                .modelName(MODEL)
                .logResponses(true)
                .logRequests(true)
                .build();

        Response<Double> score = model.score(bio, "Is this Nora Jemisin?");
        System.out.println(score.content());

        score = model.score(bio, "Is this John Lennon?");
        System.out.println(score.content());
    }
}
