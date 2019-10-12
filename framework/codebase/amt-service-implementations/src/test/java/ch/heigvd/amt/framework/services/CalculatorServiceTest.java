package ch.heigvd.amt.framework.services;

import ch.heigvd.amt.framework.exceptions.InvalidOperationException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorServiceTest extends ServiceTest{

  @BeforeEach
  void setupService() {
    service = new CalculatorService();
  }

  @Test
  void itShouldProvideAnAddOperation() throws InvalidOperationException {
    String value = service.execute(CalculatorService.OPERATION_ADD, Arrays.asList("7", "5"));
    assertEquals(Integer.toString(12), value);
  }

  @Test
  void itShouldProvideAMultOperation() throws InvalidOperationException {
    String value = service.execute(CalculatorService.OPERATION_MULT, Arrays.asList("7", "5"));
    assertEquals(Integer.toString(35), value);
  }

  @Test
  void itShouldThrowAnExceptionIfOperationDoesNotExist() {
    assertThrows(InvalidOperationException.class, () -> {
      service.execute("doesnotexist", null);
    });
  }
}