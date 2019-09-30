package ch.heigvd.amt.framework.services;

import ch.heigvd.amt.framework.exceptions.InvalidOperationException;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorServiceTest {

  @Test
  void itShouldProvideAnAddOperation() throws InvalidOperationException {
    CalculatorService service = new CalculatorService();
    String value = service.execute(CalculatorService.OPERATION_ADD, Arrays.asList("7", "5"));
    assertEquals(Integer.toString(12), value);
  }

  @Test
  void itShouldProvideAMultOperation() throws InvalidOperationException {
    CalculatorService service = new CalculatorService();
    String value = service.execute(CalculatorService.OPERATION_MULT, Arrays.asList("7", "5"));
    assertEquals(Integer.toString(35), value);
  }
}