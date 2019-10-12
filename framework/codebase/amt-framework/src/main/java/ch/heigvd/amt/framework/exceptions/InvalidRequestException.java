package ch.heigvd.amt.framework.exceptions;

public class InvalidRequestException extends Exception {

  public final static int MISSING_SERVICE_OR_OPERATION = 4000;
  public static final String MISSING_SERVICE_OR_OPERATION_HUMAN = "Invalid command: service name and service operation are mandatory";

  private int statusCode;

  public InvalidRequestException(int statusCode, String message) {
    super(message);
    this.statusCode = statusCode;
  }

}
