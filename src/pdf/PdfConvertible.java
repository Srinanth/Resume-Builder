package pdf;

public interface PdfConvertible {
    String convertToPdf(String htmlContent, String fileName) throws Exception;
}