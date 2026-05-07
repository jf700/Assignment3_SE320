package com.digitaltherapy.mcp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.digitaltherapy.dto.ActiveSession;
import com.digitaltherapy.dto.ChatResponse;
import com.digitaltherapy.dto.CopingStrategy;
import com.digitaltherapy.dto.CrisisDetectionResultDto;
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

class TherapyToolsProviderTest {

    @Mock private SessionService sessionService;
    @Mock private DiaryService diaryService;
    @Mock private AiService aiService;
    @Mock private CrisisService crisisService;
    @Mock private ProgressService progressService;

    private TherapyToolsProvider provider;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        provider = new TherapyToolsProvider(sessionService, diaryService, aiService, crisisService, progressService);
    }

    @Test
    void startSession_delegatesToSessionService() {
        String userId = UUID.randomUUID().toString();
        String sessionId = UUID.randomUUID().toString();
        ActiveSession expected = new ActiveSession();
        when(sessionService.startSession(any(UUID.class), any(UUID.class))).thenReturn(expected);

        ActiveSession result = provider.startSession(userId, sessionId);

        assertEquals(expected, result);
        verify(sessionService).startSession(UUID.fromString(userId), UUID.fromString(sessionId));
    }

    @Test
    void chatInSession_delegatesToSessionService() {
        String sessionId = UUID.randomUUID().toString();
        ChatResponse expected = new ChatResponse();
        when(sessionService.chat(any(UUID.class), eq("Hello"))).thenReturn(expected);

        ChatResponse result = provider.chatInSession(sessionId, "Hello");

        assertEquals(expected, result);
    }

    @Test
    void endSession_delegatesToSessionService() {
        String sessionId = UUID.randomUUID().toString();
        SessionSummary expected = new SessionSummary();
        when(sessionService.endSession(any(UUID.class), eq("done"))).thenReturn(expected);

        SessionSummary result = provider.endSession(sessionId, "done");

        assertEquals(expected, result);
    }

    @Test
    void getSessionLibrary_delegatesToSessionService() {
        String userId = UUID.randomUUID().toString();
        List<SessionModuleDto> expected = List.of();
        when(sessionService.getSessionLibrary(any(UUID.class))).thenReturn(expected);

        List<SessionModuleDto> result = provider.getSessionLibrary(userId);

        assertEquals(expected, result);
    }

    @Test
    void getSessionHistory_delegatesToSessionService() {
        String userId = UUID.randomUUID().toString();
        List<SessionHistoryEntry> expected = List.of();
        when(sessionService.getSessionHistory(any(UUID.class))).thenReturn(expected);

        List<SessionHistoryEntry> result = provider.getSessionHistory(userId);

        assertEquals(expected, result);
    }

    @Test
    void analyzeThought_delegatesToAiService() {
        List<DistortionSuggestion> expected = List.of();
        when(aiService.analyzeThought("I'm a failure")).thenReturn(expected);

        List<DistortionSuggestion> result = provider.analyzeThought("I'm a failure");

        assertEquals(expected, result);
    }

    @Test
    void suggestReframing_delegatesToAiService() {
        List<String> expected = List.of("Try thinking about it differently");
        when(aiService.generateReframingPrompts(eq("I'm a failure"), any())).thenReturn(expected);

        List<String> result = provider.suggestReframing("I'm a failure", "all-or-nothing,catastrophizing");

        assertEquals(expected, result);
    }

    @Test
    void detectCrisis_delegatesToCrisisService() {
        CrisisDetectionResultDto expected = new CrisisDetectionResultDto();
        when(crisisService.detectCrisis("I feel hopeless")).thenReturn(expected);

        CrisisDetectionResultDto result = provider.detectCrisis("I feel hopeless");

        assertEquals(expected, result);
    }

    @Test
    void getWeeklyProgress_delegatesToProgressService() {
        String userId = UUID.randomUUID().toString();
        WeeklyProgress expected = new WeeklyProgress();
        when(progressService.getWeeklyProgress(any(UUID.class))).thenReturn(expected);

        WeeklyProgress result = provider.getWeeklyProgress(userId);

        assertEquals(expected, result);
    }

    @Test
    void getInsights_delegatesToDiaryService() {
        String userId = UUID.randomUUID().toString();
        DiaryInsights expected = new DiaryInsights();
        when(diaryService.getInsights(any(UUID.class))).thenReturn(expected);

        DiaryInsights result = provider.getInsights(userId);

        assertEquals(expected, result);
    }

    @Test
    void getCopingStrategies_delegatesToCrisisService() {
        List<CopingStrategy> expected = List.of();
        when(crisisService.getCopingStrategies()).thenReturn(expected);

        List<CopingStrategy> result = provider.getCopingStrategies();

        assertEquals(expected, result);
    }
}
