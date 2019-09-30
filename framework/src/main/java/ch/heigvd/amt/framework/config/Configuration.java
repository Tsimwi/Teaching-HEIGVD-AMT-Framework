package ch.heigvd.amt.framework.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class Configuration {

  @JsonProperty("managed-components")
  private Map<String,ManagedComponent> managedComponents;

}
