import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.example.CommandLineInterpeter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommandLineInterpeterTest {
    
    private final CommandLineInterpeter cmd = new CommandLineInterpeter();

    @Test
    public  void testPwd(){
        String output = cmd.pwd();
        String expectedOutput = System.getProperty("user.dir");
        assertEquals(output, expectedOutput);
    }
    
    @Test
    public void testCatWithExistingFile() throws IOException{
        Path testFile = Files.createTempFile("testFile", ".txt");
        Files.writeString(testFile, "Hello, World!\nThis is a test file.");

        try (Stream<String> output = cmd.cat(testFile.toString())) {
            List<String> lines = output.collect(Collectors.toList());
            assertEquals(List.of("Hello, World!", "This is a test file."), lines);
        } finally {
            Files.deleteIfExists(testFile);
        }
    }

    @Test
    public void testCatWithNonExistentFile() {
        String nonExistentPath = "nonExistentFile.txt";        
        try (Stream<String> output = cmd.cat(nonExistentPath)) {
            List<String> lines = output.collect(Collectors.toList());
            assertEquals(List.of("cat: " + nonExistentPath + " does not exist!"), lines);
        }
    }

    @Test
    public void testCatWithDirectoryPath() throws IOException {
        Path tempDir = Files.createTempDirectory("testDir");

        try (Stream<String> output = cmd.cat(tempDir.toString())) {
            List<String> lines = output.collect(Collectors.toList());
            assertEquals(List.of("cat: " + tempDir + " is a directory, not a file!"), lines);
        } finally {
            Files.deleteIfExists(tempDir);
        }
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
  @Test
    public  void TestExit(){
//      String result = cmd.ExitCommand(Scanner scanner);
//      assertEquals("Directory deleted: " + Paths.get("bashar").toAbsolutePath(), result);
    }
}