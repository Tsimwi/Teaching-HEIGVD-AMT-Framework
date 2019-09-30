package ch.heigvd.amt.framework.engine;

import lombok.Data;

import java.io.IOException;
import java.io.Writer;

@Data
class Response {

  private int statusCode;

  private String value;

  public String serialize() throws IOException {
    return statusCode + ":" + value;
  }

}
