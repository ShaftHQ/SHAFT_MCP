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
                
                // Configure remote WebDriver if environment variables are set
                // This allows Docker containers to connect to Selenium Server on host machine
                // Environment variables only apply when the corresponding system property is not already set,
                // so explicit -D flags on the JVM command line always take precedence.
                String executionType = System.getenv("EXECUTION_TYPE");
                String remoteDriverAddress = System.getenv("REMOTE_DRIVER_ADDRESS");
                
                if (executionType != null && !executionType.isEmpty()
                        && System.getProperty("web.executionType") == null) {
                    System.setProperty("web.executionType", executionType);
                    logger.info("Remote execution type configured: {}", executionType);
                }
                
                if (remoteDriverAddress != null && !remoteDriverAddress.isEmpty()
                        && System.getProperty("web.remoteDriverAddress") == null) {
                    System.setProperty("web.remoteDriverAddress", remoteDriverAddress);
                    // Log only that remote mode is active, not the full URL which may contain credentials
                    logger.info("Remote driver address configured (address redacted for security)");
                }
                
                // Pre-create directories to prevent issues during SHAFT Engine initialization.
                // The allure-results directory must exist before Allure lifecycle is initialized.
                // The properties directory must exist (empty) before engineSetup() to prevent
                // SHAFT Engine from extracting default property files with subdirectories that
                // cause "Is a directory" IOException during ReportHelper.attachPropertyFiles().
                for (String dirPath : new String[]{
                        System.getProperty("user.dir") + File.separator + "allure-results",
                        "src" + File.separator + "main" + File.separator + "resources" + File.separator + "properties"
                }) {
                    File dir = new File(dirPath);
                    if (!dir.exists()) {
                        if (dir.mkdirs()) {
                            logger.debug("Created directory: {}", dirPath);
                        } else {
                            logger.warn("Failed to create directory: {}", dirPath);
                        }
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