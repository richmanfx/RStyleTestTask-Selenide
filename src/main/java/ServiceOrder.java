/**
 * Created by Zoer (Александр Ящук) on 12.11.2016.
 *
 */

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;

import java.io.File;

import static com.codeborne.selenide.Selenide.*;


public class ServiceOrder {
    public static void main(String[] args) {

        final String searchServer = "google.com";
        final String searchSite = "http://www.performance-lab.ru/";
        final String searchPhrase1 = "performance lab";
        final String searchPhrase2 = "Услуги и продукты";
        final String searchPhrase3 = "Автоматизация тестирования";
        final String buttonLabel = "Заказать услугу";

        final String clientName = "Спиридон";
        final String clientEmail = "invalid#email";
        final String clientPhone = "8-123-456-78-90";
        final String clientCompany = "HyperSoft AG";

        final String fileName = "documentation.odt";
        final String filePath = "src" + File.separator + "main" + File.separator +
                                "resources" + File.separator + "documents";
        File uploadedFile = new File(filePath + File.separator + fileName);

        Configuration.browserSize = "1880x800";
        Configuration.timeout = 2000;

        // Использовать Chrome.
        final String PATH_TO_CHROMEDRIVER_EXE = "src" + File.separator +
                                                "main" + File.separator +
                                                "resources" + File.separator +
                                                "web_drivers" + File.separator +
                                                "chromedriver.exe";
        System.setProperty("webdriver.chrome.driver", PATH_TO_CHROMEDRIVER_EXE);
        Configuration.browser = "chrome";

        // Временно без Гугля, для скорости
        open(searchSite);
//        open("http://" + searchServer);                             // Открываем google.com
//        $(By.name("q")).setValue(searchPhrase1).pressEnter();       // Находим "performance lab"
//        $(By.xpath("//a[@href = '" + searchSite + "']")).click();   // Переходим на www.performance-lab.ru

        // Часто не разворачивает и сразу сворачивает меню
        SelenideElement element;
         do {
            element = $(By.xpath("//li[@id='menu-item-317']/a[@href='#' and contains(text(),'" + searchPhrase2 + "')]")).hover();

        // Ссылка видна, она единственная, но при клике становится невидна - печаль, не победил пока, сделал обход
//        $(By.xpath("//li[@id='menu-item-957']//a[contains(text(),'Автоматизация тестирования')]")).isDisplayed();
//        $(By.xpath("//li[contains(@id, 'menu-item-957')]//a[contains(text(),'Автоматизация тестирования')]")).click();
//        $$(By.xpath("//li[@id='menu-item-957']//a[contains(text(),'Автоматизация тестирования')]")).shouldHave(size(1));


         } while (!$(By.xpath(".//p/a[contains(text(), 'Посмотреть все услуги')]")).isDisplayed()); // В обход

        $(By.xpath(".//p/a[contains(text(), 'Посмотреть все услуги')]")).click();
        $(By.xpath("//article[@id='post-929']//a[text()='" + searchPhrase3 + "']")).click();

        // Кнопка "Заказать услугу"
        $(By.xpath("//div[contains(text(),'" + buttonLabel.split(" ")[0] + "') and " +
                         "contains(text(),'" + buttonLabel.split(" ")[1] + "')]")).click();

        // Заполнение формы
        $(By.name("your-name")).setValue(clientName);
        $(By.name("your-email")).setValue(clientEmail);
        $(By.name("your-phone")).setValue(clientPhone);
        $(By.name("your-company")).setValue(clientCompany);

        // Чекбокс выбора услуги
        $(By.xpath(".//input[@type='checkbox' and @value='" + searchPhrase3 + "']")).click();

        // Загрузка файла
        $(By.name("technical-documentation")).uploadFile(uploadedFile);

        // Кнопка отправки заявки
        $(By.xpath(".//input[@type='submit']")).click();




        int sleepTime = 10; System.out.println("Слипуем " + sleepTime + " секунд.");    sleep(sleepTime * 1000);
        close();
    }
}
