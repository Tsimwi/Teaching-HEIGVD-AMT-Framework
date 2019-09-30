package ch.heigvd.amt.framework.engine;

import ch.heigvd.amt.framework.api.IService;
import ch.heigvd.amt.framework.exceptions.InvalidOperationException;
import ch.heigvd.amt.framework.exceptions.InvalidRequestException;
import ch.heigvd.amt.framework.exceptions.LookupException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ServiceRegistryTest {

  @Test
  void itShouldAllowMeToRegisterAndLookupServices() throws LookupException {
    ServiceRegistry registry = new ServiceRegistry();
    IService service = new IService() {
      @Override
      public String execute(String operationName, List<String> parameterValues) throws InvalidOperationException {
        return null;
      }
    };
    registry.register("myService", service);
    IService lookup = registry.lookup("myService");
    assertEquals(service, lookup);
  }

  @Test
  void itShouldThrowAnExceptionWhenServiceHasNotBeenRegistered() {
    ServiceRegistry registry = new ServiceRegistry();
    Assertions.assertThrows(LookupException.class, () -> {
      registry.lookup("nameOfAServiceThatWasNeverRegistered");
    });
  }
}