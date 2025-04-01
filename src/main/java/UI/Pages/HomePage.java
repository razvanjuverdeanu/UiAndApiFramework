package UI.Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HomePage {
    @Autowired
    BasePage basePage;

    By calculateBtn = By.xpath("//span[text()='Calculate']");
    By rejectCookiesBtn = By.id("onetrust-reject-all-handler");
    By originCountryDropdown = By.id("origin-country");
    By destinationCountryDropdown = By.id("destination-country");
    By originPostcodeInput = By.id("origin-postcode");
    By originPostcodeWarningMsg = By.xpath("//input[@id='origin-postcode']/following-sibling::p");
    By unableToRetrieveMsg = By.xpath("//p[contains(text(),'the online tool is unable to retrieve')]");
    By destinationPostcodeInput = By.id("destination-postcode");
    By destinationPostcodeWarningMsg = By.xpath("//input[@id='destination-postcode']/following-sibling::p");
    By datePickerCalendar = By.id("leadtime-datepicker");
    By closeChat = By.xpath("//button[@aria-label='Close Button']");
    By dhlWidgetFrame = By.id("dhl-va-widget-iframe");
    By dropdownOriginCountryOptions = By.xpath("//select[@id='origin-country']/option");
    By dropdownDestinationCountryOptions = By.xpath("//select[@id='destination-country']/option");

    public void clickCalculate() {
        basePage.clickElement(calculateBtn);
    }

    public void rejectCookies() {
        basePage.clickElement(rejectCookiesBtn);
    }

    public void setOriginPostCode(String code) {
        basePage.typeValueInTextBox(originPostcodeInput, code);
    }

    public boolean isOriginPostCodeEmpty() {
        return basePage.getElementText(originPostcodeInput).isEmpty();
    }

    public void setDestinationPostCode(String code) {
        basePage.typeValueInTextBox(destinationPostcodeInput, code);
    }

    public boolean isDestinationPostCodeEmpty() {
        return basePage.getElementText(destinationPostcodeInput).isEmpty();
    }

    public void setOriginCountryDropdownCountry(String originCountry) {
        basePage.scrollToElement(originCountryDropdown);
        basePage.setDropdownValue(originCountryDropdown, dropdownOriginCountryOptions, originCountry);
    }

    public void setDestinationCountryDropdownCountry(String destinationCountry) {
        basePage.setDropdownValue(destinationCountryDropdown, dropdownDestinationCountryOptions, destinationCountry);
    }

    public boolean isDatePickerCalendarDisplayed() {
        try {
            basePage.waitForElementToBeDisplayed(datePickerCalendar);
        }catch (Exception e){
            basePage.switchToFrame(dhlWidgetFrame);
            basePage.clickElement(closeChat);
            basePage.switchToParentFrame();
        }
        List<WebElement> elements = basePage.getElements(datePickerCalendar);
        for (WebElement el: elements){
            if(el.isDisplayed())
                return true;
        }
        return false;
    }

    public boolean isUnableToRetrieveWarningDisplayed() {
        basePage.waitForElementToBeDisplayed(unableToRetrieveMsg);
        return basePage.isElementDisplayed(unableToRetrieveMsg);
    }

    public String getUnableToRetrieveWarning() {
        basePage.waitForElementToBeDisplayed(unableToRetrieveMsg);
        return basePage.getElementText(unableToRetrieveMsg);
    }

    public String getOriginPostcodeWarning() {
        basePage.waitForElementToBeDisplayed(originPostcodeWarningMsg);
        return basePage.getElementText(originPostcodeWarningMsg);
    }

    public boolean isOriginPostcodeWarningDisplayed() {
        basePage.waitForElementToBeDisplayed(originPostcodeWarningMsg);
        return basePage.isElementDisplayed(originPostcodeWarningMsg);

    }

    public boolean isDestinationPostcodeWarningDisplayed() {
        basePage.waitForElementToBeDisplayed(destinationPostcodeWarningMsg);
        return basePage.isElementDisplayed(destinationPostcodeWarningMsg);
    }

    public String getDestinationPostcodeWarning() {
        basePage.waitForElementToBeDisplayed(destinationPostcodeWarningMsg);
        return basePage.getElementText(destinationPostcodeWarningMsg);
    }

}
