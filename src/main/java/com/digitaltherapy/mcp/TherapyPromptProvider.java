package com.digitaltherapy.mcp;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Component
public class TherapyPromptProvider {

    @Tool(description = "Prompt: Structured prompt for analyzing an automatic thought for cognitive distortions")
    public String thoughtAnalysis(
            @ToolParam(description = "The automatic thought to analyze") String thought) {
        return """
                You are a cognitive behavioral therapy assistant. Analyze the following automatic thought \
                for cognitive distortions. For each distortion found, provide:
                1. The distortion type (e.g., All-or-Nothing Thinking, Catastrophizing, Mind Reading)
                2. A confidence score (0.0 to 1.0)
                3. A brief explanation of why this distortion applies
                4. A suggested reframing of the thought

                Automatic Thought: "%s"

                Respond with a structured analysis identifying all applicable cognitive distortions.
                """.formatted(thought);
    }

    @Tool(description = "Prompt: Prompt for generating a therapeutic session summary")
    public String sessionSummary(
            @ToolParam(description = "The session UUID to summarize") String sessionId) {
        return """
                You are a cognitive behavioral therapy assistant. Generate a comprehensive therapeutic \
                session summary for session ID: %s. Include:
                1. Key themes discussed during the session
                2. Cognitive distortions identified
                3. Coping strategies explored
                4. Progress observations
                5. Recommended follow-up actions
                6. Mood trajectory during the session

                Provide an empathetic, professional summary suitable for therapeutic records.
                """.formatted(sessionId);
    }

    @Tool(description = "Prompt: Guided weekly check-in template with mood and progress questions")
    public String weeklyCheckIn(
            @ToolParam(description = "The user's UUID") String userId) {
        return """
                You are a cognitive behavioral therapy assistant conducting a weekly check-in for user %s. \
                Guide the conversation through these areas:

                1. **Mood Check**: "How would you rate your overall mood this week on a scale of 1-10?"
                2. **Highlights**: "What went well this week? Any small wins you'd like to celebrate?"
                3. **Challenges**: "What challenges did you face? How did you cope with them?"
                4. **Thought Patterns**: "Did you notice any recurring thought patterns this week?"
                5. **Coping Skills**: "Which coping strategies did you use? How effective were they?"
                6. **Goals**: "What would you like to focus on in the coming week?"
                7. **Self-Care**: "How have you been taking care of yourself physically and emotionally?"

                Be warm, supportive, and non-judgmental. Validate their experiences and \
                offer gentle therapeutic guidance.
                """.formatted(userId);
    }
}
