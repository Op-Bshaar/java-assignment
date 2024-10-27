import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.example.CommandLineInterpeter;

import java.nio.file.Paths;
import java.util.Scanner;

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

   @Test
    public void testmkdir(){
       CommandLineInterpeter cmd = new CommandLineInterpeter();
       String result = cmd.MkdirCommand(new String[] {"bashar"});
       assertEquals("Directory created: " + Paths.get("bashar").toAbsolutePath(), result);
    }
 @Test
   public void testLs(){
        CommandLineInterpeter cmd = new CommandLineInterpeter();
        String result = cmd.MkdirCommand(new String[] {"bashar"});
  }
 @Test
    public void TestRm(){
        CommandLineInterpeter cmd = new CommandLineInterpeter();
        String result = cmd.RmCommand(new String[] {"bashar"});
        assertEquals("Directory deleted: " + Paths.get("bashar").toAbsolutePath(), result);
    }
  @Test
    public  void TestExit(){
      CommandLineInterpeter cmd = new CommandLineInterpeter();
//      String result = cmd.ExitCommand(Scanner scanner);
//      assertEquals("Directory deleted: " + Paths.get("bashar").toAbsolutePath(), result);
    }
}