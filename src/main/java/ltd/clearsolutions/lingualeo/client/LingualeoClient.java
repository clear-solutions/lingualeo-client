package ltd.clearsolutions.lingualeo.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;


public class LingualeoClient implements Lingualeo {

    private static final Logger logger = LoggerFactory.getLogger(LingualeoClient.class);

    private static final String API_URL = "https://api.lingualeo.com/";

    private final String email;
    private final String password;
    HttpClientContext authCookie;

    public LingualeoClient(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Override
    public void auth() {

        String urlParameters = "email=" + email + "&password=" + password;
        String requestUrl = API_URL + "login" + "?" + urlParameters;

        BasicCookieStore cookieStore = new BasicCookieStore();

        HttpClientContext context = HttpClientContext.create();
        context.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);

        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            client.execute(new HttpGet(requestUrl), context);

            this.authCookie = context;

        } catch (IOException e) {
            logger.info((Marker) Level.WARNING, e.getMessage());
        }
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
            logger.info((Marker) Level.WARNING, e.getMessage());
        }
        return Collections.emptyList();
    }

    @Override
    public Map<String, Object> addWord(String word, String translate) {

        String urlParameters = "word=" +
                URLEncoder.encode(word, StandardCharsets.UTF_8) + "&tword=" +
                URLEncoder.encode(translate, StandardCharsets.UTF_8);
        String requestUrl = API_URL + "addword?" + urlParameters;

        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            CloseableHttpResponse response = client.execute(new HttpGet(requestUrl), authCookie);

            String body = EntityUtils.toString(response.getEntity());

            ObjectMapper objectMapper = new ObjectMapper();

            return objectMapper.readValue(body, new TypeReference<HashMap<String, Object>>() {
            });

        } catch (IOException e) {
            logger.info((Marker) Level.WARNING, e.getMessage());
        }
        return Collections.emptyMap();
    }
}
