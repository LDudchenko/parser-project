package parser.service;

import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class WorkWithFile {

    public void fileCreation(String fileName){
        File file = new File(fileName);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> readFromFile(File fileName) {
        List<String> contentOfFile = new ArrayList<>();
        try (FileReader fileReader = new FileReader(fileName);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String line = bufferedReader.readLine();
            while (line != null) {
                contentOfFile.add(line);
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contentOfFile;
    }

    public String readFromFile(File fileName, int numOfLine) {
        String line="";
        try (FileReader fileReader = new FileReader(fileName);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            line = bufferedReader.readLine();
            int countOfLines = 1;
            while (countOfLines != numOfLine) {
                line = bufferedReader.readLine();
                countOfLines++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return line;
    }

    public void writeToFile(List<String> lines, File nameOfFile, boolean isWriteInTheEnd) {
        try (FileWriter fileWriter = new FileWriter(nameOfFile, isWriteInTheEnd)) {
            for (String line : lines) {
                fileWriter.write(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isQueryExists(String name) {
        List<String> listOfFileNames = getAllFilesFromCurrentDirectory("", ".txt");
        if(listOfFileNames.contains(name)){
            return true;
        }
        return false;
    }

    public List<String> getAllFilesFromCurrentDirectory(String startOfNameFile, String endOfNameFile){
        File folder = new File(System.getProperty("user.dir"));
        List<String> listOfFileNames = new ArrayList<>();
        String nameOfFile;
        for (File file : folder.listFiles()) {
            nameOfFile = file.getName();
            if (nameOfFile.startsWith(startOfNameFile)&&nameOfFile.endsWith(endOfNameFile)) {
                    listOfFileNames.add(nameOfFile);
            }
        }
        return listOfFileNames;
    }
}
