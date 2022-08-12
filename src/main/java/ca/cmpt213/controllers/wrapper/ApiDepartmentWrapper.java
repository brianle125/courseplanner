package ca.cmpt213.controllers.wrapper;

public class ApiDepartmentWrapper {
    public long deptId;
    public String name;

    public ApiDepartmentWrapper(long deptId, String name) {
        this.deptId = deptId;
        this.name = name;
    }

    public long getDeptId() {
        return deptId;
    }

    public String getName() {
        return name;
    }
}
