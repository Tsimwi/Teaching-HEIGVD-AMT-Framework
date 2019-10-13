package ch.heigvd.amt.framework.engine;

import ch.heigvd.amt.framework.exceptions.InvalidOperationException;
import ch.heigvd.amt.framework.exceptions.InvalidRequestException;
import ch.heigvd.amt.framework.services.HealthCheckService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class RequestProcessorTest {

  RequestProcessor requestProcessor = new RequestProcessor();

  @BeforeAll
  static void registerServices() {
    ServiceRegistry registry = ServiceRegistry.getServiceRegistry();
    registry.register(HealthCheckService.SERVICE_NAME, new HealthCheckService());
  }

  @Test
  void processHealthCheckServicePing() throws InvalidRequestException, InvalidOperationException {
    String command = "healthCheckService:ping";
    Request request = new Request();
    request.deserialize(command);
    Response response = new Response();
    requestProcessor.processRequest(request, response);
    assertEquals(0,response.getStatusCode());
    assertEquals("I am alive", response.getValue());
  }

  @Test
  void processHealthCheckServiceUptime() throws InvalidRequestException, InvalidOperationException {
    String command = "healthCheckService:uptime";
    Request request = new Request();
    request.deserialize(command);
    Response response = new Response();
    requestProcessor.processRequest(request, response);
    assertEquals(0,response.getStatusCode());
    assertNotEquals(0, Long.parseLong(response.getValue()));
  }

  @Test
  void processHelpCommand() throws InvalidRequestException, InvalidOperationException {
    String command = "help:all";
    Request request = new Request();
    request.deserialize(command);
    Response response = new Response();
    requestProcessor.processRequest(request, response);
    assertEquals(0,response.getStatusCode());
  }
}