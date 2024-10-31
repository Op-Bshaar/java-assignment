import org.example.CommandLineInterpreter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


public class RmdirTest {
    private CommandLineInterpreter cmd;

    @BeforeEach
    public void setUp() {
        cmd = new CommandLineInterpreter();
    }

    @Test
    public void testRemoveEmptyDirectory() throws IOException {
        Path emptyDir = Files.createTempDirectory("emptyDirTest");
        String output = cmd.RmdirCommand(emptyDir.toString());

        // Verify the directory was deleted successfully
        assertEquals("Directory deleted: " + emptyDir.toAbsolutePath(), output);
        assertFalse(Files.exists(emptyDir), "Directory should be deleted.");
    }

    @Test
    public void testRemoveNonEmptyDirectory() throws IOException {
        Path nonEmptyDir = Files.createTempDirectory("nonEmptyDirTest");
        Files.createFile(nonEmptyDir.resolve("testFile.txt"));
        String output = cmd.RmdirCommand(nonEmptyDir.toString());
        assertEquals("Directory is not empty: " + nonEmptyDir.toAbsolutePath(), output);
        assertTrue( Files.exists(nonEmptyDir), "Non-empty directory should not be deleted.");
    }

    @Test
    public void testRemoveNonExistentDirectory() {
        Path nonExistentDir = Path.of("nonExistentDirTest");
        String output = cmd.RmdirCommand(nonExistentDir.toString());
        assertEquals("Not a directory: " + nonExistentDir.toAbsolutePath(), output);
    }
}
