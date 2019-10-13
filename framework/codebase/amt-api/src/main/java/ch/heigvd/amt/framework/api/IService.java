package ch.heigvd.amt.framework.api;

import ch.heigvd.amt.framework.exceptions.InvalidOperationException;

import java.util.List;

public interface IService {
  String execute(String operationName, List<String> parameterValues) throws InvalidOperationException;
  String getHelpMessage();
}
