package ch.heigvd.amt.framework.engine;

import ch.heigvd.amt.framework.exceptions.InvalidOperationException;

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

    // TODO: Not a big fan of using a switch statement, which is going to grow large as we add commands...
    switch (request.getServiceName()) {
      case "healthCheckService": // this is a "framework" service, so kind of OK
        response.setStatusCode(0);
        switch (request.getOperationName()) {
          case "ping":
            response.setValue("I am alive");
            break;
          case "uptime":
            response.setValue(Long.toString(Server.getServer().getUptime()));
            break;
        }
        break;
      case "clockService": // is this a "framework" or "application" service...
        response.setStatusCode(0);
        response.setValue("2019-01-01");
        break;
      case "calculatorService": // TODO: this is definitely an "application" service and needs to be extracted!!
        response.setStatusCode(0);
        switch (request.getOperationName()) {
          case "add":
            Integer p1 = Integer.parseInt(request.getParameterValues().get(0));
            Integer p2 = Integer.parseInt(request.getParameterValues().get(1));
            response.setValue(Integer.toString(p1 + p2));
            break;
          case "mult":
            p1 = Integer.parseInt(request.getParameterValues().get(0));
            p2 = Integer.parseInt(request.getParameterValues().get(1));
            response.setValue(Integer.toString(p1 * p2));
            break;
        }
        break;
    }
  }

}
