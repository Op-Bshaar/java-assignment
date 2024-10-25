import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.example.PwdCommand;
public class PwdTest {
    @Test
    public  void testpwd(){
        PwdCommand pwd = new PwdCommand();
        String output = pwd.run(new String[0]);
        String expectedOutput = System.getProperty("user.dir");
        assertEquals(output, expectedOutput);
    }

}