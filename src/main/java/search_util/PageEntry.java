package search_util;

import lombok.AllArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
public class PageEntry implements Comparable<PageEntry> {
    private final String pdfName;
    private final int page;
    private final int count;


    @Override
    public int compareTo(PageEntry o) {
        return Integer.compare(o.count, count);
    }

}