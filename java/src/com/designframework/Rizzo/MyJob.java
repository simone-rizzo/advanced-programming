package com.designframework.Rizzo;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MyJob<K,V> extends AJob {

    private String filename;

    public String getFilename() {
        return filename;
    }

    public MyJob(String filename) {
        this.filename = filename;
    }

    @Override
    Stream<Pair<K,V>> execute() {
        //accede al filename e si va a prendere le cose.
        try (Stream<String> lines = Files.lines(Paths.get("./parole/a.txt"), StandardCharsets.UTF_8)) {
            return lines.filter(x-> x.length()>4 && !x.matches("^.*[^a-zA-Z0-9 ].*$")).map(x-> new Pair(x.toLowerCase(),x.toLowerCase()));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
