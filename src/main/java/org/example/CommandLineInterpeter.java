package org.example;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
                String path =  MkdirCommand(commandData.parameters);
                System.out.println(path);
                break;
            case "rm":
                String output  =  RmCommand(commandData.parameters);
                System.out.println(output);
                break;
            case "ls":
                String out =  LsCommand(commandData.parameters);
                System.out.println(out);
                break;
            case "pwd":
            {
                String any = pwd();
                System.out.println(any);
                break;   
            }  
            case "cat":
            {
                String any = commandData.parameters.length > 0 ? commandData.parameters[0]:null;
                cat(any).forEach(System.out::println);
                break;
            }
            case "exit":
                ExitCommand(scanner);

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

    //bashar command

    public String MkdirCommand(String[] args){


            if (args.length < 1) {
                return "Usage: mkdir <directory_name>";
            }

            Path dirPath = Paths.get(args[0]);
            try {
                Files.createDirectories(dirPath);
                return "Directory created: " + dirPath.toAbsolutePath();
            } catch (IOException e) {
                return "Error creating directory: " + e.getMessage();
            }

    };

    public  String RmCommand(String[] args){

            if (args.length < 1) {
                return "Usage: rm <file_or_directory_name>";
            }
            Path rmPath = Paths.get(args[0]);

            try{
                if (Files.isDirectory(rmPath)) {
                    Files.deleteIfExists(rmPath);
                    return "Directory deleted: " + rmPath.toAbsolutePath();
                }else if (Files.isRegularFile(rmPath)) {
                    Files.deleteIfExists(rmPath);
                    return "File deleted: " + rmPath.toAbsolutePath();
                }
                else{
                    return "not found and file or directory";
                }

            }
            catch (IOException e) {
                return "Error deleting file or directory: " + e.getMessage();
            }

    };

    public String LsCommand(String[] args){

            if (args.length < 1) {
                return "Usage: ls <directory_name>";
            }
            Path currentDir = Paths.get(".");
            StringBuilder listingOutput = new StringBuilder("contents:\n");
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(currentDir)) {
                for (Path entry : stream) {
                    listingOutput.append(entry.getFileName()).append("\n");
                }
            } catch (IOException e) {
                return "Error listing contentes: " + e.getMessage();
            }
            return listingOutput.toString();
        }
    public void ExitCommand(Scanner scanner){
        System.out.println("Exiting..");
        scanner.close();
        System.exit(0);

    };
}
