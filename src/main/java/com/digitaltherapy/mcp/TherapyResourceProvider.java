package com.digitaltherapy.mcp;

import java.util.List;
import java.util.UUID;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.digitaltherapy.dto.CrisisHub;
import com.digitaltherapy.dto.DiaryEntryDetail;
import com.digitaltherapy.dto.DiaryEntrySummary;
import com.digitaltherapy.dto.SafetyPlanDto;
import com.digitaltherapy.dto.SessionDetail;
import com.digitaltherapy.dto.WeeklyProgress;
import com.digitaltherapy.entity.CognitiveDistortion;
import com.digitaltherapy.repository.CognitiveDistortionRepository;
import com.digitaltherapy.service.CrisisService;
import com.digitaltherapy.service.DiaryService;
import com.digitaltherapy.service.ProgressService;
import com.digitaltherapy.service.SessionService;

@Component
public class TherapyResourceProvider {

    private final SessionService sessionService;
    private final DiaryService diaryService;
    private final ProgressService progressService;
    private final CrisisService crisisService;
    private final CognitiveDistortionRepository distortionRepository;

    public TherapyResourceProvider(@Lazy SessionService sessionService, @Lazy DiaryService diaryService,
                                   @Lazy ProgressService progressService, @Lazy CrisisService crisisService,
                                   @Lazy CognitiveDistortionRepository distortionRepository) {
        this.sessionService = sessionService;
        this.diaryService = diaryService;
        this.progressService = progressService;
        this.crisisService = crisisService;
        this.distortionRepository = distortionRepository;
    }

    @Tool(description = "Resource: Get session details by ID (therapy://sessions/{sessionId})")
    public SessionDetail getSessionResource(
            @ToolParam(description = "The session UUID") String sessionId) {
        return sessionService.getSessionDetails(UUID.fromString(sessionId));
    }

    @Tool(description = "Resource: Get all diary entries for a user (therapy://diary/{userId})")
    public List<DiaryEntrySummary> getDiaryResource(
            @ToolParam(description = "The user's UUID") String userId) {
        return diaryService.getEntries(UUID.fromString(userId), Pageable.unpaged()).getContent();
    }

    @Tool(description = "Resource: Get a single diary entry detail (therapy://diary/entry/{entryId})")
    public DiaryEntryDetail getDiaryEntryResource(
            @ToolParam(description = "The diary entry UUID") String entryId) {
        return diaryService.getEntryDetail(UUID.fromString(entryId));
    }

    @Tool(description = "Resource: Get user's progress overview (therapy://progress/{userId})")
    public WeeklyProgress getProgressResource(
            @ToolParam(description = "The user's UUID") String userId) {
        return progressService.getWeeklyProgress(UUID.fromString(userId));
    }

    @Tool(description = "Resource: Get all cognitive distortion definitions (therapy://distortions)")
    public List<CognitiveDistortion> getDistortionsResource() {
        return distortionRepository.findAll();
    }

    @Tool(description = "Resource: Get emergency resources and contacts (therapy://crisis/resources)")
    public CrisisHub getCrisisResourcesResource() {
        return crisisService.getCrisisHub(null);
    }

    @Tool(description = "Resource: Get user's safety plan (therapy://safety-plan/{userId})")
    public SafetyPlanDto getSafetyPlanResource(
            @ToolParam(description = "The user's UUID") String userId) {
        return crisisService.getSafetyPlan(UUID.fromString(userId));
    }
}
