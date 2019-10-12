package ch.heigvd.amt.framework.services;

import ch.heigvd.amt.framework.api.IService;
import ch.heigvd.amt.framework.exceptions.InvalidOperationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class ServiceTest {

  IService service;

  @Test
  void itShouldThrowAnExceptionIfOperationDoesNotExist() {
    assertThrows(InvalidOperationException.class, () -> {
      service.execute("operationNameThatDoesNotExist", null);
    });
  }

}
