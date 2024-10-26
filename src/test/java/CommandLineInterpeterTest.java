import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.example.CommandLineInterpeter;
public class CommandLineInterpeterTest {
    @Test
    public  void testpwd(){
        CommandLineInterpeter cmd = new CommandLineInterpeter();
        String output = cmd.pwd();
        String expectedOutput = System.getProperty("user.dir");
        assertEquals(output, expectedOutput);
    }

}