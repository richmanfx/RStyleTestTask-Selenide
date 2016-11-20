package ru.r5am;

import com.codeborne.selenide.Configuration;
import org.openqa.selenium.By;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.*;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;


/**
 * Created by Zoer on 14.11.2016.
 * Тестирование сайта "http://www.performance-lab.ru"
 */

@Title("Тестирование сайта 'www.performance-lab.ru'.")
@Description("Тестирование сайта 'www.performance-lab.ru'.")
public class PerformanceLabTest {

    /**
     * Использовать Chrome.
     */
    @BeforeClass
    public void  browserInitialize() {
        final String pathToChromedriverExe = "src" + File.separator +
                                             "test" + File.separator +
                                             "resources" + File.separator +
                                             "web_drivers" + File.separator +
                                             "chromedriver.exe";
        System.setProperty("webdriver.chrome.driver", pathToChromedriverExe);
        Configuration.browserSize = "1880x800";
        Configuration.browser = "chrome";
    }

    @Title("Открытие поискового сервера, поиск по фразе.")
    @Step
    @Test(priority=1)
    public void searchServerOpen() {
        String searchServer = "google.com";
        String searchPhrase = "performance lab";
        open("http://" + searchServer);
        $(By.name("q")).setValue(searchPhrase).pressEnter();
    }

    @Title("Поиск и открытие сайта 'www.performance-lab.ru'.")
    @Step
    @Test(priority=2)
    public void searchAndJumpToWebsite(){
        System.out.println("");
        String searchSite = "http://www.performance-lab.ru/";
        $(By.xpath("//a[@href = '" + searchSite + "']")).click();
        switchTo().window(1);   // Переключиться на новую вкладку
        $("h1").shouldHave(text("Выберите продукт, чтобы начать тестирование"));
    }

    @Title("Переход в меню 'Услуги и продукт -> Автоматизация тестирования'.")
    @Step
    @Test(priority = 3)
    public void menuJump() {
        String menuItem = "Услуги и продукты";
        String symptomOpenedMenu = "Посмотреть все услуги";
        String watcherAlert = "Убери МЫШЬ из окна браузера и нажми ESCAPE!";
        Integer alertPeriod = 3000;    // В миллисекундах

        // Открывает выпадающее меню пока не уберём мышь из окна браузера
        do {
            $(By.xpath("//li[@id='menu-item-317']/a[@href='#' and contains(text(),'" + menuItem + "')]")).hover();
            if (!$(By.xpath(".//p/a[contains(text(), '" + symptomOpenedMenu + "')]")).isDisplayed()) {
                executeJavaScript("alert('" + watcherAlert + "');");
                sleep(alertPeriod);
            }
        } while (!$(By.xpath(".//p/a[contains(text(), '" + symptomOpenedMenu + "')]")).isDisplayed());

        // Выбор подпункта выпадающего меню
        String subMenuItem = "Автоматизация тестирования";
        $(By.xpath(".//div[@id='nav_top']//li[contains(@class, 'menu-item-141')]/a[contains(text(), '" +
                   subMenuItem + "')]")).click();
    }

    @Title("Нажатие кнопки 'Заказать услугу'.")
    @Step
    @Test(priority = 4)
    public void orderButtonClick(){
        String orderButton = "Заказать услугу";
        $(By.xpath("//div[contains(text(),'" + orderButton.split(" ")[0] + "') and " +
                   "contains(text(),'" + orderButton.split(" ")[1] + "')]")).click();
    }

    @Title("Заполнение формы, прикладывание файла документации.")
    @Step("Step: Заполнение формы, прикладывание файла документации.")
    @Test(priority = 5)
    public void formFilling() throws URISyntaxException {
        String checkBoxName = "Автоматизация тестирования";
        String clientName = "Спиридон";
        String clientEmail = "invalid#email.trash";
        String clientPhone = "8-111-222-33-44";
        String clientCompany = "HyperSoft AG";

        // Файл документации
        String documentFileName = "documentation.pdf";
        String documentPath = "documents";
        URL uploadedDocument = PerformanceLabTest.class.getResource(
            "/" + documentPath + File.separator + documentFileName
        );
        File uploadedFile = new File(uploadedDocument.toURI());

        // Прикрепить документ к Allure-отчёту
        saveDocumentAttach(uploadedFile);


        // Заполнение полей
        $(By.name("your-name")).setValue(clientName);
        $(By.name("your-email")).setValue(clientEmail);
        $(By.name("your-phone")).setValue(clientPhone);
        $(By.name("your-company")).setValue(clientCompany);

        // Чекбокс выбора услуги
        $(By.xpath(".//input[@type='checkbox' and @value='" + checkBoxName + "']")).click();

        // Загрузка файла
        $(By.name("technical-documentation")).uploadFile(uploadedFile);
    }

    /**
     * Прикрепление файла к Allure-отчёту
     * @param attachFile - прикрепляемый к отчёту файл
     */
    @Attachment(value = "Загружаемый файл документации", type = "application/pdf")
    private static byte[] saveDocumentAttach(File attachFile) {
        try {
            return toByteArray(attachFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    private static byte[] toByteArray(File file) throws IOException {
        return Files.readAllBytes(Paths.get(file.getPath()));
    }

    @Title("Отправка заявки, проверка неотправлености заявки.")
    @Step
    @Test(priority = 6)
    public void orderSending() {
        // Кнопка отправки заявки
        $(By.xpath(".//input[@type='submit']")).click();

        // Проверка того, что заявка не отправилась
        $(By.xpath(".//div[contains(@class, 'validation-errors') and " +
                "contains(text(), 'Проверьте правильность введенных данных.')] ")).isDisplayed();

        // Есть ли сообщение про Е-мейл
        $(By.xpath(".//div[contains(@class, 'not-valid-tip') and " +
                "contains(text(), 'Адрес e-mail')] ")).isDisplayed();
    }

//    @Attachment(value = "Page screenshot", type = "image/png")
//    public byte[] saveScreenshot(byte[] screenShot) {
//        return screenShot;
//    }

    @AfterClass
    public void browserClose() {

    // Смотрим на результат глазами :-(
        int sleepTime = 5; // секунды
        System.out.println("Слипуем " + sleepTime + " секунд.");
        sleep(sleepTime * 1000);

        close();

    }
}
