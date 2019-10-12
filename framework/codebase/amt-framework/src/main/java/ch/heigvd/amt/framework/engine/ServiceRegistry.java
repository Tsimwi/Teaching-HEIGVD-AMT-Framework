package ch.heigvd.amt.framework.engine;

import ch.heigvd.amt.framework.api.IService;
import ch.heigvd.amt.framework.exceptions.LookupException;

import java.util.HashMap;
import java.util.Map;

public class ServiceRegistry {

  private Map<String, IService> registry = new HashMap<>();
  private static ServiceRegistry singleton = new ServiceRegistry();

  protected ServiceRegistry() {}
  public static ServiceRegistry getServiceRegistry() {
    return singleton;
  }

  public void register(String serviceName, IService service) {
    registry.put(serviceName, service);
  }

  public IService lookup(String serviceName) throws LookupException {
    IService candidate = registry.get(serviceName);
    if (candidate == null) {
      throw new LookupException("Service " + serviceName + " could not be looked up.");
    }
    return candidate;
  }

}
