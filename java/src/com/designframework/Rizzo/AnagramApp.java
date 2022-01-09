package com.designframework.Rizzo;

import com.designframework.Rizzo.framework.JobSchedulerContext;

public class AnagramApp {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Please insert the folder path as parameter:\n" +
                    "java AnagramApp folder_path");
            return;
        }
        String folder_path = args[0];
        AnagramStrategy strategy = new AnagramStrategy(folder_path);
        JobSchedulerContext context = new JobSchedulerContext(strategy);
        context.start();
    }
}
