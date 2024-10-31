import org.example.CommandLineInterpreter;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MkdirTest {
    private CommandLineInterpreter cmd;

    @BeforeEach
    public void setUp() {
        cmd = new CommandLineInterpreter();
    }

    @Test
    public void testMkdir() {
        String dirName = "mkdirTest";
        Path dirPath = Paths.get(dirName);

        try {
            // Execute the mkdir command
            String result = cmd.MkdirCommand(dirName);

            // Verify the output
            assertEquals("Directory created: " + dirPath.toAbsolutePath(), result);

            // Verify that the directory was actually created
            assertTrue(Files.exists(dirPath) && Files.isDirectory(dirPath), "Directory should exist");

        } finally {
            // Clean up: Delete the created directory if it exists
            try {
                Files.deleteIfExists(dirPath);
            } catch (IOException e) {
                System.err.println("Cleanup failed: Could not delete directory " + dirName);
            }
        }
    }
}
