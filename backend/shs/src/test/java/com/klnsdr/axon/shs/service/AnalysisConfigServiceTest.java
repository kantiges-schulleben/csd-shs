package com.klnsdr.axon.shs.service;

import com.klnsdr.axon.shs.entity.analysis.AnalysisConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.util.Pair;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AnalysisConfigServiceTest {
    private AnalysisConfigService analysisConfigService;
    private AnalysisConfigRepository mockAnalysisConfigRepository;

    private static final String RESULT_SUCCESS_KEY = "result_success";
    private static final String RESULT_MESSAGE_KEY = "result_message";
    private static final String IS_PHASE_TWO = "is_phase_two";

    @BeforeEach
    public void setUp() {
        mockAnalysisConfigRepository = mock(AnalysisConfigRepository.class);
        analysisConfigService = new AnalysisConfigService(mockAnalysisConfigRepository);
    }

    @Test
    public void resetSetsPhaseTwoToFalse() {
        analysisConfigService.reset();

        verify(mockAnalysisConfigRepository, times(1)).save(argThat(config ->
                IS_PHASE_TWO.equals(config.getKey()) && "false".equals(config.getValue())));
    }

    @Test
    public void setResultSavesSuccessAndMessageConfigs() {
        final boolean success = true;
        final String message = "Operation completed";

        analysisConfigService.setResult(success, message);

        verify(mockAnalysisConfigRepository, times(1)).save(argThat(config ->
                RESULT_SUCCESS_KEY.equals(config.getKey()) && "true".equals(config.getValue())));
        verify(mockAnalysisConfigRepository, times(1)).save(argThat(config ->
                RESULT_MESSAGE_KEY.equals(config.getKey()) && message.equals(config.getValue())));
    }

    @Test
    public void getResultReturnsSuccessAndMessageWhenConfigsExist() {
        final AnalysisConfig successConfig = new AnalysisConfig();
        successConfig.setKey(RESULT_SUCCESS_KEY);
        successConfig.setValue("true");

        final AnalysisConfig messageConfig = new AnalysisConfig();
        messageConfig.setKey(RESULT_MESSAGE_KEY);
        messageConfig.setValue("Operation completed");

        when(mockAnalysisConfigRepository.findById(RESULT_SUCCESS_KEY)).thenReturn(Optional.of(successConfig));
        when(mockAnalysisConfigRepository.findById(RESULT_MESSAGE_KEY)).thenReturn(Optional.of(messageConfig));

        Pair<Boolean, String> result = analysisConfigService.getResult();
        assertTrue(result.getFirst());
        assertEquals("Operation completed", result.getSecond());

        verify(mockAnalysisConfigRepository, times(1)).findById(RESULT_SUCCESS_KEY);
        verify(mockAnalysisConfigRepository, times(1)).findById(RESULT_MESSAGE_KEY);
    }

    @Test
    public void getResultReturnsDefaultValuesWhenConfigsDoNotExist() {
        when(mockAnalysisConfigRepository.findById(RESULT_SUCCESS_KEY)).thenReturn(Optional.empty());
        when(mockAnalysisConfigRepository.findById(RESULT_MESSAGE_KEY)).thenReturn(Optional.empty());

        Pair<Boolean, String> result = analysisConfigService.getResult();
        assertFalse(result.getFirst());
        assertEquals("", result.getSecond());

        verify(mockAnalysisConfigRepository, times(1)).findById(RESULT_SUCCESS_KEY);
        verify(mockAnalysisConfigRepository, times(1)).findById(RESULT_MESSAGE_KEY);
    }

    @Test
    public void setPhaseTwoSavesPhaseTwoConfig() {
        final boolean isPhaseTwo = true;

        analysisConfigService.setPhaseTwo(isPhaseTwo);

        verify(mockAnalysisConfigRepository, times(1)).save(argThat(config ->
                IS_PHASE_TWO.equals(config.getKey()) && "true".equals(config.getValue())));
    }

    @Test
    public void isPhaseTwoReturnsTrueWhenConfigExistsAndIsTrue() {
        final AnalysisConfig phaseTwoConfig = new AnalysisConfig();
        phaseTwoConfig.setKey(IS_PHASE_TWO);
        phaseTwoConfig.setValue("true");

        when(mockAnalysisConfigRepository.findById(IS_PHASE_TWO)).thenReturn(Optional.of(phaseTwoConfig));

        boolean result = analysisConfigService.isPhaseTwo();
        assertTrue(result);

        verify(mockAnalysisConfigRepository, times(1)).findById(IS_PHASE_TWO);
    }

    @Test
    public void isPhaseTwoReturnsFalseWhenConfigDoesNotExist() {
        when(mockAnalysisConfigRepository.findById(IS_PHASE_TWO)).thenReturn(Optional.empty());

        boolean result = analysisConfigService.isPhaseTwo();
        assertFalse(result);

        verify(mockAnalysisConfigRepository, times(1)).findById(IS_PHASE_TWO);
    }
}
