// src/details/ResumeDetails.java
package details;

public class ResumeDetails {
    private int userId;
    private String name;
    private String contact;
    private String education;
    private String experience;
    private String skills;
    private String projects;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getProjects() {
        return projects;
    }

    public void setProjects(String projects) {
        this.projects = projects;
    }

    @Override
    public String toString() {
        return "ResumeDetails{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", contact='" + contact + '\'' +
                ", education='" + education + '\'' +
                ", experience='" + experience + '\'' +
                ", skills='" + skills + '\'' +
                ", projects='" + projects + '\'' +
                '}';
    }
}