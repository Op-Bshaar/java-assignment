import org.example.CommandLineInterpreter;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class LsTest {
    private CommandLineInterpreter cmd;

    @BeforeEach
    public void setUp() {
        cmd = new CommandLineInterpreter();
    }
    @Test
    public void testLs(){
        String result = cmd.LsCommand();

        assertTrue(result.contains("file1.txt"), "Output should include file1.txt");
        assertTrue(result.contains("file2.txt"), "Output should include file2.txt");
    }
    @Test
    public void testLsR(){
        //finish
    }
    @Test
    public void testLsA(){
        //finish
    }
}
