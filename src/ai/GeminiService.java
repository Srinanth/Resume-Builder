package ai;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import details.ResumeDetails;
import okhttp3.Request;

public class GeminiService extends AbstractApiClient implements GenerateResume {
    private final Gson gson;
    
    public GeminiService() {
        super();
        this.gson = new Gson();
    }
    
    @Override
    public String generateResumeHTML(ResumeDetails details, String style) throws Exception {
        try {
            String prompt = buildResumePrompt(details, style);
            String response = callGeminiAPI(prompt);
            return parseResponse(response);
        } catch (Exception e) {
            System.err.println("Error generating resume with Gemini: " + e.getMessage());
            return getFallbackErrorHTML();
        }
    }
    
    private String buildResumePrompt(ResumeDetails details, String style) {
        return "Create a professional HTML resume with this data:\n\n" +
               "STYLE: " + style + "\n" +
               "NAME: " + details.getName() + "\n" +
               "CONTACT: " + details.getContact() + "\n" +
               "EDUCATION: " + formatForPrompt(details.getEducation()) + "\n" +
               "EXPERIENCE: " + formatForPrompt(details.getExperience()) + "\n" +
               "SKILLS: " + formatForPrompt(details.getSkills()) + "\n" +
               "PROJECTS: " + formatForPrompt(details.getProjects()) + "\n\n" +
               
               "REQUIREMENTS:\n" +
               "- Complete HTML document with CSS\n" +
               "- " + getStyleDescription(style) + "\n" +
               "- Professional layout with clear sections\n" +
               "- Mobile-responsive design\n" +
               "- Use semantic HTML5\n" +
               "- Enhance content with achievements/action verbs\n" +
               "- Modern, visually appealing design\n\n" +
               
               "Return ONLY HTML code, no explanations.";
    }
    
    private String formatForPrompt(String content) {
        if (content == null || content.trim().isEmpty()) {
            return "Not specified";
        }
        if (content.length() > 500) {
            content = content.substring(0, 500) + "...";
        }
        return content.replace("\n", " | ");
    }
    
    private String getStyleDescription(String style) {
        switch (style.toLowerCase()) {
            case "modern":
                return "Modern: Clean sans-serif fonts, vibrant colors (#2c3e50, #3498db), CSS Grid, subtle shadows, professional spacing";
            case "classic":
                return "Classic: Serif fonts, traditional layout, elegant typography, conservative colors (#1a1a1a, #8b7355)";
            case "minimalist":
                return "Minimalist: Maximum white space, minimal colors, clean lines, focus on content";
            case "creative":
                return "Creative: Bold colors, unique layout, gradients, modern fonts, portfolio-style";
            default:
                return "Professional: Clean layout, balanced colors, readable fonts, ATS-friendly";
        }
    }
    
    private String callGeminiAPI(String prompt) throws Exception {
        JsonObject requestBody = new JsonObject();
        
        JsonObject part = new JsonObject();
        part.addProperty("text", prompt);
        
        JsonObject content = new JsonObject();
        content.add("parts", gson.toJsonTree(new JsonObject[]{part}));
        
        requestBody.add("contents", gson.toJsonTree(new JsonObject[]{content}));
        
        JsonObject generationConfig = new JsonObject();
        generationConfig.addProperty("temperature", 0.3); 
        generationConfig.addProperty("maxOutputTokens", 4096);
        
        requestBody.add("generationConfig", generationConfig);
        
        return sendRequest(requestBody.toString());
    }
    
    @Override
    protected String sendRequest(String payload) throws Exception {
        JsonObject requestBody = JsonParser.parseString(payload).getAsJsonObject();
        
        Request request = new Request.Builder()
            .url(endpoint + "?key=" + apiKey)
            .post(createRequestBody(requestBody.toString()))
            .addHeader("Content-Type", "application/json")
            .build();
            
        return makeHttpRequest(request);
    }
    
    protected String parseResponse(String response) {
        try {
            JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();
            
            if (jsonResponse.has("candidates")) {
                String text = jsonResponse.getAsJsonArray("candidates")
                    .get(0).getAsJsonObject()
                    .getAsJsonObject("content")
                    .getAsJsonArray("parts")
                    .get(0).getAsJsonObject()
                    .get("text").getAsString();
                    
                return cleanHTMLResponse(text);
            } else if (jsonResponse.has("promptFeedback")) {
                throw new Exception("API feedback: " + jsonResponse.getAsJsonObject("promptFeedback"));
            } else {
                throw new Exception("Unexpected API response format");
            }
        } catch (Exception e) {
            System.err.println("Error parsing Gemini response: " + e.getMessage());
            return getFallbackErrorHTML();
        }
    }
    
    private String cleanHTMLResponse(String html) {
        html = html.replaceAll("(?i)```html", "")
                   .replaceAll("```", "")
                   .trim();
        
        if (!html.toLowerCase().contains("<!doctype html>") && 
            !html.toLowerCase().contains("<html")) {
            html = "<!DOCTYPE html>\n<html>\n<head>\n<title>Resume</title>\n</head>\n<body>\n" + 
                   html + "\n</body>\n</html>";
        }
        
        return html;
    }
    
    protected String getFallbackErrorHTML() {
        return "<!DOCTYPE html>" +
               "<html>" +
               "<head><title>Resume</title>" +
               "<style>" +
               "body { font-family: Arial, sans-serif; margin: 40px; line-height: 1.6; max-width: 800px; margin: 0 auto; padding: 20px; }" +
               ".error { color: #e74c3c; padding: 20px; border-left: 4px solid #e74c3c; background: #fdf2f2; }" +
               "</style>" +
               "</head>" +
               "<body>" +
               "<div class='error'>" +
               "<h1>Resume Generation Temporarily Unavailable</h1>" +
               "<p>We encountered an error generating your resume with AI. Please try again later or use a basic template.</p>" +
               "</div>" +
               "</body>" +
               "</html>";
    }
}