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

import java.io.File;

@Service
public class EngineService {
    private static final Logger logger = LoggerFactory.getLogger(EngineService.class);
    private static SHAFT.GUI.WebDriver driver;
    private static boolean engineInitialized = false;

    /**
     * Retrieves the current WebDriver instance.
     *
     * @return The current WebDriver instance.
     * @throws IllegalStateException if no active browser session is found.
     */
    static SHAFT.GUI.WebDriver getDriver() {
        if (driver == null) {
            logger.error("No active browser session found. Please initialize a browser session first.");
            throw new IllegalStateException("No active browser session");
        }
        return driver;
    }

    /**
     * Finds a web element using the specified locator strategy and value.
     *
     * @param locatorStrategy The strategy to locate the element (e.g., ID, XPATH, CSSSELECTOR).
     * @param locatorValue    The value used with the locator strategy to find the element.
     * @return The located web element.
     */
    static By getLocator(locatorStrategy locatorStrategy, String locatorValue) {
        return switch (locatorStrategy) {
            case ID -> SHAFT.GUI.Locator.hasAnyTagName().hasId(locatorValue).build();
            case CSSSELECTOR, CSS, SELECTOR -> By.cssSelector(locatorValue);
            case XPATH -> By.xpath(locatorValue);
            case NAME -> SHAFT.GUI.Locator.hasAnyTagName().hasAttribute("name", locatorValue).build();
            case TAGNAME -> SHAFT.GUI.Locator.hasTagName(locatorValue).build();
            case CLASSNAME -> SHAFT.GUI.Locator.hasAnyTagName().hasAttribute("class", locatorValue).build();
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
            // Initialize engine setup only once to avoid repeated initialization warnings
            if (!engineInitialized) {
                logger.info("Initializing SHAFT Engine for AI Agent mode...");
                
                // Set default ReportPortal property if not already set to prevent NPE in SHAFT_ENGINE
                if (System.getProperty("rp.enable") == null) {
                    System.setProperty("rp.enable", "false");
                }
                
                // Pre-create the allure-results directory to prevent warnings during initialization
                // This ensures the directory exists before Allure lifecycle is initialized
                String allureResultsPath = System.getProperty("user.dir") + File.separator + "allure-results";
                File allureResultsDir = new File(allureResultsPath);
                if (!allureResultsDir.exists()) {
                    boolean created = allureResultsDir.mkdirs();
                    if (created) {
                        logger.debug("Created allure-results directory at: {}", allureResultsPath);
                    } else {
                        logger.warn("Failed to create allure-results directory at: {}. This may cause Allure warnings.", allureResultsPath);
                    }
                }
                
                TestNGListener.engineSetup(ProjectStructureManager.RunType.AI_AGENT);
                engineInitialized = true;
            }
            SHAFT.Properties.web.set().targetBrowserName(targetBrowser.name());
            driver = new SHAFT.GUI.WebDriver();
            logger.info("Driver initialized successfully: {}", targetBrowser.name());
        } catch (Exception e) {
            logger.error("Failed to initialize driver for browser: {}", targetBrowser.name(), e);
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
}