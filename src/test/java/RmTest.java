import org.example.CommandLineInterpreter;
import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class RmTest {
    private CommandLineInterpreter cmd;

    @BeforeEach
    public void setUp() {
        cmd = new CommandLineInterpreter();
    }

    @Test
    public void TestRm(){
        String result = cmd.RmCommand(new String[] {"bashar"});
        assertEquals("Directory deleted: " + Paths.get("bashar").toAbsolutePath(), result);
    }
}
