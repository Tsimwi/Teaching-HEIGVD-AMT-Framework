package ch.heigvd.amt.framework.engine;

import ch.heigvd.amt.framework.api.IService;
import ch.heigvd.amt.framework.exceptions.InvalidOperationException;
import ch.heigvd.amt.framework.exceptions.InvalidRequestException;
import ch.heigvd.amt.framework.services.CalculatorService;
import ch.heigvd.amt.framework.services.ClockService;
import ch.heigvd.amt.framework.services.HealthCheckService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class RequestProcessorTest {

  RequestProcessor requestProcessor = new RequestProcessor();

  @BeforeAll
  static void registerServices() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
    ServiceRegistry registry = ServiceRegistry.getServiceRegistry();
    registry.register(CalculatorService.SERVICE_NAME, new CalculatorService());
    registry.register(ClockService.SERVICE_NAME, new ClockService());

    // IService jokesService = (IService) Class.forName("com.wasabi.amt.services.JokesService").getConstructor().newInstance();
    /// registry.register("jokesService", jokesService);
  }

  @Test
  void processClockServiceGetCurrentDateCommand() throws InvalidRequestException, InvalidOperationException {
    String command = "clockService:getCurrentDate";
    Request request = new Request();
    request.deserialize(command);
    Response response = new Response();
    requestProcessor.processRequest(request, response);
    String value = response.getValue();
    Pattern p = Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2}");
    Matcher m = p.matcher(value);
    assertEquals(0,response.getStatusCode());
    assertTrue(m.matches());
  }

  @Test
  void processCalculatorServiceAddCommand() throws InvalidRequestException, InvalidOperationException {
    String command = "calculatorService:add:3:5";
    Request request = new Request();
    request.deserialize(command);
    Response response = new Response();
    requestProcessor.processRequest(request, response);
    assertEquals(0,response.getStatusCode());
    assertEquals("8", response.getValue());
  }

  @Test
  void processCalculatorServiceMultCommand() throws InvalidRequestException, InvalidOperationException {
    String command = "calculatorService:mult:3:5";
    Request request = new Request();
    request.deserialize(command);
    Response response = new Response();
    requestProcessor.processRequest(request, response);
    assertEquals(0,response.getStatusCode());
    assertEquals("15", response.getValue());
  }

  @Test
  @Disabled
  void processJokesServiceTellJokeCommand() throws InvalidRequestException, InvalidOperationException {
    String command = "jokesService:tellJoke";
    Request request = new Request();
    request.deserialize(command);
    Response response = new Response();
    requestProcessor.processRequest(request, response);
    assertEquals(0,response.getStatusCode());
    assertNotNull(response.getValue());
  }

}