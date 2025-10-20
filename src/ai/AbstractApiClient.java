package ai;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.MediaType;
import utils.Constants;
import java.io.IOException;

public abstract class AbstractApiClient {
    protected final String apiKey;
    protected final String endpoint;
    protected final OkHttpClient httpClient;
    
    public AbstractApiClient() {
        this.apiKey = Constants.getGeminiApiKey();
        this.endpoint = Constants.getGeminiApiEndpoint();
        this.httpClient = new OkHttpClient();
    }
    
    protected abstract String sendRequest(String payload) throws Exception;
    
    protected String makeHttpRequest(Request request) throws Exception {
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("HTTP error: " + response.code() + " - " + response.message());
            }
            return response.body().string();
        }
    }
    
    protected RequestBody createRequestBody(String json) {
        return RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));
    }
}