import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.example.CommandLineInterpeter;
public class CommandLineInterpeterTest {
    @Test
    public  void testPwd(){
        CommandLineInterpeter cmd = new CommandLineInterpeter();
        String output = cmd.pwd();
        String expectedOutput = System.getProperty("user.dir");
        assertEquals(output, expectedOutput);
    }
    @Test
    public void testCat(){
        
    }
}