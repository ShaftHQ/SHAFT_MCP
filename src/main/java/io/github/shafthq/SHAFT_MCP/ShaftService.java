package io.github.shafthq.SHAFT_MCP;

import com.shaft.driver.SHAFT;
import com.shaft.listeners.TestNGListener;
import com.shaft.tools.io.internal.AllureManager;
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

    SHAFT.GUI.WebDriver getDriver() {
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

    /**
     * Initializes the WebDriver for the specified browser type.
     *
     * @param targetBrowser The type of browser to initialize (e.g., CHROME, FIREFOX).
     */
    @Tool(name = "driver_initialize", description = "launches browser")
    public void initializeDriver(BrowserType targetBrowser) {
        try {
            TestNGListener.engineSetup(ProjectStructureManager.RunType.AI_AGENT);
            SHAFT.Properties.web.set().targetBrowserName(targetBrowser.name());
            driver = new SHAFT.GUI.WebDriver();
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
        try {
            logger.info("Driver {} will be closed", driver);
            driver.quit();
        } catch (Exception e) {
            logger.error("Failed to close driver.", e);
            throw e;
        }
    }

    /**
     * Generate a test report for the current session.
     * This method compiles and generates a detailed test report based on the actions performed during the session.
     * It utilizes SHAFT's reporting capabilities to create a comprehensive report that includes
     * information such as test steps, outcomes, screenshots, and logs.
     * The generated report is saved in a predefined location for easy access and review.
     * This method should be called at the end of the test session to ensure all actions are documented.
     */
    @Tool(name = "generate_test_report", description = "generates a test report for the current session")
    public void generateTestReport() {
        try {
            AllureManager.openAllureReportAfterExecution();
            logger.info("Test report generated successfully.");
        } catch (Exception e) {
            logger.error("Failed to generate test report.", e);
            throw e;
        }
    }

    /**
     * Get the source code of the current page.
     * This is a support method for the AI agent to better explore the page.
     * @return The HTML source code of the current page as a string.
     */
    @Tool(name = "browser_get_page_source", description = "gets the source code of the current page")
    public String getPageSource() {
        try {
            SHAFT.GUI.WebDriver driver = getDriver();
            String pageSource = driver.browser().getPageSource();
            logger.info("Retrieved page source successfully.");
            return pageSource;
        } catch (Exception e) {
            logger.error("Failed to retrieve page source.", e);
            throw e;
        }
    }

    /**
     * Hovers over an element identified by the specified locator strategy and value.
     *
     * @param locatorStrategy The strategy to locate the element (e.g., ID, XPATH).
     * @param locatorValue    The value used with the locator strategy to find the element.
     */
    @Tool(name = "element_hover", description = "hovers over an element")
    public void hover(locatorStrategy locatorStrategy, String locatorValue) {
        try {
            SHAFT.GUI.WebDriver driver = getDriver();
            By locator = getLocator(locatorStrategy, locatorValue);
            driver.element().hover(locator);
            logger.info("Hovered over element with locator: {} - {}", locatorStrategy, locatorValue);
        } catch (Exception e) {
            logger.error("Failed to hover over element with locator: {} - {}", locatorStrategy, locatorValue, e);
            throw e;
        }
    }

    /**
     * Clicks on an element identified by the specified locator strategy and value.
     *
     * @param locatorStrategy The strategy to locate the element (e.g., ID, XPATH).
     * @param locatorValue    The value used with the locator strategy to find the element.
     */
    @Tool(name = "element_click", description = "clicks an element")
    public void click(locatorStrategy locatorStrategy, String locatorValue) {
        try {
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
     * Clicks on an element identified by the specified name using Artificial Intelligence.
     *
     * @param elementName The name of the element to click, as recognized by AI.
     */
    @Tool(name = "element_click_ai", description = "clicks an element using AI")
    public void clickUsingAI(String elementName) {
        try {
            SHAFT.GUI.WebDriver driver = getDriver();
            driver.element().click(elementName);
            logger.info("Clicked element using AI with name: {}", elementName);
        } catch (Exception e) {
            logger.error("Failed to click element using AI with name: {}", elementName, e);
            throw e;
        }
    }

    /**
     * Click using JavaScript on an element identified by the specified locator strategy and value.
     *
     * @param locatorStrategy The strategy to locate the element (e.g., ID, XPATH).
     * @param locatorValue    The value used with the locator strategy to find the element.
     */
    @Tool(name = "element_click_js", description = "clicks an element using JavaScript")
    public void clickUsingJavaScript(locatorStrategy locatorStrategy, String locatorValue) {
        try {
            SHAFT.GUI.WebDriver driver = getDriver();
            By locator = getLocator(locatorStrategy, locatorValue);
            driver.element().clickUsingJavascript(locator);
            logger.info("Clicked element using JavaScript with locator: {} - {}", locatorStrategy, locatorValue);
        } catch (Exception e) {
            logger.error("Failed to click element using JavaScript with locator: {} - {}", locatorStrategy, locatorValue, e);
            throw e;
        }
    }

    /**
     * Double-clicks on an element identified by the specified locator strategy and value.
     *
     * @param locatorStrategy The strategy to locate the element (e.g., ID, XPATH).
     * @param locatorValue    The value used with the locator strategy to find the element.
     */
    @Tool(name = "element_double_click", description = "double clicks an element")
    public void doubleClick(locatorStrategy locatorStrategy, String locatorValue) {
        try {
            SHAFT.GUI.WebDriver driver = getDriver();
            By locator = getLocator(locatorStrategy, locatorValue);
            driver.element().doubleClick(locator);
            logger.info("Double-clicked element with locator: {} - {}", locatorStrategy, locatorValue);
        } catch (Exception e) {
            logger.error("Failed to double-click element with locator: {} - {}", locatorStrategy, locatorValue, e);
            throw e;
        }
    }

    /**
     * Click and hold on an element identified by the specified locator strategy and value.
     *
     * @param locatorStrategy The strategy to locate the element (e.g., ID, XPATH).
     * @param locatorValue    The value used with the locator strategy to find the element.
     */
    @Tool(name = "element_click_and_hold", description = "clicks and holds an element")
    public void clickAndHold(locatorStrategy locatorStrategy, String locatorValue) {
        try {
            SHAFT.GUI.WebDriver driver = getDriver();
            By locator = getLocator(locatorStrategy, locatorValue);
            driver.element().clickAndHold(locator);
            logger.info("Clicked and held element with locator: {} - {}", locatorStrategy, locatorValue);
        } catch (Exception e) {
            logger.error("Failed to click and hold element with locator: {} - {}", locatorStrategy, locatorValue, e);
            throw e;
        }
    }

    /**
     * Types the specified text into an element identified by the given locator strategy and value.
     *
     * @param locatorStrategy The strategy to locate the element (e.g., ID, XPATH).
     * @param locatorValue    The value used with the locator strategy to find the element.
     * @param textValue       The text to type into the element.
     */
    @Tool(name = "element_type", description = "types value to an element")
    public void type(locatorStrategy locatorStrategy, String locatorValue, CharSequence... textValue) {
        try {
            SHAFT.GUI.WebDriver driver = getDriver();
            By locator = getLocator(locatorStrategy, locatorValue);
            driver.element().type(locator, textValue);
            logger.info("Typed text '{}' into element with locator: {} - {}", String.join(", ", textValue), locatorStrategy, locatorValue);
        } catch (Exception e) {
            logger.error("Failed to type text '{}' into element with locator: {} - {}", String.join(", ", textValue), locatorStrategy, locatorValue, e);
            throw e;
        }
    }

    /**
     * Appends the specified text to an element identified by the given locator strategy and value.
     *
     * @param locatorStrategy The strategy to locate the element (e.g., ID, XPATH).
     * @param locatorValue    The value used with the locator strategy to find the element.
     * @param textValue       The text to append to the element.
     */
    @Tool(name = "element_append_text", description = "appends text to an element")
    public void appendText(locatorStrategy locatorStrategy, String locatorValue, CharSequence... textValue) {
        try {
            SHAFT.GUI.WebDriver driver = getDriver();
            By locator = getLocator(locatorStrategy, locatorValue);
            driver.element().typeAppend(locator, textValue);
            logger.info("Appended text '{}' to element with locator: {} - {}", String.join(", ", textValue), locatorStrategy, locatorValue);
        } catch (Exception e) {
            logger.error("Failed to append text '{}' to element with locator: {} - {}", String.join(", ", textValue), locatorStrategy, locatorValue, e);
            throw e;
        }
    }

    /**
     * Types the specified text into an element identified by the given name using Artificial Intelligence.
     *
     * @param elementName The name of the element to type into, as recognized by AI.
     * @param textValue   The text to type into the element.
     */
    @Tool(name = "element_type_ai", description = "types value to an element using AI")
    public void typeUsingAI(String elementName, CharSequence... textValue) {
        try {
            SHAFT.GUI.WebDriver driver = getDriver();
            driver.element().type(elementName, textValue);
            logger.info("Typed text '{}' into element using AI with name: {}", String.join(", ", textValue), elementName);
        } catch (Exception e) {
            logger.error("Failed to type text '{}' into element using AI with name: {}", String.join(", ", textValue), elementName, e);
            throw e;
        }
    }

    /**
     * Set value using JavaScript on an element identified by the specified locator strategy and value.
     *
     * @param locatorStrategy The strategy to locate the element (e.g., ID, XPATH).
     * @param locatorValue    The value used with the locator strategy to find the element.
     * @param textValue       The text to set into the element.
     */
    @Tool(name = "element_set_value_js", description = "sets value to an element using JavaScript")
    public void setValueUsingJavaScript(locatorStrategy locatorStrategy, String locatorValue, String textValue) {
        try {
            SHAFT.GUI.WebDriver driver = getDriver();
            By locator = getLocator(locatorStrategy, locatorValue);
            driver.element().setValueUsingJavaScript(locator, textValue);
            logger.info("Set value '{}' to element using JavaScript with locator: {} - {}", textValue, locatorStrategy, locatorValue);
        } catch (Exception e) {
            logger.error("Failed to set value '{}' to element using JavaScript with locator: {} - {}", textValue, locatorStrategy, locatorValue, e);
            throw e;
        }
    }

    /**
     * Clears the text from an input element identified by the specified locator strategy and value.
     *
     * @param locatorStrategy The strategy to locate the element (e.g., ID, XPATH).
     * @param locatorValue    The value used with the locator strategy to find the element.
     */
    @Tool(name = "element_clear", description = "clears text from an element")
    public void clear(locatorStrategy locatorStrategy, String locatorValue) {
        try {
            SHAFT.GUI.WebDriver driver = getDriver();
            By locator = getLocator(locatorStrategy, locatorValue);
            driver.element().clear(locator);
            logger.info("Cleared text from element with locator: {} - {}", locatorStrategy, locatorValue);
        } catch (Exception e) {
            logger.error("Failed to clear text from element with locator: {} - {}", locatorStrategy, locatorValue, e);
            throw e;
        }
    }

    /**
     * Drop file to upload to an element identified by the specified locator strategy and value.
     *
     * @param locatorStrategy The strategy to locate the element (e.g., ID, XPATH).
     * @param locatorValue    The value used with the locator strategy to find the element.
     * @param filePath        The path of the file to upload.
     */
    @Tool(name = "element_drop_file_to_upload", description = "drops file to an element to upload")
    public void dropFileToUpload(locatorStrategy locatorStrategy, String locatorValue, String filePath) {
        try {
            SHAFT.GUI.WebDriver driver = getDriver();
            By locator = getLocator(locatorStrategy, locatorValue);
            driver.element().dropFileToUpload(locator, filePath);
            logger.info("Dropped file '{}' to element with locator: {} - {}", filePath, locatorStrategy, locatorValue);
        } catch (Exception e) {
            logger.error("Failed to drop file '{}' to element with locator: {} - {}", filePath, locatorStrategy, locatorValue, e);
            throw e;
        }
    }

    /**
     * Drag and drops an element from a source locator to a target locator.
     *
     * @param sourceLocatorStrategy The strategy to locate the source element (e.g., ID, XPATH).
     * @param sourceLocatorValue    The value used with the source locator strategy to find the source element.
     * @param targetLocatorStrategy The strategy to locate the target element (e.g., ID, XPATH).
     * @param targetLocatorValue    The value used with the target locator strategy to find the target element.
     */
    @Tool(name = "element_drag_and_drop", description = "drags and drops an element from source to target")
    public void dragAndDrop(locatorStrategy sourceLocatorStrategy, String sourceLocatorValue, locatorStrategy
            targetLocatorStrategy, String targetLocatorValue) {
        try {
            SHAFT.GUI.WebDriver driver = getDriver();
            By sourceLocator = getLocator(sourceLocatorStrategy, sourceLocatorValue);
            By targetLocator = getLocator(targetLocatorStrategy, targetLocatorValue);
            driver.element().dragAndDrop(sourceLocator, targetLocator);
            logger.info("Dragged and dropped element from locator: {} - {} to locator: {} - {}",
                    sourceLocatorStrategy, sourceLocatorValue, targetLocatorStrategy, targetLocatorValue);
        } catch (Exception e) {
            logger.error("Failed to drag and drop element from locator: {} - {} to locator: {} - {}",
                    sourceLocatorStrategy, sourceLocatorValue, targetLocatorStrategy, targetLocatorValue, e);
            throw e;
        }
    }

    /**
     * Drag and drop by offset an element identified by the specified locator strategy and value.
     *
     * @param locatorStrategy The strategy to locate the element (e.g., ID, XPATH).
     * @param locatorValue    The value used with the locator strategy to find the element.
     * @param xOffset         The horizontal offset to drag the element.
     * @param yOffset         The vertical offset to drag the element.
     */
    @Tool(name = "element_drag_and_drop_by_offset", description = "drags and drops an element by offset")
    public void dragAndDropByOffset(locatorStrategy locatorStrategy, String locatorValue, int xOffset, int yOffset) {
        try {
            SHAFT.GUI.WebDriver driver = getDriver();
            By locator = getLocator(locatorStrategy, locatorValue);
            driver.element().dragAndDropByOffset(locator, xOffset, yOffset);
            logger.info("Dragged and dropped element with locator: {} - {} by offset: ({}, {})",
                    locatorStrategy, locatorValue, xOffset, yOffset);
        } catch (Exception e) {
            logger.error("Failed to drag and drop element with locator: {} - {} by offset: ({}, {})",
                    locatorStrategy, locatorValue, xOffset, yOffset, e);
            throw e;
        }
    }

    /**
     * Retrieves the text content of an element identified by the specified locator strategy and value.
     *
     * @param locatorStrategy The strategy to locate the element (e.g., ID, XPATH).
     * @param locatorValue    The value used with the locator strategy to find the element.
     * @return The text content of the element.
     */
    @Tool(name = "element_get_text", description = "gets text of an element")
    public String getText(locatorStrategy locatorStrategy, String locatorValue) {
        try {
            SHAFT.GUI.WebDriver driver = getDriver();
            By locator = getLocator(locatorStrategy, locatorValue);
            String text = driver.element().get().text(locator);
            logger.info("Retrieved text '{}' from element with locator: {} - {}", text, locatorStrategy, locatorValue);
            return text;
        } catch (Exception e) {
            logger.error("Failed to retrieve text from element with locator: {} - {}", locatorStrategy, locatorValue, e);
            throw e;
        }
    }

    /**
     * Retrieves the value of a specified DOM attribute from an element identified by the given locator strategy and value.
     *
     * @param locatorStrategy  The strategy to locate the element (e.g., ID, XPATH).
     * @param locatorValue     The value used with the locator strategy to find the element.
     * @param domAttributeName The name of the DOM attribute whose value is to be retrieved.
     * @return The value of the specified DOM attribute, or null if the attribute does not exist.
     */
    @Tool(name = "element_get_dom_attribute", description = "gets a DOM attribute value of an element")
    public String getDomAttribute(locatorStrategy locatorStrategy, String locatorValue, String domAttributeName) {
        try {
            SHAFT.GUI.WebDriver driver = getDriver();
            By locator = getLocator(locatorStrategy, locatorValue);
            String attributeValue = driver.element().get().domAttribute(locator, domAttributeName);
            logger.info("Retrieved DOM attribute '{}' with value '{}' from element with locator: {} - {}",
                    domAttributeName, attributeValue, locatorStrategy, locatorValue);
            return attributeValue;
        } catch (Exception e) {
            logger.error("Failed to retrieve DOM attribute '{}' from element with locator: {} - {}",
                    domAttributeName, locatorStrategy, locatorValue, e);
            throw e;
        }
    }

    /**
     * Retrieves the value of a specified DOM property from an element identified by the given locator strategy and value.
     *
     * @param locatorStrategy The strategy to locate the element (e.g., ID, XPATH).
     * @param locatorValue    The value used with the locator strategy to find the element.
     * @param domPropertyName The name of the DOM property whose value is to be retrieved.
     * @return The value of the specified DOM property, or null if the property does not exist.
     */
    @Tool(name = "element_get_dom_property", description = "gets a DOM property value of an element")
    public String getDomProperty(locatorStrategy locatorStrategy, String locatorValue, String domPropertyName) {
        try {
            SHAFT.GUI.WebDriver driver = getDriver();
            By locator = getLocator(locatorStrategy, locatorValue);
            String propertyValue = driver.element().get().domProperty(locator, domPropertyName);
            logger.info("Retrieved DOM property '{}' with value '{}' from element with locator: {} - {}",
                    domPropertyName, propertyValue, locatorStrategy, locatorValue);
            return propertyValue;
        } catch (Exception e) {
            logger.error("Failed to retrieve DOM property '{}' from element with locator: {} - {}",
                    domPropertyName, locatorStrategy, locatorValue, e);
            throw e;
        }
    }

    /**
     * Retrieves the value of a specified CSS property from an element identified by the given locator strategy and value.
     *
     * @param locatorStrategy The strategy to locate the element (e.g., ID, XPATH).
     * @param locatorValue    The value used with the locator strategy to find the element.
     * @param cssPropertyName The name of the CSS property whose value is to be retrieved.
     * @return The value of the specified CSS property, or null if the property does not exist.
     */
    @Tool(name = "element_get_css_value", description = "gets a CSS property value of an element")
    public String getCssValue(locatorStrategy locatorStrategy, String locatorValue, String cssPropertyName) {
        try {
            SHAFT.GUI.WebDriver driver = getDriver();
            By locator = getLocator(locatorStrategy, locatorValue);
            String cssValue = driver.element().get().cssValue(locator, cssPropertyName);
            logger.info("Retrieved CSS property '{}' with value '{}' from element with locator: {} - {}",
                    cssPropertyName, cssValue, locatorStrategy, locatorValue);
            return cssValue;
        } catch (Exception e) {
            logger.error("Failed to retrieve CSS property '{}' from element with locator: {} - {}",
                    cssPropertyName, locatorStrategy, locatorValue, e);
            throw e;
        }
    }

    /**
     * Checks if an element identified by the specified locator strategy and value is displayed on the page.
     *
     * @param locatorStrategy The strategy to locate the element (e.g., ID, XPATH).
     * @param locatorValue    The value used with the locator strategy to find the element.
     * @return True if the element is visible, false otherwise.
     */
    @Tool(name = "element_is_displayed", description = "checks if an element is displayed")
    public boolean isDisplayed(locatorStrategy locatorStrategy, String locatorValue) {
        try {
            SHAFT.GUI.WebDriver driver = getDriver();
            By locator = getLocator(locatorStrategy, locatorValue);
            boolean isDisplayed = driver.element().get().isDisplayed(locator);
            logger.info("Element with locator: {} - {} is displayed: {}", locatorStrategy, locatorValue, isDisplayed);
            return isDisplayed;
        } catch (Exception e) {
            logger.error("Failed to check visibility of element with locator: {} - {}", locatorStrategy, locatorValue, e);
            throw e;
        }
    }

    /**
     * Checks if an element identified by the specified locator strategy and value is enabled.
     *
     * @param locatorStrategy The strategy to locate the element (e.g., ID, XPATH).
     * @param locatorValue    The value used with the locator strategy to find the element.
     * @return True if the element is enabled, false otherwise.
     */
    @Tool(name = "element_is_enabled", description = "checks if an element is enabled")
    public boolean isEnabled(locatorStrategy locatorStrategy, String locatorValue) {
        try {
            SHAFT.GUI.WebDriver driver = getDriver();
            By locator = getLocator(locatorStrategy, locatorValue);
            boolean isEnabled = driver.element().get().isEnabled(locator);
            logger.info("Element with locator: {} - {} is enabled: {}", locatorStrategy, locatorValue, isEnabled);
            return isEnabled;
        } catch (Exception e) {
            logger.error("Failed to check if element with locator: {} - {} is enabled", locatorStrategy, locatorValue, e);
            throw e;
        }
    }

    /**
     * Checks if an element identified by the specified locator strategy and value is selected.
     *
     * @param locatorStrategy The strategy to locate the element (e.g., ID, XPATH).
     * @param locatorValue    The value used with the locator strategy to find the element.
     * @return True if the element is selected, false otherwise.
     */
    @Tool(name = "element_is_selected", description = "checks if an element is selected")
    public boolean isSelected(locatorStrategy locatorStrategy, String locatorValue) {
        try {
            SHAFT.GUI.WebDriver driver = getDriver();
            By locator = getLocator(locatorStrategy, locatorValue);
            boolean isSelected = driver.element().get().isSelected(locator);
            logger.info("Element with locator: {} - {} is selected: {}", locatorStrategy, locatorValue, isSelected);
            return isSelected;
        } catch (Exception e) {
            logger.error("Failed to check if element with locator: {} - {} is selected", locatorStrategy, locatorValue, e);
            throw e;
        }
    }

    /**
     * Navigates the browser to the specified URL.
     *
     * @param targetUrl The URL to navigate to.
     */
    @Tool(name = "browser_navigate", description = "navigates to a URL")
    public void navigate(String targetUrl) {
        try {
            SHAFT.GUI.WebDriver driver = getDriver();
            driver.browser().navigateToURL(targetUrl);
            logger.info("Navigated to URL: {}", targetUrl);
        } catch (Exception e) {
            logger.error("Failed to navigate to URL: {}", targetUrl, e);
            throw e;
        }
    }

    /**
     * Navigates the browser to the specified URL using Basic Authentication.
     *
     * @param targetUrl          The URL to navigate to.
     * @param username           The username for Basic Authentication.
     * @param password           The password for Basic Authentication.
     * @param targetUrlAfterAuth The URL to navigate to after authentication.
     */
    @Tool(name = "browser_navigate_with_basic_auth", description = "navigates to a URL with Basic Authentication")
    public void navigateWithBasicAuth(String targetUrl, String username, String password, String targetUrlAfterAuth) {
        try {
            SHAFT.GUI.WebDriver driver = getDriver();
            driver.browser().navigateToURLWithBasicAuthentication(targetUrl, username, password, targetUrlAfterAuth);
            logger.info("Navigated to URL with Basic Authentication: {}", targetUrl);
        } catch (Exception e) {
            logger.error("Failed to navigate to URL with Basic Authentication: {}", targetUrl, e);
            throw e;
        }
    }

    /**
     * Refreshes the current page in the browser.
     */
    @Tool(name = "browser_refresh", description = "refreshes the current page")
    public void refreshPage() {
        try {
            SHAFT.GUI.WebDriver driver = getDriver();
            driver.browser().refreshCurrentPage();
            logger.info("Page refreshed successfully.");
        } catch (Exception e) {
            logger.error("Failed to refresh the page.", e);
            throw e;
        }
    }

    /**
     * Navigates back to the previous page in the browser's history.
     */
    @Tool(name = "browser_navigate_back", description = "navigates back to the previous page")
    public void navigateBack() {
        try {
            SHAFT.GUI.WebDriver driver = getDriver();
            driver.browser().navigateBack();
            logger.info("Navigated back to the previous page.");
        } catch (Exception e) {
            logger.error("Failed to navigate back.", e);
            throw e;
        }
    }

    /**
     * Navigates forward to the next page in the browser's history.
     */
    @Tool(name = "browser_navigate_forward", description = "navigates forward to the next page")
    public void navigateForward() {
        try {
            SHAFT.GUI.WebDriver driver = getDriver();
            driver.browser().navigateForward();
            logger.info("Navigated forward to the next page.");
        } catch (Exception e) {
            logger.error("Failed to navigate forward.", e);
            throw e;
        }
    }

    /**
     * Maximizes the browser window.
     */
    @Tool(name = "browser_maximize_window", description = "maximizes the browser window")
    public void maximizeWindow() {
        try {
            SHAFT.GUI.WebDriver driver = getDriver();
            driver.browser().maximizeWindow();
            logger.info("Browser window maximized.");
        } catch (Exception e) {
            logger.error("Failed to maximize browser window.", e);
            throw e;
        }
    }

    /**
     * Sets the browser window to a specific size.
     *
     * @param width  The desired width of the browser window.
     * @param height The desired height of the browser window.
     */
    @Tool(name = "browser_set_window_size", description = "sets the browser window to a specific size")
    public void setWindowSize(int width, int height) {
        try {
            SHAFT.GUI.WebDriver driver = getDriver();
            driver.browser().setWindowSize(width, height);
            logger.info("Browser window size set to {}x{}.", width, height);
        } catch (Exception e) {
            logger.error("Failed to set browser window size to {}x{}.", width, height, e);
            throw e;
        }
    }

    /**
     * Sets the browser window to fullscreen mode.
     */
    @Tool(name = "browser_fullscreen_window", description = "sets the browser window to fullscreen mode")
    public void fullscreenWindow() {
        try {
            SHAFT.GUI.WebDriver driver = getDriver();
            driver.browser().fullScreenWindow();
            logger.info("Browser window set to fullscreen mode.");
        } catch (Exception e) {
            logger.error("Failed to set browser window to fullscreen mode.", e);
            throw e;
        }
    }

    /**
     * Deletes all cookies in the current browser session.
     */
    @Tool(name = "browser_delete_all_cookies", description = "deletes all cookies")
    public void deleteAllCookies() {
        try {
            SHAFT.GUI.WebDriver driver = getDriver();
            driver.browser().deleteAllCookies();
            logger.info("All cookies deleted.");
        } catch (Exception e) {
            logger.error("Failed to delete all cookies.", e);
            throw e;
        }
    }

    /**
     * Deletes a specific cookie by name in the current browser session.
     *
     * @param cookieName The name of the cookie to delete.
     */
    @Tool(name = "browser_delete_cookie", description = "deletes a specific cookie by name")
    public void deleteCookie(String cookieName) {
        try {
            SHAFT.GUI.WebDriver driver = getDriver();
            driver.browser().deleteCookie(cookieName);
            logger.info("Cookie '{}' deleted.", cookieName);
        } catch (Exception e) {
            logger.error("Failed to delete cookie '{}'.", cookieName, e);
            throw e;
        }
    }

    /**
     * Adds a cookie to the current browser session.
     *
     * @param name  The name of the cookie.
     * @param value The value of the cookie.
     */
    @Tool(name = "browser_add_cookie", description = "adds a cookie")
    public void addCookie(String name, String value) {
        try {
            SHAFT.GUI.WebDriver driver = getDriver();
            driver.browser().addCookie(name, value);
            logger.info("Cookie added: {}={}", name, value);
        } catch (Exception e) {
            logger.error("Failed to add cookie: {}={}", name, value, e);
            throw e;
        }
    }

    /**
     * Retrieves a cookie by name from the current browser session.
     *
     * @param cookieName The name of the cookie to retrieve.
     * @return The cookie value as a string, or null if not found.
     */
    @Tool(name = "browser_get_cookie", description = "gets a cookie by name")
    public String getCookie(String cookieName) {
        try {
            SHAFT.GUI.WebDriver driver = getDriver();
            String cookieValue = driver.browser().getCookie(cookieName).getValue();
            logger.info("Retrieved cookie: {}={}", cookieName, cookieValue);
            return cookieValue;
        } catch (Exception e) {
            logger.error("Failed to retrieve cookie '{}'.", cookieName, e);
            throw e;
        }
    }

    /**
     * Retrieves all cookies from the current browser session.
     *
     * @return A string representation of all cookies.
     */
    @Tool(name = "browser_get_all_cookies", description = "gets all cookies")
    public String getAllCookies() {
        try {
            SHAFT.GUI.WebDriver driver = getDriver();
            String allCookies = driver.browser().getAllCookies().toString();
            logger.info("Retrieved all cookies: {}", allCookies);
            return allCookies;
        } catch (Exception e) {
            logger.error("Failed to retrieve all cookies.", e);
            throw e;
        }
    }

    /**
     * Retrieves the current URL of the browser.
     *
     * @return The current URL as a string.
     */
    @Tool(name = "browser_get_current_url", description = "gets current URL")
    public String getCurrentUrl() {
        try {
            SHAFT.GUI.WebDriver driver = getDriver();
            String currentUrl = driver.browser().getCurrentURL();
            logger.info("Current URL retrieved: {}", currentUrl);
            return currentUrl;
        } catch (Exception e) {
            logger.error("Failed to retrieve current URL.", e);
            throw e;
        }
    }

    /**
     * Retrieves the title of the current page in the browser.
     *
     * @return The page title as a string.
     */
    @Tool(name = "browser_get_title", description = "gets current page title")
    public String getTitle() {
        try {
            SHAFT.GUI.WebDriver driver = getDriver();
            String title = driver.browser().getCurrentWindowTitle();
            logger.info("Page title retrieved: {}", title);
            return title;
        } catch (Exception e) {
            logger.error("Failed to retrieve page title.", e);
            throw e;
        }
    }

    public enum locatorStrategy {
        ID, CSSSELECTOR, CSS, SELECTOR, XPATH, NAME, TAGNAME, CLASSNAME
    }

    public enum BrowserType {
        CHROME,
        FIREFOX,
        SAFARI,
        EDGE
    }


}