package ch.heigvd.amt.framework.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ManagedComponent {

  @JsonProperty("interface")
  private String interfaceFqdn;

  @JsonProperty("implementation")
  private String implementationFqdn;

  private List<String> dependencies;
}
