package ca.cmpt213.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Model class for section of a course.
 */

public class Section {
    private String location;
    private int enrollment_capacity;
    private int enrollment_total;
    private List<String> instructors = new ArrayList<>();
    private String component_code;

    public Section(CourseInfo courseInfo) {
        location = courseInfo.getLocation();
        enrollment_capacity = Integer.parseInt(courseInfo.getEnrolmentCap());
        enrollment_total = Integer.parseInt(courseInfo.getEnrolmentTotal());
        Collections.addAll(instructors, courseInfo.getInstructors().split(","));
        component_code = courseInfo.getComponentCode();
    }

    public void addEnrollment(Section other) {
        enrollment_capacity += other.getEnrollment_capacity();
        enrollment_total += other.getEnrollment_total();
    }

    public void addInstructors(Section other) {
        if (Collections.disjoint(getInstructors(), other.getInstructors())) {
            for (String instructor : other.getInstructors()) {
                if (!checkInstructor(instructor)) {
                    instructors.add(instructor);
                }
            }
        }
    }

    public boolean checkInstructor(String theInstructor) {
        for (String instructor : getInstructors()) {
            if (instructor.equals(theInstructor)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "TYPE=" + getComponent_code() + ", Enrollment=" + getEnrollment_total() + "/" + getEnrollment_capacity();
    }

    public String getLocation() {
        return location;
    }

    public int getEnrollment_capacity() {
        return enrollment_capacity;
    }

    public int getEnrollment_total() {
        return enrollment_total;
    }

    public List<String> getInstructors() {
        return instructors;
    }

    public String getInstructorsString() {
        String str = "";
        int listSize = getInstructors().size();
        for (int i = 0; i < listSize; i++) {
            str += instructors.get(i);
            if (i != listSize-1) {
                str += ",";
            }
        }
        return str;
    }

    public String getComponent_code() {
        return component_code;
    }
}
