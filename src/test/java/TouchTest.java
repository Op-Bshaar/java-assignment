import org.example.CommandLineInterpreter;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TouchTest {
    private Path filepath;
    private CommandLineInterpreter cmd;

    
    @BeforeEach
    public void setUp() {
        cmd = new CommandLineInterpreter();
        
   
    }
    @Test
    public void TouchTestNotExists()
    {
        //test if file doesn't exist
        
        String[] filename= new String[1];
        filename[0]="TouchTest";
        filepath=Paths.get(filename[0]);
        String result = cmd.TouchCommand(filename);
        assertEquals("created file:" + filename[0], result);
    }
    @Test


    public void TouchTestExists()
    {
        //test if file exists
        String[] filename= new String[1];
        String result;
        try{
        filename[0]="TouchTest";
        filepath=Paths.get(filename[0]);
        Files.createFile(filepath);
         result = cmd.TouchCommand(filename);
        }
        catch(IOException e)
        {
             result= e + "failed on TouchTestExists()";
        }
        assertEquals("touched: " + filename[0], result);
        
    }
    public void TouchTestFolder()
    {
        //test if file exists
        String[] filename= new String[1];
        String result;
        try{
        filename[0]="TouchTest";
        filepath=Paths.get(filename[0]);
        Files.createFile(filepath);
         result = cmd.TouchCommand(filename);
        }
        catch(IOException e)
        {
             result= e + "failed on TouchTestExists()";
        }
        assertEquals("touched: " + filename[0], result);
        
    }

    @AfterEach
    
        public void tearDown() throws IOException {
            // Clean up created test file
            Files.deleteIfExists(filepath);
        }
    

}
