package ch.heigvd.amt.framework.services;

import ch.heigvd.amt.framework.api.IService;
import ch.heigvd.amt.framework.exceptions.InvalidOperationException;

import java.util.List;

public class CalculatorService implements IService {
  public static final String SERVICE_NAME = "calculatorService";
  public static final String OPERATION_ADD = "add";
  public static final String OPERATION_MULT = "mult";

  @Override
  public String execute(String operationName, List<String> parameterValues) throws InvalidOperationException {
    switch (operationName) {
      case OPERATION_ADD:
        Integer p1 = Integer.parseInt(parameterValues.get(0));
        Integer p2 = Integer.parseInt(parameterValues.get(1));
        return Integer.toString(p1 + p2);
      case OPERATION_MULT:
        p1 = Integer.parseInt(parameterValues.get(0));
        p2 = Integer.parseInt(parameterValues.get(1));
        return Integer.toString(p1 * p2);
      default:
        throw new InvalidOperationException("Operation " + operationName + " is not valid.");
    }
  }

  @Override
  public String getHelpMessage() {
    return "service: " + this.getClass().getCanonicalName() + "\r\n"
      + " operation: " + OPERATION_ADD + " (Integer v1, Integer v2)\r\n"
      + " operation: " + OPERATION_MULT + " (Integer v1, Integer v2)";
  }
}
