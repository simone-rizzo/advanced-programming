package com.Rizzo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestAlgs {

    public static void main(String[] args) {
        String path = "";
        if(args.length>0){
            path = args[0];
            try {
                List<String[]> keys_list = Files.lines(Paths.get(path+"/keys.list")).map(x-> x.split(" ")).collect(Collectors.toList());
                List<String> secret_list = Files.lines(Paths.get(path+"/secret.list")).collect(Collectors.toList());

            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(path);
        }
    }
}
