import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Paths;

import org.example.CommandLineInterpreter;

public class CommandLineInterpreterTest {
    
    private CommandLineInterpreter cmd;

    @BeforeEach
    public void setUp() {
        cmd = new CommandLineInterpreter();
    }

    @Test
    public void testPwd(){
        String output = cmd.pwd();
        String expectedOutput = System.getProperty("user.dir");
        assertEquals(output, expectedOutput);
    }
}