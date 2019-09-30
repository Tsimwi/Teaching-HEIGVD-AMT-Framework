package ch.heigvd.amt.framework.services;

import ch.heigvd.amt.framework.api.IService;
import ch.heigvd.amt.framework.exceptions.InvalidOperationException;

public class HealthCheckService implements IService {

  public final static String OPERATION_PING = "ping";
  public final static String OPERATION_PING_RESPONSE_VALUE = "pong!";

  @Override
  public String getName() {
    return null;
  }

  @Override
  public String invokeOperation(String operation, String... parameters) throws InvalidOperationException {
    if (OPERATION_PING.equalsIgnoreCase(operation)) {
      return OPERATION_PING_RESPONSE_VALUE;
    }
    throw new InvalidOperationException("Operation " + operation + " not defined on service " + this.getClass().getName());
  }
}
