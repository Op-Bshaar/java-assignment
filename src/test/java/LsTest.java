import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.example.LsCommand;
import java.nio.file.Paths;
public class LsTest {
    @Test
    public void testls(){
        LsCommand rm = new LsCommand();
        String[] args = {"bashar"};
        String result = rm.run(args);
        assertEquals("Directory deleted: " + Paths.get("bashar").toAbsolutePath(), result);
    }

}