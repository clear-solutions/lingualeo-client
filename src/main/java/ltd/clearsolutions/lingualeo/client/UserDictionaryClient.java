package ltd.clearsolutions.lingualeo.client;

import java.util.Map;

public interface UserDictionaryClient {

    Map<String, Object> addWord(String word, String translate);
}
