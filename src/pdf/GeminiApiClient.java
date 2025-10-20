package pdf;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.Request;
import okhttp3.RequestBody;
import ai.AbstractApiClient;

public class GeminiApiClient extends AbstractApiClient implements AIAnalyzable {
    private final Gson gson;

    public GeminiApiClient() {
        super();
        this.gson = new Gson();
    }

    @Override
    public ResumeFeedbackAndRating analyzeResume(String resumeData) throws Exception {
        String prompt = "Analyze this resume and provide feedback:\n" + resumeData +
                       "\nProvide feedback in JSON format with these fields: " +
                       "suggestions (array of strings), weakAreas (array of strings), " +
                       "careerAdvice (array of strings), rating (number between 0-10).";

        JsonObject requestBody = new JsonObject();
        JsonObject contents = new JsonObject();
        JsonObject part = new JsonObject();

        part.addProperty("text", prompt);
        JsonArray partsArray = new JsonArray();
        partsArray.add(part);

        contents.add("parts", partsArray);
        JsonArray contentsArray = new JsonArray();
        contentsArray.add(contents);

        requestBody.add("contents", contentsArray);

        String response = sendRequest(requestBody.toString());
        return parseResponse(response);
    }

    @Override
    protected String sendRequest(String payload) throws Exception {
        JsonObject requestBody = JsonParser.parseString(payload).getAsJsonObject();

        Request request = new Request.Builder()
            .url(endpoint + "?key=" + apiKey)
            .post(RequestBody.create(requestBody.toString(), okhttp3.MediaType.parse("application/json")))
            .build();

        return makeHttpRequest(request);
    }

    private ResumeFeedbackAndRating parseResponse(String response) throws Exception {
        try {
            JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();
            JsonArray candidates = jsonResponse.getAsJsonArray("candidates");
            if (candidates == null || candidates.size() == 0) {
                throw new Exception("No candidates found in response");
            }

            String text = candidates.get(0).getAsJsonObject()
                .getAsJsonObject("content")
                .getAsJsonArray("parts")
                .get(0).getAsJsonObject()
                .get("text").getAsString();

            String jsonStr = text.replaceAll("```json", "").replaceAll("```", "").trim();
            return gson.fromJson(jsonStr, ResumeFeedbackAndRating.class);
        } catch (Exception e) {
            System.err.println("Error parsing analysis response: " + e.getMessage());
            throw new Exception("Failed to parse resume analysis response", e);
        }
    }
}