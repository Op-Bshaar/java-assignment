import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.example.MkdirCommand;
import java.nio.file.Paths;
public class MkdirTest {
    @Test
    public  void testmkdir(){
        MkdirCommand mk = new MkdirCommand();
        String[] args = {"bashar"};
        String result = mk.run(args);
        assertEquals("Directory created: " + Paths.get("bashar").toAbsolutePath(), result);
    }

}
