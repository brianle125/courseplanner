package ca.cmpt213.model;

import java.io.File;

public class Main {

    public static void main(String[] args) {
        String input = "data/course_data_2018.csv";
        File inputFile = new File(input);
        CourseList c = new CourseList(new CSVFileReader(inputFile));
        c.printSpecificCourse("CMPT", "250");
        c.printCourseList();
    }
}
