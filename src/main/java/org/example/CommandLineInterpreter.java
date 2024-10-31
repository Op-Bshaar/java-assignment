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
import java.util.stream.Stream;

public class CommandLineInterpreter {

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
            if (commandData.getRedirectFile() != null) {
                // Open a FileOutputStream in append or overwrite mode based on the `append`
                // flag
                FileOutputStream fos = new FileOutputStream(commandData.getRedirectFile(), commandData.isAppend());
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

        switch (commandData.getCommand()) {
            case "mkdir":
                String path = MkdirCommand(commandData.getFirstParameter());
                printStream.println(path);
                break;
            case "rm":
                String output = RmCommand(commandData.getParameters());
                printStream.println(output);
                break;
            case "help":
                String result = HelpCommand();
                printStream.println(result);

            case "ls":
                printStream.println(LsCommand(commandData.getParameters()));
                break;
            case "cd":
                String help = CdCommand(commandData.getFirstParameter());
                printStream.println(help);
                break;
            case "rmdir":
                String any = RmdirCommand(commandData.getFirstParameter());
                printStream.println(any);
                break;
            case "pwd": {
                printStream.println(pwd());
                break;
            }
            case "cat": {
                try (Stream<String> lines = cat(commandData.getFirstParameter())) {
                    lines.forEach(printStream::println);
                } catch (Exception e) {
                    printStream.println("Error processing file: " + e.getMessage());
                }
                break;
            }
            case "echo":
                String message = commandData.getParameters().length > 0
                        ? String.join(" ", commandData.getParameters())
                        : "Usage: echo <message>";
                printStream.println(message);
                break;
            case "touch":
                result = TouchCommand(commandData.getParameters());
                printStream.println(result);
                break;
            case "mv":
                result = MvCommand(commandData.getParameters());
                printStream.println(result);
                break;
            default:
                printStream.println("Command " + commandData.getCommand() + " not found.");
        }
        return  !commandData.getCommand().equalsIgnoreCase("exit");
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

    public String MkdirCommand(String path) {

        if (path == null) {
            return "Usage: mkdir <directory_name>";
        }

        Path dirPath = Paths.get(path).isAbsolute() ? Paths.get(path).normalize() : Paths.get(pwd(), path).normalize();
        try {
            Files.createDirectories(dirPath);
            return "Directory created: " + dirPath.toAbsolutePath();
        } catch (IOException e) {
            return "Error creating directory: " + e.getMessage();
        }

    };

    final String helpMessage = String.join(System.lineSeparator(),
            "mv [source dir] [target]",
            "mkdir [directory name]",
            "rmdir [directory name]",
            "ls [options] [file/directory]",
            "rm [filename]") + System.lineSeparator();

    public String HelpCommand() {
        return helpMessage;
    }

    public String RmCommand(String file) {
        Path rmPath = Paths.get(file).isAbsolute() ? Paths.get(file).normalize() : Paths.get(pwd(), file).normalize();
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

    }

    public String RmCommand(String[] files) {

        if (files.length < 1) {
            return "Usage: rm <file_or_directory_name>";
        }

        StringBuilder result = new StringBuilder();
        for (String file : files) {
            String s = RmCommand(file);
            result.append(s);
            result.append(System.lineSeparator());
        }
        return result.toString();
    }

    public String LsCommand(String[] args) {
        boolean reverse = false;
        boolean all = false;
        for (String param : args) {
            switch (param) {
                case "-r":
                    reverse = true;
                    break;
                case "-a":
                    all = true;
                    break;
                default:
                    break;
            }
        }

        return LsCommand(all, reverse);
    }

    public String LsCommand(boolean all, boolean reverse) {
        Path currentDir = Paths.get(pwd());
        final String newLine = System.lineSeparator();
        StringBuilder listingOutput = new StringBuilder(newLine);
        listingOutput.append("Directory: ").append(currentDir.toAbsolutePath())
                .append(newLine).append(newLine);
        listingOutput.append(String.format("%-5s %-20s %10s %s\n", "Mode", "LastWriteTime", "Length", "Name"));
        listingOutput.append("------------------------------------------------------------").append(newLine);

        // Directory Stream to Iterate Through Files

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(currentDir)) {
            List<Path> itemPaths = new ArrayList<>();
            for (Path entry : stream) {
                itemPaths.add(entry);
            }
            if (reverse) {
                Collections.reverse(itemPaths);
            }
            for (Path entry : itemPaths) {
                // Loop Through Files and Get File Attributes
                if (Files.isHidden(entry) && !all) {
                    continue;
                }
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

    public String TouchCommand(String[] args) {
        if (args.length < 1) {
            return "usage: Touch <FileName>";
        }
        Path dir = Paths.get(args[0]);
        if (Files.exists(dir)) {
            try {
                Files.setLastModifiedTime(dir, FileTime.from(Instant.now()));
                return "touched" + args[0];
            } catch (IOException e) {
                return "Error editing date: " + e.getMessage();
            }
        }
        File newfile = new File(args[0]);
        try {
            newfile.createNewFile();
            return "created file:" + args[0];
        } catch (IOException e) {
            return "Error creating new file:" + e.getMessage();
        }

    }

    public String RmdirCommand(String dir) {
        Path dirPath = Paths.get(dir).isAbsolute() ? Paths.get(dir).normalize() : Paths.get(pwd(), dir).normalize();
        try {
            if (Files.isDirectory(dirPath)) {
                // Check if the directory is empty
                try (DirectoryStream<Path> entries = Files.newDirectoryStream(dirPath)) {
                    if (entries.iterator().hasNext()) {
                        return "Directory is not empty: " + dirPath.toAbsolutePath();
                    }
                }
                // Directory is empty, proceed to delete
                Files.delete(dirPath);
                return "Directory deleted: " + dirPath.toAbsolutePath();
            } else {
                return "Not a directory: " + dirPath.toAbsolutePath();
            }
        } catch (IOException e) {
            return "Error deleting directory: " + e.getMessage();
        }
    }

    /*
     * public String RmdirCommand(String[] args) {
     * if (args.length < 1) {
     * return "Usage: rmdir <directory_name>";
     * }
     * 
     * Path dirPath = Paths.get(pwd(), args[0]).normalize();
     * try {
     * Files.walk(dirPath)
     * .forEach(path -> {
     * try {
     * Files.delete(path);
     * } catch (IOException e) {
     * throw new RuntimeException("Error deleting " + path + ": " + e.getMessage());
     * }
     * });
     * 
     * return "Directory deleted: " + dirPath.toAbsolutePath();
     * } catch (IOException e) {
     * return "Error deleting directory: " + e.getMessage();
     * }
     * }
     */
    public String CdCommand(String path) {
        if (path == null) {
            return "Usage: cd <directory_name>";
        }
        Path targetPath;
        if ("..".equals(path)) {
            targetPath = Paths.get(pwd()).getParent();
            if (targetPath == null) { // already at the root
                return "Already at the root directory";
            }
        } else {
            // If a specific directory is provided, use it as the target path
            targetPath = Paths.get(path);
        }

        // Resolve relative paths correctly (e.g., ".." to go up a directory)
        try {
            Path newDir = targetPath.toAbsolutePath().normalize();

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

    public String MvCommand(String[] args) {

        if(args.length<2){
            return "Usage: mv <source><destentaion>";
        }
        Path currntpath = Paths.get(args[0]);
        Path Newdirct = Paths.get(args[1]);
        if(!currntpath.isAbsolute()){
            currntpath=Paths.get(pwd(), args[0]).normalize();
        }
        if(!Newdirct.isAbsolute()){
            Newdirct=Paths.get(pwd(), args[1]).normalize();
        }
        try{
            if(!Files.exists(currntpath)){
                return "no file or dircotry";
            }
            if(Files.isDirectory(Newdirct)){
                Newdirct = Newdirct.resolve(currntpath.getFileName());
            }
            Files.move(currntpath,Newdirct);
            return"move";

        }
        catch(IOException e){
            return"error"+e.getMessage();

        }

    }
}
