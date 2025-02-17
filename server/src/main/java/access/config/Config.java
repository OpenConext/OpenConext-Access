package access.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "config")
@Getter
@Setter
@NoArgsConstructor
public class Config {

    private String clientUrl;
    private String name;
    private boolean authenticated;
    private List<String> missingAttributes;

    public Config(Config base) {
        this.clientUrl = base.clientUrl;
    }

    public Config withAuthenticated(boolean authenticated) {
        this.setAuthenticated(authenticated);
        return this;
    }

    public Config withName(String name) {
        this.setName(name);
        return this;
    }

    public Config withMissingAttributes(List<String> missingAttributes) {
        this.setMissingAttributes(missingAttributes);
        return this;
    }

}
