package com.designframework.Rizzo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MyJobScheduler extends JobSchedulerFramerowk {

    private String folder_path;
    public MyJobScheduler(String folder_path) {
        this.folder_path = folder_path;
    }

    @Override
    Stream<AJob> emit() {
        String fileExtension = ".txt";
        try (Stream<Path> walk = Files.walk(Paths.get(folder_path))) {
            return walk.filter(p -> !Files.isDirectory(p))
                    .filter(f -> f.endsWith(fileExtension))
                    .map(p -> new MyJob(p.toString()));
        } catch (IOException e) {
            e.printStackTrace();
            return Stream.empty();
        }
    }

    @Override
    void output(Stream collected_stream) {

    }
}
