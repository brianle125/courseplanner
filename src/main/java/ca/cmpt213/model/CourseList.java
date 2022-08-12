package ca.cmpt213.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Model class for list of courses.
 */

public class CourseList {
    private final List<Course> courseList = new ArrayList<>();

    public CourseList(CSVFileReader csvReader) {
        readCourses(csvReader);
    }

    public void readCourses(CSVFileReader csvReader) {
        List<String> list = csvReader.getLineList();
        list.remove(0); // Removes header
        for (String line : list) {
            CourseInfo courseInfo = getSeparatedStringArray(line);
            Course course = new Course(courseInfo);
            addCourse(course);
        }
    }

    public CourseInfo getSeparatedStringArray(String line) {
        final String separator = ",";
        String[] dataArr = line.split(separator);
        CourseInfo courseInfo = new CourseInfo(line.split(separator));
        if (courseInfo.getInstructors().equals("<null>")) {
            courseInfo.setInstructors("");
        }
        if (courseInfo.getInstructors().startsWith("\"")) {
            courseInfo.setInstructors(readMultipleInstructors(line));
            courseInfo.setComponentCode(dataArr[dataArr.length - 1]);
        }
        courseInfo.trim();
        return courseInfo;
    }

    public void addCourse(Course course) {
        int courseIndex = checkCourse(course);
        if (courseIndex < 0) {
            courseList.add(course);
        } else {
            courseList.get(courseIndex).processSemesters(new Semester(course.getCourseInfo()));
        }
    }

    public int checkCourse(Course course) {
        for (int i = 0; i < courseList.size(); i++) {
            Course c = courseList.get(i);
            if (c.getSubject().equals(course.getSubject())
                    && c.getCatalog_number().equals(course.getCatalog_number())) {
                return i;
            }
        }
        return -1;
    }

    public String readMultipleInstructors(String line) {
        String instructors = "";
        boolean foundStartQuotations = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (foundStartQuotations) {
                instructors += c;
            }
            if (c == '\"' && !foundStartQuotations) {
                foundStartQuotations = true;
            } else if (c == '\"') { // found end quotations
                break;
            }
        }
        return instructors;
    }

    public List<Course> getList() {
        return courseList;
    }

    public void printCourseList() {
        for (Course c : courseList) {
            System.out.println(c);
        }
    }

    public void printSpecificCourse(String subject, String num) {
        for (Course c : courseList) {
            if (c.getSubject().equals(subject) && c.getCatalog_number().equals(num)) {
                System.out.println(c);
                return;
            }
        }
    }
}
