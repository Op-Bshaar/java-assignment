import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Paths;

import org.example.CommandLineInterpeter;

public class CommandLineInterpeterTest {
    
    private final CommandLineInterpeter cmd = new CommandLineInterpeter();

    @Test
    public  void testPwd(){
        String output = cmd.pwd();
        String expectedOutput = System.getProperty("user.dir");
        assertEquals(output, expectedOutput);
    }
    

   @Test
    public void testmkdir(){
       String result = cmd.MkdirCommand(new String[] {"bashar"});
       assertEquals("Directory created: " + Paths.get("bashar").toAbsolutePath(), result);
    }
 @Test
   public void testLs(){
        String result = cmd.MkdirCommand(new String[] {"bashar"});
  }
 @Test
    public void TestRm(){
        String result = cmd.RmCommand(new String[] {"bashar"});
        assertEquals("Directory deleted: " + Paths.get("bashar").toAbsolutePath(), result);
    }
}