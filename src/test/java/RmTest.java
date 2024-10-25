import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.example.RmCommand;
import java.nio.file.Paths;
public class RmTest {
    @Test
    public  void testrm(){
        RmCommand rm = new RmCommand();
        String[] args = {"bashar"};
        String result = rm.run(args);
        assertEquals("Directory deleted: " + Paths.get("bashar").toAbsolutePath(), result);
    }

}