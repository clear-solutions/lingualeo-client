package ltd.clearsolutions.lingualeo.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LingualeoClientIntegrationTest {

    LingualeoClient lingualeoClient = new LingualeoClient("vasyll.danylenko@gmail.com", "6485644Df");
    ObjectMapper objectMapper = new ObjectMapper();

    //todo
    @Test
    void auth_PutCorrectCredentials_True() {
        lingualeoClient.auth();
    }

    @Test
    void auth_PutIncorrectCredentials_False() {
        LingualeoClient lingualeoClient = new LingualeoClient("vasyll.danyleil.com", "64");
        lingualeoClient.auth();
    }

    @Test
    void auth_PutEmptyCredentials_False() {
        LingualeoClient lingualeoClient = new LingualeoClient("", "");
        lingualeoClient.auth();
    }

    @Test
    void auth_PutNotTheLatinAlphabetCredentials_False() {
        LingualeoClient lingualeoClient = new LingualeoClient("xaxaxa", "ухаха");
        lingualeoClient.auth();
    }

    // Completed.
    @Test
    void getTranslates_PutCorrectWord_DataWordClass() throws IOException {
        DataWord actual = getActualResultFromFile("/response/getTranslates/translatesCorrectResponse");

        List<TranslatedWord> expected = lingualeoClient.getTranslates("Cat");
        assertEquals(actual.translate(), expected);
    }

    @Test
    void getTranslates_PutIncorrectWord_DataWordClass() throws IOException {

        DataWord actual = getActualResultFromFile("/response/getTranslates/translatesIncorrectResponse");

        List<TranslatedWord> expected = lingualeoClient.getTranslates("fdsafasdafasdf");
        assertEquals(actual.translate(), expected);
    }

    @Test
    void getTranslates_PutNotTheLatinAlphabetWord_DataWordClass() throws IOException {

        DataWord actual = getActualResultFromFile("/response/getTranslates/translatesNotTheLatinAlphabetResponse");

        List<TranslatedWord> expected = lingualeoClient.getTranslates("Кот1");
        assertEquals(actual.translate(), expected);
    }

    @Test
    void getTranslates_PutEmptyWord_DataWordClass() throws IOException {

        DataWord actual = getActualResultFromFile("/response/getTranslates/translatesEmptyResponse");

        List<TranslatedWord> expected = lingualeoClient.getTranslates("");
        assertEquals(actual.translate(), expected);
    }

    // todo
    @Test
    void addWord_PutCorrectWord_True() {

    }

    @Test
    void addWord_PutIncorrectWord_True() {

    }

    @Test
    void addWord_PutNotTheLatinAlphabetWord_True() {

    }

    @Test
    void addWord_PutEmptyWord_True() {

    }

    private DataWord getActualResultFromFile(String fileName) throws IOException {
        InputStream inputFile = getClass().getResourceAsStream(fileName);
        BufferedReader readFile = null;
        if (inputFile != null) {
            readFile = new BufferedReader(new InputStreamReader(inputFile, StandardCharsets.UTF_8));
        }

        return objectMapper.readValue(readFile, DataWord.class);
    }
}