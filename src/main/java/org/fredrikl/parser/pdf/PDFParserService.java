package org.fredrikl.parser.pdf;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBufferedFile;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.IOException;

/**
 * Service responsible for extracting plain text from PDF documents.
 * <p>
 * Some language models do not support multiple content types within a single
 * user message (for example, combining text and a PDF attachment).
 * Attempting to do so may result in runtime errors such as:
 * </p>
 *
 * <pre>
 * java.lang.RuntimeException: Expecting single text content, but got:
 * [TextContent { text = "Summarize the following PDF file" }, ...]
 * </pre>
 *
 * <p>
 * This service converts PDF files into plain text so that the extracted content
 * can be sent to the model as a single {@code TextContent} message, ensuring
 * compatibility with models that only accept one content type per message.
 * </p>
 *
 * <p>
 * Typical usage:
 * </p>
 *
 * <pre>
 * String text = pdfParserService.extractText(pathToPdf);
 * UserMessage message = UserMessage.from(TextContent.from(text));
 * </pre>
 */
public class PDFParserService {
    private PDFParserService() {}

    public static String extractText(String pdfFilePath) throws IOException {
        try (PDDocument document = Loader.loadPDF(new RandomAccessReadBufferedFile(pdfFilePath))) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            return pdfStripper.getText(document);
        }
    }
}
