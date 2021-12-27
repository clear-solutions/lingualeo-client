package ltd.clearsolutions.lingualeo.client;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@WireMockTest
public class LingualeoClientTest {

    @Test
    void getTranslates_PutCorrectWord_WordTranslationDataList(WireMockRuntimeInfo wmRuntimeInfo) throws IOException {
        //Given
        var client = new LingualeoClient("login", "password", wmRuntimeInfo.getHttpBaseUrl() + "/");
        var word = "set";
        stubFor(get("/gettranslates" + "?" + "word=" + word)
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(getResourceFileAsString("response/getTranslates/getTranslates_PutCorrectWord_CorrectResponse.json"))
                )
        );

        //When:
        List<TranslatedWord> actual = client.getTranslates(word);

        //Then:
        assertEquals(5, actual.size());
        assertEquals("положить", actual.get(0).value());
        assertEquals(24201, actual.get(0).votes());
        assertEquals(16, actual.get(0).id());

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
            if (is == null) return null;
            try (InputStreamReader isr = new InputStreamReader(is);
                 BufferedReader reader = new BufferedReader(isr)) {
                return reader.lines().collect(Collectors.joining(System.lineSeparator()));
            }
        }
    }
}
