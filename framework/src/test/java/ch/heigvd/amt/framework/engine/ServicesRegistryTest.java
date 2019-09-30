package ch.heigvd.amt.framework.engine;

import ch.heigvd.amt.framework.api.IService;
import ch.heigvd.amt.framework.exceptions.InvalidOperationException;
import ch.heigvd.amt.framework.exceptions.ServiceLookupException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

class ServicesRegistryTest {

  @Test
  void itShouldBePossibleToRegisterAndLookupAService() throws ServiceLookupException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InvalidOperationException {
    ServicesRegistry registry = new ServicesRegistry();
    Assertions.assertThrows(ServiceLookupException.class, () -> { registry.lookup("myService"); });
    IService myService = new IService() {
      @Override
      public String getName() {
        return "my service name";
      }

      @Override
      public String invokeOperation(String operation, String... parameters) {
        return "my service has been invoked";
      }

    };
    registry.register("myService", myService);
    IService result = registry.lookup("myService");
    result.invokeOperation("doSomethingForMe");
    assertNotEquals(myService, result);
  }

}