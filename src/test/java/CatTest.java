import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.example.CommandLineInterpreter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CatTest {
    
    private CommandLineInterpreter cmd;

    @BeforeEach
    public void setUp() {
        cmd = new CommandLineInterpreter();
    }

     @Test
    public void testCatWithExistingFile() throws IOException{
        Path testFile = Files.createTempFile("testFile", ".txt");
        Files.writeString(testFile, "Hello, World!\nThis is a test file.");

        try (Stream<String> output = cmd.cat(testFile.toString())) {
            List<String> lines = output.collect(Collectors.toList());
            assertEquals(List.of("Hello, World!", "This is a test file."), lines);
        } finally {
            Files.deleteIfExists(testFile);
        }
    }

    @Test
    public void testCatWithNonExistentFile() {
        String nonExistentPath = "nonExistentFile.txt";        
        try (Stream<String> output = cmd.cat(nonExistentPath)) {
            List<String> lines = output.collect(Collectors.toList());
            assertEquals(List.of("cat: " + nonExistentPath + " does not exist!"), lines);
        }
    }

    @Test
    public void testCatWithDirectoryPath() throws IOException {
        Path tempDir = Files.createTempDirectory("testDir");

        try (Stream<String> output = cmd.cat(tempDir.toString())) {
            List<String> lines = output.collect(Collectors.toList());
            assertEquals(List.of("cat: " + tempDir + " is a directory, not a file!"), lines);
        } finally {
            Files.deleteIfExists(tempDir);
        }
    }
}
