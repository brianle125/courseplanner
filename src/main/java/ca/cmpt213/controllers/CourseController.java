package ca.cmpt213.controllers;

import ca.cmpt213.controllers.wrapper.*;
import ca.cmpt213.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class CourseController {
    private final String input = "data/course_data_2018.csv";
    private final File inputFile = new File(input);
    private final CourseList courseList = new CourseList(new CSVFileReader(inputFile));
    private List<Course> courses = courseList.getList();
    private final List<ApiWatcherWrapper> watchers = new ArrayList<>();
    private int watchersIndex = 0;

    @GetMapping("/api/dump-model")
    @ResponseStatus(HttpStatus.OK)
    public void dumpModel() {
        courseList.printCourseList();
    }

    @GetMapping("/api/about")
    @ResponseStatus(HttpStatus.OK)
    public ApiAboutWrapper getAbout() {
        return new ApiAboutWrapper("Course Planner", "Andre Luong & Brian Le");
    }

    @GetMapping("/api/departments")
    @ResponseStatus(HttpStatus.OK)
    public List<ApiDepartmentWrapper> getDepartments() {
        List<ApiDepartmentWrapper> departments = new ArrayList<>();
        for (int i = 0; i < courses.size(); i++) {
            Course c = courses.get(i);
            if (!checkCourseExistence(departments, c)) {
                departments.add(new ApiDepartmentWrapper(i, c.getSubject()));
            }
        }
        departments.sort(Comparator.comparing(ApiDepartmentWrapper::getName));
        return departments;
    }

    @GetMapping("/api/departments/{deptId}/courses")
    @ResponseStatus(HttpStatus.OK)
    public List<ApiCourseWrapper> listCoursesFromDepartment(@PathVariable("deptId") long deptId) {
        List<ApiCourseWrapper> coursesFromDept = new ArrayList<>();
        String subject = getSubjectOfDept(deptId);

        for (int i = 0; i < courses.size(); i++) {
            Course c = courses.get(i);
            if (c.getSubject().equals(subject)) {
                coursesFromDept.add(new ApiCourseWrapper(i, c.getCatalog_number()));
            }
        }
        coursesFromDept.sort(Comparator.comparing(ApiCourseWrapper::getCatalogNumber));
        return coursesFromDept;
    }

    @GetMapping("/api/departments/{deptId}/courses/{courseId}/offerings")
    @ResponseStatus(HttpStatus.OK)
    public List<ApiCourseOfferingWrapper> listOfferingsFromCourse(@PathVariable("deptId") long deptId,
                                                                  @PathVariable("courseId") long courseId) {
        List<ApiCourseOfferingWrapper> offeringsFromCourse = new ArrayList<>();
        String subject = getSubjectOfDept(deptId);
        String catalog = getCatalogOfCourse(deptId, courseId);

        int index = 0;
        for (Course course : filterCourses(catalog, subject)) {
            for (Semester semester : course.getSemesters()) {
                for (Section section : semester.getSections()) {
                    offeringsFromCourse.add(new ApiCourseOfferingWrapper(index, section, semester));
                    index++;
                    break;
                }
            }
        }
        offeringsFromCourse.sort(Comparator.comparingLong(ApiCourseOfferingWrapper::getSemesterCode));
        return offeringsFromCourse;
    }

    @GetMapping("/api/departments/{deptId}/courses/{courseId}/offerings/{offeringId}")
    @ResponseStatus(HttpStatus.OK)
    public List<ApiOfferingSectionWrapper> listSectionsFromOfferings(@PathVariable("deptId") long deptId,
                                                                     @PathVariable("courseId") long courseId,
                                                                     @PathVariable("offeringId") long offeringId) {
        List<ApiOfferingSectionWrapper> sectionsFromOfferings = new ArrayList<>();
        List<ApiCourseOfferingWrapper> courseOfferings = listOfferingsFromCourse(deptId, courseId);
        String subject = getSubjectOfDept(deptId);
        String catalog = getCatalogOfCourse(deptId, courseId);

        ApiCourseOfferingWrapper offeringToSearch = courseOfferings.stream()
            .filter(offering -> offering.getCourseOfferingId() == offeringId)
            .findAny()
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Offering ID does not Exist"));

        for (Course c : filterCourses(catalog, subject)) {
            for (Semester semester : c.getSemesters()) {
                if (semester.getSemester() == offeringToSearch.getSemesterCode()) {
                    addSectionsFromOfferings(semester, offeringToSearch, sectionsFromOfferings);
                }
            }
        }
        return sectionsFromOfferings;
    }

    @PostMapping("/api/addoffering")
    @ResponseStatus(HttpStatus.CREATED)
    public Course addSection(@RequestBody ApiOfferingDataWrapper data) {
        String message = data.getDataMessage();
        CourseInfo courseInfo = courseList.getSeparatedStringArray(message);
        Course course = new Course(courseInfo);
        courseList.addCourse(course);
        courses = courseList.getList();
        updateWatchers(data, courseInfo);
        return course;
    }

    @GetMapping("/api/watchers")
    @ResponseStatus(HttpStatus.OK)
    public List<ApiWatcherWrapper> getWatchers() {
        return watchers;
    }

    @PostMapping("/api/watchers")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiWatcherWrapper addWatcher(@RequestBody Watcher w) {
        ApiDepartmentWrapper dept = getApiDepartmentFromId(w.getDeptId());
        ApiCourseWrapper course = getApiCourseFromId(w.getDeptId(), w.getCourseId());
        ApiWatcherWrapper watcher = new ApiWatcherWrapper(watchersIndex, dept, course, new ArrayList<>());
        watchersIndex++;
        watchers.add(watcher);
        return watcher;
    }

    @GetMapping("/api/watchers/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ApiWatcherWrapper getWatcherById(@PathVariable long id) {
        for (ApiWatcherWrapper watcher : watchers) {
            if (watcher.getId() == id) {
                return watcher;
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Watcher ID does not exist");
    }

    @DeleteMapping("/api/watchers/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeWatcher(@PathVariable long id) {
        for (ApiWatcherWrapper watcher: watchers) {
            if (watcher.getId() == id) {
                watchers.remove(watcher);
                return;
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ID does not exist");
    }

    @GetMapping("/api/stats/students-per-semester?deptId=5")


    private void updateWatchers(ApiOfferingDataWrapper data, CourseInfo courseInfo) {
        String newSubject = data.getSubjectName();
        String newCatalog = data.getCatalogNumber();

        for (ApiWatcherWrapper watcher: watchers) {
            String subject = watcher.getDepartment().getName();
            String catalog = watcher.getCourse().getCatalogNumber();
            if (subject.equals(newSubject) && catalog.equals(newCatalog)) {
                watcher.update(courseInfo);
            }
        }
    }

    private String getSubjectOfDept(long deptId) {
        ApiDepartmentWrapper deptToSearch = getApiDepartmentFromId(deptId);
        return deptToSearch.getName();
    }

    private String getCatalogOfCourse(long deptId, long courseId) {
        ApiCourseWrapper courseToSearch = getApiCourseFromId(deptId, courseId);
        return courseToSearch.getCatalogNumber();
    }

    private void addSectionsFromOfferings(Semester semester, ApiCourseOfferingWrapper offeringToSearch,
                                          List<ApiOfferingSectionWrapper> sectionsFromOfferings) {
        for (Section section : semester.getSections()) {
            if (section.getLocation().equals(offeringToSearch.getLocation())) {
                String type = section.getComponent_code();
                sectionsFromOfferings.add(new ApiOfferingSectionWrapper(type,
                        section.getEnrollment_capacity(), section.getEnrollment_total()));
            }
        }
    }

    private List<Course> filterCourses(String catalog, String subject) {
        return courses.stream()
            .filter(course -> course.getCatalog_number().equals(catalog))
            .filter(course -> course.getSubject().equals(subject))
            .collect(Collectors.toList());
    }

    private boolean checkCourseExistence(List<ApiDepartmentWrapper> list, Course course) {
        return list.stream().anyMatch(dept -> dept.getName().equals(course.getSubject()));
    }

    private ApiDepartmentWrapper getApiDepartmentFromId(long deptId) {
        for (ApiDepartmentWrapper dept : getDepartments()) {
            if (dept.getDeptId() == deptId) {
                return dept;
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Dept ID does not exist");
    }

    private ApiCourseWrapper getApiCourseFromId(long deptId, long courseId) {
        for (ApiCourseWrapper course : listCoursesFromDepartment(deptId)) {
            if (course.getCourseId() == courseId) {
                return course;
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course ID does not exist");
    }
}