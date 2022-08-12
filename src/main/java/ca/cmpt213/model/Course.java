package ca.cmpt213.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Model class for a course.
 */

public class Course {
    private String subject;
    private String catalog_number;
    private List<Semester> semesters = new ArrayList<>();
    CourseInfo courseInfo;

    public Course(CourseInfo courseInfo) {
        this.courseInfo = courseInfo;
        subject = courseInfo.getSubject();
        catalog_number = courseInfo.getCatalogNum();
        addSemester(new Semester(courseInfo));
    }

    // Adds semester or edits an existing one
    public void processSemesters(Semester other) {
        int semIndex = checkSemester(other);
        if (semIndex < 0) {
            semesters.add(other);
        } else {
            Section section = other.getSections().get(0);
            int secIndex = semesters.get(semIndex).checkSections(section);
            if (secIndex < 0) {
                semesters.get(semIndex).getSections().add(section);
            } else {
                semesters.get(semIndex).getSections().get(secIndex).addInstructors(section);
                semesters.get(semIndex).getSections().get(secIndex).addEnrollment(section);
            }
        }
    }

    public int checkSemester(Semester other) {
        for (int i = 0; i < semesters.size(); i++) {
            if (semesters.get(i).getSemester() == other.getSemester()
                && semesters.get(i).getLocations().equals(other.getLocations())) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public String toString() {
        String header = subject + " " + catalog_number + "\n";
        String body = "";
        for (Semester s : semesters) {
            body += s + "\n";
        }
        return header + body;
    }

    public List<Semester> getSemesters() {
        return semesters;
    }

    public String getSubject() {
        return subject;
    }

    public String getCatalog_number() {
        return catalog_number;
    }

    public void addSemester(Semester s) {
        semesters.add(s);
    }

    public CourseInfo getCourseInfo() {
        return courseInfo;
    }
}
