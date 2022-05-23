package search_util;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import com.itextpdf.styledxmlparser.jsoup.select.Collector;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BooleanSearchEngine implements SearchEngine {

    private final Map<String, List<PageEntry>> mapWithAllPages = new HashMap<>();

    public BooleanSearchEngine(File pdfsDir) throws IOException {

        File[] pdfFilesList = Objects.requireNonNull(pdfsDir.listFiles());

        for (File pdf : pdfFilesList) {
            var doc = new PdfDocument(new PdfReader(pdf));
            int numberOfPages = doc.getNumberOfPages();

            for (int i = 1; i <= numberOfPages; i++) {
                PdfPage page = doc.getPage(i);
                var text = PdfTextExtractor.getTextFromPage(page);
                var words = text.split("\\P{IsAlphabetic}+");

                Map<String, Integer> wordToSearch = new HashMap<>();
                for (var word : words) {
                    if (word.isEmpty()) {
                        continue;
                    }
                    wordToSearch.put(word.toLowerCase(), wordToSearch.getOrDefault(word, 0) + 1);
                }

                for (Map.Entry<String, Integer> entry : wordToSearch.entrySet()) {
                    List<PageEntry> responsesList = new ArrayList<>();
                    if (mapWithAllPages.containsKey(entry.getKey())) {
                        responsesList = mapWithAllPages.get(entry.getKey());
                    }
                    responsesList.add(new PageEntry(pdf.getName(), i, entry.getValue()));
                    mapWithAllPages.put(entry.getKey(), responsesList);
                }
                wordToSearch.clear();
            }
        }
    }

    @Override
    public List<PageEntry> search(String word) {
        if (mapWithAllPages.containsKey(word.toLowerCase())) {

            return mapWithAllPages.get(word.toLowerCase());
        }
        return Collections.emptyList();
    }
}