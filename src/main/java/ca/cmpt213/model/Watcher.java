package ca.cmpt213.model;

public class Watcher {
    private long deptId;
    private long courseId;

    public Watcher(long deptId, long courseId) {
        this.deptId = deptId;
        this.courseId = courseId;
    }

    public long getDeptId() {
        return deptId;
    }

    public long getCourseId() {
        return courseId;
    }
}
