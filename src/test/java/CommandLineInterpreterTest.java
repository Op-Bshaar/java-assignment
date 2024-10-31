import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.example.CommandLineInterpreter;
import org.example.CommandData; // Assuming CommandData is in the same package

public class CommandLineInterpreterTest {

    private CommandLineInterpreter cmd;

    @BeforeEach
    public void setUp() {
        cmd = new CommandLineInterpreter();
    }

    @Test
    public void testPwd() {
        String output = cmd.pwd();
        String expectedOutput = System.getProperty("user.dir");
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testBasicCommand() {
        String commandInput = "ls";
        CommandData commandData = new CommandData(commandInput);

        assertEquals("ls", commandData.getCommand());
        assertArrayEquals(new String[] {}, commandData.getParameters());
    }

    @Test
    public void testCommandWithParameters() {
        String commandInput = "cd tempDir";
        CommandData commandData = new CommandData(commandInput);

        assertEquals("cd", commandData.getCommand());
        assertArrayEquals(new String[] { "tempDir" }, commandData.getParameters());
    }

    @Test
    public void testQuotedPath() {
        String commandInput = "cd \"C:\\Program Files\"";
        CommandData commandData = new CommandData(commandInput);

        assertEquals("cd", commandData.getCommand());
        assertArrayEquals(new String[] { "C:\\Program Files" }, commandData.getParameters());
    }

    @Test
    public void testManyParameters() {
        String commandInput = "echo \"av ac\" ex ref \" ds\"";
        CommandData commandData = new CommandData(commandInput);

        assertEquals("echo", commandData.getCommand());
        assertArrayEquals(new String[] { "av ac", "ex", "ref", " ds" }, commandData.getParameters());
    }

    @Test
    public void testRedirectFile() {
        CommandData commandData = new CommandData("command p1 p2 > output.txt");
        assertEquals("output.txt", commandData.getRedirectFile());
        assertEquals(false, commandData.isAppend());
    }

    @Test
    public void testRedirectFileAppend() {
        CommandData commandData = new CommandData("command p1 p2 >> output.txt");
        assertEquals("output.txt", commandData.getRedirectFile());
        assertEquals(true, commandData.isAppend());
    }
}
