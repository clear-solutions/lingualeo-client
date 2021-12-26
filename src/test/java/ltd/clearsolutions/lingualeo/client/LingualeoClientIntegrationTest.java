package ltd.clearsolutions.lingualeo.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.cookie.Cookie;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class LingualeoClientIntegrationTest {

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void auth_PutCorrectCredentials_ListOfCookies() {
        LingualeoClient correctLingualeoClient = new LingualeoClient("vasyll.danylenko@gmail.com", "6485644Df");
        correctLingualeoClient.auth();

        List<Cookie> expected = Collections.emptyList();
        List<Cookie> actual = correctLingualeoClient.authCookie.getCookieStore().getCookies();

        assertNotEquals(expected, actual);
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
    void getTranslates_PutCorrectWord_WordTranslationDataList() throws IOException {
        LingualeoClient correctLingualeoClient = new LingualeoClient("vasyll.danylenko@gmail.com", "6485644Df");

        DataWord expected = getActualResultFromFile("/response/getTranslates/getTranslates_PutCorrectWord_CorrectResponse.json",
                new TypeReference<>() {
                });

        List<TranslatedWord> actual = correctLingualeoClient.getTranslates("Set");

        assertEquals(expected.translate().get(0).value(), actual.get(0).value());
        assertEquals(expected.translate().get(0).votes(), actual.get(0).votes());
    }

    @Test
    void getTranslates_PutIncorrectWord_WordTranslationDataList() throws IOException {
        LingualeoClient correctLingualeoClient = new LingualeoClient("vasyll.danylenko@gmail.com", "6485644Df");

        DataWord expected = getActualResultFromFile("/response/getTranslates/getTranslates_PutIncorrectWord_CorrectResponse.json",
                new TypeReference<>() {
                });

        List<TranslatedWord> actual = correctLingualeoClient.getTranslates("fdsafasdafasdf");

        assertEquals(expected.translate().get(0).value(), actual.get(0).value());
        assertEquals(expected.translate().get(0).votes(), actual.get(0).votes());
    }


    @Test
    void addWord_PutCorrectWord_WordAdditionData() throws IOException {
        LingualeoClient correctLingualeoClient = new LingualeoClient("vasyll.danylenko@gmail.com", "6485644Df");
        correctLingualeoClient.auth();

        Map<String, Object> expected = getActualResultFromFile("/response/addWord/addWord_PutCorrectWordIfMissing_CorrectResponse.json",
                new TypeReference<>() {
                });

        Map<String, Object> actual = correctLingualeoClient.addWord("Set", "Положить");

        assertEquals(expected.get("added_translate_count"), actual.get("added_translate_count"));
        assertEquals(expected.get("lang"), actual.get("lang"));
        assertEquals(expected.get("translate_id"), actual.get("translate_id"));
        assertEquals(expected.get("translate_value"), actual.get("translate_value"));
        assertEquals(expected.get("word_value"), actual.get("word_value"));
    }

    @Test
    void addWord_PutIncorrectWord_WordAdditionData() throws IOException {
        LingualeoClient correctLingualeoClient = new LingualeoClient("vasyll.danylenko@gmail.com", "6485644Df");
        correctLingualeoClient.auth();
        Map<String, Object> actual = correctLingualeoClient.addWord("fdasfdfasdf", "фдасфдфасдф");
        assertEquals("фдасфдфасдф", actual.get("translate_value"));
        assertEquals("fdasfdfasdf", actual.get("word_value"));
        assertEquals(2, actual.get("added_translate_count"));
        assertEquals("en", ((Map) actual.get("lang")).get("current"));
        assertEquals("ru", ((Map) actual.get("lang")).get("target"));
        assertEquals(0, actual.get("translate_id"));
    }

    private <T> T getActualResultFromFile(String fileName, TypeReference<T> T) throws IOException {
        InputStream inputFile = getClass().getResourceAsStream(fileName);
        BufferedReader readFile = null;
        if (inputFile != null) {
            readFile = new BufferedReader(new InputStreamReader(inputFile, StandardCharsets.UTF_8));
        }

        return objectMapper.readValue(readFile, T);
    }
}
