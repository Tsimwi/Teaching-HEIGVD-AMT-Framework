package ch.heigvd.amt.framework.api;

import ch.heigvd.amt.framework.exceptions.InvalidOperationException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IServiceTest {

  IService myService = new IService() {
    @Override
    public String execute(String operationName, List<String> parameterValues) throws InvalidOperationException {
      if (operationName.equalsIgnoreCase("ping")) {
        return "pong";
      }
      throw new InvalidOperationException("I only understand the command 'ping'");
    }
  };

  @Test
  void itShouldBePossibleToInvokeAnOperationOnAService() throws InvalidOperationException {
    String returnValue = myService.execute("ping", null);
    assertEquals("pong", returnValue);
  }

  @Test
  void invokingUnknownOperationOnServiceShouldThrowAnException() {
    assertThrows(InvalidOperationException.class, () -> {
      String returnValue = myService.execute("doesNotExist", null);
    });
  }

}