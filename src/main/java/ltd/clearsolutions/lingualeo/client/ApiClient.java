package ltd.clearsolutions.lingualeo.client;

import javax.naming.AuthenticationException;
import java.util.List;
import java.util.Map;

public interface ApiClient {

    String auth() throws AuthenticationException;

    List<TranslatedWord> getTranslates(String word) throws AuthenticationException;

    Map<String, Object> addWord(String word, String translate) throws AuthenticationException;
}
