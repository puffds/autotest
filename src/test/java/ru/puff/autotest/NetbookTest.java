package ru.puff.autotest;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

import java.util.concurrent.TimeUnit;

public class NetbookTest {
    @Test
    public void run () throws InterruptedException {
        System.setProperty("webdriver.chrome.driver","C:\\Program Files (x86)\\Google\\Chrome\\Application\\chromedriver.exe");
        //создание экземпляра WevDriver
        WebDriver driver = new ChromeDriver();
        //Открытие окна на весь экран
        driver.manage().window().maximize();
        //Переход на страницу https://yandex.ru
        driver.get("https://yandex.ru");

        //Поиск яндекс маркета и клик не него
        driver.findElement(By.cssSelector("a.home-link[data-id=market]")).click();

        //Нведение на меню компьютеры
        Actions builder = new Actions(driver);
        builder.moveToElement(driver.findElement(By.cssSelector("li.topmenu__item[data-department=Компьютеры]>a"))).build().perform();

        TimeUnit.SECONDS.sleep(2);

        //Поиск "нотбуки" в выпавшем меню и клик
        driver.findElements(By.cssSelector("a.topmenu__subitem")).stream().filter(link -> link.getText().contains("Ноутбуки")).findFirst().get().click();

        //Клик на расширенный поиск (перейти ко всем фильтрам)
        driver.findElement(By.cssSelector("div.n-filter-panel-aside__show-more>a")).click();

        //Задание параметра поиска до 30000 рублей
        driver.findElement(By.id("glf-priceto-var")).sendKeys("30000");

        //Поиск производителей HP и Lenovov, выбор нужных чекбоксов
        driver.findElements(By.cssSelector("a.n-filter-block__item-link label")).stream().filter(label -> {
            String labelText = label.getText();
            return labelText.contains("HP") || labelText.contains("Lenovo");
        }).forEach(label -> label.click());

        //Клик на "Показать подходящие"
        driver.findElement(By.cssSelector("a.n-filter-panel-extend__controll-button_size_big")).click();

        //Сравнение количества элементов на старнице (на странице с результатами 12 элементов)
        Assert.assertEquals("Количество элементов в поиске на странице 12", 12, driver.findElements(By.cssSelector("div.n-snippet-card")).size());

        //Поиск и сохранение первого элемента на странице
        String nameFirstElem = driver.findElements(By.cssSelector("div.n-snippet-card")).get(0).findElement(By.cssSelector("a.snippet-card__header-link")).getText();

        //Поиск первого элемента в поисковой строке
        driver.findElement(By.id("header-search")).sendKeys(nameFirstElem);

        TimeUnit.SECONDS.sleep(3);

        //Клик на наш элемент, результат автозаполнения
        driver.findElement(By.cssSelector("li.suggest2-item")).click();

        TimeUnit.SECONDS.sleep(3);

        //Сравнение найденного товара с первым элементов в списке
        Assert.assertEquals("Найденный нами товар соответствует первому элементу в списке", nameFirstElem, driver.findElement(By.cssSelector("div.n-title__text")).getText());

        //Закрытие окна Google Chrome
        driver.quit();
    }
}
