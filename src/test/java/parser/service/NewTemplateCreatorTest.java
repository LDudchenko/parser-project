package parser.service;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import parser.сonfig.SpringConfig;

import javax.annotation.PreDestroy;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class NewTemplateCreatorTest {
    private static NewTemplateCreator newTemplateCreator;
    private static AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);

    @BeforeClass
    public static void beforeAll(){
        newTemplateCreator = context.getBean("newTemplateCreator", NewTemplateCreator.class);
    }

    @PreDestroy
    public void preDestroy(){
        newTemplateCreator.preDestroy();
    }

    @Test
    public void proccessLinkForBeingNameOfFileTest(){
        String link = "https://www.olx.ua/uk/nedvizhimost/garazhy-parkovki/arenda-garazhey-parkovok/borispol/";
        String expected = "httpswww.olx.uauknedvizhimostgarazhy-parkovkiarenda-garazhey-parkovokborispol";

        String actual = newTemplateCreator.proccessLinkForBeingNameOfFile(link);

        assertEquals(expected, actual);
    }

    @Test
    public void goToRealtyPageTest(){

        String realtyType = "Гаражі, парковки";
        String expected = "https://www.olx.ua/uk/nedvizhimost/garazhy-parkovki/";

        String actual = newTemplateCreator.goToRealtyPage(realtyType);

        assertEquals(expected, actual);
    }

    @Test
    public void goToRentPageTest(){
        String link = "https://www.olx.ua/uk/nedvizhimost/garazhy-parkovki/";
        String expected = "https://www.olx.ua/uk/nedvizhimost/garazhy-parkovki/arenda-garazhey-parkovok/";
        newTemplateCreator.getDriver().get(link);

        String actual = newTemplateCreator.goToRentPage();

        assertEquals(expected, actual);
    }

    @Test
    public void goToCityPageTest(){
        String expected = "https://www.olx.ua/uk/nedvizhimost/garazhy-parkovki/arenda-garazhey-parkovok/obukhov/";
        String link = "https://www.olx.ua/uk/nedvizhimost/garazhy-parkovki/arenda-garazhey-parkovok/";
        newTemplateCreator.getDriver().get(link);

        String actual = newTemplateCreator.goToCityPage("Обухів");

        assertEquals(expected, actual);
    }

    @Test
    public void createNewTemplates(){
        String url = "https://www.olx.ua/uk/nedvizhimost/garazhy-parkovki/arenda-garazhey-parkovok/obukhov/";
        String realty="Гаражі, парковки";
        String city = "Обухів";
        List<String> expected = new ArrayList<>();
        expected.add(url);
        expected.add("Тип нерухомості: "+realty+"; місто: "+city);

        newTemplateCreator.createNewTemplate(realty,city);
        List<String> actual = new ArrayList<>();
        actual.add(newTemplateCreator.getWorkWithFile().readFromFile(new File("httpswww.olx.uauknedvizhimostgarazhy-parkovkiarenda-garazhey-parkovokobukhov.txt"),1));
        actual.add(newTemplateCreator.getWorkWithFile().readFromFile(new File("httpswww.olx.uauknedvizhimostgarazhy-parkovkiarenda-garazhey-parkovokobukhov.txt"),2));

        assertEquals(expected, actual);
    }
}