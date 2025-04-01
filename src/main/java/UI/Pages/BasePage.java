package UI.Pages;

import UI.Driver.DhlDriver;
import UI.Utils.Helpers;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BasePage {
    @Autowired
    DhlDriver driver;

    @Autowired
    Helpers helpers;

    public void waitForElementToBeDisplayed(By locator) {

        helpers.waitCondition(() -> driver.getDriver().findElement(locator).isDisplayed());
    }

    public boolean isElementDisplayed(By locator) {
        return driver.getDriver().findElement(locator).isDisplayed();

    }

    public void switchToFrame(By locator) {
        WebElement element = driver.getDriver().findElement(locator);
        driver.getDriver().switchTo().frame(element);
    }

    public void switchToParentFrame() {
        driver.getDriver().switchTo().parentFrame();
    }

    public void waitUntilValueIsSelected(By locator) {
        helpers.waitCondition(() -> !driver.getDriver().findElement(locator).getText().isEmpty());
    }

    public void clickElement(By locator) {
        driver.getDriver().findElement(locator).click();
    }

    public void typeValueInTextBox(By locator, String text) {
        driver.getDriver().findElement(locator).sendKeys(text);
    }

    public String getElementText(By locator) {
        return driver.getDriver().findElement(locator).getText();
    }

    public List<WebElement> getElements(By locator) {
        return driver.getDriver().findElements(locator);
    }

    public void setDropdownValue(By selectLocator, By optionLocator, String value) {
        String selectedCountry = driver.getDriver().findElement(optionLocator).getAccessibleName();
        if (!selectedCountry.equalsIgnoreCase(value)) {
            clickElement(selectLocator);
            driver.getDriver().findElements(optionLocator).stream()
                    .filter(country -> country.getText().equalsIgnoreCase(value))
                    .findFirst().get().click();
            waitUntilValueIsSelected(selectLocator);
        }
    }

    public void scrollToElement(By locator) {
        JavascriptExecutor js = (JavascriptExecutor) driver.getDriver();
        js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", driver.getDriver().findElement(locator));
    }


}
