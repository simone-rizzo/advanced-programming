package com.designframework.Rizzo;

import javafx.util.Pair;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MyJobScheduler extends JobSchedulerFramerowk {

    @Override
    Stream<AJob> emit() {
        String folder_path = "./parole";
        String fileExtension = ".txt";
        try (Stream<Path> walk = Files.walk(Paths.get(folder_path))) {
            return walk.filter(p -> !Files.isDirectory(p))
                    .filter(f -> f.endsWith(fileExtension))
                    .map(p -> new MyJob(p.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //leggi tutti i file .txt
            //passali nel costruttore di MyJob
        //ritorna lo stream di myjob.
        return null;
    }

    @Override
    void output(Stream collected_stream) {

    }
}
