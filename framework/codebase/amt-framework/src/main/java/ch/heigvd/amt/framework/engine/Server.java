package ch.heigvd.amt.framework.engine;

import ch.heigvd.amt.framework.api.IService;
import ch.heigvd.amt.framework.services.CalculatorService;
import ch.heigvd.amt.framework.services.ClockService;
import ch.heigvd.amt.framework.services.HealthCheckService;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
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

  public void start() throws IOException {
    startSignal = new CountDownLatch(1);
    shouldRun = true;

    serverSocket = new ServerSocket(PORT);
    startTime = System.currentTimeMillis();
    startSignal.countDown();

    Thread acceptThread = new Thread(new Runnable() {
      @Override
      public void run() {
        while (shouldRun) {
          try {
            Socket socket = serverSocket.accept();
            executorService.submit(() -> {
              try {
                handleClient(socket);
              } catch (IOException e) {
                e.printStackTrace();
              }
            });
          } catch (IOException e) {
            e.printStackTrace();
          }
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
    System.out.println("prep stop signal");
    stopSignal = new CountDownLatch(1);
    System.out.println(stopSignal);
    shouldRun = false;
    try {
      if (serverSocket != null) {
        serverSocket.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    try {
      System.out.println("await on stop signal");
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

  public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, IOException {
    ServiceRegistry registry = ServiceRegistry.getServiceRegistry();
    registry.register(HealthCheckService.class.getCanonicalName(), new HealthCheckService());

    IService service = ((IService) Class.forName("ch.heigvd.amt.framework.services.ClockService").getDeclaredConstructor().newInstance());
    registry.register(service.getClass().getCanonicalName(), service);

    service = ((IService) Class.forName("ch.heigvd.amt.framework.services.CalculatorService").getDeclaredConstructor().newInstance());
    registry.register(service.getClass().getCanonicalName(), service);

    Server server = new Server();
    server.start();
  }

  public boolean isRunning() {
    return shouldRun;
  }
}
