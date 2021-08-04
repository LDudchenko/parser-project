package parser.service;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class Parser {
    private String URL;
    @Autowired
    private ChromeDriver driver;
    @Autowired
    private WorkWithFile workWithFile;

    @PreDestroy
    public void preDestroy(){
        driver.quit();
    }

    public int parseAllQueries() {
        String url = "";
        int count = 0;
        List<String> listOfFileNames = workWithFile.getAllFilesFromCurrentDirectory("https",".txt");
        for(String file:listOfFileNames){
            url = workWithFile.readFromFile(new File(file),1);
            parse(new File(file), new File("new"+file), url);
            count++;
        }
        return count;
    }


    public List<String> parse(File nameOfFile, File nameOfFileForNewAnnouncements, String url){
        URL = url;

        List<String> currentLinks = findLinks("a.marginright5.link.linkWithHash.detailsLink");
        List<String> contentOfFile = workWithFile.readFromFile(nameOfFile);

        List<String> newAnnouncements = findNewAnnouncement(contentOfFile, currentLinks);

        workWithFile.writeToFile(newAnnouncements, nameOfFile, true);

        workWithFile.writeToFile(newAnnouncements, nameOfFileForNewAnnouncements, false);

        return newAnnouncements;
    }

    public List<String> findLinks(String cssSelector) {
        List<WebElement> allLinks = findAllSimilarPages();

        Set<String> links = new HashSet<>();

        int quantity = 1;
        if (allLinks.size() != 0) {
            quantity = Integer.parseInt(allLinks.get(allLinks.size() - 1).getText());
        }

        for (int i = 1; i <= quantity; i++) {
            driver.get(URL + "?page=" + i);
            List<WebElement> lists = driver.findElementsByCssSelector(cssSelector);
            for (WebElement list : lists) {
                String startLink = list.getAttribute("href");
                links.add(startLink);
            }
        }
        return links.stream().collect(Collectors.toList());
    }

    public List<WebElement> findAllSimilarPages(){
        driver.get(URL);
        List<WebElement> allLinks = driver.findElementsByCssSelector("a.block.br3.brc8.large.tdnone.lheight24");
        return allLinks;
    }

    public String cutTheLastPartOfLink(String link) {
        String proccessedLink = link;
        if (link.contains(";promoted")) {
            proccessedLink = link.substring(0, link.indexOf('I'));
        }
        return proccessedLink;
    }

    public List<String> findNewAnnouncement(List<String> contentOfFile, List<String> currentAnnouncemnts) {
        List<String> newAnnouncements = new ArrayList<>();
        boolean isPresent = false;

        for (String announcement : currentAnnouncemnts) {
            String proccessedAnnouncement = cutTheLastPartOfLink(announcement);
            if(proccessedAnnouncement.equals(announcement) && contentOfFile.contains(cutTheLastPartOfLink(announcement))) {
                    continue;
            } else{
                for(String line:contentOfFile){
                    if(line.contains(proccessedAnnouncement)){
                        isPresent = true;
                        break;
                    }
                }
                if(isPresent) continue;
            }
            newAnnouncements.add(announcement);
            contentOfFile.add(announcement);
        }
        return newAnnouncements;
    }
}
