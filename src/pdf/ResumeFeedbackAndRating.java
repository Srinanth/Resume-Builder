package pdf;

import java.util.List;

public class ResumeFeedbackAndRating {
    private List<String> suggestions;
    private List<String> weakAreas;
    private List<String> careerAdvice;
    private double rating;

    public List<String> getSuggestions() { return suggestions; }
    public void setSuggestions(List<String> suggestions) { this.suggestions = suggestions; }
    public List<String> getWeakAreas() { return weakAreas; }
    public void setWeakAreas(List<String> weakAreas) { this.weakAreas = weakAreas; }
    public List<String> getCareerAdvice() { return careerAdvice; }
    public void setCareerAdvice(List<String> careerAdvice) { this.careerAdvice = careerAdvice; }
    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }
}