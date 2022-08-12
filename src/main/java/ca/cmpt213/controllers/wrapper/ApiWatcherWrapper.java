package ca.cmpt213.controllers.wrapper;

import ca.cmpt213.model.CourseInfo;

import java.util.Calendar;
import java.util.List;

public class ApiWatcherWrapper {
    public long id;
    public ApiDepartmentWrapper department;
    public ApiCourseWrapper course;
    public List<String> events;

    public ApiWatcherWrapper(long id, ApiDepartmentWrapper department, ApiCourseWrapper course, List<String> events) {
        this.id = id;
        this.department = department;
        this.course = course;
        this.events = events;
    }

    public void update(CourseInfo courseInfo) {
        Calendar c = Calendar.getInstance();
        String str = c.getTime() + ": Added section " + courseInfo.getComponentCode() + " with enrollment ("
                + courseInfo.getEnrolmentTotal() + " / " + courseInfo.getEnrolmentCap() + ") to offering "
                + getTermName(courseInfo.getSemester()) + " " + getNumericYear(courseInfo.getSemester());
        events.add(str);
    }

    public int getNumericYear(String num) {
        int[] digits = new int[3];
        int year = Integer.parseInt(num.substring(0,num.length() - 1));
        for (int i = 0; i < num.length() - 1; i++) {
            digits[i] = year % 10;
            year /= 10;
        }
        year = 1900 + 100*(digits[2]) + 10*(digits[1]) + (digits[0]);
        return year;
    }

    public String getTermName(String num) {
        String term = "";
        switch(Integer.parseInt(num) % 10) {
            case 1: term = "Spring";
                break;
            case 4: term = "Summer";
                break;
            case 7: term = "Fall";
                break;
        }
        return term;
    }

    public long getId() {
        return id;
    }

    public ApiDepartmentWrapper getDepartment() {
        return department;
    }

    public ApiCourseWrapper getCourse() {
        return course;
    }

    public List<String> getEvents() {
        return events;
    }
}
