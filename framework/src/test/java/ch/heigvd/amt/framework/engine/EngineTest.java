package ch.heigvd.amt.framework.engine;

import ch.heigvd.amt.framework.annotations.Engine;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class EngineTest {

  @Test
  void start() {
  }

  @Test
  void scanClassPath() throws Exception {
    Engine engine = new Engine();
    // Set<Class> services = engine.scanClassPath();
    // assertNotNull(services);
  }
}