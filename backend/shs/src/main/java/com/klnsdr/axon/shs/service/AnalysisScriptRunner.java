package com.klnsdr.axon.shs.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

@Component
public class AnalysisScriptRunner {
    private static final Logger logger = LoggerFactory.getLogger(AnalysisScriptRunner.class);
    public Pair<Boolean, String> runAnalysisScript(String param) {
        final Optional<Path> scriptPath = copyScriptToTmp();
        if (scriptPath.isEmpty()) {
            return Pair.of(false, "Failed to copy analysis script to temporary location");
        }

        try {
            final Pair<Integer, String> result = runScript(scriptPath.get(), param);
            if (result.getFirst() == 0) {
                return Pair.of(true, result.getSecond());
            } else {
                logger.error("Script execution failed with exit code {}: {}", result.getFirst(), result.getSecond());
                return Pair.of(false, "Script execution failed: " + result.getSecond());
            }
        } catch (IOException | InterruptedException e) {
            logger.error("Error running analysis script", e);
            return Pair.of(false, "Error running analysis script: " + e.getMessage());
        } finally {
            try {
                Files.deleteIfExists(Path.of("/tmp/script.py"));
            } catch (IOException e) {
                logger.error("Failed to delete temporary script file", e);
            }
        }
    }

    Pair<Integer, String> runScript(Path path, String param) throws IOException, InterruptedException {
        final ProcessBuilder pb = new ProcessBuilder("python3", path.toString(), param);
        final Process process = pb.start();

        final StringBuilder output = new StringBuilder();
        final StringBuilder errorOutput = new StringBuilder();

        final Thread stdoutThread = new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append(System.lineSeparator());
                }
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        });

        final Thread stderrThread = new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    errorOutput.append(line).append(System.lineSeparator());
                }
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        });

        stdoutThread.start();
        stderrThread.start();

        int exitCode = process.waitFor();
        stdoutThread.join();
        stderrThread.join();

        return Pair.of(exitCode, exitCode == 0 ? output.toString() : errorOutput.toString());
    }

    Optional<Path> copyScriptToTmp() {
        final InputStream scriptStream = AnalysisScriptRunner.class.getResourceAsStream("/script.py");
        if (scriptStream == null) {
            logger.error("Cannot find analysis script script.py");
            return Optional.empty();
        }

        try {
            final Path tempScript = Files.createTempFile("script", ".py");
            Files.copy(scriptStream, tempScript, StandardCopyOption.REPLACE_EXISTING);
            if (!tempScript.toFile().setExecutable(true)) {
                throw new IOException("Failed to set script executable: " + tempScript);
            }
            return Optional.of(tempScript);
        } catch (Exception e) {
            logger.error("Failed to copy script", e);
            return Optional.empty();
        }
    }
}
