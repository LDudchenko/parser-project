package parser.service;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import parser.сonfig.SpringConfig;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ParserTest {
    private static Parser parser;
    private static AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);

    @BeforeClass
    public static void beforeAll(){
        parser = context.getBean("parser", Parser.class);
    }

    @Test
    public void parseAllQueriesTest(){
        WorkWithFile workWithFile = new WorkWithFile();
        int expected = workWithFile.getAllFilesFromCurrentDirectory("https", ".txt").size();

        int actual = parser.parseAllQueries();

        assertEquals(expected, actual);
    }

    @Test
    public void cutTheLastPartOfLinkTest1(){
        String link = "https://www.olx.ua/d/uk/obyavlenie/arenda-garazha-v-borispole-IDMb4Jn.html#27882372d9;promoted";
        String expected = "https://www.olx.ua/d/uk/obyavlenie/arenda-garazha-v-borispole-";

        String actual = parser.cutTheLastPartOfLink(link);

        assertEquals(actual, expected);
    }


    @Test
    public void cutTheLastPartOfLinkTest2(){
        String link = "https://www.olx.ua/d/uk/obyavlenie/arenda-garazha-v-borispole-IDMb4Jn.html#27882372d9";
        String expected = "https://www.olx.ua/d/uk/obyavlenie/arenda-garazha-v-borispole-IDMb4Jn.html#27882372d9";

        String actual = parser.cutTheLastPartOfLink(link);

        assertEquals(actual, expected);
    }

    @Test
    public void findNewAnnouncementsTest(){
        List<String> contentOfFile = new ArrayList<>();
        contentOfFile.add("test1");
        contentOfFile.add("test2");
        contentOfFile.add("test3");

        List<String> currentLinks = new ArrayList<>();
        currentLinks.add("test1");
        currentLinks.add("test4");
        currentLinks.add("test5-I12345;promoted");
        currentLinks.add("test5-I67890;promoted");

        List<String> expected = new ArrayList<>();
        expected.add("test4");
        expected.add("test5-I12345;promoted");

        List<String> actual = parser.findNewAnnouncement(contentOfFile, currentLinks);
        assertEquals(expected, actual);
    }
}