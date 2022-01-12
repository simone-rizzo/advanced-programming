package com.designframework.Rizzo;

import com.designframework.Rizzo.framework.AJob;
import com.designframework.Rizzo.framework.JobSchedulerStrategy;
import com.designframework.Rizzo.framework.Pair;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public class AnagramStrategy extends JobSchedulerStrategy<String,String> {

    private final String folder_path;
    public AnagramStrategy(String folder_path) {
        this.folder_path = folder_path;
    }

    public String getFolder_path() {
        return folder_path;
    }

    @Override
    protected Stream<AJob<String, String>> emit() {
        String fileExtension = ".txt";
        checkFolderPath();
        try {
            return Files.walk(Path.of(folder_path)).filter(Files::isRegularFile)
                    .filter(f -> f.toString().endsWith(fileExtension))
                    .map(p -> new AnagramJob(p.toString()));
        } catch (IOException e) {
            e.printStackTrace();
            return Stream.empty();
        }
    }

    /**
     * Method that check if the folder_path is a directory.
     * Otherwise throw a RuntimeException
     */
    private void checkFolderPath(){
        File folder = new File(folder_path);
        if (!folder.isDirectory()) {
            throw new RuntimeException("The given path is not a directory!");
        }
    }

    @Override
    protected void output(Stream<Pair<String, List<String>>>  collected_stream) {
        File fout = new File("count_anagrams.txt");
        try {
            FileOutputStream fos = new FileOutputStream(fout);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
            collected_stream.forEach(p -> {
                try {
                    bw.write((p.getKey()+","+(p.getValue()).size()));
                    bw.write("\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            bw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
