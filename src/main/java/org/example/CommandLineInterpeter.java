package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Stream;

public class CommandLineInterpeter {
    public void runCommandLine(){
        
        Scanner scanner = new Scanner(System.in);
        System.out.print(pwd() + "> ");

        while (true) {
            String commandIn = scanner.nextLine().trim();
            String[] parts = commandIn.split(" ");
            String cmd = parts[0];
            String[] argsArray = parts.length > 1 ? Arrays.copyOfRange(parts, 1, parts.length) : new String[0];
            Command command;

            switch (cmd) {
                case "mkdir":
                    command = new MkdirCommand();
                    System.out.println(command.run(argsArray));
                    break;
                case "rm":
                    command = new RmCommand();
                    System.out.println(command.run(argsArray));
                    break;
                case "ls":
                    command = new LsCommand();
                    System.out.println(command.run(argsArray));
                    break;
                case "pwd":
                {
                    String path = pwd();
                    System.out.println(path);
                    break;   
                }  
                case "cat":
                {
                    String path = argsArray.length > 0 ? argsArray[0]:null;
                    cat(path);
                    break;
                }
                case "exit":
                    System.out.println("Exiting..");
                    scanner.close();
                    return;
                default:
                    System.out.println("Command " + cmd + " not found.");
            }
            System.out.print(pwd() + "> ");
        }
    }
    public String pwd(){
        return System.getProperty("user.dir");
    }
    public void cat(String path){
        if (path == null) {
            System.out.println( "Usage: cat <file_path>");
            return;
        }
        Path filePath = Path.of(path);
        try  (Stream<String> lines = Files.lines(filePath)) {
            lines.forEach(System.out::println);
        } catch (IOException e) {
            System.out.println("cat: " + e.getMessage());
        }
    }
}
