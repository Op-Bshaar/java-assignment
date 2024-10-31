import org.example.CommandLineInterpreter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PipeTest {
    private CommandLineInterpreter cmd;
    private ByteArrayOutputStream outputStream;
    private PrintStream printStream;

    @BeforeEach
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        printStream = new PrintStream(outputStream);
        cmd = new CommandLineInterpreter(printStream);
    }

    @AfterEach
    public void cleanUp() throws IOException {
        outputStream.close();
        printStream.close();
    }

    @Test
    public void testPipeTwoCommands() {
        cmd.executeCommand("pwd | echo");
        String expected = cmd.pwd();
        String output = outputStream.toString();
        assertEquals(expected.trim(), output.trim());
    }

    @Test
    public void testPipeExit() {
        cmd.executeCommand("ls | exit | pwd");
        String output = outputStream.toString();
        assertEquals("", output.trim());
    }

    @Test
    public void testPipeMkdir() throws IOException{
        final Path filePath = Files.createTempFile("testFile", ".txt");
        Files.writeString(filePath, "testdir");
        cmd.executeCommand("cat "+ filePath +" | mkdir");
        final Path dirPath = Paths.get(cmd.pwd(), "testdir");
        assertTrue(Files.exists(dirPath));
        Files.deleteIfExists(filePath);
        Files.deleteIfExists(dirPath);
    }
}
