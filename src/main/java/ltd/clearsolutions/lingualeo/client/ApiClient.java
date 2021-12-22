package ltd.clearsolutions.lingualeo.client;

import javax.naming.AuthenticationException;
import java.util.List;
import java.util.Map;

public interface ApiClient {

    Map<String, Object> auth() throws AuthenticationException;

    List<TranslatedWord> getTranslates(String word) throws AuthenticationException;

    void addWord(String word, String translate) throws AuthenticationException;
}
