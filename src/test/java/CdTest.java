import org.example.CommandLineInterpreter;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CdTest {
    private CommandLineInterpreter cmd;
    private String initialDir;

    @BeforeEach
    public void setUp() {
        cmd = new CommandLineInterpreter();
        initialDir = System.getProperty("user.dir"); // Store the initial working directory
    }

    @AfterEach
    public void tearDown() {
        // Restore the initial working directory after each test
        System.setProperty("user.dir", initialDir);
    }

    @Test
    public void testChangeToValidDirectory() throws IOException {
        Path tempDir = Files.createTempDirectory("cdTestDir");
        String output = cmd.CdCommand(tempDir.toString());
        Assertions.assertEquals("Changed directory to: " + tempDir.toAbsolutePath(), output);
        Assertions.assertEquals(tempDir.toAbsolutePath().toString(), System.getProperty("user.dir"));
        Files.delete(tempDir);
    }

    @Test
    public void testChangeToParentDirectory() throws IOException {
        Path tempDir = Files.createTempDirectory("cdTestDir");
        System.setProperty("user.dir", tempDir.toString());
        String output = cmd.CdCommand("..");
        Assertions.assertTrue(output.contains("Changed directory to:"), output);
        Assertions.assertEquals(tempDir.getParent().toAbsolutePath().toString(), System.getProperty("user.dir"));
        Files.delete(tempDir);
    }

    @Test
    public void testChangeToAbsolutePath() throws IOException {
        Path tempDir = Files.createTempDirectory("cdTestDir");
        String output = cmd.CdCommand(tempDir.toAbsolutePath().toString());
        Assertions.assertEquals("Changed directory to: " + tempDir.toAbsolutePath(), output);
        Assertions.assertEquals(tempDir.toAbsolutePath().toString(), System.getProperty("user.dir"));
        Files.delete(tempDir);
    }

    @Test
    public void testChangeToNonexistentDirectory() {
        String output = cmd.CdCommand("nonexistentDir");
        Assertions.assertEquals("Directory not found: " + Paths.get("nonexistentDir").toAbsolutePath(), output);
        Assertions.assertEquals(initialDir, System.getProperty("user.dir"));
    }
}
