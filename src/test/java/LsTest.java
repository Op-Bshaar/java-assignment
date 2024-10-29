import org.example.CommandLineInterpreter;
import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;



public class LsTest {
    private CommandLineInterpreter cmd;

    @BeforeEach
    public void setUp() {
        cmd = new CommandLineInterpreter();
    }
    public void testLs(){
        String result = cmd.MkdirCommand(new String[] {"bashar"});
    }
}
