import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.example.Calculator;

public class TestBashar {
 @Test
 void any() {
  Calculator calculator = new Calculator();
  int result = calculator.multiply(2, 3);
  assertEquals(6, result);
 }
}
