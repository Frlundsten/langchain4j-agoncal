package org.fredrikl.parser.pdf;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentByLineSplitter;
import dev.langchain4j.data.document.splitter.DocumentByParagraphSplitter;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import org.tinylog.Logger;

import java.util.List;
import java.util.Optional;

public class ParseByLangchain4j {

    public Optional<Document> loadFile() {
        try {
            return Optional.of(FileSystemDocumentLoader.loadDocument("PB.txt", new TextDocumentParser()));
        } catch (IllegalArgumentException e) {
            Logger.error("You need to provide a PB.txt file on classpath. " + e.getMessage());
        }
        return Optional.empty();
    }

    public List<TextSegment> split(Document document) {
        List<TextSegment> textSegments = new DocumentByLineSplitter(100, 8).split(document);

        for (TextSegment segment : textSegments) {
            System.out.println(segment);
        }

        DocumentSplitter splitter = DocumentSplitters.recursive(100, 50);
        List<TextSegment> splitted = splitter.split(document);
        System.out.println("\n\n\n\n");
        for (TextSegment segment : splitted) {
            System.out.println(segment);
        }

        return textSegments;
    }
}
