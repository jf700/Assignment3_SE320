package com.digitaltherapy.mcp;

import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class McpServerConfig {

    @Bean
    public ToolCallbackProvider therapyToolCallbacks(
            TherapyToolsProvider toolsProvider,
            TherapyResourceProvider resourceProvider,
            TherapyPromptProvider promptProvider) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(toolsProvider, resourceProvider, promptProvider)
                .build();
    }
}
