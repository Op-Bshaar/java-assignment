import org.example.CommandLineInterpreter;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class mvTest {
    private CommandLineInterpreter cmd;
    private String initialDir;

    @BeforeEach
    public void setUp() {
        cmd = new CommandLineInterpreter();
        initialDir = System.getProperty("user.dir"); // Store the initial working directory
    }

    @Test
    public void testMoveFileToExistingDirectory() throws IOException {
        // Create a temporary file and directory
        Path currentFile = Files.createTempFile("sourceFile", ".txt");
        Path Newdirct = Files.createTempDirectory("targetDir");

        // Test the mv command
        String[] args = { currentFile.toString(), Newdirct.toString() };
        String result = cmd.MvCommand(args);

        // Assert the file was moved successfully
        Path movedFilePath = Newdirct.resolve(currentFile.getFileName());
        assertTrue(Files.exists(movedFilePath));
        assertEquals("move", result);

        // Clean up
        Files.deleteIfExists(movedFilePath);
        Files.deleteIfExists(Newdirct);
    }

    @Test
    public void testMoveFileToNonExistingDirectory() throws IOException {
        // Create a temporary file
        Path currentFile = Files.createTempFile("sourceFile", ".txt");
        Path Newdirct = Files.createTempDirectory("tempDir").resolve("nonExistingDir");

        // Test the mv command
        String[] args = { currentFile.toString(), Newdirct.toString() };
        String result = cmd.MvCommand(args);

        // Assert the new directory was created and the file was moved successfully
        Path movedFilePath = Newdirct.resolve(currentFile.getFileName());
        assertTrue(Files.exists(movedFilePath));
        assertEquals("move", result);

        // Clean up
        Files.deleteIfExists(movedFilePath);
        Files.deleteIfExists(Newdirct.getParent());
    }

    @Test
    public void testMoveNonExistentFile() {
        // Define non-existent source and target paths
        Path currentFile = Path.of("nonExistentFile.txt");
        Path Newdirct = Path.of("nonExistentDir");

        // Test the mv command
        String[] args = { currentFile.toString(), Newdirct.toString() };
        String result = cmd.MvCommand(args);

        // Assert error message for non-existent file
        assertEquals("no file or dircotry", result);
    }

    @AfterEach
    public void tearDown() {
        System.setProperty("user.dir", initialDir); // Restore the initial working directory if changed
    }
}
