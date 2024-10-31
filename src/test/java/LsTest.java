import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.example.CommandLineInterpreter;
import org.junit.jupiter.api.*;


public class LsTest {
    private CommandLineInterpreter cmd;
    private Path folderpath;
    @BeforeEach
    public void setUp() throws IOException {
        String result="";
        cmd = new CommandLineInterpreter();
        // create directory
        // go to the new directory
        folderpath=  Files.createTempDirectory("TestDir");
        cmd.CdCommand(folderpath.toString());
        //need help moving here
    
        
        
        // add some files

            Files.createFile(Paths.get(folderpath.toString(),"testfile"));
        
        // add some directories

            Path Subdir= Paths.get(folderpath.toString(), "subdir");
            Files.createDirectory(Subdir);

        // add some hidden files

        Path hiddenfile= Paths.get(folderpath.toString(),".hiddenfile");
        Files.createFile(hiddenfile);
        Files.setAttribute(hiddenfile, "dos:hidden", true);
        
    }

    @AfterEach
    public void cleanUp() throws IOException{
        // delete any thing created in setup
        Files.walk(folderpath)
             .map(Path::toFile)
             .forEach(file -> file.delete());
    }

    @Test
    public void testLs() throws IOException{
        // Expected output after running ls command without parameters
        String[] param= new String[1];
        param[0]= "";
        String output = cmd.LsCommand(param);
        String expected=correctoutput(false,false);
        assertEquals(expected, output);
    
    }
    @Test
    public void testLsR() throws IOException{
        //testLsR
        String[] param= new String[1];
        param[0]= "-r";
        String output = cmd.LsCommand(param);

            String expected=correctoutput(true,false);
            assertEquals(expected, output);
    }
    @Test
    public void testLsA() throws IOException{
        //testLsA
        String[] param= new String[1];
        param[0]= "-a";
        String output = cmd.LsCommand(param);
            String expected=correctoutput(false,true);
            assertEquals(expected, output);
    }
    @Test
    public void testLsAR() throws IOException{
        //testLsAR
        String[] param= new String[2];
        param[0]= "-a";
        param[1]="-r";
        String output = cmd.LsCommand(param);

            String expected=correctoutput(true,true);
            assertEquals(expected, output);
    }
    
    private String correctoutput(boolean rev,boolean all) throws IOException {
        
        Path currentDir = folderpath;
        final String newLine = System.lineSeparator();
        StringBuilder expectedOutput = new StringBuilder(newLine);
        expectedOutput.append("Directory: ").append(currentDir.toAbsolutePath()).append(newLine).append(newLine);
        expectedOutput.append(String.format("%-5s %-20s %10s %s\n", "Mode", "LastWriteTime", "Length", "Name"));
        expectedOutput.append("------------------------------------------------------------").append(newLine);
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(currentDir)) {
            List<Path> itemPaths = new ArrayList<>();
            for (Path entry : stream) {
                itemPaths.add(entry);
            }
            Collections.sort(itemPaths);
            if(rev==true)
            {
                Collections.reverse(itemPaths);
            }
            for (Path entry : itemPaths) {
                if ((Files.isHidden(entry) && !all)) {
                    continue;
                }
                BasicFileAttributes attrs = Files.readAttributes(entry, BasicFileAttributes.class);
                String type = attrs.isDirectory() ? "d----" : "-a---";
                String lastModifiedTime = new SimpleDateFormat("MM/dd/yyyy hh:mm a")
                        .format(new Date(attrs.lastModifiedTime().toMillis()));
                long size = attrs.size();
                String fileName = entry.getFileName().toString();

                // Append formatted output to expectedOutput
                expectedOutput.append(String.format("%-5s %-20s %10d %s\n", type, lastModifiedTime, size, fileName));
            }
        }

        return expectedOutput.toString();
    }
}

