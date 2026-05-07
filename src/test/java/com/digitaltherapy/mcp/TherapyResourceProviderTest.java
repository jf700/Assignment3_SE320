package com.digitaltherapy.mcp;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

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

class TherapyResourceProviderTest {

    @Mock private SessionService sessionService;
    @Mock private DiaryService diaryService;
    @Mock private ProgressService progressService;
    @Mock private CrisisService crisisService;
    @Mock private CognitiveDistortionRepository distortionRepository;

    private TherapyResourceProvider provider;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        provider = new TherapyResourceProvider(sessionService, diaryService, progressService, crisisService, distortionRepository);
    }

    @Test
    void getSessionResource_returnsSessionDetail() {
        String sessionId = UUID.randomUUID().toString();
        SessionDetail expected = new SessionDetail();
        when(sessionService.getSessionDetails(any(UUID.class))).thenReturn(expected);

        SessionDetail result = provider.getSessionResource(sessionId);

        assertEquals(expected, result);
    }

    @Test
    void getDiaryResource_returnsDiaryEntries() {
        String userId = UUID.randomUUID().toString();
        List<DiaryEntrySummary> entries = List.of();
        Page<DiaryEntrySummary> page = new PageImpl<>(entries);
        when(diaryService.getEntries(any(UUID.class), any(Pageable.class))).thenReturn(page);

        List<DiaryEntrySummary> result = provider.getDiaryResource(userId);

        assertEquals(entries, result);
    }

    @Test
    void getDiaryEntryResource_returnsEntryDetail() {
        String entryId = UUID.randomUUID().toString();
        DiaryEntryDetail expected = new DiaryEntryDetail();
        when(diaryService.getEntryDetail(any(UUID.class))).thenReturn(expected);

        DiaryEntryDetail result = provider.getDiaryEntryResource(entryId);

        assertEquals(expected, result);
    }

    @Test
    void getProgressResource_returnsWeeklyProgress() {
        String userId = UUID.randomUUID().toString();
        WeeklyProgress expected = new WeeklyProgress();
        when(progressService.getWeeklyProgress(any(UUID.class))).thenReturn(expected);

        WeeklyProgress result = provider.getProgressResource(userId);

        assertEquals(expected, result);
    }

    @Test
    void getDistortionsResource_returnsAllDistortions() {
        List<CognitiveDistortion> expected = List.of();
        when(distortionRepository.findAll()).thenReturn(expected);

        List<CognitiveDistortion> result = provider.getDistortionsResource();

        assertEquals(expected, result);
    }

    @Test
    void getCrisisResourcesResource_returnsCrisisHub() {
        CrisisHub expected = new CrisisHub();
        when(crisisService.getCrisisHub(null)).thenReturn(expected);

        CrisisHub result = provider.getCrisisResourcesResource();

        assertEquals(expected, result);
    }

    @Test
    void getSafetyPlanResource_returnsSafetyPlan() {
        String userId = UUID.randomUUID().toString();
        SafetyPlanDto expected = new SafetyPlanDto();
        when(crisisService.getSafetyPlan(any(UUID.class))).thenReturn(expected);

        SafetyPlanDto result = provider.getSafetyPlanResource(userId);

        assertEquals(expected, result);
    }
}
