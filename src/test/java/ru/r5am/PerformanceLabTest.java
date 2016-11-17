package ru.r5am;

import com.codeborne.selenide.Configuration;
import org.openqa.selenium.By;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Description;
import ru.yandex.qatools.allure.annotations.Title;

import java.io.File;
import java.io.UnsupportedEncodingException;

import static com.codeborne.selenide.Selenide.*;


/**
 * Created by Zoer on 14.11.2016.
 * Тестирование сайта "http://www.performance-lab.ru"
 */
@Description("Тестирование сайта 'www.performance-lab.ru'.")
public class PerformanceLabTest {

    @BeforeClass
    public void beforeClass() throws UnsupportedEncodingException {

        // Использовать Chrome.
        final String pathToChromedriverExe = "src" + File.separator +
                "test" + File.separator +
                "resources" + File.separator +
                "web_drivers" + File.separator +
                "chromedriver.exe";
        System.setProperty("webdriver.chrome.driver", pathToChromedriverExe);
        Configuration.browserSize = "1880x800";
//        Configuration.timeout = 2000;
        Configuration.browser = "chrome";
    }

    @Title("Тестирование отправки заявки на услугу.")
    @Test
    public void clientOrderPFLB() {

        final String searchServer = "google.com";
        final String searchSite = "http://www.performance-lab.ru/";
        final String searchPhrase = "performance lab";
        final String menuLink = "Услуги и продукты";
        final String subMenuLink = "Автоматизация тестирования";
        final String orderButton = "Заказать услугу";

        final String clientName = "Спиридон";
        final String clientEmail = "invalid#email";
        final String clientPhone = "8-123-456-78-90";
        final String clientCompany = "HyperSoft AG";

        final String documentFileName = "documentation.odt";
        final String documentFilePath = "src" + File.separator + "test" + File.separator +
                "resources" + File.separator + "documents";
        File uploadedFile = new File(documentFilePath + File.separator + documentFileName);

        // Временно без Гугля, для скорости
//        open(searchSite);

        // Через Гугль
        open("http://" + searchServer);                             // Открываем google.com
        $(By.name("q")).setValue(searchPhrase).pressEnter();        // Находим "performance lab"
        $(By.xpath("//a[@href = '" + searchSite + "']")).click();   // Переходим на www.performance-lab.ru
        switchTo().window(1);          // Переключиться на новую вкладку
//        $("h1").shouldHave(text("Выберите продукт, чтобы начать тестирование"));    // На performance?

        // Открыть меню
        menuOpen(menuLink);

        // Выбрать пункт в меню
        selectMenuItem(subMenuLink);

        // Кнопка "Заказать услугу"
        $(By.xpath("//div[contains(text(),'" + orderButton.split(" ")[0] + "') and " +
                "contains(text(),'" + orderButton.split(" ")[1] + "')]")).click();

        // Заполнение формы
        $(By.name("your-name")).setValue(clientName);
        $(By.name("your-email")).setValue(clientEmail);
        $(By.name("your-phone")).setValue(clientPhone);
        $(By.name("your-company")).setValue(clientCompany);

        // Чекбокс выбора услуги
        $(By.xpath(".//input[@type='checkbox' and @value='" + subMenuLink + "']")).click();

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

    /**
     *  Выбирает подпункт выпадающего меню
     * @param subMenuLink - текст подпункта меню
     */
    private void selectMenuItem(String subMenuLink) {
        $(By.xpath(".//div[@id='nav_top']//li[contains(@class, 'menu-item-141')]/a[contains(text(), '" +
                subMenuLink + "')]")).click();
    }

    /**
     * Открывает выпадающее меню пока не уберём мышь из окна браузера
     * @param menuItem - текст пункта меню
     */
    private void menuOpen(String menuItem) {
        String symptomOpenedMenu = "Посмотреть все услуги";
        String watcherAlert = "Убери МЫШЬ из окна браузера и нажми ESCAPE!";
        Integer alertPeriod = 3000;    // В миллисекундах
        do {
            $(By.xpath("//li[@id='menu-item-317']/a[@href='#' and contains(text(),'" + menuItem + "')]")).hover();
            if(!$(By.xpath(".//p/a[contains(text(), '" + symptomOpenedMenu + "')]")).isDisplayed()) {
                executeJavaScript("alert('" + watcherAlert + "');");
                sleep(alertPeriod);
            }
        } while (!$(By.xpath(".//p/a[contains(text(), '" + symptomOpenedMenu + "')]")).isDisplayed());
    }

    @AfterClass
    public void afterClass() {

    // Смотрим на результат глазами :-(
        int sleepTime = 5; // секунды
        System.out.println("Слипуем " + sleepTime + " секунд.");
        sleep(sleepTime * 1000);

        close();

    }
}
