package UI.Driver;

import UI.Config.ApplicationContextConfig;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class DhlDriver extends ApplicationContextConfig {

    private WebDriver webDriver;

    public DhlDriver(@Value("${browser}") String browser, @Value("${url}") String url) {
        initiateDhlDriver(browser);
        openBrowserToGivenLink(url);
    }

    private void initiateDhlDriver(@Value("${browser}") String browser) {

        switch (browser) {
            case "firefox":
                FirefoxOptions firefoxOptions = new FirefoxOptions();
//        firefoxOptions.addArguments("--headless=new");
                firefoxOptions.addArguments("--start-maximized");
                firefoxOptions.setPageLoadStrategy(PageLoadStrategy.NONE);
                firefoxOptions.addArguments("--disable-blink-features=AutomationControlled");
                firefoxOptions.addArguments("--disable-extensions");
                firefoxOptions.addArguments("useAutomationExtension: false");
                firefoxOptions.addArguments("excludeSwitches", "enable-automation");
                webDriver = WebDriverManager.firefoxdriver().clearDriverCache().capabilities(firefoxOptions).create();

                break;
            default:
                ChromeOptions chromeOptionsDef = new ChromeOptions();
//                chromeOptionsDef.addArguments("--headless=new");
                chromeOptionsDef.addArguments("--start-maximized");
                chromeOptionsDef.setPageLoadStrategy(PageLoadStrategy.NONE);
                chromeOptionsDef.addArguments("--disable-blink-features=AutomationControlled");
                chromeOptionsDef.addArguments("--disable-extensions");
                chromeOptionsDef.addArguments("useAutomationExtension: false");
                chromeOptionsDef.addArguments("excludeSwitches", "enable-automation");
                webDriver = WebDriverManager.chromedriver().clearDriverCache().capabilities(chromeOptionsDef).create();
                break;
        }
    }

    public WebDriver getDriver() {
        return webDriver;
    }

    public void openBrowserToGivenLink(@Value("${url}") String url) {
        webDriver.get(url);
        webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }

}
