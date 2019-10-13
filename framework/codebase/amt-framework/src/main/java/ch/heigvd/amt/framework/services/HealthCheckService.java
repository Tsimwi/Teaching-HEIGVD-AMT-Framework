package ch.heigvd.amt.framework.services;

import ch.heigvd.amt.framework.api.IService;
import ch.heigvd.amt.framework.engine.Server;
import ch.heigvd.amt.framework.exceptions.InvalidOperationException;

import java.util.List;

public class HealthCheckService implements IService {
  public static final String SERVICE_NAME = "healthCheckService";
  public static final String OPERATION_PING = "ping";
  public static final String OPERATION_PING_RESPONSE = "I am alive";
  public static final String OPERATION_UPTIME = "uptime";

  @Override
  public String execute(String operationName, List<String> parameterValues) throws InvalidOperationException {
    switch (operationName) {
      case OPERATION_PING:
        return OPERATION_PING_RESPONSE;
      case OPERATION_UPTIME:
        return Long.toString(Server.getServer().getUptime());
      default:
        throw new InvalidOperationException("Operation " + operationName + " is not valid.");
    }
  }

  @Override
  public String getHelpMessage() {
    return "service: " + this.getClass().getCanonicalName() + "\r\n"
      + " operation: " + OPERATION_PING + " (no arguments)\r\n"
      + " operation: " + OPERATION_UPTIME + " (no arguments)";
  }

}
