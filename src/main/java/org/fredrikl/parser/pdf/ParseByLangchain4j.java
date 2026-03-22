package org.fredrikl.parser.pdf;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentByLineSplitter;
import dev.langchain4j.data.document.splitter.DocumentByParagraphSplitter;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;

import java.util.List;

public class ParseByLangchain4j {

    public Document loadFile() {
        return FileSystemDocumentLoader.loadDocument("PB.txt", new TextDocumentParser());
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
