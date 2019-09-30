package ch.heigvd.amt.framework.engine;

import ch.heigvd.amt.framework.exceptions.InvalidRequestException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RequestTest {

  @Test
  void itShouldParseAValidRequestString() throws InvalidRequestException {
    String valid = "myService:myOperation:v1:v2";
    Request request = new Request();
    request.deserialize(valid);
    assertEquals("myService", request.getServiceName());
    assertEquals("myOperation", request.getOperationName());
    assertEquals(2, request.getParameterValues().size());
    assertEquals("v1", request.getParameterValues().get(0));
    assertEquals("v2", request.getParameterValues().get(1));
  }

  @Test
  void itShouldRefuseARequestWithAMissingOperationOrService() throws InvalidRequestException {
    String invalid = "serviceButMissingOperation";
    Assertions.assertThrows(InvalidRequestException.class, () -> {
      Request request = new Request();
      request.deserialize(invalid);
    });
  }

  @Test
  void itShouldRefuseARequestWithAnEmptyOperation() throws InvalidRequestException {
    String invalid = "serviceButMissingOperation:";
    Assertions.assertThrows(InvalidRequestException.class, () -> {
      Request request = new Request();
      request.deserialize(invalid);
    });
  }

  @Test
  void itShouldRefuseAnEmptyRquest() throws InvalidRequestException {
    String invalid = "";
    Assertions.assertThrows(InvalidRequestException.class, () -> {
      Request request = new Request();
      request.deserialize(invalid);
    });
  }

}