import org.example.CommandLineInterpreter;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


public class RmTest {
    private CommandLineInterpreter cmd;

    @BeforeEach
    public void setUp() {
        cmd = new CommandLineInterpreter();
    }

    @Test
    public void testRemoveFile() throws IOException {
        Path filePath = Files.createTempFile("rmTestFile", ".txt");
        String output = cmd.RmCommand(new String[]{ filePath.toString()});
        output = output.trim();
        Assertions.assertFalse(Files.exists(filePath), "File should be deleted.");
        Assertions.assertEquals("File deleted: " + filePath.toAbsolutePath(), output);
    }

    @Test
    public void testRemoveDirectory() throws IOException {
        Path dirPath = Files.createTempDirectory("rmTestDir");

        String output = cmd.RmCommand(new String[]{ dirPath.toString()});
        output = output.trim();
        Assertions.assertFalse(Files.exists(dirPath), "Directory should be deleted.");
        Assertions.assertEquals("Directory deleted: " + dirPath.toAbsolutePath(), output);
    }

    @Test
    public void testRemoveNonexistentFile() {
        String output = cmd.RmCommand(new String[]{"nonexistent.txt"});
        output = output.trim();
        Assertions.assertEquals("not found and file or directory", output);
    }

    @Test
    public void testRemoveMultipleFilesAndDirectories() throws IOException {
        Path tempDir1 = Files.createTempDirectory("testDir1");
        Path tempDir2 = Files.createTempDirectory("testDir2");
        Path tempFile1 = Files.createTempFile("testFile1", ".txt");
        Path tempFile2 = Files.createTempFile("testFile2", ".txt");
        String output = cmd.RmCommand(new String[]{
                tempFile1.toString(),
                tempFile2.toString(),
                tempDir1.toString(),
                tempDir2.toString()
        });

        // Verify that the files and directories were deleted
        assertFalse(Files.exists(tempFile1), "File should be deleted: " + tempFile1);
        assertFalse(Files.exists(tempFile2), "File should be deleted: " + tempFile2);
        assertFalse(Files.exists(tempDir1), "Directory should be deleted: " + tempDir1);
        assertFalse(Files.exists(tempDir2), "Directory should be deleted: " + tempDir2);

        // Verify output messages
        String expectedOutput = String.join(System.lineSeparator(),
                "File deleted: " + tempFile1.toAbsolutePath(),
                "File deleted: " + tempFile2.toAbsolutePath(),
                "Directory deleted: " + tempDir1.toAbsolutePath(),
                "Directory deleted: " + tempDir2.toAbsolutePath()
        );
        assertEquals(expectedOutput, output.trim());
    }
}
