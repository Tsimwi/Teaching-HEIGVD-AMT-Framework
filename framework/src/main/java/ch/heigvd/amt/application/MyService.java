package ch.heigvd.amt.application;

import ch.heigvd.amt.framework.annotations.AmtService;

@AmtService
public class MyService {
  public String getName() {
    return "My Service";
  }
}
