package ch.heigvd.amt.framework.engine;

import ch.heigvd.amt.framework.exceptions.InvalidRequestException;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
class Request {

  private String serviceName;
  private String operationName;
  private List<String> parameterValues = new ArrayList<>();

  /**
   * When the server has read a command from the client socket, it creates an empty Request instance and then calls this
   * method to set the state of the Request by reading the bytes sent by the client
   *
   * @param requestString the command sent by the client, with the CRLF sequence stripped off
   * @throws InvalidRequestException
   */
  public void deserialize(String requestString) throws InvalidRequestException {
      String[] elements = requestString.split(":");

      if (elements.length < 2) {
        throw new InvalidRequestException(InvalidRequestException.MISSING_SERVICE_OR_OPERATION, InvalidRequestException.MISSING_SERVICE_OR_OPERATION_HUMAN);
      }

      serviceName = elements[0];
      operationName = elements[1];
      for (int i = 2; i < elements.length; i++) {
        parameterValues.add(elements[i]);
      }
  }

}
