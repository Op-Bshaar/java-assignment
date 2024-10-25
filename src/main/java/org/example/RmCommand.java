package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RmCommand implements Command {
    public String run(String[] args) {
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
    }

}
