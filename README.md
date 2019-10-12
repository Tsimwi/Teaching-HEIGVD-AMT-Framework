# Teaching-HEIGVD-AMT-Framework
## Introduction

This project was create to illustrate how an application server works "behind the scenes". We have tried to write as little code as possible to demonstrate a number of patterns and mechanisms. We have also done that in a step-by-step fashion, adding more features in every git branch.

While application servers work do most of their work with HTTP requests, we have made the choice to specify our own application-level protocol. This protocol allows clients to connect to the server over TCP, and to send a series of one-line commands (every command contains the name of a service, the name of a commands and a list of optional parameters).

## Specification of the AMT protocol

* The server accepts connection requests on TCP port 2205.

* Once the connection has been established, the client sends a command with the following syntax:

  ```
  request = serviceName ":" operationName *(":" parameterValue) CRLF
  ```

  In other words, a request specified the **name of a service** (e.g. clockService), the **name of an operation** provided by this service (e.g. getTime), and a list of **n parameters**. Each element in the command is separated by a semicolon. The command is terminated by the sequence CRLF.

  Note that service names, operation names and parameter values MUST NOT contain semicolons, nor the CRLF sequence.

* The server processes the command and sends back a response with the following syntax:

* ```
  response = statusCode ":" returnValue CRLF
  ```

  If the command has been processed successfully, `statusCode` is equal to 0 and returnValue is the value returned by the service operation. If the server was unable to process the command, `statusCode`has a non-zero value and `returnValue` contains an error message.

  Commands and responses are always encoded in UTF-8.

* When the client has read the server response, it can send a new command or close the TCP connection.

### Example scenario

```
S: accept connections on TCP port 2205
C: opens connection
C: healthCheckService:ping CRLF
S: 0:I am alive CRLF
C: healthCheckService:uptime CRLF
S: 0:12884
C: doesNotExistService:doSomething:v1:v2 CRLF
S: 4001:invalid service name
C: healthCheckService:doesNotExistOperation:v1 CRLF
S: 4002:invalid operation name
C: calculatorService:add:12:23
S: 0:35
C: calculatorService:add:12:string
S: 4003:invalid parameter (2)
C: calculatorService:add:12
S: 4004:missing parameter
```

## Design of the second version (step-02-service-registry)

In the second implementation, we achieved a nice refactoring by introducing the notions of **Service** and **ServiceRegistry**. Instead of having a huge method in the **RequestProcessor** class. Instead of an ugly switch statement, we now have the following code:

```
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
```

* From the request sent over TCP, we extract a service name, an operation name and a list of parameters (they are all String values)
* We use the service name to look up a service in the registry. With this instance, we can now invoke the operation and pass the parameter values. We obtain a return value, which we feed into the protocol response.

For this to work, we also have to register the services at startup:

```
  public static void main(String[] args) {
    ServiceRegistry registry = ServiceRegistry.getServiceRegistry();
    registry.register(HealthCheckService.SERVICE_NAME, new HealthCheckService());
    registry.register(ClockService.SERVICE_NAME, new ClockService());
    registry.register(CalculatorService.SERVICE_NAME, new CalculatorService());

    Server server = new Server();
    server.start();
  }
```

And obviously to implement the services:

```
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
}
```



## Critique of this version (step-02-service-registry)

This version has a number of flaws:

* The code is **monolithic**. We have a single project, which contains everything. There is **no separation** between the **generic server behaviour** (accepting TCP connections, reading protocol commands, dispatching work) and the **specific behaviour of individual command handlers**.
* It is not easy to ask **third-party developers** to extend the protocol and to provide additional command handlers. We **do not have any SDK** and they need to work in our codebase.

## Suggested refactoring

To improve the design, we could:

* **Split the codebase into 3 parts**: i) the core server, ii) a library with interfaces that we provide to third-party developers (our SDK) and iii) the service implementations created by third-party developers.
* Use the **Java reflection API** to dynamically instantiate and invoke classed provided by third-party developers.

