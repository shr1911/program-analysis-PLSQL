package com.demo.plsqlprogramanalysis.cfg;

import java.io.*;
import java.util.ArrayList;
/*
* This class to read input file and do pre-processing on it.
* */
public class InputCodeReader {
    // Read all sentences of input file here
    // Store them in a sentence(str), line number format
    // Return object of type ArrayList<Statement>

    public ArrayList<String> inputFileRead(){
        System.out.println("Reading source of PL/SQL to be analyzed...");
        ArrayList<String> lineList = new ArrayList<>();

        StringBuilder out = new StringBuilder();
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream("5. multiple-taint-present-example.txt");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lineList.add(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lineList;
    }
}
