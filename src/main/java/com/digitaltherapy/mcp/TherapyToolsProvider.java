package com.digitaltherapy.mcp;

import java.util.List;
import java.util.UUID;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.digitaltherapy.dto.ActiveSession;
import com.digitaltherapy.dto.ChatResponse;
import com.digitaltherapy.dto.CopingStrategy;
import com.digitaltherapy.dto.CrisisDetectionResultDto;
import com.digitaltherapy.dto.DiaryEntryCreate;
import com.digitaltherapy.dto.DiaryEntryResponse;
import com.digitaltherapy.dto.DiaryInsights;
import com.digitaltherapy.dto.DistortionSuggestion;
import com.digitaltherapy.dto.SessionHistoryEntry;
import com.digitaltherapy.dto.SessionModuleDto;
import com.digitaltherapy.dto.SessionSummary;
import com.digitaltherapy.dto.WeeklyProgress;
import com.digitaltherapy.service.AiService;
import com.digitaltherapy.service.CrisisService;
import com.digitaltherapy.service.DiaryService;
import com.digitaltherapy.service.ProgressService;
import com.digitaltherapy.service.SessionService;

@Component
public class TherapyToolsProvider {

    private final SessionService sessionService;
    private final DiaryService diaryService;
    private final AiService aiService;
    private final CrisisService crisisService;
    private final ProgressService progressService;

    public TherapyToolsProvider(@Lazy SessionService sessionService, @Lazy DiaryService diaryService,
                                @Lazy AiService aiService, @Lazy CrisisService crisisService,
                                @Lazy ProgressService progressService) {
        this.sessionService = sessionService;
        this.diaryService = diaryService;
        this.aiService = aiService;
        this.crisisService = crisisService;
        this.progressService = progressService;
    }

    @Tool(description = "Start a new CBT session for a user")
    public ActiveSession startSession(
            @ToolParam(description = "The user's UUID") String userId,
            @ToolParam(description = "The CBT session module UUID") String sessionId) {
        return sessionService.startSession(UUID.fromString(userId), UUID.fromString(sessionId));
    }

    @Tool(description = "Send a message in an active CBT session")
    public ChatResponse chatInSession(
            @ToolParam(description = "The active session UUID") String sessionId,
            @ToolParam(description = "The user's message") String message) {
        return sessionService.chat(UUID.fromString(sessionId), message);
    }

    @Tool(description = "End an active session with summary")
    public SessionSummary endSession(
            @ToolParam(description = "The active session UUID") String sessionId,
            @ToolParam(description = "Reason for ending the session") String reason) {
        return sessionService.endSession(UUID.fromString(sessionId), reason);
    }

    @Tool(description = "List available CBT session modules")
    public List<SessionModuleDto> getSessionLibrary(
            @ToolParam(description = "The user's UUID") String userId) {
        return sessionService.getSessionLibrary(UUID.fromString(userId));
    }

    @Tool(description = "View user's past session history")
    public List<SessionHistoryEntry> getSessionHistory(
            @ToolParam(description = "The user's UUID") String userId) {
        return sessionService.getSessionHistory(UUID.fromString(userId));
    }

    @Tool(description = "Create a thought diary entry")
    public DiaryEntryResponse createDiaryEntry(
            @ToolParam(description = "The user's UUID") String userId,
            @ToolParam(description = "The situation that triggered the thought") String situation,
            @ToolParam(description = "The automatic thought that occurred") String automaticThought,
            @ToolParam(description = "Comma-separated list of emotions") String emotions) {
        DiaryEntryCreate request = new DiaryEntryCreate();
        request.setSituation(situation);
        request.setAutomaticThought(automaticThought);
        return diaryService.createEntry(UUID.fromString(userId), request);
    }

    @Tool(description = "Analyze a thought for cognitive distortions")
    public List<DistortionSuggestion> analyzeThought(
            @ToolParam(description = "The automatic thought to analyze") String thought) {
        return aiService.analyzeThought(thought);
    }

    @Tool(description = "Generate reframing prompts for a thought")
    public List<String> suggestReframing(
            @ToolParam(description = "The automatic thought to reframe") String thought,
            @ToolParam(description = "Comma-separated cognitive distortion IDs") String distortionIds) {
        List<String> ids = List.of(distortionIds.split(","));
        return aiService.generateReframingPrompts(thought, ids);
    }

    @Tool(description = "Analyze text for crisis indicators")
    public CrisisDetectionResultDto detectCrisis(
            @ToolParam(description = "The text to analyze for crisis indicators") String text) {
        return crisisService.detectCrisis(text);
    }

    @Tool(description = "Get user's weekly progress summary")
    public WeeklyProgress getWeeklyProgress(
            @ToolParam(description = "The user's UUID") String userId) {
        return progressService.getWeeklyProgress(UUID.fromString(userId));
    }

    @Tool(description = "Get AI-generated insights from diary entries")
    public DiaryInsights getInsights(
            @ToolParam(description = "The user's UUID") String userId) {
        return diaryService.getInsights(UUID.fromString(userId));
    }

    @Tool(description = "Retrieve coping strategies")
    public List<CopingStrategy> getCopingStrategies() {
        return crisisService.getCopingStrategies();
    }
}
