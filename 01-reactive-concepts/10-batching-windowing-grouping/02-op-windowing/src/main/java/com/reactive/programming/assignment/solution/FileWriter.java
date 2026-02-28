package com.reactive.programming.assignment.solution;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileWriter {

    private static final Logger log = LoggerFactory.getLogger(FileWriter.class);
    private final Path path;
    private BufferedWriter writer;

    public FileWriter(Path path) {
        this.path = path;
    }

    public void createFile() {
        try {
            this.writer = Files.newBufferedWriter(path);
            log.info("Created File: {}", path.getFileName());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void closeFile() {
        try {
            this.writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void write(String content) {
        try {
            this.writer.write(content);
            this.writer.newLine();
            this.writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
