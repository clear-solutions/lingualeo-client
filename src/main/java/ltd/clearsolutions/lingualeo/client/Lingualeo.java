package ltd.clearsolutions.lingualeo.client;

import java.util.List;

public interface Lingualeo extends UserDictionaryClient {

    void auth();

    List<TranslatedWord> getTranslates(String word);
}
