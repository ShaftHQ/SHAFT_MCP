package io.github.shafthq.SHAFT_MCP;

import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class ShaftMcpApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShaftMcpApplication.class, args);
	}

	@Bean
	public List<ToolCallback> shaftTools(ShaftService shaftService) {
		return List.of(ToolCallbacks.from(shaftService));
	}

}
