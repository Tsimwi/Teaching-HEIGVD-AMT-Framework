package ch.heigvd.amt.framework.api;

import ch.heigvd.amt.framework.exceptions.InvalidOperationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IServiceTest extends ServiceTest {

  @BeforeEach
  void setupService() {
    service = new IService() {
      @Override
      public String execute(String operationName, List<String> parameterValues) throws InvalidOperationException {
        if (operationName.equalsIgnoreCase("ping")) {
          return "pong";
        }
        throw new InvalidOperationException("I only understand the command 'ping'");
      };

      @Override
      public String getHelpMessage() {
        return "service name: " + this.getClass() + "\r\n"
          + " operation: ping (nor arguments)";
      }
    };
  }

  @Test
  void itShouldBePossibleToInvokeAnOperationOnAService() throws InvalidOperationException {
    String returnValue = service.execute("ping", null);
    assertEquals("pong", returnValue);
  }

  @Test
  void invokingUnknownOperationOnServiceShouldThrowAnException() {
    assertThrows(InvalidOperationException.class, () -> {
      String returnValue = service.execute("doesNotExist", null);
    });
  }

}