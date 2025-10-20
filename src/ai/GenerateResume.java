package ai;

import details.ResumeDetails; 

public interface GenerateResume {
    String generateResumeHTML(ResumeDetails details, String style) throws Exception;
}