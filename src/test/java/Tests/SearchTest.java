package Tests;

import API.Request.GetPostalCodes;
import UI.Config.ApplicationContextConfig;
import UI.Driver.DhlDriver;
import UI.Pages.HomePage;
import UI.Utils.Helpers;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Random;

import static org.testng.Assert.assertTrue;

@ContextConfiguration(classes = ApplicationContextConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class SearchTest extends AbstractTestNGSpringContextTests {

    public static final String CORRECT_POSTAL_CODE_WARNING = "Correct postal code";
    public static final String UNABLE_TO_RETRIEVE = "Unfortunately the online tool is unable to retrieve data for the specified shipment";
    public static final String FIVE_SPACES = "     ";
    @Autowired
    public HomePage homePage;
    @Autowired
    public DhlDriver driver;
    @Autowired
    Helpers helper;
    @Autowired
    GetPostalCodes getPostalCodes;

    @DataProvider(name = "dhl-dp")
    public static Object[][] dataProviderMethod() {
        return new Object[][]{
                {"Sweden", "Denmark"},
                {"Sweden", "Sweden"}
        };
    }

    @AfterMethod
    public void closeBrowser() {
        driver.getDriver().quit();
    }


    @Test(dataProvider = "dhl-dp")
    public void testCorrectPostCode(String originCountry, String destinationCountry) throws JsonProcessingException {
        homePage.rejectCookies();

        String originPostalCode = getPostalCodeForGivenCountry(originCountry);
        homePage.setOriginCountryDropdownCountry(originCountry);
        homePage.setOriginPostCode(originPostalCode);

        String destinationPostalCode = getPostalCodeForGivenCountry(destinationCountry);
        homePage.setDestinationCountryDropdownCountry(destinationCountry);
        homePage.setDestinationPostCode(destinationPostalCode);

        homePage.clickCalculate();

        assertTrue(homePage.isDatePickerCalendarDisplayed());
    }

    @Test(dataProvider = "dhl-dp")
    public void testSpacesInPostcode(String originCountry, String destinationCountry) throws JsonProcessingException {
        homePage.rejectCookies();

        String originPostalCode = getPostalCodeForGivenCountry(originCountry);
        homePage.setOriginCountryDropdownCountry(originCountry);
        homePage.setOriginPostCode(FIVE_SPACES + originPostalCode + FIVE_SPACES);

        String destinationPostalCode = getPostalCodeForGivenCountry(destinationCountry);
        homePage.setDestinationCountryDropdownCountry(destinationCountry);
        homePage.setDestinationPostCode(FIVE_SPACES + destinationPostalCode + FIVE_SPACES);

        homePage.clickCalculate();

        assertTrue(homePage.isDatePickerCalendarDisplayed());
    }

    @Test(dataProvider = "dhl-dp")
    public void testLargeNumberPostcode(String originCountry, String destinationCountry) {
        homePage.rejectCookies();

        homePage.setOriginCountryDropdownCountry(originCountry);
        homePage.setOriginPostCode(String.valueOf(new Random().nextInt(100000000, 999999999)));

        homePage.setDestinationCountryDropdownCountry(destinationCountry);
        homePage.setDestinationPostCode(String.valueOf(new Random().nextInt(100000000, 999999999)));

        homePage.clickCalculate();

        checkWarningMessage(originCountry, destinationCountry);
    }

    @Test(dataProvider = "dhl-dp")
    public void testEmptyPostCode(String originCountry, String destinationCountry) {
        homePage.rejectCookies();

        homePage.setOriginCountryDropdownCountry(originCountry);

        homePage.setDestinationCountryDropdownCountry(destinationCountry);

        homePage.clickCalculate();

        assertTrue(homePage.isOriginPostCodeEmpty(), "Origin Post Code it's not empty!");
        assertTrue(homePage.getOriginPostcodeWarning().contains(CORRECT_POSTAL_CODE_WARNING));

        assertTrue(homePage.isDestinationPostCodeEmpty(), "Destination Post Code it's not empty!");
        assertTrue(homePage.getDestinationPostcodeWarning().contains(CORRECT_POSTAL_CODE_WARNING));
    }

    @Test(dataProvider = "dhl-dp")
    public void testSpecialCharactersPostcode(String originCountry, String destinationCountry) throws JsonProcessingException {
        homePage.rejectCookies();

        String originPostalCode = getPostalCodeForGivenCountry(originCountry);
        homePage.setOriginCountryDropdownCountry(originCountry);
        homePage.setOriginPostCode(helper.getPostalCodeWithSpecialCharacters(originPostalCode));

        String destinationPostalCode = getPostalCodeForGivenCountry(destinationCountry);
        homePage.setDestinationCountryDropdownCountry(destinationCountry);
        homePage.setDestinationPostCode(helper.getPostalCodeWithSpecialCharacters(destinationPostalCode));

        homePage.clickCalculate();

        checkWarningMessage(originCountry, destinationCountry);
    }


    private void checkWarningMessage(String originCountry, String destinationCountry) {
        if (originCountry.equalsIgnoreCase(destinationCountry)) {
            assertTrue(homePage.isOriginPostcodeWarningDisplayed());
            assertTrue(homePage.getOriginPostcodeWarning().contains(CORRECT_POSTAL_CODE_WARNING),
                    "Found: " + homePage.getOriginPostcodeWarning());
            assertTrue(homePage.isDestinationPostcodeWarningDisplayed());
            assertTrue(homePage.getDestinationPostcodeWarning().contains(CORRECT_POSTAL_CODE_WARNING),
                    "Found: " + homePage.getOriginPostcodeWarning());
        } else {
            assertTrue(homePage.isUnableToRetrieveWarningDisplayed());
            assertTrue(homePage.getUnableToRetrieveWarning().contains(UNABLE_TO_RETRIEVE),
                    "Found: " + homePage.getOriginPostcodeWarning());
        }
    }

    private String getPostalCodeForGivenCountry(String countryName) throws JsonProcessingException {
        String countryCode = helper.getCountryCodeBasedOnCountryName(countryName);
        Response response = getPostalCodes.getAllPostCodes(countryName, countryCode);
        List<String> postalCodes = helper.getPostalCodesFromApiUsingJackson(response);
        System.out.println("Postal codes for " + countryName + ": " + postalCodes);
        return helper.getRandomValueFromList(postalCodes);
    }
}
