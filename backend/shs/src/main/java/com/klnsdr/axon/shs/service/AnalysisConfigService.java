package com.klnsdr.axon.shs.service;

import com.klnsdr.axon.shs.entity.analysis.AnalysisConfig;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AnalysisConfigService {
    private static final String RESULT_SUCCESS_KEY = "result_success";
    private static final String RESULT_MESSAGE_KEY = "result_message";
    private static final String IS_PHASE_TWO = "is_phase_two";

    private final AnalysisConfigRepository analysisConfigRepository;

    public AnalysisConfigService(AnalysisConfigRepository analysisConfigRepository) {
        this.analysisConfigRepository = analysisConfigRepository;
    }

    public void reset() {
        setPhaseTwo(false);
    }

    @Transactional
    public void setResult(boolean success, String message) {
        final AnalysisConfig successConfig = new AnalysisConfig();
        successConfig.setKey(RESULT_SUCCESS_KEY);
        successConfig.setValue(Boolean.toString(success));
        analysisConfigRepository.save(successConfig);

        final AnalysisConfig messageConfig = new AnalysisConfig();
        messageConfig.setKey(RESULT_MESSAGE_KEY);
        messageConfig.setValue(message);
        analysisConfigRepository.save(messageConfig);
    }

    @Transactional(readOnly = true)
    public Pair<Boolean, String> getResult() {
        final AnalysisConfig successConfig = analysisConfigRepository.findById(RESULT_SUCCESS_KEY).orElse(null);
        final AnalysisConfig messageConfig = analysisConfigRepository.findById(RESULT_MESSAGE_KEY).orElse(null);

        final boolean success = successConfig != null && Boolean.parseBoolean(successConfig.getValue());
        final String message = messageConfig != null ? messageConfig.getValue() : "";

        return Pair.of(success, message);
    }

    public void setPhaseTwo(boolean isPhaseTwo) {
        final AnalysisConfig phaseTwoConfig = new AnalysisConfig();
        phaseTwoConfig.setKey(IS_PHASE_TWO);
        phaseTwoConfig.setValue(Boolean.toString(isPhaseTwo));
        analysisConfigRepository.save(phaseTwoConfig);
    }

    public boolean isPhaseTwo() {
        final AnalysisConfig phaseTwoConfig = analysisConfigRepository.findById(IS_PHASE_TWO).orElse(null);
        return phaseTwoConfig != null && Boolean.parseBoolean(phaseTwoConfig.getValue());
    }
}
