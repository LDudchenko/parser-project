package parser.service;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.pagefactory.ByChained;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
public class NewTemplateCreator {
    @Autowired
    private WorkWithFile workWithFile;
    @Autowired
    private Parser parser;
    @Autowired
    private ChromeDriver driver;

    @PreDestroy
    public void preDestroy(){
        driver.quit();
    }

    public ChromeDriver getDriver() {
        return driver;
    }

    public WorkWithFile getWorkWithFile() {
        return workWithFile;
    }

    public void createNewTemplate(String realty, String nameOfCity) {

        goToRealtyPage(realty);
        if(!realty.equals("Подобова оренда житла")){
           goToRentPage();
        }
        goToCityPage(nameOfCity);
        String linkWithNeededInfo = driver.getCurrentUrl();

        String linkProccessedForNameOfFile = proccessLinkForBeingNameOfFile(linkWithNeededInfo);
        if (!workWithFile.isQueryExists(linkProccessedForNameOfFile)) {
            String nameOfFile = linkProccessedForNameOfFile + ".txt";
            workWithFile.fileCreation(nameOfFile);
            workWithFile.fileCreation("new"+nameOfFile);
            List<String> linesWithInfoAboutQuery = new ArrayList<>();
            linesWithInfoAboutQuery.add(linkWithNeededInfo);
            linesWithInfoAboutQuery.add("Тип нерухомості: "+realty+"; місто: "+nameOfCity+"\n");

            workWithFile.writeToFile(linesWithInfoAboutQuery, new File(nameOfFile), false);
            parser.parse(new File(nameOfFile), new File("new"+nameOfFile), workWithFile.readFromFile(new File(nameOfFile),1));
        }
    }

    public String goToRealtyPage(String realty){
        driver.get("https://www.olx.ua/uk/nedvizhimost/");
        WebElement realtyType = driver.findElement(new ByChained(By.partialLinkText(realty)));
        String link = realtyType.getAttribute("href");
        driver.get(link);
        return link;
    }

    public String goToRentPage(){
        WebElement rent =  driver.findElement(new ByChained(By.partialLinkText("ренда")));
        String link = rent.getAttribute("href");
        driver.get(link);
        return link;
    }

    public String goToCityPage(String nameOfCity){
        driver.findElement(By.cssSelector("input#cityField")).sendKeys(nameOfCity);
        sleep();
        WebElement elementOfCityList = driver.findElement(By.cssSelector("a.tdnone.title.block.c000.brtop-5.nowrap.search-choose.geo-suggest-row.search-choose-selected.search-choosed"));
        elementOfCityList.click();
        sleep();
        return driver.getCurrentUrl();
    }

    public void sleep() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String proccessLinkForBeingNameOfFile(String linkWithNeededInfo) {
        return linkWithNeededInfo.replaceAll("[/:]", "");
    }
}
