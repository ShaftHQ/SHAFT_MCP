package io.github.shafthq.SHAFT_MCP;

import com.shaft.driver.SHAFT;
import org.apache.commons.io.FileUtils;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;

@SpringBootApplication
public class ShaftMcpApplication {

    /**
     * The main entry point for the ShaftMcpApplication.
     * @param args command-line arguments
     */
	public static void main(String[] args) throws IOException {
        SpringApplication app = new SpringApplication(ShaftMcpApplication.class);

        URL url = URI.create("https://raw.githubusercontent.com/ShaftHQ/SHAFT_MCP/refs/heads/main/src/main/resources/application.properties").toURL();
        File propertiesFile = new File("application.properties");
        FileUtils.copyURLToFile(url, propertiesFile);

        var props = new Properties();
        props.load(Files.newInputStream(Path.of(propertiesFile.toURI())));
        app.setBannerMode(Banner.Mode.OFF);
        app.setDefaultProperties(props);
        app.run(args);
	}

    /**
     * Registers the ShaftService tool callbacks.
     * @param shaftService the ShaftService instance
     * @return a list of ToolCallback instances
     */
	@Bean
	public List<ToolCallback> shaftTools(ShaftService shaftService) {
		return List.of(ToolCallbacks.from(shaftService));
	}
}
