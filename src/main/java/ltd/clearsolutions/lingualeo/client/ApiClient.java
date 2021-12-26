package ltd.clearsolutions.lingualeo.client;

import java.util.List;
import java.util.Map;

public interface ApiClient {

    void auth();

    List<TranslatedWord> getTranslates(String word);

    Map<String, Object> addWord(String word, String translate);
}
