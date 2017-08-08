package ru.puff.autotest;

import org.junit.Assert;
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
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://yandex.ru");

        driver.findElement(By.cssSelector("a.home-link[data-id=market]")).click();

        Actions builder = new Actions(driver);
        builder.moveToElement(driver.findElement(By.cssSelector("li.topmenu__item[data-department=Компьютеры]>a"))).build().perform();

        TimeUnit.SECONDS.sleep(2);

        driver.findElements(By.cssSelector("a.topmenu__subitem")).stream().filter(link -> link.getText().contains("Ноутбуки")).findFirst().get().click();

        driver.findElement(By.cssSelector("div.n-filter-panel-aside__show-more>a")).click();

        driver.findElement(By.id("glf-priceto-var")).sendKeys("30000");

        driver.findElements(By.cssSelector("a.n-filter-block__item-link label")).stream().filter(label -> {
            String labelText = label.getText();
            return labelText.contains("HP") || labelText.contains("Lenovo");
        }).forEach(label -> label.click());

        driver.findElement(By.cssSelector("a.n-filter-panel-extend__controll-button_size_big")).click();

        Assert.assertEquals("Количество элементов в поиске на странице 12", 12, driver.findElements(By.cssSelector("div.n-snippet-card")).size());

        WebElement firstElem = driver.findElements(By.cssSelector("div.n-snippet-card")).get(0);
        String nameFirstElem = firstElem.findElement(By.cssSelector("a.snippet-card__header-link")).getText();

        driver.findElement(By.id("header-search")).sendKeys(nameFirstElem);

        TimeUnit.SECONDS.sleep(3);

        driver.findElement(By.cssSelector("li.suggest2-item")).click();

        TimeUnit.SECONDS.sleep(3);

        Assert.assertEquals("Найденный нами товар соответствует первому элементу в списке", nameFirstElem, driver.findElement(By.cssSelector("div.n-title__text")).getText());

        driver.quit();
    }
}
