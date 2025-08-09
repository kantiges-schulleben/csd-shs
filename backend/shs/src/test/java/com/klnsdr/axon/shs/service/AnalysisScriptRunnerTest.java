package com.klnsdr.axon.shs.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.util.Pair;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AnalysisScriptRunnerTest {
    private AnalysisScriptRunner analysisScriptRunner;

    @BeforeEach
    public void setUp() {
        analysisScriptRunner = spy(new AnalysisScriptRunner());
    }

    @Test
    public void testRunAnalysisScript_ScriptCopyFails() {
        doReturn(Optional.empty()).when(analysisScriptRunner).copyScriptToTmp();

        Pair<Boolean, String> result = analysisScriptRunner.runAnalysisScript("param");

        assertFalse(result.getFirst());
        assertEquals("Failed to copy analysis script to temporary location", result.getSecond());
    }

    @Test
    public void testRunAnalysisScript_Success() throws Exception {
        Path fakePath = Path.of("/tmp/fakeScript.py");

        doReturn(Optional.of(fakePath)).when(analysisScriptRunner).copyScriptToTmp();
        doReturn(Pair.of(0, "Script output")).when(analysisScriptRunner)
                .runScript(fakePath, "param");

        Pair<Boolean, String> result = analysisScriptRunner.runAnalysisScript("param");

        assertTrue(result.getFirst());
        assertEquals("Script output", result.getSecond());
    }

    @Test
    public void testRunAnalysisScript_NonZeroExitCode() throws Exception {
        Path fakePath = Path.of("/tmp/fakeScript.py");

        doReturn(Optional.of(fakePath)).when(analysisScriptRunner).copyScriptToTmp();
        doReturn(Pair.of(1, "Error occurred")).when(analysisScriptRunner)
                .runScript(fakePath, "param");

        Pair<Boolean, String> result = analysisScriptRunner.runAnalysisScript("param");

        assertFalse(result.getFirst());
        assertEquals("Script execution failed: Error occurred", result.getSecond());
    }

    @Test
    public void testRunAnalysisScript_RunScriptThrowsIOException() throws Exception {
        Path fakePath = Path.of("/tmp/fakeScript.py");

        doReturn(Optional.of(fakePath)).when(analysisScriptRunner).copyScriptToTmp();
        doThrow(new IOException("IO problem")).when(analysisScriptRunner)
                .runScript(fakePath, "param");

        Pair<Boolean, String> result = analysisScriptRunner.runAnalysisScript("param");

        assertFalse(result.getFirst());
        assertTrue(result.getSecond().contains("IO problem"));
    }

    @Test
    public void testRunAnalysisScript_RunScriptThrowsInterruptedException() throws Exception {
        Path fakePath = Path.of("/tmp/fakeScript.py");

        doReturn(Optional.of(fakePath)).when(analysisScriptRunner).copyScriptToTmp();
        doThrow(new InterruptedException("Interrupted")).when(analysisScriptRunner)
                .runScript(fakePath, "param");

        Pair<Boolean, String> result = analysisScriptRunner.runAnalysisScript("param");

        assertFalse(result.getFirst());
        assertTrue(result.getSecond().contains("Interrupted"));
    }
}
