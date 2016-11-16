import java.io.File;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.codeborne.selenide.Configuration;
import org.openqa.selenium.By;
import static com.codeborne.selenide.Selenide.*;

/**
 * Created by Zoer on 14.11.2016.
 * Тестирование сайта "http://www.performance-lab.ru"
 */

public class PerformanceLabTest {

    @Title("First cool check")
    @Test
    public void clientOrderPFLB() {

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
        final String filePath = "src" + File.separator + "test" + File.separator +
                "resources" + File.separator + "documents";
        File uploadedFile = new File(filePath + File.separator + fileName);




        // Временно без Гугля, для скорости
        open(searchSite);
//        open("http://" + searchServer);                             // Открываем google.com
//        $(By.name("q")).setValue(searchPhrase1).pressEnter();       // Находим "performance lab"
//        $(By.xpath("//a[@href = '" + searchSite + "']")).click();   // Переходим на www.performance-lab.ru

        // Пытаемся открыть меню пока не уберём мышь из окна браузера
        do {
            $(By.xpath("//li[@id='menu-item-317']/a[@href='#' and contains(text(),'" + searchPhrase2 + "')]")).hover();
        } while (!$(By.xpath(".//p/a[contains(text(), 'Посмотреть все услуги')]")).isDisplayed());

        // Выбрать пункт в меню
        $(By.xpath(".//div[@id='nav_top']//li[contains(@class, 'menu-item-141')]/a[contains(text(), '" +
                searchPhrase3 + "')]")).click();

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

        // Проверка того, что заявка не отправилась
        $(By.xpath(".//div[contains(@class, 'validation-errors') and " +
                   "contains(text(), 'Проверьте правильность введенных данных.')] ")).isDisplayed();

        // Есть ли сообщение про Е-мейл
        $(By.xpath(".//div[contains(@class, 'not-valid-tip') and " +
                   "contains(text(), 'Адрес e-mail')] ")).isDisplayed();
//        <span role="alert" class="wpcf7-not-valid-tip">Адрес e-mail, введенный отправителем, неверен.</span>
    }

    @BeforeMethod
    public void beforeMethod() throws UnsupportedEncodingException {

        // Для вывода русских сообщений в консоль Windows
//        PrintStream ps = new PrintStream(System.out, true, "CP866");

        // Использовать Chrome.
        final String pathToChromedriverExe = "src" + File.separator +
                "test" + File.separator +
                "resources" + File.separator +
                "web_drivers" + File.separator +
                "chromedriver.exe";
        System.setProperty("webdriver.chrome.driver", pathToChromedriverExe);
        Configuration.browserSize = "1880x800";
        Configuration.timeout = 2000;
        Configuration.browser = "chrome";
    }

    @AfterMethod
    public void afterMethod() {

        // Смотрим на результат глазами :-(
        int sleepTime = 10; // секунды
        System.out.println("Слипуем " + sleepTime + " секунд.");
        sleep(sleepTime * 1000);

        close();

    }
}
