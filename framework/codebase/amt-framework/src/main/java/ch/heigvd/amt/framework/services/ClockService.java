package ch.heigvd.amt.framework.services;

import ch.heigvd.amt.framework.api.IService;
import ch.heigvd.amt.framework.exceptions.InvalidOperationException;

import java.util.List;

public class ClockService implements IService {
  public static final String SERVICE_NAME = "clockService";
  public static final String OPERATION_GET_DATE = "getCurrentDate";

  @Override
  public String execute(String operationName, List<String> parameterValues) throws InvalidOperationException {
    switch (operationName) {
      case OPERATION_GET_DATE:
        return "2019-01-01";
      default:
        throw new InvalidOperationException("Operation " + operationName + " is not valid.");
    }
  }
}
