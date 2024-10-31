import org.example.CommandLineInterpreter;
import org.junit.jupiter.api.*;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class MkdirTest {
    private CommandLineInterpreter cmd;

    @BeforeEach
    public void setUp() {
        cmd = new CommandLineInterpreter();
    }

    @Test
    public void testmkdir(){
        String result = cmd.MkdirCommand(new String[] {"bashar"});
        assertEquals("Directory created: " + Paths.get("bashar").toAbsolutePath(), result);
    }
}
