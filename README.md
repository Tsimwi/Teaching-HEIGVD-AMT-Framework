# Teaching-HEIGVD-AMT-Framework
## A simple application-level protocol

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



## Framework vs Application

The first notion that we want to illustrate with this project is the **Inversion of Control (IoC) pattern**. When this pattern is applied, the developer is not invoking methods or functions provided by a **library**. Instead, the developer provides code that is invoked by a **framework**, when a certain event occurs.

