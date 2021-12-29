package ltd.clearsolutions.lingualeo.client;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@WireMockTest
public class LingualeoClientTest {

    @Test
    void getTranslates_PutCorrectWord_WordTranslationDataList(WireMockRuntimeInfo wmRuntimeInfo) throws IOException {
        //Given
        var client = new LingualeoClient("login", "password", wmRuntimeInfo.getHttpBaseUrl() + "/");
        var word = "set";
        String pathToFile = "response/getTranslates/getTranslates_PutCorrectWord_CorrectResponse.json";

        stubFor(get("/gettranslates" + "?" + "word=" + word)
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(getResourceFileAsString(pathToFile))
                ));

        //When:
        List<TranslatedWord> actual = client.getTranslates(word);

        //Then:
        assertEquals(5, actual.size());
        assertEquals("положить", actual.get(0).value());
        assertEquals(24201, actual.get(0).votes());
        assertEquals(16, actual.get(0).id());
    }

    @Test
    void getTranslates_PutIncorrectWord_WordTranslationDataList(WireMockRuntimeInfo wmRuntimeInfo) throws IOException {
        //Given
        var client = new LingualeoClient("login", "password", wmRuntimeInfo.getHttpBaseUrl() + "/");
        var word = "fdsafasdafasdf";
        String pathToFile = "response/getTranslates/getTranslates_PutIncorrectWord_CorrectResponse.json";

        stubFor(get("/gettranslates" + "?" + "word=" + word)
                .willReturn(aResponse().withStatus(200)
                        .withHeader("Content-Type", "application/json;charset=UTF-8")
                        .withBody(getResourceFileAsString(pathToFile))));

        //When:
        List<TranslatedWord> actual = client.getTranslates(word);

        //Then:
        assertEquals(1, actual.size());
        assertEquals("фдсафасдафасдф", actual.get(0).value());
        assertEquals(0, actual.get(0).votes());
        assertEquals(0, actual.get(0).id());
    }

    @Test
    void addWord_PutCorrectWord_WordTranslationDataList(WireMockRuntimeInfo wmRuntimeInfo) throws IOException {
        //Given
        var client = new LingualeoClient("login", "password", wmRuntimeInfo.getHttpBaseUrl() + "/");
        var word = "set";
        var translate = "положить";
        String pathToFile = "response/addWord/addWord_PutCorrectWord_CorrectResponse.json";

        String urlParameters = "word=" +
                URLEncoder.encode(word, StandardCharsets.UTF_8) + "&tword=" +
                URLEncoder.encode(translate, StandardCharsets.UTF_8);
        String requestUrl = "/addword?" + urlParameters;

        stubFor(get(requestUrl)
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(getResourceFileAsString(pathToFile))));

        //When:
        Map<String, Object> actual = client.addWord("set", "положить");

        //Then:
        assertEquals(1, actual.get("added_translate_count"));
        assertEquals("en", ((Map<?, ?>) actual.get("lang")).get("current"));
        assertEquals("ru", ((Map<?, ?>) actual.get("lang")).get("target"));
        assertEquals("https://audiocdn.lingualeo.com/v2/3/c2V00a1e2d2a.mp3", actual.get("sound_url"));
        assertEquals(36569, actual.get("word_id"));
        assertEquals("set", actual.get("word_value"));
    }

    @Test
    void addWord_PutIncorrectWord_WordTranslationDataList(WireMockRuntimeInfo wmRuntimeInfo) throws IOException {
        //Given
        var client = new LingualeoClient("login", "password", wmRuntimeInfo.getHttpBaseUrl() + "/");
        var word = "fdasfdfasdf";
        var translate = "фдасфдфасдф";
        String pathToFile = "response/addWord/addWord_PutIncorrectWord_CorrectResponse.json";

        String urlParameters = "word=" +
                URLEncoder.encode(word, StandardCharsets.UTF_8) + "&tword=" +
                URLEncoder.encode(translate, StandardCharsets.UTF_8);
        String requestUrl = "/addword?" + urlParameters;

        stubFor(get(requestUrl)
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(getResourceFileAsString(pathToFile))));

        //When:
        Map<String, Object> actual = client.addWord("fdasfdfasdf", "фдасфдфасдф");

        //Then:
        assertEquals(1, actual.get("added_translate_count"));
        assertEquals("en", ((Map<?, ?>) actual.get("lang")).get("current"));
        assertEquals("ru", ((Map<?, ?>) actual.get("lang")).get("target"));
        assertEquals("https://audiocdn.lingualeo.com/v2/3/ZmRhc2ZkZmFzZGY=5222b0fc.mp3", actual.get("sound_url"));
        assertNull(actual.get("word_id"));
        assertEquals("fdasfdfasdf", actual.get("word_value"));
    }

    /**
     * Reads given resource file as a string.
     *
     * @param fileName path to the resource file
     * @return the file's contents
     * @throws IOException if read fails for any reason
     */
    static String getResourceFileAsString(String fileName) throws IOException {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        try (InputStream is = classLoader.getResourceAsStream(fileName)) {
            if (is == null) return "";
            try (InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
                 BufferedReader reader = new BufferedReader(isr)) {
                return reader.lines().collect(Collectors.joining(System.lineSeparator()));
            }
        }
    }
}
