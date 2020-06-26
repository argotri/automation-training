package id.ac.pens.automation;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.hamcrest.CoreMatchers;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;

public class WonderfulQuoteSteps {

    WebDriver webDriver;
    List<WebElement> listQuoteElements;
    List<String> listQuotes;

    @Before
    public void settingUp(){
        WebDriverManager.chromedriver().setup();
        webDriver = new ChromeDriver();
    }

    @After
    public void tearDown(){
        webDriver.close();
    }

    @Given("user open wonderful quote")
    public void userOpenWonderfulQuote() {
        webDriver.get("https://gosoft.web.id/wonderfulQuote");
    }

    @When("user add {string} quote with author {string}")
    public void userAddQuoteWithAuthor(String arg0, String arg1) {
        webDriver
                .findElement(By.xpath("//textarea[@placeholder='input quotes here']"))
                .sendKeys(arg0);
        webDriver
                .findElement(By.xpath("//input[@placeholder=\"input author's name here\"]"))
                .sendKeys(arg1);
        Select bgColor = new Select(webDriver.findElement(By.xpath("//select[@class='form-control']")));
        bgColor.selectByVisibleText("cyan");
        webDriver
                .findElement(By.xpath("//button[@class='btn btn-primary']"))
                .click();
    }


    @Then("user should able to see {string} quote and the number should be {string}")
    public void userShouldAbleToSeeQuoteAndTheNumberShouldBe(String arg0, String arg1) {
        String counter = webDriver.findElement(By.className("progress-bar")).getText();
        counter = counter.replace("/ 10" , "").trim();
        Integer actual = Integer.valueOf(counter);
        assertThat("The Counter is wrong" , actual , is(Integer.valueOf(arg1)));

        listQuoteElements =
                webDriver.findElements(By.xpath("//div[@class='quote']/q"));
        listQuotes = listQuoteElements.stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
        System.out.println(listQuotes);
        assertThat("Your quote is not there " , listQuotes , CoreMatchers.hasItem(arg0));
    }

    @When("user click on quote {string} panel")
    public void userClickOnQuotePanel(String arg0) {
        WebElement ourQuoteElement = listQuoteElements
                .stream()
                .filter(
                        webElement -> webElement.getText().equalsIgnoreCase(arg0))
                .findFirst().get();
        Actions actions = new Actions(webDriver);
        actions.moveToElement(ourQuoteElement).click().build().perform();
    }

    @Then("user should not see {string} quote")
    public void userShouldNotSeeQuote(String arg0) {
        listQuoteElements =
                webDriver.findElements(By.xpath("//div[@class='quote']/q"));
        listQuotes = listQuoteElements.stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
        assertThat("Your quote is not there " , listQuotes , not(CoreMatchers.hasItem(arg0)));
    }


}
