package ch.heigvd.amt.framework.engine;

import ch.heigvd.amt.framework.api.IService;
import ch.heigvd.amt.framework.exceptions.InvalidOperationException;
import ch.heigvd.amt.framework.exceptions.LookupException;
import ch.heigvd.amt.framework.services.HealthCheckService;

/**
 * The RequestProcessor is responsible to process requests sent by clients to the server. In this initial
 * implementation, the RequestProcessor tries to do everything himself.
 *
 * This raises 2 problems:
 *
 * 1) the class is going to become large and hard to maintain if we keep adding commands (long switch statement)
 * 2) we don't have a separation between "framework" and "application" commands: if we want to extend the behavior,
 *    we have to modify this class instead of adding new code outside
 */
class RequestProcessor {

  void processRequest(Request request, Response response) throws InvalidOperationException {

    ServiceRegistry registry = ServiceRegistry.getServiceRegistry();
    try {
      IService service = registry.lookup(request.getServiceName());
      String returnValue = service.execute(request.getOperationName(), request.getParameterValues());
      response.setStatusCode(0);
      response.setValue(returnValue);
    } catch (LookupException e) {
      throw new InvalidOperationException(e.getMessage());
    }

  }

}
