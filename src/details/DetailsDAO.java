// src/details/DetailsDAO.java
package details;

import utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DetailsDAO {

    public boolean insertOrUpdateDetails(ResumeDetails details) throws SQLException {
        String sql = "INSERT INTO resume_details (user_id, name, contact, education, experience, skills, projects) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE " +
                     "name = ?, contact = ?, education = ?, experience = ?, skills = ?, projects = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, details.getUserId());
            pstmt.setString(2, details.getName());
            pstmt.setString(3, details.getContact());
            pstmt.setString(4, details.getEducation());
            pstmt.setString(5, details.getExperience());
            pstmt.setString(6, details.getSkills());
            pstmt.setString(7, details.getProjects());
            pstmt.setString(8, details.getName());
            pstmt.setString(9, details.getContact());
            pstmt.setString(10, details.getEducation());
            pstmt.setString(11, details.getExperience());
            pstmt.setString(12, details.getSkills());
            pstmt.setString(13, details.getProjects());
            return pstmt.executeUpdate() > 0;
        }
    }

    public ResumeDetails getDetails(int userId) throws SQLException {
        String sql = "SELECT * FROM resume_details WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                ResumeDetails details = new ResumeDetails();
                details.setUserId(rs.getInt("user_id"));
                details.setName(rs.getString("name"));
                details.setContact(rs.getString("contact"));
                details.setEducation(rs.getString("education"));
                details.setExperience(rs.getString("experience"));
                details.setSkills(rs.getString("skills"));
                details.setProjects(rs.getString("projects"));
                return details;
            }
        }
        return null;
    }
}