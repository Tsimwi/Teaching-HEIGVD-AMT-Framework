package ch.heigvd.amt.framework.services;

import ch.heigvd.amt.framework.exceptions.InvalidOperationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HealthCheckServiceTest {

  @Test
  void shouldReturnPongWhenReceivingPing() throws InvalidOperationException {
    HealthCheckService service = new HealthCheckService();
    String returnValue = service.invokeOperation(HealthCheckService.OPERATION_PING);
    assertEquals(HealthCheckService.OPERATION_PING_RESPONSE_VALUE, returnValue);
  }

  @Test
  void shouldRefuseInvalidOperation() {
    HealthCheckService service = new HealthCheckService();
    assertThrows(InvalidOperationException.class, () -> {
      service.invokeOperation("this string is not an operation name");
    });
  }

}