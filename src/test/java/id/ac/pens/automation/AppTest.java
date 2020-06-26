package id.ac.pens.automation;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    WebDriver webDriver;
    @Before
    public void preparingBrowser(){
        WebDriverManager.chromedriver().setup();
        webDriver = new ChromeDriver();
    }


    @Test
    public void testWonderfulQuotes(){
        webDriver.get("https://gosoft.web.id/wonderfulQuote");
        webDriver
                .findElement(By.xpath("//textarea[@placeholder='input quotes here']"))
                .sendKeys("Hello World");
        webDriver
                .findElement(By.xpath("//input[@placeholder=\"input author's name here\"]"))
                .sendKeys("Argo Triwidodo");
        Select bgColor = new Select(webDriver.findElement(By.xpath("//select[@class='form-control']")));
        bgColor.selectByVisibleText("cyan");
        webDriver
                .findElement(By.xpath("//button[@class='btn btn-primary']"))
                .click();
        String counter = webDriver.findElement(By.className("progress-bar")).getText();
        counter = counter.replace("/ 10" , "").trim();
        Integer actual = Integer.valueOf(counter);
        assertThat("The Counter is wrong" , actual , is(6));
        List<WebElement> listQuoteElements =
                webDriver.findElements(By.xpath("//div[@class='quote']/q"));

        List<String> listQuotes = listQuoteElements.stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
        System.out.println(listQuotes);
        assertThat("Your quote is not there " , listQuotes , CoreMatchers.hasItem("Hello World"));
        WebElement ourQuoteElement = listQuoteElements
                .stream()
                .filter(
                        webElement -> webElement.getText().equalsIgnoreCase("Hello World"))
                .findFirst().get();
        Actions actions = new Actions(webDriver);
        actions.moveToElement(ourQuoteElement).click().build().perform();
        listQuoteElements =
                webDriver.findElements(By.xpath("//div[@class='quote']/q"));
        listQuotes = listQuoteElements.stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
        assertThat("Your quote is not there " , listQuotes , not(CoreMatchers.hasItem("Hello World")));
    }

    @After
    public void tearDownBrowser(){
        webDriver.close();
    }

}
