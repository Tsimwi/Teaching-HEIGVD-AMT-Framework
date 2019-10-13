package ch.heigvd.amt.framework.engine;

import ch.heigvd.amt.framework.api.IService;
import ch.heigvd.amt.framework.exceptions.InvalidOperationException;
import ch.heigvd.amt.framework.exceptions.LookupException;

/**
 * The RequestProcessor is responsible to process requests sent by clients to the server. In this second
 * implementation, the RequestProcessor delegates work to services, which it looks up in the service registry.
 */
class RequestProcessor {

  void processRequest(Request request, Response response) throws InvalidOperationException {

    ServiceRegistry registry = ServiceRegistry.getServiceRegistry();

    if ("help".equalsIgnoreCase(request.getServiceName())) {
      response.setStatusCode(0);
      StringBuilder sb = new StringBuilder("*** HELP FOR REGISTERED SERVICES ***\r\n\r\n");
      registry.getServices().forEach(iService -> {
        sb.append(iService.getHelpMessage());
        sb.append("\r\n");
        sb.append("\r\n");
      });

      response.setValue(sb.toString());
      return;
    }

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
