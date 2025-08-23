package io.github.shafthq.SHAFT_MCP;

import com.shaft.driver.SHAFT;
import com.shaft.listeners.TestNGListener;
import com.shaft.tools.io.internal.ProjectStructureManager;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

@Service
public class ShaftService {

    private static final Logger logger = LoggerFactory.getLogger(ShaftService.class);
    SHAFT.GUI.WebDriver driver;

    // Helper functions
    SHAFT.GUI.WebDriver getDriver (){
        if (driver == null) {
            logger.error("No active browser session found. Please initialize a browser session first.");
            throw new IllegalStateException("No active browser session");
        }
        return driver;
    }

    By getLocator(locatorStrategy locatorStrategy, String locatorValue) {
        return switch (locatorStrategy) {
            case ID -> By.id(locatorValue);
            case CSSSELECTOR, CSS, SELECTOR -> By.cssSelector(locatorValue);
            case XPATH -> By.xpath(locatorValue);
            case NAME -> By.name(locatorValue);
            case TAGNAME -> By.tagName(locatorValue);
            case CLASSNAME -> By.className(locatorValue);
        };
    }

    public enum locatorStrategy {
        ID,CSSSELECTOR,CSS,SELECTOR,XPATH,NAME,TAGNAME,CLASSNAME
    }

    public enum BrowserType {
        CHROME,
        FIREFOX,
        SAFARI,
        EDGE
    }

    /**
     * Initializes the WebDriver for the specified browser type.
     * @param targetBrowser The type of browser to initialize (e.g., CHROME, FIREFOX).
     */
    @Tool(name = "driver_initialize", description = "launches browser")
    public void initializeDriver(BrowserType targetBrowser) {
        try{
            TestNGListener.engineSetup(ProjectStructureManager.RunType.AI_AGENT);
            SHAFT.Properties.web.set().targetBrowserName(targetBrowser.name());
            driver  = new SHAFT.GUI.WebDriver();
            logger.info("Driver initialized: {}", driver);
        } catch (Exception e) {
            logger.error("Failed to initialize driver.", e);
            throw e;
        }
    }

    /**
     * Quits the WebDriver, closing all associated browser windows.
     */
    @Tool(name = "driver_quit", description = "closes browser")
    public void quitDriver() {
        try{
            logger.info("Driver {} will be closed", driver);
            driver.quit();
        } catch (Exception e) {
            logger.error("Failed to close driver.", e);
            throw e;
        }
    }

    /**
     * Navigates the browser to the specified URL.
     * @param targetUrl The URL to navigate to.
     */
    @Tool(name = "browser_navigate", description = "navigates to a URL")
    public void navigate(String targetUrl) {
        try{
            SHAFT.GUI.WebDriver driver = getDriver();
            driver.browser().navigateToURL(targetUrl);
            logger.info("Navigated to URL: {}", targetUrl);
        } catch (Exception e) {
            logger.error("Failed to navigate to URL: {}", targetUrl, e);
            throw e;
        }
    }


    /**
     * Clicks on an element identified by the specified locator strategy and value.
     * @param locatorStrategy The strategy to locate the element (e.g., ID, XPATH).
     * @param locatorValue The value used with the locator strategy to find the element.
     */
    @Tool(name = "element_click", description = "clicks an element")
    public void click(locatorStrategy locatorStrategy, String locatorValue) {
        try{
            SHAFT.GUI.WebDriver driver = getDriver();
            By locator = getLocator(locatorStrategy, locatorValue);
            driver.element().click(locator);
            logger.info("Clicked element with locator: {} - {}", locatorStrategy, locatorValue);
        } catch (Exception e) {
            logger.error("Failed to click element with locator: {} - {}", locatorStrategy, locatorValue, e);
            throw e;
        }
    }

    /**
     * Types the specified text into an element identified by the given locator strategy and value.
     * @param locatorStrategy The strategy to locate the element (e.g., ID, XPATH).
     * @param locatorValue The value used with the locator strategy to find the element.
     * @param textValue The text to type into the element.
     */
    @Tool(name = "element_type", description = "types value to an element")
    public void type(locatorStrategy locatorStrategy, String locatorValue, CharSequence... textValue) {
        try{
            SHAFT.GUI.WebDriver driver = getDriver();
            By locator = getLocator(locatorStrategy, locatorValue);
            driver.element().type(locator, textValue);
            logger.info("Typed text '{}' into element with locator: {} - {}", String.join(", ", textValue), locatorStrategy, locatorValue);
        } catch (Exception e) {
            logger.error("Failed to type text '{}' into element with locator: {} - {}", String.join(", ", textValue), locatorStrategy, locatorValue, e);
            throw e;
        }
    }
}