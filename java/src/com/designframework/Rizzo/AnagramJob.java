package com.designframework.Rizzo;
import com.designframework.Rizzo.framework.AJob;
import com.designframework.Rizzo.framework.Pair;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;

public class AnagramJob extends AJob<String,String> {

    private String filename;
    private final static int MAXIMUM_WORD_LENGHT = 4;

    public String getFilename() {
        return filename;
    }

    public AnagramJob(String filename) {
        this.filename = filename;
    }

    @Override
    public Stream<Pair<String,String>> execute() {
        // We remove all the withe spaces, then
        // we filter out the words less than MAXIMUM_WORD_LENGHT
        // and those containing non-alphabetic characters
        try {
            return Files.lines(Paths.get(filename), StandardCharsets.UTF_8)
                    .flatMap(f -> Arrays.stream(f.split("\\s").clone()))
                    .filter(x -> x.length() > MAXIMUM_WORD_LENGHT && !x.matches("^.*[^a-zA-Z0-9 ].*$"))
                    .map(x -> new Pair(ciaoHash(x), x.toLowerCase()));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Function for computing the CIAO "Characters In Alphabetic Order" string.
     * @param word
     * @return
     */
    private String ciaoHash(String word){
        char charArray[] = word.toLowerCase().toCharArray();
        Arrays.sort(charArray);
        return new String(charArray).replaceAll("\\s","");
    }
}
