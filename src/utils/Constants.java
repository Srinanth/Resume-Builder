package utils;

public class Constants {
    private static final String GEMINI_API_KEY = ""; // Replace with actual key
    private static final String GEMINI_API_ENDPOINT = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent"; // Adjust as needed

    public static String getGeminiApiKey() {
        return GEMINI_API_KEY;
    }

    public static String getGeminiApiEndpoint() {
        return GEMINI_API_ENDPOINT;
    }
}