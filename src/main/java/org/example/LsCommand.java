package org.example;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class LsCommand implements Command {
    public String run(String [ ]args) {
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
}

