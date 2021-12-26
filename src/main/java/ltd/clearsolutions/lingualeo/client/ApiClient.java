package ltd.clearsolutions.lingualeo.client;

import javax.naming.AuthenticationException;
import java.util.List;
import java.util.Map;

public interface ApiClient {

    void auth() throws AuthenticationException;

    List<TranslatedWord> getTranslates(String word);

    Map<String, Object> addWord(String word, String translate);
}
