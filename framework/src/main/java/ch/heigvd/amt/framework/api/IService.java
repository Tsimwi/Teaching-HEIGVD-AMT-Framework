package ch.heigvd.amt.framework.api;

import ch.heigvd.amt.framework.exceptions.InvalidOperationException;

public interface IService {

  String getName();

  String invokeOperation(String operation, String... parameters) throws InvalidOperationException;

}
