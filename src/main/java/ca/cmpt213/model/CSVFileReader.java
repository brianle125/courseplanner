package ca.cmpt213.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads lines from a .CSV file and adds it to a list of Strings.
 * Takes in a file to process in the constructor.
 */

public class CSVFileReader {
    private File inputFile;
    private List<String> lineList = new ArrayList<>();
    private final int FAILURE = -1;

    public CSVFileReader(File inputFile) {
        if (!inputFile.exists() || !inputFile.getName().endsWith(".csv")) {
            System.err.println("Error! .CSV File not found.");
            System.exit(FAILURE);
        }
        this.inputFile = inputFile;
        readFromFile();
    }

    public void readFromFile() {
        String line = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(inputFile));
            while ((line = br.readLine()) != null) {
                lineList.add(line);
            }
        } catch(Exception e) {
            System.err.println("Error!");
            e.printStackTrace();
            System.exit(FAILURE);
        }
    }

    public List<String> getLineList() {
        return lineList;
    }
}
