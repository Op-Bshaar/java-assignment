package org.example;

import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class CommandLineInterpreter {

    private class CommandData {
        private String command;
        private String[] parameters;
        private String redirectFile = null;
        private boolean append = false;

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

            for (int i = partsList.size() - 1; i >= 0; i--) {
                String part = partsList.get(i);
                if (part.equals(">") || part.equals(">>")) {
                    if (i + 1 < partsList.size()) {  // Ensure there is a filename after `>` or `>>`
                        this.redirectFile = partsList.get(i + 1);
                        this.append = part.equals(">>");
                        partsList = new ArrayList<>(partsList.subList(0, i));
                    } else {
                        System.out.println("Usage <command> " + part +" <file path>");
                    }
                    break;
                }
            }

            this.command = partsList.get(0);
            this.parameters = partsList.size() > 1 ? partsList.subList(1, partsList.size()).toArray(new String[0])
                    : new String[0];
        }

    }

    public void runCommandLine() {

        Scanner scanner = new Scanner(System.in);
        boolean run = true;
        while (run) {
            System.out.print(pwd() + "> ");
            String command = scanner.nextLine().trim();
            run = executeCommand(command);
        }
        System.out.println("Exiting..");
        scanner.close();
    }

    public Boolean executeCommand(String command) {
        Boolean run = false;
        CommandData commandData = new CommandData(command);
        PrintStream printStream = System.out; // Default to standard output
        try {
            // Check if output redirection is needed
            if (commandData.redirectFile != null) {
                // Open a FileOutputStream in append or overwrite mode based on the `append`
                // flag
                FileOutputStream fos = new FileOutputStream(commandData.redirectFile, commandData.append);
                printStream = new PrintStream(fos);
            }

            // Execute the command, redirecting output as necessary
            run = executeCommand(commandData, printStream);
        } catch (IOException e) {
            System.out.println("Error with redirection: " + e.getMessage());
        } finally {
            // Close the PrintStream if it’s not System.out
            if (printStream != System.out) {
                printStream.close();
            }
        }
        return run;
    }

    private Boolean executeCommand(CommandData commandData, PrintStream printStream) {

        switch (commandData.command) {
            case "mkdir":
                String path = MkdirCommand(commandData.parameters);
                printStream.println(path);
                break;
            case "rm":
                String output = RmCommand(commandData.parameters);
                printStream.println(output);
                break;
            case "ls":
            Integer flag= 0;
            for(String param: commandData.parameters)
            {
                if(param.equals("-r"))
                {
                    flag=1;
                }
                else if(param.equals("-a"))
                {
                    flag=2;
                }
                
            }
            String result;
            switch(flag)
            {
                
                case 0:
                    result = LsCommand(commandData.parameters);
                    printStream.println(result);
                break;
                case 1:
                    result = LsRCommand(commandData.parameters);
                    printStream.println(result);
                break;
                case 2:
                    result = LsACommand(commandData.parameters);
                    printStream.println(result);
                break;
            }

            break;
            case "cd":
                String help = CdCommand(commandData.parameters);
                printStream.println(help);
                break;
            case "rmdir":
                String any = RmdirCommand(commandData.parameters);
                printStream.println(any);
                break;
            case "pwd": {
                printStream.println(pwd());
                break;
            }
            case "cat": {
                String filePath = commandData.parameters.length > 0 ? commandData.parameters[0] : null;
                try (Stream<String> lines = cat(filePath)) {
                    lines.forEach(printStream::println);
                } catch (Exception e) {
                    printStream.println("Error processing file: " + e.getMessage());
                }
                break;
            }
            case "echo":
                String message = commandData.parameters.length > 0
                        ? String.join(" ", commandData.parameters)
                        : "Usage: echo <message>";
                printStream.println(message);
                break;
            case "touch":
            result = TouchCommand(commandData.parameters);
            printStream.println(result);
                break;
            case "exit":
                return false;
            default:
                printStream.println("Command " + commandData.command + " not found.");
        }
        return true;
    }

    public String pwd() {
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

    // bashar command

    public String MkdirCommand(String[] args) {

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

    public String RmCommand(String[] args) {

        if (args.length < 1) {
            return "Usage: rm <file_or_directory_name>";
        }
        Path rmPath = Paths.get(args[0]);

        try {
            if (Files.isDirectory(rmPath)) {
                Files.deleteIfExists(rmPath);
                return "Directory deleted: " + rmPath.toAbsolutePath();
            } else if (Files.isRegularFile(rmPath)) {
                Files.deleteIfExists(rmPath);
                return "File deleted: " + rmPath.toAbsolutePath();
            } else {
                return "not found and file or directory";
            }

        } catch (IOException e) {
            return "Error deleting file or directory: " + e.getMessage();
        }

    };

    public String LsCommand(String[] args) {
        Path currentDir = Paths.get(".");
        StringBuilder listingOutput = new StringBuilder("\n");
        listingOutput.append("Directory: ").append(currentDir.toString()).append("\n\n");
        listingOutput.append(String.format("%-5s %-20s %10s %s\n", "Mode", "LastWriteTime", "Length", "Name"));
        listingOutput.append("------------------------------------------------------------\n");

        // Directory Stream to Iterate Through Files

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(currentDir)) {

            for (Path entry : stream) {
                // Loop Through Files and Get File Attributes
                if(Files.isHidden(entry)){continue;}
                BasicFileAttributes attrs = Files.readAttributes(entry, BasicFileAttributes.class);
                
                String type = attrs.isDirectory() ? "d----" : "-a---";

                String lastModifiedTime = new SimpleDateFormat("MM/dd/yyyy hh:mm a")
                        .format(new Date(attrs.lastModifiedTime().toMillis()));

                long size = attrs.size();
                String fileName = entry.getFileName().toString();

                // Formatting the output
                listingOutput.append(String.format("%-5s %-20s %10d %s\n", type, lastModifiedTime, size, fileName));
            }
        } catch (IOException e) {
            return "Error listing contents: " + e.getMessage();
        }

        return listingOutput.toString();
    }


    public String LsACommand(String[] args) {
        Path currentDir = Paths.get(".");
        StringBuilder listingOutput = new StringBuilder("\n");
        listingOutput.append("Directory").append(currentDir.toString()).append("\n\n");
        listingOutput.append(String.format("%-5s %-20s %10s %s\n", "Mode", "LastWriteTime", "Length", "Name"));
        listingOutput.append("------------------------------------------------------------\n");

        try(DirectoryStream<Path> stream =Files.newDirectoryStream(currentDir))
        {
            for (Path entry : stream) {
                // Loop Through Files and Get File Attributes

                BasicFileAttributes attrs = Files.readAttributes(entry, BasicFileAttributes.class);

                String type = attrs.isDirectory() ? "d----" : "-a---";

                String lastModifiedTime = new SimpleDateFormat("MM/dd/yyyy hh:mm a")
                        .format(new Date(attrs.lastModifiedTime().toMillis()));
                long size = attrs.size();
                String fileName = entry.getFileName().toString();
                // Formatting the output
                listingOutput.append(String.format("%-5s %-20s %10d %s\n", type, lastModifiedTime, size, fileName));
            }
        } catch (IOException e) {
            return "Error listing contents: " + e.getMessage();
        }
        return listingOutput.toString();
        }
        
        public String LsRCommand(String[] args) {
            List<Path> rev= new ArrayList<>();
            Path currentDir = Paths.get(".");
            StringBuilder listingOutput = new StringBuilder("\n");
            listingOutput.append("Directory").append(currentDir.toString()).append("\n\n");
            listingOutput.append(String.format("%-5s %-20s %10s %s\n", "Mode", "LastWriteTime", "Length", "Name"));
            listingOutput.append("------------------------------------------------------------\n");
    
            try(DirectoryStream<Path> stream =Files.newDirectoryStream(currentDir))
            {
                for(Path entry:stream)
                {
                    rev.add(entry);
                }
                Collections.reverse(rev);
                for (Path entry : rev) {
                    // Loop Through Files and Get File Attributes
    
                    BasicFileAttributes attrs = Files.readAttributes(entry, BasicFileAttributes.class);
    
                    String type = attrs.isDirectory() ? "d----" : "-a---";
    
                    String lastModifiedTime = new SimpleDateFormat("MM/dd/yyyy hh:mm a")
                            .format(new Date(attrs.lastModifiedTime().toMillis()));
    
                    long size = attrs.size();
                    String fileName = entry.getFileName().toString();
    
                    // Formatting the output
                    listingOutput.append(String.format("%-5s %-20s %10d %s\n", type, lastModifiedTime, size, fileName));
                }
            } catch (IOException e) {
                return "Error listing contents: " + e.getMessage();
            }
            return listingOutput.toString();
            }
    public String TouchCommand(String[] args)
    {
        if(args.length<1)
        {
            return "usage: Touch <FileName>";
        }
        StringBuilder path= new StringBuilder();
        Path dir = Paths.get(args[0]);
        if(Files.exists(dir))
        {
            try{
            Files.setLastModifiedTime(dir, FileTime.from(Instant.now()));
            return "touched" + args[0];
            }
            catch(IOException e)
            {
                return "Error editing date: " + e.getMessage();
            }
        }
        File newfile= new File(args[0]);
        try{
        newfile.createNewFile();
        return "created file:" + args[0];
        }
        catch(IOException e)
        {
            return "Error creating new file:" + e.getMessage();
        }
        
        
    }
    
    public String RmdirCommand(String[] args) {
        if (args.length < 1) {
            return "Usage: rmdir <directory_name>";
        }

        Path dirPath = Paths.get(args[0]);
        try {
            Files.walk(dirPath)
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                        } catch (IOException e) {
                            throw new RuntimeException("Error deleting " + path + ": " + e.getMessage());
                        }
                    });

            return "Directory deleted: " + dirPath.toAbsolutePath();
        } catch (IOException e) {
            return "Error deleting directory: " + e.getMessage();
        }
    }

    public String CdCommand(String[] args) {
        if (args.length < 1) {
            return "Usage: cd <directory_name>";
        }
        Path targetPath;
        if ("..".equals(args[0])) {
            targetPath = Paths.get(System.getProperty("user.dir")).getParent();
            if (targetPath == null) { // already at the root
                return "Already at the root directory";
            }
        } else {
            // If a specific directory is provided, use it as the target path
            targetPath = Paths.get(args[0]);
        }

        // Resolve relative paths correctly (e.g., ".." to go up a directory)
        try {
            Path newDir = targetPath.isAbsolute() ? targetPath
                    : Paths.get(System.getProperty("user.dir")).resolve(targetPath).normalize();

            // Check if the directory exists and is a directory
            if (Files.exists(newDir) && Files.isDirectory(newDir)) {
                // Change the current working directory
                System.setProperty("user.dir", newDir.toString());
                return "Changed directory to: " + newDir.toAbsolutePath();
            } else {
                return "Directory not found: " + newDir.toAbsolutePath();
            }
        } catch (Exception e) {
            return "Error changing directory: " + e.getMessage();
        }
    }
}
