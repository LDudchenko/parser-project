package parser.сonfig;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import parser.service.NewTemplateCreator;
import parser.service.Parser;
import parser.service.WorkWithFile;

import javax.annotation.PostConstruct;

@Configuration
@ComponentScan("parser.service")
public class SpringConfig {
    @PostConstruct
    public void postConstructor() {
        //System.setProperty("webdriver.chrome.driver", ".\\src\\main\\resources\\drivers\\chromedriver.exe");
        WebDriverManager.chromedriver().setup();

    }

    @Bean
    public WorkWithFile workWithFileBean(){
        return new WorkWithFile();
    }

    @Bean
    public ChromeDriver chromeDriverBean(){
        return new ChromeDriver();
    }

    @Bean
    public Parser parserBean(){
        return new Parser();
    }

    @Bean
    public NewTemplateCreator newTemplateCreatorBean(){
        return new NewTemplateCreator();
    }
}
