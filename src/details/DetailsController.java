// src/details/DetailsController.java
package details;

public class DetailsController {
    private DetailsDAO detailsDAO;

    public DetailsController() {
        this.detailsDAO = new DetailsDAO();
    }

    public boolean saveDetails(int userId, ResumeDetails details) {
        details.setUserId(userId);
        try {
            return detailsDAO.insertOrUpdateDetails(details);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public ResumeDetails loadDetails(int userId) {
        try {
            return detailsDAO.getDetails(userId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}