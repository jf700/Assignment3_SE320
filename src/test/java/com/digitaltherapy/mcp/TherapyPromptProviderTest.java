package com.digitaltherapy.mcp;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TherapyPromptProviderTest {

    private TherapyPromptProvider provider;

    @BeforeEach
    void setUp() {
        provider = new TherapyPromptProvider();
    }

    @Test
    void thoughtAnalysis_containsThoughtInPrompt() {
        String thought = "Nobody likes me";
        String result = provider.thoughtAnalysis(thought);

        assertTrue(result.contains(thought));
        assertTrue(result.contains("cognitive distortions"));
        assertTrue(result.contains("confidence score"));
    }

    @Test
    void sessionSummary_containsSessionIdInPrompt() {
        String sessionId = UUID.randomUUID().toString();
        String result = provider.sessionSummary(sessionId);

        assertTrue(result.contains(sessionId));
        assertTrue(result.contains("session summary"));
    }

    @Test
    void weeklyCheckIn_containsUserIdInPrompt() {
        String userId = UUID.randomUUID().toString();
        String result = provider.weeklyCheckIn(userId);

        assertTrue(result.contains(userId));
        assertTrue(result.contains("Mood Check"));
        assertTrue(result.contains("Challenges"));
        assertTrue(result.contains("Goals"));
    }
}
