package search_util;

import java.util.List;

public interface SearchEngine {
    List<PageEntry> search(String word);
}
