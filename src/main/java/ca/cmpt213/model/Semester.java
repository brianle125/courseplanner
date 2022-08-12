package ca.cmpt213.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Model Class for a semester. Takes in the semester and the
 * locations and sections of the semester
 */

public class Semester {
    private int semester;
    private List<String> locations = new ArrayList<>();
    private List<Section> sections = new ArrayList<>();

    public Semester(CourseInfo courseInfo) {
        semester = Integer.parseInt(courseInfo.getSemester());
        locations.add(courseInfo.getLocation());
        Section s = new Section(courseInfo);

        int secIndex = checkSections(s);
        if (secIndex < 0) {
            sections.add(s);
        }
        else {
            sections.get(secIndex).addInstructors(s);
            sections.get(secIndex).addEnrollment(s);
        }
    }

    public int checkSections(Section other) {
        for (int i = 0; i < sections.size(); i++) {
            Section sec = sections.get(i);
            if (sec.getLocation().equals(other.getLocation())
                && sec.getComponent_code().equals(other.getComponent_code())) {
                return i;
            }
        }
        return -1;
    }

    public boolean hasLocation(String location) {
        for (String loc : locations) {
            if (loc.equals(location)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        String str = "";
        for (String loc : locations) {
            str += "\t" + getSemester() + " in " + loc;

            for (int i = 0; i < sections.size(); i++) {
                Section s = sections.get(i);
                if (i == 0) {
                    str += " by " + s.getInstructorsString() + "\n";
                }
                str += "\t\t" + s;
                if (i != sections.size()-1) {
                    str += "\n";
                }
            }
        }
        return str;
    }

    public int getSemester() {
        return semester;
    }

    public List<Section> getSections() {
        return sections;
    }

    public List<String> getLocations() {
        return locations;
    }
}
