package ca.cmpt213.controllers.wrapper;

import ca.cmpt213.model.Section;
import ca.cmpt213.model.Semester;

public class ApiCourseOfferingWrapper {
    public long courseOfferingId;
    public String location;
    public String instructors;
    public String term;
    public long semesterCode;
    public int year;

    public ApiCourseOfferingWrapper(long id, Section section, Semester semester) {
        courseOfferingId = id;
        location = section.getLocation();
        instructors = section.getInstructorsString();
        semesterCode = semester.getSemester();
        year =  getNumericYear();
        term = getTermName();
    }

    public int getNumericYear() {
        String num = String.valueOf(semesterCode);
        int[] digits = new int[3];
        int year = Integer.parseInt(num.substring(0,num.length() - 1));
        for (int i = 0; i < num.length() - 1; i++) {
            digits[i] = year % 10;
            year /= 10;
        }
        year = 1900 + 100*(digits[2]) + 10*(digits[1]) + (digits[0]);
        return year;
    }

    public String getTermName() {
        String termName = "";
        int termNum = (int) semesterCode % 10;
        switch (termNum) {
            case 1: termName = "Spring";
                break;
            case 4: termName = "Summer";
                break;
            case 7: termName = "Fall";
                break;
        }
        return termName;
    }

    public long getCourseOfferingId() {
        return courseOfferingId;
    }

    public String getLocation() {
        return location;
    }

    public String getInstructors() {
        return instructors;
    }

    public String getTerm() {
        return term;
    }

    public long getSemesterCode() {
        return semesterCode;
    }

    public int getYear() {
        return year;
    }
}