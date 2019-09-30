package ch.heigvd.amt.framework.services;

import ch.heigvd.amt.framework.exceptions.InvalidOperationException;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class HealthCheckServiceTest {

  @Test
  void itShouldProvideAPingOperation() throws InvalidOperationException {
    HealthCheckService service = new HealthCheckService();
    String value = service.execute(HealthCheckService.OPERATION_PING, Arrays.asList());
    assertEquals(HealthCheckService.OPERATION_PING_RESPONSE, value);
  }

  @Test
  void itShouldProvideAnUptimeOperation() throws InvalidOperationException {
    HealthCheckService service = new HealthCheckService();
    String value = service.execute(HealthCheckService.OPERATION_UPTIME, Arrays.asList());
    assertNotEquals(0, value);
  }
}