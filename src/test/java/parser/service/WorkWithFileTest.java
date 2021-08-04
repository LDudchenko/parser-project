package parser.service;

import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class WorkWithFileTest {
    private WorkWithFile workWithFile = new WorkWithFile();

    @Test
    public void fileCreationTest1(){
        String name = "test.txt";
        File file = new File(name);

        workWithFile.fileCreation(name);

        assertTrue(file.exists());
    }

    @Test
    public void writeToFileAndReadFromFileTest(){
        List<String> lines = new ArrayList<>();
        File file = new File("test.txt");
        lines.add("test1");
        lines.add("test2");

        workWithFile.writeToFile(lines, file, false);

        assertEquals(lines, workWithFile.readFromFile(file));
    }

    @Test
    public void readFromFileTest(){
        List<String> lines = new ArrayList<>();
        File file = new File("test.txt");
        lines.add("test1");
        lines.add("test2");
        String expected = lines.get(1);
        String actual = workWithFile.readFromFile(file,2);

        assertEquals(expected, actual);
    }

    @Test
    public void isQueryExistsTest1(){
        String name = "test.txt";
        assertTrue(workWithFile.isQueryExists(name));
    }


    @Test
    public void isQueryExistsTest2(){
        String name = "test999.txt";
        assertFalse(workWithFile.isQueryExists(name));
    }

    @Test
    public void getAllFilesFromCurrentDirectory(){
        String startName = "t";
        String endName=".txt";

        List<String> fileNames = workWithFile.getAllFilesFromCurrentDirectory(startName, endName);
        boolean result = false;
        for(String fileName:fileNames){
            result = fileName.startsWith(startName)&&fileName.endsWith(endName);
        }

        assertTrue(result);
    }
}