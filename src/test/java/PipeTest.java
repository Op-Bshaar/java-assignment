import org.example.CommandLineInterpreter;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PipeTest {
    private CommandLineInterpreter cmd;

    @BeforeEach
    public void setUp() {
        cmd = new CommandLineInterpreter();
    }

    @Test
    public void testPipeTwoCommands() {
        // Test piping output from one command to another
        String command = "echo a | echo b";
        cmd.executeCommand(command);
    }

}
