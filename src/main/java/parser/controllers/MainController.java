package parser.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import parser.entity.QueryForParsingDTO;
import parser.service.NewTemplateCreator;
import parser.service.Parser;
import parser.service.WorkWithFile;
import parser.сonfig.SpringConfig;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MainController {
    private AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
    @Autowired
    private WorkWithFile workWithFile;

    @GetMapping("/")
    public String mainPage(){
        return "main";
    }

    @GetMapping("/update")
    public String updateData(Model model){
        Parser parser = context.getBean("parser", Parser.class);
        parser.parseAllQueries();
        model.addAttribute("message", "Дані оновлені успішно.");
        return "success";
    }

    @GetMapping("/update/{nameOfFile}")
    public String updateDefiniteData(@PathVariable("nameOfFile")String nameOfFile, Model model){
        Parser parser = context.getBean("parser", Parser.class);
        String nameOfFileWithAllLinks = nameOfFile.replace("new","");
        parser.parse(new File(nameOfFileWithAllLinks), new File(nameOfFile), workWithFile.readFromFile(new File(nameOfFileWithAllLinks),1));
        model.addAttribute("message", "Дані оновлені успішно.");
        return "success";
    }

    @GetMapping("/newAnnouncements")
    public String getNewAnnouncements(Model model) throws FileNotFoundException {
        List<String> filesWithLinks = workWithFile.getAllFilesFromCurrentDirectory("https", ".txt");
        List<QueryForParsingDTO> queryForParsingList = new ArrayList<>();
        QueryForParsingDTO queryForParsingDTO;
        String description="";
        for(String fileWithLinks:filesWithLinks){
            description = workWithFile.readFromFile(new File(fileWithLinks), 2);
            queryForParsingDTO = new QueryForParsingDTO();
            queryForParsingDTO.setDescription(description);
            queryForParsingDTO.setNameOfFile("new"+fileWithLinks);
            queryForParsingList.add(queryForParsingDTO);
        }
        model.addAttribute("isExist", queryForParsingList.size()>0?true:false);
        model.addAttribute("queryForParsingList", queryForParsingList);
        return "announcements";
    }

    @GetMapping("/creating")
    public String create(){
        return "creator";
    }

    @PostMapping("/createNewQuery")
    public String createNewQuery(@ModelAttribute("queryForParsingDTO") QueryForParsingDTO queryForParsingDTO, Model model){
        NewTemplateCreator newTemplateCreator = context.getBean("newTemplateCreator", NewTemplateCreator.class);
        newTemplateCreator.createNewTemplate(queryForParsingDTO.getRealtyType(), queryForParsingDTO.getCity());
        model.addAttribute("message", "Запит створений успішно.");
        return "success";
    }

    @GetMapping("/newAnnouncements{query}")
    public String newAnnouncementAccordingToQuery(@PathVariable("query") String nameOfFile, Model model){
        List<String> newAnnouncements = workWithFile.readFromFile(new File(nameOfFile));
        int quantity = newAnnouncements.size();
        if(quantity>0) {
            model.addAttribute("quantity", quantity);
        }
        model.addAttribute("newAnnouncements", newAnnouncements);
        return "announcementsAccordingToQuery";
    }
}
