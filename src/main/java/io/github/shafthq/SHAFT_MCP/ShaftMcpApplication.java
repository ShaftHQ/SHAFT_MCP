package io.github.shafthq.SHAFT_MCP;

import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class ShaftMcpApplication {

    /**
     * The main entry point for the ShaftMcpApplication.
     * @param args command-line arguments
     */
	public static void main(String[] args) {
		SpringApplication.run(ShaftMcpApplication.class, args);
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
