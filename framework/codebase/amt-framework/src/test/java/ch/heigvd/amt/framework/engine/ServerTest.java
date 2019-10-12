package ch.heigvd.amt.framework.engine;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

class ServerTest {

  @Test
  void itShouldBePossibleToStartAndStopServer() {
    Server theServer = Server.getServer();
    theServer.start();
    assertTrue(theServer.isRunning());
    theServer.stop();
    assertFalse(theServer.isRunning());
    theServer.start();
    assertTrue(theServer.isRunning());
    theServer.stop();
    assertFalse(theServer.isRunning());
  }

  @Test
  void itShouldBePossibleToSendCommandsToServer() throws IOException, InterruptedException {
    Server theServer = Server.getServer();
    theServer.start();
    Socket socket = new Socket("localhost", Server.PORT);
    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
    BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
    out.write("unknownService:unknownOperation\r\n");
    out.flush();
    String response = in.readLine();
    assertNotNull(response);
    theServer.stop();
  }

}