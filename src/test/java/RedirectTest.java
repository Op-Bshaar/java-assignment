import org.example.CommandLineInterpreter;
import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class RedirectTest {
    private CommandLineInterpreter cmd;

    @BeforeEach
    public void setUp() {
        cmd = new CommandLineInterpreter();
    }

    @Test
    public void testOutputRedirectionToFile() throws IOException {
        String command = "echo Hello, World! > testOutput.txt";
        executeAndVerifyOutput(command, "testOutput.txt", "Hello, World!" + System.lineSeparator());
    }

    @Test
    public void testAppendRedirectionToFile() throws IOException {
        String initialCommand = "echo First line > testAppend.txt";
        String appendCommand = "echo Second line >> testAppend.txt";

        executeAndVerifyOutput(initialCommand, "testAppend.txt", "First line" + System.lineSeparator());
        executeAndVerifyOutput(appendCommand, "testAppend.txt", "First line"+ System.lineSeparator() + "Second line"+ System.lineSeparator());
    }

    @AfterEach
    public void tearDown() throws IOException {
        Files.deleteIfExists(Path.of("testOutput.txt"));
        Files.deleteIfExists(Path.of("testAppend.txt"));
    }

    private void executeAndVerifyOutput(String command, String fileName, String expectedContent) throws IOException {

        // Run the command
        cmd.executeCommand(command);
        // Verify file content
        String content = Files.readString(Path.of(fileName));
        Assertions.assertEquals(expectedContent.trim(), content.trim());
    }
}
