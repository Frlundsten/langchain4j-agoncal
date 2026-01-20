package org.fredrikl;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PDFDemoHelper {

    private static List<String> wrapText(String text,
            PDFont font,
            int fontSize,
            float maxWidth) throws IOException {
        List<String> lines = new ArrayList<>();
        StringBuilder currentLine = new StringBuilder();

        for (String word : text.split(" ")) {
            String testLine = currentLine + word + " ";
            float size = font.getStringWidth(testLine) / 1000 * fontSize;

            if (size > maxWidth) {
                lines.add(currentLine.toString());
                currentLine = new StringBuilder(word).append(" ");
            } else {
                currentLine.append(word)
                        .append(" ");
            }
        }

        if (!currentLine.isEmpty()) {
            lines.add(currentLine.toString());
        }

        return lines;
    }

    public static void createDemoPDF() throws IOException {
        int fontSize = 12;
        PDType1Font font = new PDType1Font(Standard14Fonts.FontName.TIMES_ROMAN);
        String text = "I want to deepen my understanding of LangChain4j and explore how large language models work in practice, not just in theory. My goal is to learn how LLMs are integrated into real-world applications using Java, including prompt handling, document processing and embeddings. This learning journey will help me better understand the strengths, limitations, and responsibilities that come with using LLMs in production systems.";
        float margin = 25;
        float yStart = 500;
        float leading = 14; // line spacing
        PDPage page = new PDPage();
        float width = page.getMediaBox()
                .getWidth() - 2 * margin;

        List<String> lines = wrapText(text, font, fontSize, width);

        try (PDDocument document = new PDDocument()) {
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.beginText();
                contentStream.setFont(font, fontSize);
                contentStream.newLineAtOffset(margin, yStart);

                for (String line : lines) {
                    contentStream.showText(line);
                    contentStream.newLineAtOffset(0, -leading);
                }

                contentStream.endText();
            }
            document.save("stuff/demo.pdf");
        }
    }
}
