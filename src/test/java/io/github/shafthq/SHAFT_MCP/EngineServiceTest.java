package io.github.shafthq.SHAFT_MCP;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for SHAFT MCP services.
 * These tests verify that the MCP server can actually open a browser,
 * navigate to pages, interact with elements, and retrieve data.
 */
@SpringBootTest
class EngineServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(EngineServiceTest.class);
    private static final String TEST_URL = "https://shafthq.github.io/";

    @Autowired
    private EngineService engineService;

    @Autowired
    private BrowserService browserService;

    @Autowired
    private ElementService elementService;

    @AfterEach
    void tearDown() {
        try {
            engineService.quitDriver();
        } catch (Exception e) {
            logger.warn("Failed to quit driver in cleanup", e);
        }
    }

    /**
     * Tests that the MCP server can initialize a browser, navigate to a URL,
     * and retrieve the page title.
     */
    @Test
    void testBrowserNavigationAndTitleRetrieval() {
        engineService.initializeDriver(BrowserType.CHROME);
        browserService.navigate(TEST_URL);

        String title = browserService.getTitle();
        assertNotNull(title, "Page title should not be null");
        assertFalse(title.isEmpty(), "Page title should not be empty");
        logger.info("Retrieved page title: {}", title);
    }

    /**
     * Tests that the MCP server can retrieve the current URL after navigation.
     */
    @Test
    void testGetCurrentUrl() {
        engineService.initializeDriver(BrowserType.CHROME);
        browserService.navigate(TEST_URL);

        String url = browserService.getCurrentUrl();
        assertNotNull(url, "Current URL should not be null");
        assertTrue(url.contains("shafthq.github.io"), "URL should contain the navigated domain");
        logger.info("Retrieved current URL: {}", url);
    }

    /**
     * Tests that the MCP server can retrieve text content from a page element.
     */
    @Test
    void testElementTextRetrieval() {
        engineService.initializeDriver(BrowserType.CHROME);
        browserService.navigate(TEST_URL);

        String text = elementService.getText(locatorStrategy.TAGNAME, "h1");
        assertNotNull(text, "Element text should not be null");
        assertTrue(text.contains("SHAFT"), "H1 text should contain 'SHAFT'");
        logger.info("Retrieved element text: {}", text);
    }

    /**
     * Tests that the MCP server can retrieve the page source HTML.
     */
    @Test
    void testPageSourceRetrieval() {
        engineService.initializeDriver(BrowserType.CHROME);
        browserService.navigate(TEST_URL);

        String source = engineService.getPageSource();
        assertNotNull(source, "Page source should not be null");
        assertFalse(source.isEmpty(), "Page source should not be empty");
        assertTrue(source.contains("<html"), "Page source should contain HTML markup");
        logger.info("Retrieved page source ({} chars)", source.length());
    }

    /**
     * Tests a full browser session lifecycle: initialize, navigate, interact, quit.
     * This verifies the complete MCP tool chain works end-to-end.
     */
    @Test
    void testFullBrowserSessionLifecycle() {
        // Initialize browser
        engineService.initializeDriver(BrowserType.CHROME);

        // Navigate to URL
        browserService.navigate(TEST_URL);

        // Verify navigation succeeded via URL
        String url = browserService.getCurrentUrl();
        assertTrue(url.contains("shafthq.github.io"), "Should be on the expected page");

        // Verify page has expected content via element text
        String heading = elementService.getText(locatorStrategy.TAGNAME, "h1");
        assertTrue(heading.contains("SHAFT"), "Page heading should contain 'SHAFT'");

        // Verify page title
        String title = browserService.getTitle();
        assertNotNull(title, "Title should not be null");
        assertFalse(title.isEmpty(), "Title should not be empty");

        logger.info("Full browser session lifecycle completed successfully");
        // Driver cleanup is handled by @AfterEach tearDown()
    }

    /**
     * Tests that using browser services after quitting the driver session fails.
     * After quitDriver(), the underlying WebDriver session is closed, so any
     * subsequent browser operations should throw an exception.
     */
    @Test
    void testBrowserOperationsFailAfterQuit() {
        engineService.initializeDriver(BrowserType.CHROME);
        engineService.quitDriver();

        // After quit, the WebDriver session is closed so browser operations should fail
        assertThrows(Exception.class, () -> browserService.getCurrentUrl(),
                "Browser operations after quit should throw an exception");
    }
}
