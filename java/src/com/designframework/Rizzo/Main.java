package com.designframework.Rizzo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {
	// write your code here
        //MyJobScheduler jobScheduler = new MyJobScheduler();
        //jobScheduler.main();
    }

    static Stream<AJob> emit() {
        String folder_path = "./parole";
        String fileExtension = ".txt";
        try (Stream<Path> walk = Files.walk(Paths.get(folder_path))) {
            return walk.filter(p -> !Files.isDirectory(p))
                    .filter(f -> f.endsWith(fileExtension))
                    .map(p -> new MyJob(p.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
