package zunpiau.sqljudger.web.config;

import com.zaxxer.hikari.HikariConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "judger")
public class MutliDataSourceConfig {

    private final List<HikariConfig> datasources;

    public MutliDataSourceConfig(List<HikariConfig> datasources) {
        this.datasources = datasources;
    }

    public List<HikariConfig> getDatasources() {
        return datasources;
    }
}
