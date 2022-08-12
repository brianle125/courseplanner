package ca.cmpt213.model;

public class CourseInfo {
    private String semester;
    private String subject;
    private String catalogNum;
    private String location;
    private String enrolmentCap;
    private String enrolmentTotal;
    private String instructors;
    private String componentCode;

    public CourseInfo(String[] courseInfo) {
        semester = courseInfo[0];
        subject = courseInfo[1];
        catalogNum = courseInfo[2];
        location = courseInfo[3];
        enrolmentCap = courseInfo[4];
        enrolmentTotal = courseInfo[5];
        instructors = courseInfo[6];
        componentCode = courseInfo[7];
    }

    public void trim() {
        setSemester(semester.trim());
        setSubject(subject.trim());
        setCatalogNum(catalogNum.trim());
        setLocation(location.trim());
        setEnrolmentCap(enrolmentCap.trim());
        setEnrolmentTotal(enrolmentTotal.trim());
        setInstructors(instructors.trim());
        setComponentCode(componentCode.trim());
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getCatalogNum() {
        return catalogNum;
    }

    public void setCatalogNum(String catalogNum) {
        this.catalogNum = catalogNum;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEnrolmentCap() {
        return enrolmentCap;
    }

    public void setEnrolmentCap(String enrolmentCap) {
        this.enrolmentCap = enrolmentCap;
    }

    public String getEnrolmentTotal() {
        return enrolmentTotal;
    }

    public void setEnrolmentTotal(String enrolmentTotal) {
        this.enrolmentTotal = enrolmentTotal;
    }

    public String getInstructors() {
        return instructors;
    }

    public void setInstructors(String instructors) {
        this.instructors = instructors;
    }

    public String getComponentCode() {
        return componentCode;
    }

    public void setComponentCode(String componentCode) {
        this.componentCode = componentCode;
    }
}
