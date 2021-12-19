package ltd.clearsolutions.lingualeo.client;

import java.util.List;

public interface ApiClient {

    void auth();

    List<TranslatedWord> getTranslates(String word);

    void addWord(String word, String translate, String context);
}
