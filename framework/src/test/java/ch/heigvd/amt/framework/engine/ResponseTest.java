package ch.heigvd.amt.framework.engine;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.*;

class ResponseTest {

  @Test
  void itShouldSerializeCorrectly() throws IOException {
    Response response = new Response();
    response.setStatusCode(0);
    response.setValue("hello");
    assertEquals("0:hello", response.serialize());
  }
}