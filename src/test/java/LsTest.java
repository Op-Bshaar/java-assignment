import org.example.CommandLineInterpreter;
import org.junit.jupiter.api.*;


public class LsTest {
    private CommandLineInterpreter cmd;

    @BeforeEach
    public void setUp() {
        cmd = new CommandLineInterpreter();
        // create directory
        // go to the new directory
        // add some files
        // add some directories
        // add some hidden files
    }

    @BeforeEach
    public void cleanUp(){
        // delete any thing created in setup
    }

    @Test
    public void testLs(){
        // finish
    }
    @Test
    public void testLsR(){
        //finish
    }
    @Test
    public void testLsA(){
        //finish
    }
    @Test
    public void testLsAR(){
        //finish
    }
}
