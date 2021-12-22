package ltd.clearsolutions.lingualeo.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LingualeoClient implements ApiClient {

    private final String API_URL = "https://api.lingualeo.com/";

    private final String email;
    private final String password;

    private static final Logger logger = Logger.getLogger(LingualeoClient.class.getName());

    public LingualeoClient(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Override
    public Map<String, Object> auth() {

        String urlParameters = "email=" + email + "&password=" + password;
        String requestUrl = API_URL + "login" + "?" + urlParameters;

        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            CloseableHttpResponse response = client.execute(new HttpGet(requestUrl));

            String body = EntityUtils.toString(response.getEntity());

            ObjectMapper objectMapper = new ObjectMapper();

            return objectMapper.readValue(body,
                    new TypeReference<HashMap<String, Object>>() {
                    });

        } catch (IOException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
        return Collections.emptyMap();
    }

    @Override
    public List<TranslatedWord> getTranslates(String word) {
        String urlParameters = "word=" + word;
        String requestUrl = API_URL + "gettranslates" + "?" + urlParameters;

        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            CloseableHttpResponse response = client.execute(new HttpGet(requestUrl));

            String body = EntityUtils.toString(response.getEntity());

            ObjectMapper objectMapper = new ObjectMapper();

            DataWord dataWord = objectMapper.readValue(body, DataWord.class);

            return dataWord.translate();

        } catch (IOException | ParseException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
        return Collections.emptyList();
    }

    @Override
    public void addWord(String word, String translate, String context) {
        String urlParameters =
                "word=" + URLEncoder.encode(word, StandardCharsets.UTF_8) +
                        "&tword=" + URLEncoder.encode(translate, StandardCharsets.UTF_8) +
                        "&context=" + URLEncoder.encode(context, StandardCharsets.UTF_8);

        String requestUrl = API_URL + "addword?" + urlParameters;

        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            client.execute(new HttpPost(requestUrl));

        } catch (IOException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
    }
}
