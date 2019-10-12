package ch.heigvd.amt.framework.engine;

import ch.heigvd.amt.framework.services.HealthCheckService;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The Server is responsible to accept incoming connections on a TCP port. When client connects, it sends
 * a single line (ending with CRLF). The Server reads this line and passes it to the RequestProcessor (without
 * the CRLF). When it gets the response from the RequestProcessor, the Server sends it to the client and reads
 * the next request line.
 */
public class Server {

  public static final int PORT = 2205;
  private static final int THREAD_POOL_SIZE = 100;

  private static final int STATE_STOPPED = 0;
  private static final int STATE_RUNNING = 1;

  private long startTime;

  private boolean shouldRun = false;

  private ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
  private RequestProcessor requestProcessor = new RequestProcessor();

  private CountDownLatch startSignal;
  private CountDownLatch stopSignal;
  ServerSocket serverSocket;

  private static Server singleton = new Server();
  private Server() {
  }
  public static Server getServer() {
    return singleton;
  }

  public void start() {
    startSignal = new CountDownLatch(1);
    shouldRun = true;

    Thread acceptThread = new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          serverSocket = new ServerSocket(PORT);
          startSignal.countDown();
          startTime = System.currentTimeMillis();
          while (shouldRun) {
            Socket socket = serverSocket.accept();
            executorService.submit(() -> {
              try {
                handleClient(socket);
              } catch (IOException e) {
                e.printStackTrace();
              }
            });
          }
        } catch (IOException e) {
          startSignal.countDown();
        }
        stopSignal.countDown();

      }
    });
    acceptThread.start();
    try {
      startSignal.await();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public void stop() {
    stopSignal = new CountDownLatch(1);
    shouldRun = false;
    try {
      serverSocket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    try {
      stopSignal.await();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  private void handleClient(Socket socket) throws IOException {
    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
    BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
    String line = in.readLine();
    while (line != null) {
      try {
        Request request = new Request();
        request.deserialize(line);
        Response response = new Response();
        requestProcessor.processRequest(request, response);
        out.write(response.serialize());
        out.write(Protocol.CRLF);
        out.flush();
      } catch (Exception e) {
        out.write("INVALID REQUEST " + e.getMessage());
        out.write(Protocol.CRLF);
        out.flush();
      }
      line = in.readLine();
    }
    in.close();
    out.close();
    socket.close();
  }

  public long getUptime() {
    return System.currentTimeMillis() - startTime;
  }

  public static void main(String[] args) {
    ServiceRegistry registry = ServiceRegistry.getServiceRegistry();
    registry.register(HealthCheckService.SERVICE_NAME, new HealthCheckService());
    // registry.register(ClockService.SERVICE_NAME, new ClockService());
    // registry.register(CalculatorService.SERVICE_NAME, new CalculatorService());

    Server server = new Server();
    server.start();
  }

  public boolean isRunning() {
    return shouldRun;
  }
}
