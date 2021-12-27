package ltd.clearsolutions.lingualeo.client;

import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.junit.jupiter.api.Test;
import ltd.clearsolutions.lingualeo.client.LingualeoClient;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LingualeoClientIntegrationTest {

    @Test
    void auth_PutCorrectCredentials_ListOfCookies() {
        LingualeoClient correctLingualeoClient = new LingualeoClient("vasyll.danylenko@gmail.com", "6485644Df");
        correctLingualeoClient.auth();

        List<Cookie> actual = correctLingualeoClient.authCookie.getCookieStore().getCookies();

        assertEquals(1, actual.size());
        assertEquals("lingualeo.com", actual.get(0).getDomain());
        assertEquals("remember", actual.get(0).getName());
        assertEquals("31536000", ((BasicClientCookie) actual.get(0)).getAttribute("max-age"));
    }

    @Test
    void auth_PutIncorrectCredentials_EmptyListOfCookies() {
        LingualeoClient incorrectLingualeoClient = new LingualeoClient("vasyll.danyleil.com", "64");
        incorrectLingualeoClient.auth();

        List<Cookie> expected = Collections.emptyList();
        List<Cookie> actual = incorrectLingualeoClient.authCookie.getCookieStore().getCookies();
        assertEquals(expected, actual);
    }

    @Test
    void auth_PutCorrectCredentialsIfUserNotRegistered_EmptyListOfCookies() {
        LingualeoClient incorrectLingualeoClient = new LingualeoClient("pillowBed@gmail.com", "onthebad247");
        incorrectLingualeoClient.auth();

        List<Cookie> expected = Collections.emptyList();
        List<Cookie> actual = incorrectLingualeoClient.authCookie.getCookieStore().getCookies();

        assertEquals(expected, actual);
    }

    @Test
    void getTranslates_PutCorrectWord_WordTranslationDataList() {
        LingualeoClient correctLingualeoClient = new LingualeoClient("vasyll.danylenko@gmail.com", "6485644Df");

        List<TranslatedWord> actual = correctLingualeoClient.getTranslates("Set");

        assertEquals(5, actual.size());
        assertEquals("положить", actual.get(0).value());
        assertEquals(24201, actual.get(0).votes());
        assertEquals(16, actual.get(0).id());
    }

    @Test
    void getTranslates_PutIncorrectWord_WordTranslationDataList() {
        LingualeoClient correctLingualeoClient = new LingualeoClient("vasyll.danylenko@gmail.com", "6485644Df");

        List<TranslatedWord> actual = correctLingualeoClient.getTranslates("fdsafasdafasdf");

        assertEquals(1, actual.size());
        assertEquals("фдсафасдафасдф", actual.get(0).value());
        assertEquals(0, actual.get(0).votes());
        assertEquals(0, actual.get(0).id());
    }


    @Test
    void addWord_PutCorrectWord_WordAdditionData() {
        LingualeoClient correctLingualeoClient = new LingualeoClient("vasyll.danylenko@gmail.com", "6485644Df");
        correctLingualeoClient.auth();

        Map<String, Object> actual = correctLingualeoClient.addWord("Set", "Положить");

        assertEquals("положить", actual.get("translate_value"));
        assertEquals("Set", actual.get("word_value"));
        assertEquals(1, actual.get("added_translate_count"));
        assertEquals("en", ((Map<?, ?>) actual.get("lang")).get("current"));
        assertEquals("ru", ((Map<?, ?>) actual.get("lang")).get("target"));
        assertEquals(16, actual.get("translate_id"));
    }

    @Test
    void addWord_PutIncorrectWord_WordAdditionData() {
        LingualeoClient correctLingualeoClient = new LingualeoClient("vasyll.danylenko@gmail.com", "6485644Df");
        correctLingualeoClient.auth();
        Map<String, Object> actual = correctLingualeoClient.addWord("fdasfdfasdf", "фдасфдфасдф");

        assertEquals("фдасфдфасдф", actual.get("translate_value"));
        assertEquals("fdasfdfasdf", actual.get("word_value"));
        assertEquals(1, actual.get("added_translate_count"));
        assertEquals("en", ((Map<?, ?>) actual.get("lang")).get("current"));
        assertEquals("ru", ((Map<?, ?>) actual.get("lang")).get("target"));
        assertEquals(0, actual.get("translate_id"));
    }

}
