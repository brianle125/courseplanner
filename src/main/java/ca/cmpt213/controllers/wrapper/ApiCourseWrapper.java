package ca.cmpt213.controllers.wrapper;

public class ApiCourseWrapper {
    public long courseId;
    public String catalogNumber;

    public ApiCourseWrapper(long courseId, String catalogNumber) {
        this.courseId = courseId;
        this.catalogNumber = catalogNumber;
    }

    public long getCourseId() {
        return courseId;
    }

    public String getCatalogNumber() {
        return catalogNumber;
    }
}
