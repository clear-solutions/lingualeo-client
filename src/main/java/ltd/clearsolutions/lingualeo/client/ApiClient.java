package ltd.clearsolutions.lingualeo.client;

import javax.naming.AuthenticationException;
import java.util.List;

public interface ApiClient {

    void auth() throws AuthenticationException;

    List<TranslatedWord> getTranslates(String word) throws AuthenticationException;

    void addWord(String word, String translate) throws AuthenticationException;
}
