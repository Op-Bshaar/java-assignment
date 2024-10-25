
package org.example;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MkdirCommand implements Command {
  @Override
    public String run(String [ ]args) {
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
  }

}
