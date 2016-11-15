import com.codeborne.selenide.Configuration;
import org.openqa.selenium.By;
import org.testng.annotations.Test;
import com.codeborne.selenide.testng.BrowserPerTest;

import java.io.File;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.sleep;

/**
 * Created by Zoer on 14.11.2016.
 * Тестирование сайта "http://www.performance-lab.ru"
 */
public class PflbTest {
    @Test
    public void clientOrderPFLB() {
        final String searchSite = "http://www.performance-lab.ru/";
        final String searchPhrase2 = "Услуги и продукты";
        final String searchPhrase3 = "Автоматизация тестирования";

        // Использовать Chrome.
        final String pathToChromedriverExe = "src" + File.separator +
                "main" + File.separator +
                "resources" + File.separator +
                "web_drivers" + File.separator +
                "chromedriver.exe";
        System.setProperty("webdriver.chrome.driver", pathToChromedriverExe);
        Configuration.browser = "chrome";

        open(searchSite);

        do {
            $(By.xpath("//li[@id='menu-item-317']/a[@href='#' and contains(text(),'" + searchPhrase2 + "')]")).hover();

        } while (!$(By.xpath(".//p/a[contains(text(), 'Посмотреть все услуги')]")).isDisplayed());

        $(By.xpath(".//div[@id='nav_top']//li[contains(@class, 'menu-item-141')]/a[contains(text(), '" +
                searchPhrase3 + "')]")).click();

        int sleepTime = 5;
        System.out.println("Слипуем " + sleepTime + " секунд.");
        sleep(sleepTime * 1000);
    }
}
