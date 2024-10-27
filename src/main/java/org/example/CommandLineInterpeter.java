package org.example;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
public class CommandLineInterpeter {
    
    private class CommandData {
        private String command;
        private String[] parameters;
        private CommandData(String commandInput) {
        Pattern pattern = Pattern.compile("\"([^\"]*)\"|\\S+");
        Matcher matcher = pattern.matcher(commandInput);
        ArrayList<String> partsList = new ArrayList<>();
        while (matcher.find()) {
            if (matcher.group(1) != null) {
                partsList.add(matcher.group(1));
            } else {
                partsList.add(matcher.group());
            }
        }
        this.command = partsList.get(0);
        this.parameters = partsList.size() > 1 ? partsList.subList(1, partsList.size()).toArray(new String[0]) : new String[0];
    }
    
    }
    public void runCommandLine(){
        
        Scanner scanner = new Scanner(System.in);
        System.out.print(pwd() + "> ");
        while (true) {
            String commandIn = scanner.nextLine().trim();
            CommandData commandData = new CommandData(commandIn);
            executeCommand(commandData,scanner);
        }
    }
    public void executeCommand(CommandData commandData, Scanner scanner){
        Command command;
        switch (commandData.command) {
            case "mkdir":
                command = new MkdirCommand();
                System.out.println(command.run(commandData.parameters));
                break;
            case "rm":
                command = new RmCommand();
                System.out.println(command.run(commandData.parameters));
                break;
            case "ls":
                command = new LsCommand();
                System.out.println(command.run(commandData.parameters));
                break;
            case "pwd":
            {
                String path = pwd();
                System.out.println(path);
                break;   
            }  
            case "cat":
            {
                String path = commandData.parameters.length > 0 ? commandData.parameters[0]:null;
                cat(path).forEach(System.out::println);
                break;
            }
            case "exit":
                System.out.println("Exiting..");
                scanner.close();
                return;
            default:
                System.out.println("Command " + commandData.command + " not found.");
        }
        System.out.print(pwd() + "> ");
    }
    public String pwd(){
        return System.getProperty("user.dir");
    }
    public Stream<String> cat(String path) {
        if (path == null) {
            return Stream.of("Usage: cat <file_path>");
        }
    
        Path filePath = Path.of(path);
        if (Files.isDirectory(filePath)) {
            return Stream.of("cat: " + path + " is a directory, not a file!");
        }

        else if (!Files.exists(filePath)) {
            return Stream.of("cat: " + path + " does not exist!");
        }
        
        try {
            return Files.lines(filePath);
        } catch (IOException e) {
            return Stream.of("cat: " + e.getMessage());
        }
    }
}
