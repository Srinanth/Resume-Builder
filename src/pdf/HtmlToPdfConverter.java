package pdf;

import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

public class HtmlToPdfConverter implements PdfConvertible {
    private static final String PDFSHIFT_API_KEY = ""; 
    private static final String PDFSHIFT_API_URL = "https://api.pdfshift.io/v3/convert/pdf";
    private static final int TIMEOUT_SECONDS = 20;

    @Override
    public String convertToPdf(String htmlContent, String fileName) throws Exception {
        if (htmlContent == null || htmlContent.isEmpty()) {
            throw new Exception("No HTML content provided for PDF conversion");
        }
        return createPdfFromHtml(htmlContent, fileName);
    }

    private String createPdfFromHtml(String htmlContent, String fileName) throws Exception {
        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        String jsonPayload = "{\"source\": \"" + htmlContent.replace("\"", "\\\"").replace("\n", "\\n") + "\"}";
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(PDFSHIFT_API_URL))
                .timeout(Duration.ofSeconds(TIMEOUT_SECONDS))
                .header("Content-Type", "application/json")
                .header("Authorization", "Basic " + java.util.Base64.getEncoder().encodeToString(("api:" + PDFSHIFT_API_KEY).getBytes(StandardCharsets.UTF_8)))
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .build();

        HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());

        if (response.statusCode() == 200 || response.statusCode() == 201) {
            try (FileOutputStream fos = new FileOutputStream(fileName)) {
                fos.write(response.body());
            }
            return new File(fileName).getAbsolutePath();
        } else {
            throw new IOException("PDFShift API error: " + response.statusCode() + " - " + new String(response.body(), StandardCharsets.UTF_8));
        }
    }
}