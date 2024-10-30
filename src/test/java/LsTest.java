import org.example.CommandLineInterpreter;
import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class LsTest {
    private CommandLineInterpreter cmd;

    @BeforeEach
    public void setUp() {
        cmd = new CommandLineInterpreter();
    }
    @Test
    public void testLs(){
        String result = cmd.LsCommand(new String[] {"bashar"});

        assertTrue(result.contains("file1.txt"), "Output should include file1.txt");
        assertTrue(result.contains("file2.txt"), "Output should include file2.txt");
    }
}
