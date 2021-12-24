package ltd.clearsolutions.lingualeo.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LingualeoClientIntegrationTest {

    ObjectMapper objectMapper = new ObjectMapper();

    LingualeoClient correctLingualeoClient = new LingualeoClient("vasyll.danylenko@gmail.com", "6485644Df");

    LingualeoClient IncorrectLingualeoClient;
    String expectedMessage = "Cannot invoke " +
            "\"org.apache.http.client.protocol.HttpClientContext.getCookieStore()" +
            "\" because " +
            "\"this.IncorrectLingualeoClient.authCookie" +
            "\" is null";

    @Test
    void auth_PutCorrectCredentials_String() {
        correctLingualeoClient = new LingualeoClient("vasyll.danylenko@gmail.com", "6485644Df");

        String expected = correctLingualeoClient.auth();
        String actual = correctLingualeoClient.authCookie.getCookieStore().getCookies().get(0).getValue();

        assertEquals(expected, actual);
    }

    @Test
    void auth_PutIncorrectCredentials_NullPointerException() {
        IncorrectLingualeoClient = new LingualeoClient("vasyll.danyleil.com", "64");

        NullPointerException actual = assertThrows(NullPointerException.class, () -> IncorrectLingualeoClient.authCookie.getCookieStore());

        assertEquals(expectedMessage, actual.getMessage());
    }

    @Test
    void auth_PutEmptyCredentials_NullPointerException() {
        IncorrectLingualeoClient = new LingualeoClient("", "");

        NullPointerException actual = assertThrows(NullPointerException.class, () -> IncorrectLingualeoClient.authCookie.getCookieStore());

        assertEquals(expectedMessage, actual.getMessage());
    }

    @Test
    void auth_PutNotTheLatinAlphabetCredentials_NullPointerException() {
        IncorrectLingualeoClient = new LingualeoClient("xaxaxa", "ухаха");

        NullPointerException actual = assertThrows(NullPointerException.class, () -> IncorrectLingualeoClient.authCookie.getCookieStore());

        assertEquals(expectedMessage, actual.getMessage());
    }

    @Test
    void getTranslates_PutCorrectWord_DataWordClass() throws IOException {
        DataWord expected = getActualResultFromFile("/response/getTranslates/getTranslates_PutCorrectWord_CorrectResponse",
                new TypeReference<>() {
                });

        List<TranslatedWord> actual = correctLingualeoClient.getTranslates("Set");
        assertEquals(expected.translate(), actual);
    }

    @Test
    void getTranslates_PutIncorrectWord_DataWordClass() throws IOException {

        DataWord actual = getActualResultFromFile("/response/getTranslates/getTranslates_PutIncorrectWord_CorrectResponse",
                new TypeReference<>() {
                });

        List<TranslatedWord> expected = correctLingualeoClient.getTranslates("fdsafasdafasdf");
        assertEquals(actual.translate(), expected);
    }

    @Test
    void getTranslates_PutNotTheLatinAlphabetWord_DataWordClass() throws IOException {

        DataWord actual = getActualResultFromFile("/response/getTranslates/getTranslates_PutNotTheLatinAlphabetWord_CorrectResponse",
                new TypeReference<>() {
                });

        List<TranslatedWord> expected = correctLingualeoClient.getTranslates("Положить");
        assertEquals(actual.translate(), expected);
    }

    @Test
    void getTranslates_PutEmptyWord_DataWordClass() throws IOException {

        DataWord actual = getActualResultFromFile("/response/getTranslates/getTranslates_PutEmptyWord_ErrorResponse", new TypeReference<>() {
        });

        List<TranslatedWord> expected = correctLingualeoClient.getTranslates("");
        assertEquals(actual.translate(), expected);
    }

    @Test
    void addWord_PutCorrectWordIfMissing_MapStringObject() throws IOException {

        correctLingualeoClient.auth();

        Map<String, Object> expected = getActualResultFromFile("/response/addWord/addWord_PutCorrectWordIfMissing_CorrectResponse",
                new TypeReference<>() {
                });

        Map<String, Object> actual = correctLingualeoClient.addWord("Set", "Положить");
        assertEquals(expected, actual);
    }

    @Test
    void addWord_PutIncorrectWordIfMissing_MapStringObject() throws IOException {
        correctLingualeoClient.auth();

        Map<String, Object> expected = getActualResultFromFile("/response/addWord/addWord_PutIncorrectWordIfMissing_CorrectResponse",
                new TypeReference<>() {
                });

        Map<String, Object> actual = correctLingualeoClient.addWord("fdasfdfasdf", "фдасфдфасдф");
        assertEquals(expected, actual);
    }

    @Test
    void addWord_PutNotTheLatinAlphabetWord_True() throws IOException {
        correctLingualeoClient.auth();

        Map<String, Object> expected = getActualResultFromFile("/response/addWord/addWord_PutNotTheLatinAlphabetWord_CorrectResponse",
                new TypeReference<>() {
                });

        Map<String, Object> actual = correctLingualeoClient.addWord("Положить", "Положить");
        assertEquals(expected, actual);
    }

    @Test
    void addWord_PutEmptyWord_True() throws IOException {
        correctLingualeoClient.auth();

        Map<String, Object> expected = getActualResultFromFile("/response/addWord/addWord_PutEmptyWord_CorrectResponse",
                new TypeReference<>() {
                });

        Map<String, Object> actual = correctLingualeoClient.addWord("", "");
        assertEquals(expected, actual);
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